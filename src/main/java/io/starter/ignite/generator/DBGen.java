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

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import io.starter.ignite.generator.DMLgenerator.Table;
import io.starter.ignite.security.dao.ConnectionFactory;
import io.starter.toolkit.StringTool;
import io.swagger.annotations.ApiModelProperty;

/**
 * responsible for generating DB DML and creating RDB
 * 
 * @author john
 *
 */
public class DBGen extends Gen implements Generator {

	protected static final Logger logger = LoggerFactory.getLogger(DBGen.class);

	public void init() throws SQLException, IOException {
		logger.debug("Generating DB...");
		System.out.print("Create DB Connection...");
		Connection conn = ConnectionFactory.getConnection();
		logger.debug((conn.isValid(DB_TIMEOUT) ? "OK!" : "FAILED!"));
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

		String colName = StringTool.convertJavaStyletoDBConvention(f.getName());
		Class<?> colType = f.getType();

		String colTypeName = colType.getName();
		int pos = colTypeName.lastIndexOf(".") + 1;
		if (pos > 0) {
			colTypeName = colTypeName.substring(pos);
		}
		colTypeName = StringTool.replaceText(colTypeName, ";", "");

		// get the annotation

		// handle special cases
		if (colName.equalsIgnoreCase("id"))
			colTypeName = "Integer.fkid";

		String dml = Table.myMap.get(colTypeName);

		if (colTypeName.contains("Enum")) {
			dml = Table.myMap.get("Enum");
		}
		if (dml == null && f.getType().isPrimitive()) {
			dml = Table.myMap.get("String");
		} else if (dml == null) {
			// TODO: handle complex data types
			logger.warn("Could not map: " + f.getType().getName()
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

		ApiModelProperty anno = null;
		try {
			anno = Gen.getApiModelPropertyAnnotation(f);
		} catch (NoSuchMethodException nsme) {
			// normal, no getter
		} catch (SecurityException e) {
			logger.warn("Problem processing Annotation on: " + f.getName() + " "
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

		System.out.println(" notes: " + notes);
		if (notes != null && !"".equals(notes)) {
			notes = "COMMENT '" + notes + "'";
		}
		dml = dml.replace("${COMMENT}", notes);

		System.out.println(" nullable: " + nullable);
		dml = dml.replace("${NOT_NULL}", (nullable ? "" : "NOT NULL"));

		System.out.print(" charset: " + charset);
		dml = dml.replace("${CHAR_SET}", charset);

		System.out.print(" defaultVal: " + defaultval);
		dml = dml.replace("${DEFAULT}", defaultval);

		// TODO: implement a smarter way to handle crypto expansion
		leng *= 3;

		// Column length too big for column 'NAME' (max = 65535);
		// use BLOB or TEXT instead
		if (leng > 21844 && !rerun) {
			dml = configureDML(f, Table.myMap.get("Text"), true);
		} else {
			dml = dml.replace("${MAX_LENGTH}", (leng > 0 ? leng + "" : ""));
		}

		return dml;
	}

	Connection conn = null;

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
		String dropTableDML = Table.generateTableDropDML(className);
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
				extraColumnDML += "\r\n" + Table.myMap.get("pkid");
			}
			tableDML += ",";
			tableDML += "\r\n";
		}

		// indexes
		tableDML += extraColumnDML;
		if (!isEmpty) {
			tableDML = tableDML.substring(0, tableDML.lastIndexOf(","));
			tableDML += "\r\n";
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
		if (Configuration.DEBUG) {
			FileUtils.writeStringToFile(new File("IgniteDML.sql"), tableDML
					+ "/r/n", true);
		}

		try {
			ps.execute();
			ps.close();
			logger.info("SUCCESS: " + "\r\n" + tableDML + "\r\n"
					+ ConnectionFactory.toConfigString());

		} catch (Exception e) {
			if (e.toString().contains("already exists")
					&& DROP_EXISTING_TABLES) {
				logger.warn("Table for: " + className + " already exists.");
				if (!triedList.contains(className) && DROP_EXISTING_TABLES) {

					logger.warn("DROPPING TABLE: " + className);

					triedList.add(className);

					// drop the table
					PreparedStatement psx = conn.prepareStatement(dropTableDML);
					try {
						psx.execute();
						psx.close();
					} catch (Exception ex) {
						logger.error("Failed to drop table with DML: "
								+ dropTableDML + "  : " + ex.toString());
					}

					// try again
					generate("." + className, fieldList, getters, setters);

				} else {
					logger.warn("Skipping...");
				}
			} else {
				logger.warn("TABLE DML: " + tableDML
						+ " Failed to execute on DB "
						+ ConnectionFactory.toConfigString() + "\r\n"
						+ e.getMessage());
			}
		}
	}

	@Override
	public String toString() {
		return "DB Generator";
	}

	static void createDatabaseTablesFromModelFolder() throws Exception {
		logger.debug("Iterate Swagger Entities and create Tables...");
		File[] modelFiles = Gen.getModelJavaFiles();
		DBGen gen = new DBGen();
		// classes, this should point to the top of the package
		// structure!

		URL packagedir = new File(JAVA_GEN_SRC_FOLDER).toURI().toURL();
		URLClassLoader classLoader = new URLClassLoader(
				new URL[] { packagedir });
		logger.debug("Created Classloader: " + classLoader);

		for (File mf : modelFiles) {
			String cn = mf.getName().substring(0, mf.getName().indexOf("."));
			// cn = cn + ".class";
			cn = MODEL_PACKAGE + "." + cn;
			logger.debug("Loading Classes from ModelFile: " + cn);
			Class<?> loadedClass = classLoader.loadClass(cn);

			createTableFromClass(loadedClass, gen);
		}
		classLoader.close();
	}
}
