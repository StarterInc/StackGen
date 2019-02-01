package io.starter.ignite.generator.DMLgenerator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.starter.ignite.generator.Configuration;
import io.starter.ignite.generator.DBGen;

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

public class Table implements Configuration {

	public static final Map<String, String> myMap;

	static {
		Map<String, String> aMap = new HashMap<String, String>();

		int precision = 10;
		aMap.put("Integer.fkid", "BIGINT(" + precision
				+ ") UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Auto generated Incrementing PK - do not update'");
		aMap.put("Timestamp.createdDate", " TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Auto create record create date - do not update'");
		aMap.put("Timestamp.modifiedDate", "  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Auto update record modification date - do not update'");

		// Standard Data Type Handling
		aMap.put("Boolean", "BOOLEAN ${NOT_NULL} ${COMMENT}");
		aMap.put("Enum", "VARCHAR(256) ${DEFAULT} COMMENT \"ENUM field to store values\"");
		aMap.put("Integer", "INTEGER ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("int", "INTEGER ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("String", "VARCHAR(${MAX_LENGTH}) ${NOT_NULL} ${CHAR_SET} ${DEFAULT} ${COMMENT}");
		aMap.put("Text", "TEXT ${CHAR_SET} ${DEFAULT} ${COMMENT}");
		aMap.put("Double", "DOUBLE ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("Long", "BIGINT(" + precision
				+ ") UNSIGNED ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("Date", "DATE ${NOT_NULL} ${COMMENT}");
		aMap.put("LocalDate", "DATE ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("OffsetDateTime", "TIMESTAMP ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("Integer.pkid", "'id' BIGINT(" + precision
				+ ") SIGNED AUTO_INCREMENT," + Configuration.LINE_FEED
				+ "PRIMARY KEY (`id`), COMMENT 'Ignite-generated Integer.pkid'");
		aMap.put("pkid", "PRIMARY KEY (`ID`), UNIQUE INDEX `ID_UNIQUE` (`ID` ASC));");
		myMap = Collections.unmodifiableMap(aMap);
	}

	public static String generateTableDropDML(String tableName) {
		tableName = convertToDBSyntax(tableName);
		return DROP_TABLE + " " + tableName + Configuration.LINE_FEED;
	}

	/**
	  	ALTER TABLE `ignite`.`ignite$user` 
		RENAME TO  `ignite`.`ignite$user_old` ;
	 * @param tableName
	 * @return
	 */
	public static String generateTableRenameDML(String tableName) {
		tableName = convertToDBSyntax(tableName);
		String dml = ALTER_TABLE + " " + tableName + Configuration.LINE_FEED;
		dml += " RENAME TO " + RENAME_TABLE_SUFFIX + tableName + "_"
				+ System.currentTimeMillis();
		return dml;

	}

	public static String generateTableBeginningDML(String tableName) {
		tableName = convertToDBSyntax(tableName);
		return CREATE_TABLE + " " + tableName + CREATE_TABLE_BEGIN_BLOCK
				+ Configuration.LINE_FEED;
	}

	/**
	 * this will result in a decamelized and force-cased name.
	 * 
	 * @param colName
	 * @return
	 */
	public static String convertToDBSyntax(String colName) {
		colName = DBGen.decamelize(colName);
		if (!colName.startsWith(TABLE_NAME_PREFIX)) {
			colName = TABLE_NAME_PREFIX + colName;
		}
		if (columnsUpperCase) {
			colName = colName.toUpperCase();
		} else {
			colName = colName.toLowerCase();
		}
		return colName.trim();
	}

	public static String convertToJavaSyntax(String colName) {
		String rep = TABLE_NAME_PREFIX;
		if (columnsUpperCase) {
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