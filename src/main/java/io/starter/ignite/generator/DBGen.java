package io.starter.ignite.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import io.starter.ignite.generator.DMLgenerator.Table;
import io.starter.ignite.model.DataField;
import io.starter.ignite.security.dao.ConnectionFactory;
import io.starter.ignite.security.securefield.SecureField;
import io.swagger.annotations.ApiModelProperty;

/**
 * responsible for generating DB DML and creating RDB
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class DBGen extends Gen implements Generator {

	protected static final Logger logger = LoggerFactory.getLogger(DBGen.class);

	public void init() throws SQLException, IOException {
		logger.info("Generating DB...");
		logger.info("Create DB Connection...");
		Connection conn = ConnectionFactory.getConnection();
		logger.info((conn.isValid(DB_TIMEOUT) ? "OK!" : "FAILED!"));
	}

	/**
	 * given a Class, iterate the heriarchy and create a set of DB tables
	 * 
	 * @param c
	 * @throws Exception
	 */
	public static void createTableFromClass(Class<?> c, DBGen gen) throws Exception {
		Map<String, Object> classesToGenerate = gen
				.processClasses(c, null, gen);
		if (classesToGenerate == null) {
			throw new RuntimeException("DBGen did not generate any classes.");
		}
	}

	@Override
	public Object createSetter(Field f) {
		// TODO:
		return null;
	}

	@Override
	public Object createAccessor(Field f) {
		// TODO:
		return null;

	}

	@Override
	public Object createMember(Field f) {

		String colName = decamelize(f.getName());
		Class<?> colType = f.getType();

		String colTypeName = colType.getName();
		int pos = colTypeName.lastIndexOf(".") + 1;
		if (pos > 0) {
			colTypeName = colTypeName.substring(pos);
		}
		colTypeName = colTypeName.replace(";", "");

		// get the annotation

		// handle special built-in columns
		if (colName.equalsIgnoreCase("ID"))
			colTypeName = "Integer.fkid";
		else if (colName.equalsIgnoreCase("CREATED_DATE"))
			colTypeName = "Timestamp.createdDate";
		else if (colName.equalsIgnoreCase("MODIFIED_DATE"))
			colTypeName = "Timestamp.modifiedDate";

		String dml = Table.myMap.get(colTypeName);

		if (colTypeName.contains("Enum")) {
			dml = Table.myMap.get("Enum");
		}
		if (dml == null && f.getType().isPrimitive()) {
			dml = Table.myMap.get("String");
		} else if (dml == null) {
			// TODO: handle complex data types
			logger.info("Could not map: " + f.getType().getName()
					+ " of coltype: " + colTypeName + " to a Database Column");
			return null;
		}

		dml = configureDML(f, dml, false);

		return colName + " " + dml;
	}

	/**
	 * configure DML for column 
	 * 
	 * @param f
	 * @param dml
	 * @return
	 */
	private String configureDML(Field f, String dml, boolean rerun) {

		String notes = "";// "COMMENT 'ignite generated column for the "
							// + f.getName()
		// + " value'";
		boolean nullable = true;
		int leng = 256;

		// ${CHAR_SET} ${DEFAULT}
		String defaultval = "";
		String charset = ""; // "'utf8'";
		int minleng = 0;
		double minVal = 0d;
		double maxVal = Double.MAX_VALUE;
		boolean isSecure = false;
		boolean isDataField = false;

		SecureField sanno = null;
		try {
			sanno = Gen.getSecureFieldAnnotation(f);
			isSecure = sanno != null && sanno.enabled();
		} catch (NoSuchMethodException nsme) {
			// normal, no SecureField
		} catch (SecurityException e) {
			logger.warn("Problem getting SecureField Annotation on: "
					+ f.getName() + " " + e.toString());
			e.printStackTrace();
		}

		DataField danno = null;
		try {
			danno = Gen.getDataFieldAnnotation(f);
			isDataField = danno != null; // danno.annotationType();
		} catch (NoSuchMethodException nsme) {
			// normal, no SecureField
		} catch (SecurityException e) {
			logger.warn("Problem getting DataField Annotation on: "
					+ f.getName() + " " + e.toString());
			e.printStackTrace();
		}
		ApiModelProperty anno = null;
		try {
			anno = Gen.getApiModelPropertyAnnotation(f);
		} catch (NoSuchMethodException nsme) {
			// normal, no getter
		} catch (SecurityException e) {
			logger.warn("Problem getting Annotation on: " + f.getName() + " "
					+ e.toString());
			e.printStackTrace();
		}

		if (anno != null) {
			notes = (anno.notes().equals("") ? notes : anno.notes());
			nullable = anno.required();
			leng = anno.maxLength();
			// TODO: implement other configs
			minleng = anno.minLength();
			minVal = anno.minValue();
			maxVal = anno.maxValue();
		}

		logger.info(" notes: " + notes);
		if (notes != null && !"".equals(notes)) {
			notes = "COMMENT '" + notes + "'";
		}
		dml = dml.replace("${COMMENT}", notes);

		logger.info(" nullable: " + nullable);
		dml = dml.replace("${NOT_NULL}", (nullable ? "" : "NOT NULL"));

		logger.info(" charset: " + charset);
		dml = dml.replace("${CHAR_SET}", charset);

		logger.info(" defaultVal: " + defaultval);
		dml = dml.replace("${DEFAULT}", defaultval);

		// TODO: implement a smarter way to handle crypto expansion
		if (isSecure) // give extra room for ciphertext bytes
			leng *= DB_ENCRYPTED_COLUMN_MULTIPLIER;

		// Column length too big for column 'NAME' (max = 65535);
		// use BLOB or TEXT instead
		if (leng > 1280 && !rerun) {
			dml = configureDML(f, Table.myMap.get("Text"), true);
		} else {
			dml = dml.replace("${MAX_LENGTH}", (leng > 0 ? leng + "" : ""));
		}

		return dml;
	}

	static Connection conn = null;

	@SuppressWarnings("deprecation")
	@Override
	/**
	 * generate DB table from classfile
	 */
	public synchronized void generate(String className, List<FieldSpec> fieldList, List<MethodSpec> getters, List<MethodSpec> setters) throws Exception {
		// String packageName = null;
		int dotpos = className.lastIndexOf(".");
		if (dotpos < 0 || dotpos >= className.length()) // skip
			return;
		// packageName = className.substring(0, dotpos);
		// packageName = "gen." + packageName;
		className = className.substring(dotpos + 1);

		// collect the COLUMNs and add to Table then generate
		String tableDML = Table.generateTableBeginningDML(className);
		Iterator<?> cols = fieldList.iterator();
		boolean isEmpty = true;
		String extraColumnDML = "";
		while (cols.hasNext()) {
			isEmpty = false;
			Object o = cols.next();
			tableDML += "	" + o.toString();
			// add the PK for auto-increment ID
			String colName = o.toString();
			colName = colName.substring(0, colName.indexOf(" "));
			if (colName.equalsIgnoreCase("id")) {
				extraColumnDML += Configuration.LINE_FEED
						+ Table.myMap.get("pkid");
			}
			tableDML += ",";
			tableDML += Configuration.LINE_FEED;
		}

		// indexes
		tableDML += extraColumnDML;
		if (!isEmpty) {
			tableDML = tableDML.substring(0, tableDML.lastIndexOf(","));
			tableDML += Configuration.LINE_FEED;
		} else {
			return;
		}

		tableDML += Table.CREATE_TABLE_END_BLOCK;

		if (conn == null) {
			conn = ConnectionFactory.getDataSource().getConnection();
		}

		PreparedStatement ps = conn.prepareStatement(tableDML);
		List<String> triedList = new ArrayList<String>();

		// log the DML for troubleshooting
		if (Configuration.debug) {
			// FileUtils.writeStringToFile(new File("IgniteDML.sql"),
			// tableDML
			// + Configuration.LINE_FEED, true);
		}

		try {
			ps.execute();
			ps.close();
			logger.info("SUCCESS: creating table for: " + className);

		} catch (Exception e) {
			if (e.toString().contains("already exists") && dbGenDropTable) {
				logger.info("Table for: " + className + " already exists.");
				if (!triedList.contains(className) && dbGenDropTable) {

					if (renameTable(className, triedList)) {

						// try again
						generate("." + className, fieldList, getters, setters);

						migrateData(className);
					}

				} else {
					logger.warn("Skipping Retry of Table Creation for: "
							+ className);
				}
			} else {
				logger.error("Failed to execute DML for: " + className + " -- "
						+ ConnectionFactory.toConfigString()
						+ Configuration.LINE_FEED + e.getMessage()
						+ Configuration.LINE_FEED + tableDML);
			}
		}
	}

	/**
	 * TODO: implement data migration
	 * @param className
	 */
	private void migrateData(String className) {
		// TODO Auto-generated method stub
	}

	/**
	 * apply the generated DML to create the necessary IDX tables
	 * 
	 * @param tl
	 * @return
	 * @throws SQLException
	 */
	public static boolean createIDXTables(List<MyBatisJoin> tl) throws SQLException {
		if (!skipDbGen) {
			for (MyBatisJoin j : tl) {
				String tableName = j.myTable;
				logger.info("Creating IDX TABLE: " + tableName);

				// generate the table
				if (conn == null) {
					conn = ConnectionFactory.getDataSource().getConnection();
				}
				PreparedStatement psx = conn.prepareStatement(j.getDML());
				try {
					psx.execute();
					psx.close();
				} catch (Exception ex) {
					logger.error("Failed to createIDXTable table with DML: "
							+ ex.toString());
					return false;
				}
			}
		}
		return true;
	}

	private boolean renameTable(String className, List<String> triedList) throws SQLException {
		if (!skipDbGen) {
			logger.info("RENAMING TABLE: " + className);
			triedList.add(className);
			// rename the table
			String renameTableDML = Table.generateTableRenameDML(className);
			if (conn == null) {
				conn = ConnectionFactory.getDataSource().getConnection();
			}
			PreparedStatement psx = conn.prepareStatement(renameTableDML);
			try {
				psx.execute();
				psx.close();
			} catch (Exception ex) {
				logger.error("Failed to drop table with DML: " + renameTableDML
						+ "  : " + ex.toString());
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "DB Generator";
	}

	/**
	 * iterate the Model folder and generate DML & tables
	 * 
	 * @throws Exception
	 */
	static void createDatabaseTablesFromModelFolder() throws Exception {
		logger.info("Iterate Swagger Entities and create Tables...");
		File[] modelFiles = Gen
				.getJavaFiles(JAVA_GEN_SRC_FOLDER + "/" + MODEL_PACKAGE_DIR, false);
		DBGen gen = new DBGen();
		// classes, this should point to the top of the package
		// structure!
		URL packagedir = new File(JAVA_GEN_SRC_FOLDER).toURI().toURL();
		URLClassLoader classLoader = new URLClassLoader(
				new URL[] { packagedir });
		logger.info("Created Classloader: " + classLoader);

		for (File mf : modelFiles) {
			String cn = mf.getName().substring(0, mf.getName().indexOf("."));
			// cn = cn + ".class";
			cn = IGNITE_MODEL_PACKAGE + "." + cn;
			logger.info("Loading Classes from ModelFile: " + cn);
			// TODO: fails in web runner
			Class<?> loadedClass = classLoader.loadClass(cn);

			createTableFromClass(loadedClass, gen);
		}
		classLoader.close();
	}

	/**
	 * converts java member naming convention
	 * to underscored DB-style naming convention
	 * 
	 * ie: take upperCamelCase and turn into upper_camel_case
	 */
	public static String decamelize(String name) {
		if (name.equals(name.toLowerCase())
				|| name.equals(name.toUpperCase())) { // case insensitive
			return name;
		}

		StringBuilder sb = new StringBuilder();
		int x = 0;
		for (char c : name.toCharArray()) {
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(c);
			} else {
				if (c != '_')
					sb.append(c);
			}
			x++;
		}
		String ret = sb.toString();
		if (columnsUpperCase) {
			ret = ret.toUpperCase();
		} else {
			ret = ret.toLowerCase();
		}
		return ret;
	}

	/**
	 * converts underscored DB-style naming
	 * convention to java member naming convention
	 * 
	 * ie: take upper_camel_case and turn into upperCamelCase 
	 */
	public static String camelize(String in) {
		if (in == null)
			return null;
		StringBuilder sb = new StringBuilder();
		boolean capitalizeNext = false;
		for (char c : in.toCharArray()) {
			if (c == '_') {
				capitalizeNext = true;
			} else {
				if (capitalizeNext) {
					sb.append(Character.toUpperCase(c));
					capitalizeNext = false;
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}
}
