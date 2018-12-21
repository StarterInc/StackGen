package io.starter.ignite.generator.DMLgenerator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.starter.ignite.generator.Configuration;
import io.starter.toolkit.StringTool;

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
				+ ") SIGNED AUTO_INCREMENT," + "/r/n"
				+ "PRIMARY KEY (`id`), COMMENT 'Ignite-generated Integer.pkid'");
		aMap.put("pkid", "PRIMARY KEY (`ID`), UNIQUE INDEX `ID_UNIQUE` (`ID` ASC));");
		myMap = Collections.unmodifiableMap(aMap);
	}

	public static String generateTableDropDML(String tableName) {
		tableName = convertToDBSyntax(tableName);
		return DROP_TABLE + " " + tableName + " \r\n";
	}

	/**
	  	ALTER TABLE `ignite`.`ignite$user` 
		RENAME TO  `ignite`.`ignite$user_old` ;
	 * @param tableName
	 * @return
	 */
	public static String generateTableRenameDML(String tableName) {
		tableName = convertToDBSyntax(tableName);
		String dml = ALTER_TABLE + " " + tableName + " \r\n";
		dml += " RENAME TO " + RENAME_TABLE_SUFFIX + tableName + "_"
				+ System.currentTimeMillis();
		return dml;

	}

	public static String generateTableBeginningDML(String tableName) {
		tableName = convertToDBSyntax(tableName);
		return CREATE_TABLE + " " + tableName + CREATE_TABLE_BEGIN_BLOCK
				+ " \r\n";
	}

	public static String convertToDBSyntax(String tableName) {
		tableName = TABLE_NAME_PREFIX
				+ StringTool.convertJavaStyletoDBConvention(tableName);
		if (SETTING_COLUMNS_UPPERCASED) {
			tableName = tableName.toUpperCase();
		} else {
			tableName = tableName.toLowerCase();
		}
		return tableName;
	}

}