package io.starter.ignite.generator;

import io.starter.ignite.generator.DMLgenerator.Table;
import io.starter.ignite.security.dao.ConnectionFactory;
import io.starter.ignite.util.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.extentech.toolkit.StringTool;

/**
 * responsible for generating DB DML and creating RDB
 * 
 * @author john
 *
 */
public class DBGen extends Gen implements Generator {

	public void init() throws SQLException, IOException {
		System.out.println("Generating DB...");
		System.out.print("Create DB Connection...");
		Connection conn = ConnectionFactory.getConnection();
		System.out.println((conn.isValid(DB_TIMEOUT) ? "OK!" : "FAILED!"));
	}

	/**
	 * given a Class, iterate the heriarchy and create a set of DB tables
	 * 
	 * @param c
	 * @throws Exception
	 */
	public static void createTableFromClass(Class c, DBGen gen) throws Exception {
		Map classesToGenerate = gen.processClasses(c, null, gen);
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
	public Object createValue(Field f) {
		String colName = f.getName().toUpperCase();
		Class colType = f.getType();

		String colTypeName = colType.getName();
		int pos = colTypeName.lastIndexOf(".") + 1;
		if (pos > 0) {
			colTypeName = colTypeName.substring(pos);
		}
		colTypeName = StringTool.replaceText(colTypeName, ";", "");

		// handle special cases
		if (colName.equalsIgnoreCase("id"))
			colTypeName = "Integer.fkid";

		String dml = Table.myMap.get(colTypeName);
		if (dml != null)
			return colName + " " + dml;

		return null;
	}

	Connection conn = null;

	@Override
	/**
	 * generate DB table from classfile
	 */
	public void generate(String className, List fieldList, List getters, List setters)
			throws IOException, SQLException {
		String packageName = null;
		int dotpos = className.lastIndexOf(".");
		packageName = className.substring(0, dotpos);
		packageName = "gen." + packageName;
		className = className.substring(dotpos + 1);

		// collect the COLUMNs and add to Table then generate
		String tableDML = Table.generateTableBeginningDML(className);
		Iterator cols = fieldList.iterator();
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
		try {
			ps.execute();

			System.err.println("SUCCESS: " + "\r\n" + tableDML + "\r\n" + ConnectionFactory.toConfigString());

		} catch (Exception e) {
			if (e.toString().contains("already exists")) {
				Logger.log("Table for: " + className + " already exists. Skipping.");
			} else {
				Logger.warn("TABLE DML: " + tableDML + " Failed to execute on DB " + ConnectionFactory.toConfigString()
						+ "\r\n" + e.getMessage());
			}
		}

	}

	public String toString() {
		return "DB Generator";
	}

	static void createDatabaseTablesFromModelFolder() throws Exception {
		System.out.println("Iterate Swagger Entities and create Tables...");
		File[] modelFiles = Gen.getFiles();
		DBGen gen = new DBGen();
		// classes, this should point to the top of the package structure!
		URLClassLoader classLoader = new URLClassLoader(new URL[] { new File(JAVA_GEN_SRC_FOLDER).toURI().toURL() });

		for (File mf : modelFiles) {
			String cn = mf.getName().substring(0, mf.getName().indexOf("."));
			// cn = cn + ".class";
			cn = MODEL_PACKAGE + "." + cn;
			System.err.println("Creating Classes from ModelFile: " + cn);
			Class<?> loadedClass = classLoader.loadClass(cn);

			createTableFromClass(loadedClass, gen);
		}
	}
}
