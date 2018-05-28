package io.starter.ignite.generator.DMLgenerator;

import java.util.*;

/**
 * Create Table: CREATE TABLE `big_table` ( `TABLE_CATALOG` varchar(512)
 * CHARACTER SET utf8 NOT NULL DEFAULT '', `TABLE_SCHEMA` varchar(64) CHARACTER
 * SET utf8 NOT NULL DEFAULT '', `TABLE_NAME` varchar(64) CHARACTER SET utf8 NOT
 * NULL DEFAULT '', `COLUMN_NAME` varchar(64) CHARACTER SET utf8 NOT NULL
 * DEFAULT '', `ORDINAL_POSITION` bigint(21) unsigned NOT NULL DEFAULT '0',
 * `COLUMN_DEFAULT` longtext CHARACTER SET utf8, `IS_NULLABLE` varchar(3)
 * CHARACTER SET utf8 NOT NULL DEFAULT '', `DATA_TYPE` varchar(64) CHARACTER SET
 * utf8 NOT NULL DEFAULT '', `CHARACTER_MAXIMUM_LENGTH` bigint(21) unsigned
 * DEFAULT NULL, `CHARACTER_OCTET_LENGTH` bigint(21) unsigned DEFAULT NULL,
 * `NUMERIC_PRECISION` bigint(21) unsigned DEFAULT NULL, `NUMERIC_SCALE`
 * bigint(21) unsigned DEFAULT NULL, `DATETIME_PRECISION` bigint(21) unsigned
 * DEFAULT NULL, `CHARACTER_SET_NAME` varchar(32) CHARACTER SET utf8 DEFAULT
 * NULL, `COLLATION_NAME` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
 * `COLUMN_TYPE` longtext CHARACTER SET utf8 NOT NULL, `COLUMN_KEY` varchar(3)
 * CHARACTER SET utf8 NOT NULL DEFAULT '', `EXTRA` varchar(30) CHARACTER SET
 * utf8 NOT NULL DEFAULT '', `PRIVILEGES` varchar(80) CHARACTER SET utf8 NOT
 * NULL DEFAULT '', `COLUMN_COMMENT` varchar(1024) CHARACTER SET utf8 NOT NULL
 * DEFAULT '' ) ENGINE=InnoDB DEFAULT CHARSET=latin1
 * 
 *
 *
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

public class Table {

	public static final Map<String, String> myMap;
	public static final boolean SETTING_COLUMNS_UPPERCASED = false;

	static {
		Map<String, String> aMap = new HashMap<String, String>();

		aMap.put("Integer.fkid",
				"INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Auto Incrementing PK'");
		aMap.put("Integer",
				"bigint(21) unsigned NOT NULL DEFAULT '0' COMMENT 'Auto GEN Integer'");
		aMap.put("int",
				"bigint(21) unsigned NOT NULL DEFAULT '0' COMMENT 'Auto GEN int'");
		aMap.put(
				"String",
				"varchar(1024) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT 'Auto GEN String'");
		aMap.put("Double",
				"bigint(21) unsigned DEFAULT NULL DEFAULT 0.0 COMMENT 'Auto GEN Double'");
		aMap.put("Long",
				"bigint(21) unsigned DEFAULT NULL DEFAULT 0 COMMENT 'Auto GEN Long'");
		aMap.put("Date",
				"Date unsigned DEFAULT NULL DEFAULT '' COMMENT 'Auto GEN Date'");
		aMap.put("Ingeter.pkid",
				"`id` int(14) unsigned NOT NULL AUTO_INCREMENT," + "/r/n"
						+ "PRIMARY KEY (`id`), COMMENT 'Auto GEN Integer.pkid'");

		aMap.put("pkid",
				"PRIMARY KEY (`ID`), UNIQUE INDEX `ID_UNIQUE` (`ID` ASC));");

		myMap = Collections.unmodifiableMap(aMap);
	}

	public static String CREATE_TABLE = "CREATE TABLE";
	public static String CREATE_TABLE_BEGIN_BLOCK = "(";
	public static String CREATE_TABLE_END_BLOCK = ");";

	public static String generateTableBeginningDML(String className) {

		if (SETTING_COLUMNS_UPPERCASED)
			className = className.toUpperCase();
		else
			className = className.toLowerCase();

		String dml = CREATE_TABLE + " " + className + CREATE_TABLE_BEGIN_BLOCK
				+ " \r\n";
		return dml;
	}
}