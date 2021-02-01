package io.starter.ignite.generator;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.javapoet.MethodSpec;

import io.netty.util.CharsetUtil;
import io.starter.ignite.generator.DMLgenerator.Table;
import io.starter.ignite.model.DataField;
import io.starter.ignite.security.dao.ConnectionFactory;
import io.starter.ignite.security.securefield.SecureField;
import io.starter.ignite.util.SystemConstants;
import io.starter.ignite.generator.annotations.StackgenModelProperty;

/**
 * responsible for generating DB DML and creating RDB
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class DBGen extends Gen implements Generator {

	public DBGen(StackGenConfigurator cfg) {
		super(cfg);
	}

	protected static final Logger logger = LoggerFactory.getLogger(DBGen.class);

	public void init() throws SQLException, IOException {
		logger.info("Generating DB...");
		logger.info("Create DB Connection...");
		conn = this.getConnection();
		logger.info((conn.isValid(config.DB_TIMEOUT) ? "OK!" : "FAILED!"));
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

		final String colName = decamelize(f.getName());
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
			// final String tname = DBGen.camelize(f.getDeclaringClass().getName());
			dml = dml.replace("${MY_TABLE}", f.getDeclaringClass().getName());
		}
		if ((dml == null) && f.getType().isPrimitive()) {
			dml = Table.myMap.get("String");
		} else if (dml == null) {
			// TODO: handle complex data types
			logger.info("Could not map: " + f.getType().getName() + " of coltype: " + colTypeName
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
		// int minleng = 0;
		//// double minVal = 0d;
		// double maxVal = Double.MAX_VALUE;
		boolean isSecure = false;
		boolean isDataField = false;

		Annotation sanno = null;
		try {
			sanno = Gen.getSecureFieldAnnotation(f);
			
			if (sanno != null) {
				Class<? extends Annotation> type = sanno.annotationType();

				try {
					isSecure = (Boolean) type.getDeclaredMethod("enabled").invoke(sanno, (Object[]) null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (final NoSuchMethodException nsme) {
			// normal, no SecureField
		} catch (final SecurityException e) {
			logger.warn("Problem getting SecureField Annotation on: " + f.getName() + " " + e.toString());
			e.printStackTrace();
		}

		Annotation danno = null;
		try {
			danno = Gen.getDataFieldAnnotation(f);
			isDataField = danno != null; // danno.annotationType();
			
		} catch (final NoSuchMethodException nsme) {
			// normal, no SecureField
		} catch (final SecurityException e) {
			logger.warn("Problem getting DataField Annotation on: " + f.getName() + " " + e.toString());
			e.printStackTrace();
		}
		
		Annotation anno = null;
		try {
			anno = Gen.getApiModelPropertyAnnotation(f);
			if (anno != null) {
				Class<? extends Annotation> type = anno.annotationType();

				try {
					// notes = (String) type.getDeclaredMethod("notes").invoke(anno, (Object[]) null);
					notes = (String) type.getDeclaredMethod("value").invoke(anno, (Object[]) null);
					nullable = !(Boolean) type.getDeclaredMethod("required").invoke(anno, (Object[]) null);
					leng = (int) type.getDeclaredMethod("maxLength").invoke(anno, (Object[]) null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (final NoSuchMethodException nsme) {
			// normal, no getter
		} catch (final Exception e) {
			logger.error("Problem getting ApiModelProperty Annotation on: " + f.getName() + " " + e.toString());
			e.printStackTrace();
		}


		
		if (config.debug && !notes.isEmpty()) {
			logger.info("Field notes: " + notes);
		}
		if(notes.equals("")) {
			dml = dml.replace("${COMMENT}", "");
		} else {
			dml = dml.replace("${COMMENT}", "COMMENT \"" + notes + "\"");
		}

		logger.trace("Creating column for field: " + f.getName() + " charset: " + charset + " defaultVal: " + defaultval
				+ " nullable: " + nullable);
		dml = dml.replace("${NOT_NULL}", (nullable ? "" : "NOT NULL"));
		dml = dml.replace("${CHAR_SET}", charset);
		dml = dml.replace("${DEFAULT}", defaultval);

		// TODO: implement a smarter way to handle crypto expansion
		if (isSecure) {
			leng *= config.DB_ENCRYPTED_COLUMN_MULTIPLIER;
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

	public static Connection conn = null;

	@Override
	/**
	 * generate DB table from classfile
	 */
	public synchronized void generate(String className, List<Object> fieldList, List<MethodSpec> getters,
			List<MethodSpec> setters) throws Exception {
		// String packageName = null;
		Table table = new Table(config);
		final int dotpos = className.lastIndexOf(".");
		if ((dotpos < 0) || (dotpos >= className.length())) {
			return;
		}
		className = className.substring(dotpos + 1);

		// check if we even need to apply DML
		//try {
			//if (noTableChangesRequired(className, fieldList, table)) {
			//	return;
			//}
		//}catch(Exception e) {
		//	; // normal
		//}
		// collect the COLUMNs and add to Table then generate
		String tableDML = table.generateTableBeginningDML(className);
		final Iterator<?> fields = fieldList.iterator();
		boolean isEmpty = true;
		String extraColumnDML = "";

		while (fields.hasNext()) {
			isEmpty = false;
			final Object fld = fields.next();
			tableDML += "	" + fld.toString();
			// add the PK for auto-increment ID
			String colName = fld.toString();
			colName = colName.substring(0, colName.indexOf(" "));
			if (colName.equalsIgnoreCase("id")) {
				extraColumnDML += config.LINE_FEED + Table.myMap.get("pkid");
			}
			tableDML += ",";
			tableDML += config.LINE_FEED;
		}

		// indexes
		tableDML += extraColumnDML;
		if (!isEmpty) {
			tableDML = tableDML.substring(0, tableDML.lastIndexOf(","));
			tableDML += config.LINE_FEED;
		} else {
			return;
		}

		tableDML += Table.CREATE_TABLE_END_BLOCK;

		
		conn = this.getConnection(); 

		// use database
		conn.setSchema(config.dbName);

		final List<String> triedList = new ArrayList<>();

		// log the DML for troubleshooting
		try {
			File fx = new File(config.getGenOutputFolder() + "/dml");
			if (!fx.exists()) {
				fx.mkdirs();
			}
			FileUtils.write(new File(fx.getPath() + "/" + className + "-DML.sql"), tableDML, CharsetUtil.UTF_8);
		} catch (Exception e) {
			logger.warn("Could not save DML: ", e);
		}
		final PreparedStatement ps = conn.prepareStatement(tableDML);
		
		try {
			ps.execute();
			ps.close();
			logger.info("SUCCESS: creating new table for: " + className);

		} catch (final Exception e) {
			if (e.toString().contains("already exists") && config.dbGenDropTable) {
				logger.info("Table for: " + className + " already exists.");
				if (!triedList.contains(className) && config.dbGenDropTable) {

					if (renameTable(className, triedList, false)) {

						ps.execute();
						ps.close();
						logger.info("SUCCESS: updating table for: " + className);

						// try again
						// generate("." + className, fieldList, getters, setters);
						String newTableName = Table.RENAME_TABLE_PREFIX + table.convertToDBSyntax(className);
						String tableName = table.convertToDBSyntax(className);

						// RENAME_TABLE_PREFIX
						try {
							String migrateSQL = migrateDataSQL(tableName, newTableName);
							final PreparedStatement ps3 = conn.prepareStatement(migrateSQL);
							ps3.execute();
							logger.info("Executed migration SQL: " + migrateSQL);
							
						} catch (Exception s) {
							logger.error(
									"Could not migrate data to new table. Data retained in [ " + tableName + " ] " + s);
						}
					}

				} else {
					logger.warn("Skipping Retry of Table Creation for: " + className);
				}
			} else {
				logger.error("Failed to execute DML for: " + className + " -- " + ConnectionFactory.toConfigString()
						+ StackGenConfigurator.LINE_FEED + e.getMessage() + config.LINE_FEED + tableDML);
			}
		}
	}

	private boolean noTableChangesRequired(String className, List<Object> fieldList, Table table) throws SQLException {
		String tableName = table.convertToDBSyntax(className);	
		conn = this.getConnection();
	
		List<Object> checkList = fieldList.stream()
				.map(e -> (e.toString().contains("COMMENT") ? e.toString().substring(0, e.toString().indexOf("COMMENT"))
						: e.toString()).trim())
				.collect(Collectors.toList());

		String checkSQL1 = "" + "SELECT * FROM " + tableName + " WHERE 0 > 1";
		final PreparedStatement ps = conn.prepareStatement(checkSQL1);
		ps.executeQuery();
		ResultSet rs = ps.getResultSet();
		ResultSetMetaData srm = rs.getMetaData();
		int colct = srm.getColumnCount();

		boolean foundChange = false;
		for (int t = 1; t <= colct; t++) {
			String newColName = ps.getResultSet().getMetaData().getColumnName(t);
			int newColType = ps.getResultSet().getMetaData().getColumnType(t);
			int colPrecision = ps.getResultSet().getMetaData().getPrecision(t);
			int colScale = ps.getResultSet().getMetaData().getScale(t);
			String colClass = ps.getResultSet().getMetaData().getColumnClassName(t);
			int nullable = ps.getResultSet().getMetaData().isNullable(t);

			// TODO: handle precision
			// TODO: write out liquibase changelog

			String colCheck = newColName;

			if (colCheck.equals("id") || colCheck.equals("created_date") || colCheck.equals("modified_date")
					|| colCheck.equals("owner_id") || colCheck.equals("key_spec") || colCheck.equals("key_version")) {
				// assume unchanged
			} else {

				if (colClass.equals("java.math.BigInteger")) {
					colCheck += " BIGINT(10) UNSIGNED";
				} else if (colClass.equals("java.lang.String")) {
					colCheck += " VARCHAR(" + colPrecision + ")";
				} else if (colClass.equals("java.lang.Double")) {
					colCheck += " DOUBLE";
				} else if (colClass.equals("java.lang.Boolean")) {
					colCheck += " TINYINT(1)";
				} else if (colClass.equals("java.lang.Integer")) {
					colCheck += " INTEGER";
				} else if (colClass.equals("java.sql.Timestamp")) {
					colCheck += " TIMESTAMP";
				}
				
				if(nullable == ResultSetMetaData.columnNoNulls) { // not nullable
					colCheck += " NOT NULL";
				}
				
				if (!checkList.contains(colCheck)) {
					foundChange = true;
					logger.info("Found DB Schema Change: " + colCheck);
				}
			}
		}

		return foundChange;
	}

	/**
	 * Migrate data from old table to new
	 *
	 * @param className
	 * @param tableName
	 * @return
	 */
	public String migrateDataSQL(String tableName, String newTableName) throws SQLException {
		
		conn = this.getConnection();
	
		String checkSQL1 = "" + "SELECT * FROM " + newTableName + " WHERE 0 > 1";
		final PreparedStatement ps = conn.prepareStatement(checkSQL1);		
		ps.executeQuery();
		ResultSet rs = ps.getResultSet();
		ResultSetMetaData srm = rs.getMetaData();
		
		int colct = srm.getColumnCount();
		final List<String> newColList = new ArrayList<>();
		for (int t = 1; t <= colct; t++) {
			String newCol = ps.getResultSet().getMetaData().getColumnName(t);
			newColList.add(newCol);
		}

		/*
		 * we need to match source columns (the new columns) against the previous
		 * columns
		 */
		String checkSQL2 = "" + "SELECT * FROM " + tableName + " WHERE 0 > 1";
		final PreparedStatement ps2 = conn.prepareStatement(checkSQL2);
		ps2.executeQuery();
		List<Object> colList = new ArrayList<>();
		colct = ps2.getResultSet().getMetaData().getColumnCount();
		for (int t = 1; t <= colct; t++) {
			String matchingCol = ps2.getResultSet().getMetaData().getColumnName(t);
			if (newColList.contains(matchingCol)) {
				// TODO: check data types... for now we just trust they match
				colList.add(matchingCol);
			}
		}
		String migrateSql = "INSERT INTO `" + tableName;
		migrateSql += "`(";
		for (Object col : colList) {
			migrateSql += "`" + String.valueOf(col) + "`";
			if (colList.indexOf(col) + 1 < colList.size()) {
				migrateSql += ",";
				// migrateSql += config.LINE_FEED;
			}
		}
		migrateSql += ")";
		migrateSql += " SELECT ";
		for (Object col : colList) {
			migrateSql += "`" + String.valueOf(col) + "`";
			if (colList.indexOf(col) + 1 < colList.size()) {
				migrateSql += ",";
				// migrateSql += config.LINE_FEED;
			}
		}
		migrateSql += " FROM `" + newTableName + "`";
		return migrateSql;
	}

	/**
	 * apply the generated DML to create the necessary IDX tables
	 *
	 * @param tl
	 * @return
	 * @throws SQLException
	 */
	public boolean createIDXTables(List<MyBatisJoin> tl) throws SQLException {
		if (!config.skipDbGen) {
			for (final MyBatisJoin j : tl) {
				final String tableName = j.myTable;
				logger.info("Creating IDX TABLE: " + tableName);

				// generate the table
				conn = this.getConnection();
			
				final PreparedStatement psx = conn.prepareStatement(j.getDML());
				try {
					psx.execute();
					psx.close();
				} catch (final Exception ex) {
					logger.error("Failed to createIDXTable table with DML: " + ex.toString());
					return false;
				}
			}
		}
		return true;
	}

	private boolean renameTable(String className, List<String> triedList, boolean addTimestamp) throws SQLException {
		Table table = new Table(config);
		if (!config.skipDbGen) {
			logger.info("RENAMING TABLE: " + className);
			triedList.add(className);
			// rename the table
			final String renameTableDML = table.generateTableRenameDML(className, addTimestamp);
			conn = this.getConnection();
			
			final PreparedStatement psx = conn.prepareStatement(renameTableDML);
			try {
				psx.execute();
				psx.close();
			} catch (final SQLException e) {
				if(e.toString().contains("already exists")) {
					// first, move the old BK table out of the way 
					renameTable(className, triedList, true);
					// now retry the original rename
					renameTable(className, triedList, false);
				}
			} catch (final Exception ex) {
				logger.error("Failed to drop table with DML: " + renameTableDML + "  : " + ex.toString());
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
	void createDatabaseTablesFromModelFolder() throws Exception {
		logger.info("Iterate Swagger Entities and create Tables...");
		final File[] modelFiles = Gen.getJavaFiles(config.getJavaGenSourceFolder() + "/" + config.getModelPackageDir(),
				false);
		final DBGen gen = new DBGen(getConfig());
		// classes, this should point to the top of the package
		// structure!
		final URL packagedir = new File(config.getJavaGenSourceFolder()).toURI().toURL();
		final URLClassLoader classLoader = new URLClassLoader(new URL[] { packagedir });
		logger.info("Created Classloader: " + classLoader);

		for (final File mf : modelFiles) {
			try {
				String cn = mf.getName().substring(0, mf.getName().indexOf("."));
				// cn = cn + ".class";
				cn = config.getIgniteModelPackage() + "." + cn;
				logger.info("Loading Classes from ModelFile: " + cn);
				// TODO: fails in web runner
				final Class<?> loadedClass = classLoader.loadClass(cn);

				DBGen.createTableFromClass(loadedClass, gen);
			} catch (final Exception e) {
				logger.error("Could not create Table for: " + mf.getName() + " :" + e.toString());
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
	public String decamelize(String name) {
		if (name.equals(name.toLowerCase()) || name.equals(name.toUpperCase())) { // case insensitive
			return name;
		}

		final StringBuilder sb = new StringBuilder();
		for (final char c : name.toCharArray()) {
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(c);
			} else {
				if (c != '_') {
					sb.append(c);
				}
			}
		}
		String ret = sb.toString();
		if (config.columnsUpperCase) {
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

	private Connection getConnection() throws SQLException {
		if (conn == null) {
			conn = ConnectionFactory.getConnection(
					config.dbDriver,
					config.dbUrl,
					config.dbName,
					config.dbUser,
					config.dbPassword
				);
		}
		return conn;
	}
	
	public static String upperCaseFirstLetter(String thismember) {
		String upcase = thismember.substring(0, 1);
		upcase = upcase.toUpperCase();
		thismember = thismember.substring(1);
		thismember = upcase + thismember;
		return thismember;
	}
}
