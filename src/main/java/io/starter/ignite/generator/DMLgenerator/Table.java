package io.starter.ignite.generator.DMLgenerator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.starter.ignite.generator.Configuration;
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

public class Table implements Configuration {

	public static final Map<String, String> myMap;

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
		aMap.put("Text", "TEXT ${CHAR_SET} ${DEFAULT} ${COMMENT}");
		aMap.put("Double", "DOUBLE ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("Long", "BIGINT(" + precision + ") UNSIGNED ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("Date", "DATE ${NOT_NULL} ${COMMENT}");
		aMap.put("LocalDate", "DATE ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("OffsetDateTime", "TIMESTAMP ${NOT_NULL} ${DEFAULT} ${COMMENT}");
		aMap.put("Integer.pkid", "'id' BIGINT(" + precision + ") SIGNED AUTO_INCREMENT," + Configuration.LINE_FEED
				+ "PRIMARY KEY (`id`), COMMENT 'Ignite-generated Integer.pkid'");
		aMap.put("pkid", "PRIMARY KEY (`ID`), UNIQUE INDEX `ID_UNIQUE` (`ID` ASC));");
		myMap = Collections.unmodifiableMap(aMap);
	}

	public static String generateTableDropDML(String tableName) {
		tableName = Table.convertToDBSyntax(tableName);
		return Configuration.DROP_TABLE + " " + tableName + Configuration.LINE_FEED;
	}

	/**
	 * ALTER TABLE `ignite`.`ignite$user` RENAME TO `ignite`.`ignite$user_old` ;
	 * 
	 * @param tableName
	 * @return
	 */
	public static String generateTableRenameDML(String tableName) {
		tableName = Table.convertToDBSyntax(tableName);
		String dml = Configuration.ALTER_TABLE + " " + tableName + Configuration.LINE_FEED;
		dml += " RENAME TO " + Configuration.RENAME_TABLE_PREFIX + tableName + "_" + System.currentTimeMillis();
		return dml;

	}

	public static String generateTableBeginningDML(String tableName) {
		tableName = Table.convertToDBSyntax(tableName);
		return Configuration.CREATE_TABLE + " " + SystemConstants.dbName + "." + tableName
				+ Configuration.CREATE_TABLE_BEGIN_BLOCK + Configuration.LINE_FEED;
	}

	/**
	 * this will result in a decamelized and force-cased name.
	 *
	 * @param colName
	 * @return
	 */
	public static String convertToDBSyntax(String colName) {
		colName = DBGen.decamelize(colName);
		if (!colName.startsWith(Configuration.TABLE_NAME_PREFIX)) {
			colName = Configuration.TABLE_NAME_PREFIX + colName;
		}
		if (Configuration.columnsUpperCase) {
			colName = colName.toUpperCase();
		} else {
			colName = colName.toLowerCase();
		}
		return colName.trim();
	}

	public static String convertToJavaSyntax(String colName) {
		String rep = Configuration.TABLE_NAME_PREFIX;
		if (Configuration.columnsUpperCase) {
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