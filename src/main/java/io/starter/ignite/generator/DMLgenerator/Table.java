package io.starter.ignite.generator.DMLgenerator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.starter.ignite.generator.StackGenConfigurator;
import io.starter.ignite.generator.DBGen;
import io.starter.ignite.util.SystemConstants;

/**
 * Defines Table DML
 *
 * <pre>
 * CREATE TABLE `humorme`.`template 1` (
 *   `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Auto Incrementing PK',
 *   `one` VARCHAR(45) NULL,
 *   `create_date` DATETIME NULL DEFAULT NOW() COMMENT 'Creation Date of Record',
 *   `blob` BLOB NULL COMMENT 'Data BLOB Column',
 *   PRIMARY KEY (`id`),
 *   UNIQUE INDEX `id_UNIQUE` (`id` ASC));
 * </pre>
 */

public class Table {

	public static final Map<String, String> myMap;
	
	public static String LINE_FEED = "\r\n";
	private DBGen dbg;
	private StackGenConfigurator config;
	
	public Table(StackGenConfigurator cfg) {
		this.config = cfg;
		this.dbg = new DBGen(cfg);
	}

	public static String CREATE_TABLE = "CREATE TABLE";
	public static String CREATE_TABLE_BEGIN_BLOCK = "(";
	public static String CREATE_TABLE_END_BLOCK = ");";
	public static String DROP_TABLE = "DROP TABLE";
	public static String ALTER_TABLE = "ALTER TABLE";
	public static String RENAME_TABLE_PREFIX = "BK_";
	public static String TUPLE_TABLE_SUFFIX = "_idx";

	static {
		final Map<String, String> aMap = new HashMap<>();

		final int precision = 10;
		aMap.put("Integer.fkid", "BIGINT(" + precision
				+ ") UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Auto generated Incrementing PK - do not update'");

		aMap.put("IDX_TEMPLATE", "CREATE TABLE `${IDX_TABLE}` (\n" + "  `id` BIGINT(" + precision
				+ ") UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Auto generated Incrementing PK - do not update',\n"
				+ "  `${MY_TABLE}_id` int(11) DEFAULT -1,\n" + "  `${REF_TABLE}_id` int(11) DEFAULT -1,\n"
				+ "  PRIMARY KEY (`id`),\n" + "  UNIQUE KEY `StackGenUQIDX` (`${MY_TABLE}_id`,`${REF_TABLE}_id`))");

		aMap.put("Timestamp.createdDate",
				" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Auto create record create date - do not update'");
		aMap.put("Timestamp.modifiedDate",
				"  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Auto update record modification date - do not update'");

		// Standard Data Type Handling
		aMap.put("Boolean", "TINYINT(1) ${NOT_NULL} ${COMMENT}");
		aMap.put("Enum", "VARCHAR(256) ${DEFAULT} COMMENT \"ENUM:${MY_TABLE}: StackGen\"");
		aMap.put("Integer", "INTEGER ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("int", "INTEGER ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("String", "VARCHAR(${MAX_LENGTH}) ${NOT_NULL} ${CHAR_SET} ${DEFAULT} ${COMMENT}");
		aMap.put("Text", "LONGTEXT ${CHAR_SET} ${DEFAULT} ${COMMENT}");
		aMap.put("Double", "DOUBLE ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("Long", "BIGINT(" + precision + ") UNSIGNED ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("Date", "DATE ${NOT_NULL} ${COMMENT}");
		aMap.put("LocalDate", "DATE ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("OffsetDateTime", "TIMESTAMP ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("Integer.pkid", "'id' BIGINT(" + precision + ") SIGNED AUTO_INCREMENT," + StackGenConfigurator.LINE_FEED
				+ "PRIMARY KEY (`id`), COMMENT 'Ignite-generated Integer.pkid'");
		aMap.put("pkid", "PRIMARY KEY (`ID`), UNIQUE INDEX `ID_UNIQUE` (`ID` ASC));");
		myMap = Collections.unmodifiableMap(aMap);
	}

	public String generateTableDropDML(String tableName) {
		tableName = convertToDBSyntax(tableName);
		return DROP_TABLE + " " + tableName + LINE_FEED;
	}

	/**
	 * ALTER TABLE `ignite`.`ignite$user` RENAME TO `ignite`.`ignite$user_old` ;
	 * 
	 * @param tableName
	 * @return
	 */
	public String generateTableRenameDML(String tableName) {
		tableName = convertToDBSyntax(tableName);
		String dml = ALTER_TABLE + " " + tableName + LINE_FEED;
		dml += " RENAME TO " + RENAME_TABLE_PREFIX + tableName + "_" + System.currentTimeMillis();
		return dml;

	}

	public String generateTableBeginningDML(String tableName) {
		tableName = convertToDBSyntax(tableName);
		return CREATE_TABLE + " " + config.getSchemaName() + "." + tableName + CREATE_TABLE_BEGIN_BLOCK
				+ LINE_FEED;
	}

	/**
	 * this will result in a decamelized and force-cased name.
	 *
	 * @param colName
	 * @return
	 */
	public String convertToDBSyntax(String colName) {
		colName = dbg.decamelize(colName);
		if (!colName.startsWith(config.getTableNamePrefix())) {
			colName = config.getTableNamePrefix() + colName;
		}
		if (config.columnsUpperCase) {
			colName = colName.toUpperCase();
		} else {
			colName = colName.toLowerCase();
		}
		return colName.trim();
	}

	public String convertToJavaSyntax(String colName) {
		String rep = config.getTableNamePrefix();
		if (config.columnsUpperCase) {
			colName = colName.toUpperCase();
			rep = rep.toUpperCase();
		} else {
			colName = colName.toLowerCase();
			rep = rep.toLowerCase();
		}
		colName = colName.replace(rep, "");
		colName = colName.toLowerCase();
		colName = DBGen.camelize(colName);
		return colName.trim();
	}

}