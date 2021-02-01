package io.starter.ignite.generator.DMLgenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.mysql.cj.protocol.Resultset;
import org.junit.Before;
import org.junit.Test;

import io.starter.ignite.generator.StackGenConfigurator;
import io.starter.ignite.generator.DBGen;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.*;
import java.util.Arrays;

/**
 * test the utils
 */
public class UtilsTest {

	StackGenConfigurator config = new StackGenConfigurator();
	Table table = new Table(config);
	DBGen dbgen = new DBGen(config);

	Connection cx = null;

	@Before
	public void setup() throws SQLException {
		config.setSchemaName("stackgen");
		config.setArtifactId ("stackgen");
		PreparedStatement psx = Mockito.mock(PreparedStatement.class);
		ResultSet rsx = Mockito.mock(java.sql.ResultSet.class);

		ResultSetMetaData rsxm = Mockito.mock(ResultSetMetaData.class);
		cx = Mockito.mock(java.sql.Connection.class);

		Mockito.when(cx.prepareStatement(Mockito.any())).thenReturn(psx);
		Mockito.when(psx.getResultSet()).thenReturn(rsx);

		Mockito.when(psx.executeQuery()).thenReturn(rsx);

		Mockito.when(rsx.getMetaData()).thenReturn(rsxm);

		Mockito.when(rsxm.getColumnCount()).thenReturn(3);
		Mockito.when(rsxm.getColumnName(1)).thenReturn("id");
		Mockito.when(rsxm.getColumnName(2)).thenReturn("first_name");
		Mockito.when(rsxm.getColumnName(3)).thenReturn("last_name");
	}

	@Test
	public void createMigrateDataSQL() throws Exception {

		String tableName = Table.RENAME_TABLE_PREFIX + "stackgen$_user";
		String targetTableName = "stackgen$_user";

		DBGen dbgen = new DBGen(config);
		dbgen.conn = cx;
		String result = dbgen.migrateDataSQL(tableName , targetTableName);
		assertEquals( "INSERT INTO `stackgen$_user`(`id`,`first_name`,`last_name`) SELECT `id`,`first_name`,`last_name` FROM `BK_stackgen$_user`", result);
	}

	@Test
	public void expectedColNameFail() {
		String[] colNames = { "rds_password" };
		for (String c : colNames) {
			String t = table.convertToDBSyntax(c);
			String x = table.convertToJavaSyntax(t);
			System.out.println("Comparing; " + c + " : " + x
					+ " <- NOTE: case sensitivity unsupported: underscores stripped");
			assertNotEquals(c, x);
		}
	}

	@Test
	public void roundTripProblems() {
		String[] colNames = { "userNameBeanDEV" };
		for (String c : colNames) {
			String t = table.convertToDBSyntax(c);
			String x = table.convertToJavaSyntax(t);
			assertEquals(c, x);
		}
	}

	@Test
	public void roundTripPx() {
		String[] colNames = { "Key_Version" };
		for (String c : colNames) {
			String t = table.convertToDBSyntax(c);
			String x = table.convertToJavaSyntax(t);
			String x1 = table.convertToDBSyntax(t);
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
			String x = table.convertToJavaSyntax(c);
			// enforce uppercase here
			String y = table.convertToDBSyntax(x).toUpperCase();
			assertEquals(c, y);
		}
	}

	@Test
	public void roundTripJava() {
		String[] colNames = { "dbPassword", "userNameBean",
				"SOMEWorstCaseSCEnario" };
		for (String c : colNames) {
			String t = table.convertToDBSyntax(c);
			String x = table.convertToJavaSyntax(t);
			System.out.println("Comparing; " + c + " : " + x);
			assertEquals(c, x);
		}
	}

	@Test
	public void colNameConversion() {
		String colName1 = "rdspassword";
		String t = table.convertToDBSyntax(colName1);

		assertEquals( "stackgen$rdspassword" , t);
	}

	@Test
	public void colNameDeConversion() {
		String colName3 = "IO.STARTER.IGNITE.SECURE_KEY";
		String a = table.convertToJavaSyntax(colName3);
		assertEquals(a, "io.starter.ignite.secureKey"); // lost case
	}

	@Test
	public void colRDS_DB_NAMENameDeConversion() {

		String colName2 = "RDS_DB_NAME";
		String x = table.convertToJavaSyntax(colName2);
		assertEquals(x, "rdsDbName"); // lost case
	}

	@Test
	public void colRDS_PASSWORDDeConversion() {
		String colName1 = "stackgen$rds_password";
		String t = table.convertToJavaSyntax(colName1);
		String t1 = DBGen.camelize(colName1);
		assertEquals(t, "rdsPassword"); // lost case
	}
}