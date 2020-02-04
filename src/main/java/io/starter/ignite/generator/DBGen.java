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
import io.starter.ignite.util.SystemConstants;
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
		DBGen.logger.info("Generating DB...");
		DBGen.logger.info("Create DB Connection...");
		DBGen.conn = ConnectionFactory.getConnection();
		DBGen.logger.info((DBGen.conn.isValid(Configuration.DB_TIMEOUT) ? "OK!" : "FAILED!"));
	}

	/**
	 * given a Class, iterate the heriarchy and create a set of DB tables
	 *
	 * @param c
	 * @throws Exception
	 */
	public static void createTableFromClass(Class<?> c, DBGen gen) throws Exception {
		final Map<String, Object> classesToGenerate = gen.processClasses(c, null, gen);
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

		final String colName = DBGen.decamelize(f.getName());
		final Class<?> colType = f.getType();

		String colTypeName = colType.getName();
		final int pos = colTypeName.lastIndexOf(".") + 1;
		if (pos > 0) {
			colTypeName = colTypeName.substring(pos);
		}
		colTypeName = colTypeName.replace(";", "");

		// get the annotation

		// handle special built-in columns
		if (colName.equalsIgnoreCase("ID")) {
			colTypeName = "Integer.fkid";
		} else if (colName.equalsIgnoreCase("CREATED_DATE")) {
			colTypeName = "Timestamp.createdDate";
		} else if (colName.equalsIgnoreCase("MODIFIED_DATE")) {
			colTypeName = "Timestamp.modifiedDate";
		}

		String dml = Table.myMap.get(colTypeName);

		if (colTypeName.contains("Enum")) {
			dml = Table.myMap.get("Enum");
			final String tname = DBGen.camelize(f.getDeclaringClass().getName());
			dml = dml.replace("${MY_TABLE}", tname);
		}
		if ((dml == null) && f.getType().isPrimitive()) {
			dml = Table.myMap.get("String");
		} else if (dml == null) {
			// TODO: handle complex data types
			DBGen.logger.info("Could not map: " + f.getType().getName() + " of coltype: " + colTypeName
					+ " to a Database Column");
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
		final String defaultval = "";
		final String charset = ""; // "'utf8'";
		int minleng = 0;
		double minVal = 0d;
		double maxVal = Double.MAX_VALUE;
		boolean isSecure = false;
		boolean isDataField = false;

		SecureField sanno = null;
		try {
			sanno = Gen.getSecureFieldAnnotation(f);
			isSecure = (sanno != null) && sanno.enabled();
		} catch (final NoSuchMethodException nsme) {
			// normal, no SecureField
		} catch (final SecurityException e) {
			DBGen.logger.warn("Problem getting SecureField Annotation on: " + f.getName() + " " + e.toString());
			e.printStackTrace();
		}

		DataField danno = null;
		try {
			danno = Gen.getDataFieldAnnotation(f);
			isDataField = danno != null; // danno.annotationType();
		} catch (final NoSuchMethodException nsme) {
			// normal, no SecureField
		} catch (final SecurityException e) {
			DBGen.logger.warn("Problem getting DataField Annotation on: " + f.getName() + " " + e.toString());
			e.printStackTrace();
		}
		ApiModelProperty anno = null;
		try {
			anno = Gen.getApiModelPropertyAnnotation(f);
		} catch (final NoSuchMethodException nsme) {
			// normal, no getter
		} catch (final SecurityException e) {
			DBGen.logger.warn("Problem getting Annotation on: " + f.getName() + " " + e.toString());
			e.printStackTrace();
		}

		if (anno != null) {
			notes = (anno.notes().equals("") ? notes : anno.notes());
			nullable = !anno.required();
			leng = anno.maxLength();
			// TODO: implement other configs
			minleng = anno.minLength();
			minVal = anno.minValue();
			maxVal = anno.maxValue();
		}

		if (!notes.isEmpty()) {
			DBGen.logger.info(" notes: " + notes);
		}
		dml = dml.replace("${COMMENT}", notes);

		DBGen.logger.info(" nullable: " + nullable);
		dml = dml.replace("${NOT_NULL}", (nullable ? "" : "NOT NULL"));

		DBGen.logger.info(" charset: " + charset);
		dml = dml.replace("${CHAR_SET}", charset);

		DBGen.logger.info(" defaultVal: " + defaultval);
		dml = dml.replace("${DEFAULT}", defaultval);

		// TODO: implement a smarter way to handle crypto expansion
		if (isSecure) {
			leng *= Configuration.DB_ENCRYPTED_COLUMN_MULTIPLIER;
		}

		// Column length too big for column 'NAME' (max = 65535);
		// use BLOB or TEXT instead
		if ((leng > 1280) && !rerun) {
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
	public synchronized void generate(String className, List<FieldSpec> fieldList, List<MethodSpec> getters,
			List<MethodSpec> setters) throws Exception {
		// String packageName = null;
		final int dotpos = className.lastIndexOf(".");
		if ((dotpos < 0) || (dotpos >= className.length())) {
			return;
		}
		// packageName = className.substring(0, dotpos);
		// packageName = "gen." + packageName;
		className = className.substring(dotpos + 1);

		// collect the COLUMNs and add to Table then generate
		String tableDML = Table.generateTableBeginningDML(className);
		final Iterator<?> cols = fieldList.iterator();
		boolean isEmpty = true;
		String extraColumnDML = "";
		while (cols.hasNext()) {
			isEmpty = false;
			final Object o = cols.next();
			tableDML += "	" + o.toString();
			// add the PK for auto-increment ID
			String colName = o.toString();
			colName = colName.substring(0, colName.indexOf(" "));
			if (colName.equalsIgnoreCase("id")) {
				extraColumnDML += Configuration.LINE_FEED + Table.myMap.get("pkid");
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

		tableDML += Configuration.CREATE_TABLE_END_BLOCK;

		if (DBGen.conn == null) {
			DBGen.conn = ConnectionFactory.instance.getDataSource().getConnection();
		}

		// use database
		DBGen.conn.setSchema(SystemConstants.dbName);

		final PreparedStatement ps = DBGen.conn.prepareStatement(tableDML);
		final List<String> triedList = new ArrayList<>();

		// log the DML for troubleshooting
		if (Configuration.debug) {
			// FileUtils.writeStringToFile(new File("IgniteDML.sql"),
			// tableDML
			// + Configuration.LINE_FEED, true);
		}

		try {
			ps.execute();
			ps.close();
			DBGen.logger.info("SUCCESS: creating table for: " + className);

		} catch (final Exception e) {
			if (e.toString().contains("already exists") && Configuration.dbGenDropTable) {
				DBGen.logger.info("Table for: " + className + " already exists.");
				if (!triedList.contains(className) && Configuration.dbGenDropTable) {

					if (renameTable(className, triedList)) {

						// try again
						generate("." + className, fieldList, getters, setters);

						migrateData(className);
					}

				} else {
					DBGen.logger.warn("Skipping Retry of Table Creation for: " + className);
				}
			} else {
				DBGen.logger
						.error("Failed to execute DML for: " + className + " -- " + ConnectionFactory.toConfigString()
								+ Configuration.LINE_FEED + e.getMessage() + Configuration.LINE_FEED + tableDML);
			}
		}
	}

	/**
	 * TODO: implement data migration
	 *
	 * @param className
	 */
	private void migrateData(String className) {
		// TODO Auto-generated method stub
		DBGen.logger.info("TODO: migrate table data from old to new");
	}

	/**
	 * apply the generated DML to create the necessary IDX tables
	 *
	 * @param tl
	 * @return
	 * @throws SQLException
	 */
	public static boolean createIDXTables(List<MyBatisJoin> tl) throws SQLException {
		if (!Configuration.skipDbGen) {
			for (final MyBatisJoin j : tl) {
				final String tableName = j.myTable;
				DBGen.logger.info("Creating IDX TABLE: " + tableName);

				// generate the table
				if (DBGen.conn == null) {
					DBGen.conn = ConnectionFactory.instance.getDataSource().getConnection();
				}
				final PreparedStatement psx = DBGen.conn.prepareStatement(j.getDML());
				try {
					psx.execute();
					psx.close();
				} catch (final Exception ex) {
					DBGen.logger.error("Failed to createIDXTable table with DML: " + ex.toString());
					return false;
				}
			}
		}
		return true;
	}

	private boolean renameTable(String className, List<String> triedList) throws SQLException {
		if (!Configuration.skipDbGen) {
			DBGen.logger.info("RENAMING TABLE: " + className);
			triedList.add(className);
			// rename the table
			final String renameTableDML = Table.generateTableRenameDML(className);
			if (DBGen.conn == null) {
				DBGen.conn = ConnectionFactory.instance.getDataSource().getConnection();
			}
			final PreparedStatement psx = DBGen.conn.prepareStatement(renameTableDML);
			try {
				psx.execute();
				psx.close();
			} catch (final Exception ex) {
				DBGen.logger.error("Failed to drop table with DML: " + renameTableDML + "  : " + ex.toString());
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
		DBGen.logger.info("Iterate Swagger Entities and create Tables...");
		final File[] modelFiles = Gen
				.getJavaFiles(Configuration.JAVA_GEN_SRC_FOLDER + "/" + Configuration.MODEL_PACKAGE_DIR, false);
		final DBGen gen = new DBGen();
		// classes, this should point to the top of the package
		// structure!
		final URL packagedir = new File(Configuration.JAVA_GEN_SRC_FOLDER).toURI().toURL();
		final URLClassLoader classLoader = new URLClassLoader(new URL[] { packagedir });
		DBGen.logger.info("Created Classloader: " + classLoader);

		for (final File mf : modelFiles) {
			try {
				String cn = mf.getName().substring(0, mf.getName().indexOf("."));
				// cn = cn + ".class";
				cn = Configuration.IGNITE_MODEL_PACKAGE + "." + cn;
				DBGen.logger.info("Loading Classes from ModelFile: " + cn);
				// TODO: fails in web runner
				final Class<?> loadedClass = classLoader.loadClass(cn);

				DBGen.createTableFromClass(loadedClass, gen);
			} catch (final Exception e) {
				DBGen.logger.error("Could not create Table for: " + mf.getName() + " :" + e.toString());
			}
		}
		classLoader.close();
	}

	/**
	 * converts java member naming convention to underscored DB-style naming
	 * convention
	 *
	 * ie: take upperCamelCase and turn into upper_camel_case
	 */
	public static String decamelize(String name) {
		if (name.equals(name.toLowerCase()) || name.equals(name.toUpperCase())) { // case insensitive
			return name;
		}

		final StringBuilder sb = new StringBuilder();
		int x = 0;
		for (final char c : name.toCharArray()) {
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(c);
			} else {
				if (c != '_') {
					sb.append(c);
				}
			}
			x++;
		}
		String ret = sb.toString();
		if (Configuration.columnsUpperCase) {
			ret = ret.toUpperCase();
		} else {
			ret = ret.toLowerCase();
		}
		return ret;
	}

	/**
	 * converts underscored DB-style naming convention to java member naming
	 * convention
	 *
	 * ie: take upper_camel_case and turn into upperCamelCase
	 */
	public static String camelize(String in) {
		if (in == null) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		boolean capitalizeNext = false;
		for (final char c : in.toCharArray()) {
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

	public static String upperCaseFirstLetter(String thismember) {
		String upcase = thismember.substring(0, 1);
		upcase = upcase.toUpperCase();
		thismember = thismember.substring(1);
		thismember = upcase + thismember;
		return thismember;
	}
}
