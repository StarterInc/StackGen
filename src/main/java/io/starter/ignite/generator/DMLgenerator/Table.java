package io.starter.ignite.generator.DMLgenerator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.starter.ignite.generator.Configuration;
import io.starter.toolkit.StringTool;

/**
 * Create Table: CREATE TABLE `big_table` ( `TABLE_CATALOG` varchar(512)
 * ${CHAR_SET} NOT NULL ${DEFAULT}, `TABLE_SCHEMA` varchar(64) CHARACTER
 * SET utf8 NOT NULL ${DEFAULT}, `TABLE_NAME` varchar(64) ${CHAR_SET} NOT
 * NULL ${DEFAULT}, `COLUMN_NAME` varchar(64) ${CHAR_SET} NOT NULL
 * ${DEFAULT}, `ORDINAL_POSITION` BIGINT(21) unsigned NOT NULL ${DEFAULT},
 * `COLUMN_DEFAULT` longtext ${CHAR_SET}, `IS_NULLABLE` varchar(3)
 * ${CHAR_SET} NOT NULL ${DEFAULT}, `DATA_TYPE` varchar(64) CHARACTER SET
 * utf8 NOT NULL ${DEFAULT}, `CHARACTER_MAXIMUM_LENGTH` BIGINT(21) unsigned
 * DEFAULT NULL, `CHARACTER_OCTET_LENGTH` BIGINT(21) unsigned DEFAULT NULL,
 * `NUMERIC_PRECISION` BIGINT(21) unsigned DEFAULT NULL, `NUMERIC_SCALE`
 * BIGINT(21) unsigned DEFAULT NULL, `DATETIME_PRECISION` BIGINT(21) unsigned
 * DEFAULT NULL, `CHARACTER_SET_NAME` varchar(32) ${CHAR_SET} DEFAULT
 * NULL, `COLLATION_NAME` varchar(32) ${CHAR_SET} DEFAULT NULL,
 * `COLUMN_TYPE` longtext ${CHAR_SET} NOT NULL, `COLUMN_KEY` varchar(3)
 * ${CHAR_SET} NOT NULL ${DEFAULT}, `EXTRA` varchar(30) CHARACTER SET
 * utf8 NOT NULL ${DEFAULT}, `PRIVILEGES` varchar(80) ${CHAR_SET} NOT
 * NULL ${DEFAULT}, `COLUMN_COMMENT` varchar(10" + precision + ") ${CHAR_SET} NOT NULL
 * ${DEFAULT} ) ENGINE=InnoDB DEFAULT CHARSET=latin1
 * 
 *
 * <pre>
 * CREATE TABLE `humorme`.`template 1` (
 *   `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Auto Incrementing PK',
 *   `one` VARCHAR(45) NULL,
 *   `create_date` DATETIME NULL DEFAULT NOW() COMMENT 'Creation Date of Record',
 *   `blob` BLOB NULL COMMENT 'Data BLOB Column',
 *   PRIMARY KEY (`id`),
 *   UNIQUE INDEX `id_UNIQUE` (`id` ASC));
 * 
 * 
 * 
 * </pre>
 */

public class Table implements Configuration {

	public static final Map<String, String>	myMap;
	public static final boolean				SETTING_COLUMNS_UPPERCASED	= false;

	static {
		Map<String, String> aMap = new HashMap<String, String>();

		int precision = 10;
		aMap.put("Integer.fkid", "BIGINT(" + precision
				+ ") UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Auto Incrementing PK'");

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

		aMap.put("OffsetDateTime", "TIMESTAMP ${NOT_NULL} ${DEFAULT} ${COMMENT}");

		aMap.put("Integer.pkid", "'id' BIGINT(" + precision
				+ ") SIGNED AUTO_INCREMENT," + "/r/n"
				+ "PRIMARY KEY (`id`), COMMENT 'Ignite-generated Integer.pkid'");

		aMap.put("pkid", "PRIMARY KEY (`ID`), UNIQUE INDEX `ID_UNIQUE` (`ID` ASC));");

		myMap = Collections.unmodifiableMap(aMap);
	}

	public static String generateTableDropDML(String className) {
		className = convertToDBSyntax(className);
		return DROP_TABLE + " " + className + " \r\n";
	}

	public static String generateTableBeginningDML(String className) {

		className = convertToDBSyntax(className);

		return CREATE_TABLE + " " + className + CREATE_TABLE_BEGIN_BLOCK
				+ " \r\n";
	}

	public static String convertToDBSyntax(String className) {
		className = TABLE_NAME_PREFIX
				+ StringTool.convertJavaStyletoDBConvention(className);
		if (SETTING_COLUMNS_UPPERCASED)
			className = className.toUpperCase();
		else
			className = className.toLowerCase();
		return className;
	}

}