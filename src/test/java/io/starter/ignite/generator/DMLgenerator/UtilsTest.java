package io.starter.ignite.generator.DMLgenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import io.starter.ignite.generator.Configuration;
import io.starter.ignite.generator.DBGen;

/**
 * test the utils
 */
public class UtilsTest implements Configuration {

	@Test
	public void expectedColNameFail() {
		String[] colNames = { "rds_password" };
		for (String c : colNames) {
			String t = Table.convertToDBSyntax(c);
			String x = Table.convertToJavaSyntax(t);
			System.out.println("Comparing; " + c + " : " + x
					+ " <- NOTE: case sensitivity unsupported: underscores stripped");
			assertNotEquals(c, x);
		}
	}

	@Test
	public void roundTripProblems() {
		String[] colNames = { "userNameBeanDEV" };
		for (String c : colNames) {
			String t = Table.convertToDBSyntax(c);
			String x = Table.convertToJavaSyntax(t);
			assertEquals(c, x);
		}
	}

	@Test
	public void roundTripPx() {
		String[] colNames = { "Key_Version" };
		for (String c : colNames) {
			String t = Table.convertToDBSyntax(c);
			String x = Table.convertToJavaSyntax(t);
			String x1 = Table.convertToDBSyntax(t);
			assertEquals(t, "stackgen$_key_version");
			assertEquals(x, "KeyVersion");
			assertEquals(x1, "stackgen$_key_version");
		}
	}

	@Test
	public void roundTripDB() {
		String[] colNames = { "STACKGEN$IGNITE$USER_PREF_ACCESS.CTL",
				"STACKGEN$IGNITE$DBPASSWORD", "STACKGEN$IGNITE$IBN_DYT_BUNDLE" };
		for (String c : colNames) {
			String x = Table.convertToJavaSyntax(c);
			// enforce uppercase here
			String y = Table.convertToDBSyntax(x).toUpperCase();
			assertEquals(c, y);
		}
	}

	@Test
	public void roundTripJava() {
		String[] colNames = { "dbPassword", "userNameBean",
				"SOMEWorstCaseSCEnario" };
		for (String c : colNames) {
			String t = Table.convertToDBSyntax(c);
			String x = Table.convertToJavaSyntax(t);
			System.out.println("Comparing; " + c + " : " + x);
			assertEquals(c, x);
		}
	}

	@Test
	public void colNameConversion() {
		String colName1 = "rdspassword";
		String colName2 = "rds_db_name";
		String colName3 = "io.starter.ignite.secure_key";
		String t = Table.convertToDBSyntax(colName1);

		String t1 = DBGen.decamelize(colName1);

		String x = Table.convertToDBSyntax(colName2);
		String x1 = DBGen.decamelize(colName2);

		String a = Table.convertToDBSyntax(colName3);
		String a1 = DBGen.decamelize(colName3);

		assertEquals( "stackgen$rdspassword" , t);
	}

	@Test
	public void colNameDeConversion() {
		String colName3 = "IO.STARTER.IGNITE.SECURE_KEY";
		String a = Table.convertToJavaSyntax(colName3);
		String a1 = DBGen.camelize(colName3);
		assertEquals(a, "io.starter.ignite.secureKey"); // lost case
	}

	@Test
	public void colRDS_DB_NAMENameDeConversion() {

		String colName2 = "RDS_DB_NAME";
		String x = Table.convertToJavaSyntax(colName2);
		String x1 = DBGen.camelize(colName2);
		assertEquals(x, "rdsDbName"); // lost case
	}

	@Test
	public void colRDS_PASSWORDDeConversion() {
		String colName1 = "stackgen$rds_password";
		String t = Table.convertToJavaSyntax(colName1);
		String t1 = DBGen.camelize(colName1);
		assertEquals(t, "rdsPassword"); // lost case
	}

}