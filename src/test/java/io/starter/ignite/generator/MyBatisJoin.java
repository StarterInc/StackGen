package io.starter.ignite.generator;

/**
 * handle the generation of the "glue" between 2 tables using joins and optionally an "IDX" table to create
 * many-to-many relationships
 * 
 * @author john
 *
 */
public class MyBatisJoin {

	private static String	DML_TEMPLATE	= "CREATE TABLE {t1}_{t2}_IDX {xx, xx, xx}";

	private static String	XML_TEMPLATE	= "	 userId;" + "roleId;";

	String					joinTable		= "";
	String					joinColumn		= "";
	String					myTable			= "";
	String					myColumn		= "";

	public MyBatisJoin(String field, String[] tables) {
		myColumn = field;

	}

	public String getXMLFragment() {
		return "<widg>" + myColumn + "</widg>";
	}

	public String getDML() {
		return DBGen.CREATE_TABLE + joinTable + "_" + myTable + "_IDX"
				+ DBGen.CREATE_TABLE_BEGIN_BLOCK + DBGen.CREATE_TABLE_END_BLOCK;
	}

}
