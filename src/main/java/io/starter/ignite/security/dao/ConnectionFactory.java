package io.starter.ignite.security.dao;

import io.starter.ignite.util.SystemConstants;
import io.starter.ignite.util.Logger;
import io.starter.ignite.util.SystemConstants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 *
 * <p>
 * Title: ConnectionFactory.java
 * </p>
 *
 * @version 1.5
 */
public class ConnectionFactory {
	// Connect to the data storage
	private static int sourcePort = 3306;
	public static String driverName = "com.mysql.jdbc.Driver";
	public static String dbName = SystemConstants.DB_NAME;
	public static String sourceURL = SystemConstants.DB_URL;
	public static String userName = SystemConstants.DB_USER;
	private static String password = SystemConstants.DB_PASSWORD;
	private static String backupURL = SystemConstants.DB_URL;
	private static String backupPassword = SystemConstants.DB_PASSWORD;

	// Call the private constructor to initialize the DriverManager
	@SuppressWarnings("unused")
	private static ConnectionFactory ref = new ConnectionFactory();

	public static String toConfigString() {
		return "ConnectionFactory v." + SystemConstants.IGNITE_MAJOR_VERSION + "."
				+ SystemConstants.IGNITE_MINOR_VERSION + " \r\n" + "Settings:" + "\r\n" + "=========" + "\r\n"
				+ driverName + "\r\n" + sourceURL + "\r\n" + dbName + "\r\n" + userName;
	}

	/**
	 * Private default constructor No outside objects can create an object of this
	 * class This constructor initializes the DriverManager by loading the driver
	 * for the database
	 */
	private ConnectionFactory() {

		Logger.debug("ConnectionFactory: initializing:" + ConnectionFactory.driverName);

		try {

			Class.forName(ConnectionFactory.driverName);
			Logger.debug("ConnectionFactory: Got JDBC class " + ConnectionFactory.driverName + " OK!");
		} catch (final ClassNotFoundException e) {

			Logger.error("ConnectionFactory: Exception loading driver class: " + e.toString());
		} // end try-catch

	} // end default private constructor

	/**
	 * Get and return a Connection object that can be used to connect to the data
	 * storage
	 *
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {

		Connection con = null;

		Logger.debug("ConnectionFactory: returning connection:" + ConnectionFactory.sourceURL);
		final PoolProperties p = new PoolProperties();

		if (true) { // (System.getProperty("PARAM1") != null &&
			// System.getProperty("PARAM1").equalsIgnoreCase("production"))
			// {

			try {
				final InitialContext ic = new InitialContext();
				final DataSource dataSource = (DataSource) ic.lookup(SystemConstants.JNDI_DB_LOOKUP_STRING);
				final java.sql.Connection c = dataSource.getConnection();
				Logger.debug("ConnectionFactory.getConnection() SUCCESSFUL JNDI connection: "
						+ SystemConstants.JNDI_DB_LOOKUP_STRING + ".");
				return c;
			} catch (final Exception e) {
				Logger.warn("ConnectionFactory.getConnection() failed to get JNDI connection: ["
						+ SystemConstants.JNDI_DB_LOOKUP_STRING + "] " + "Falling back to non JNDI connection.");

			}

			p.setUrl(ConnectionFactory.sourceURL);
			p.setPassword(ConnectionFactory.password);
		} else {
			p.setUrl(ConnectionFactory.backupURL);
			p.setPassword(ConnectionFactory.backupPassword);

		}

		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername(ConnectionFactory.userName);

		p.setAlternateUsernameAllowed(true);
		p.setJmxEnabled(true);
		p.setTestWhileIdle(true);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(6);
		p.setInitialSize(1);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(5);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

		try {
			final org.apache.tomcat.jdbc.pool.DataSource datasource = (org.apache.tomcat.jdbc.pool.DataSource) ConnectionFactory
					.getDataSource();
			datasource.setPoolProperties(p);
			try {
				con = datasource.getConnectionAsync().get();
				con = datasource.getConnectionAsync().get();
				final Statement st = con.createStatement();
				final ResultSet rs = st.executeQuery("SELECT 1 FROM DUAL"); // Oracle/MySQL only
				while (rs.next()) {
					//
				}
				rs.close();
				st.close();
			} finally {
				if (con != null) {
					try {
						con.close();
					} catch (final Exception ignore) {
					}
				}
			}
		} catch (final Exception e) {
			Logger.warn(
					"ConnectionFactory Failed to achieve a Pooled DataSource... reverting to Non-Pooled JDBC connection. NOT FOR PRODUCTION.");
			return ConnectionFactory.getDataSource().getConnection();
		}
		return con;
	}

	/**
	 * wrap a datasource with a tomcat jdbc pool Connection con = null;
	 *
	 * TODO: implement FUTURE connections try { Future<Connection> future =
	 * datasource.getConnectionAsync(); while (!future.isDone()) {
	 * io.starter.ignite.util.Logger.log( "Connection is not yet available. Do some
	 * background work"); try { Thread.sleep(100); //simulate work }catch
	 * (InterruptedException x) { Thread.currentThread().interrupt(); } } con =
	 * future.get(); //should return instantly Statement st = con.createStatement();
	 * ResultSet rs = st.executeQuery("select * from user");
	 *
	 * @return
	 */
	public static DataSource getDataSource() {
		final MysqlDataSource ds = new MysqlDataSource();
		final org.apache.tomcat.jdbc.pool.DataSource dsx = new org.apache.tomcat.jdbc.pool.DataSource();

		final PoolProperties p = new PoolProperties();

		String lcname = System.getProperty("PARAM1");
		if (lcname != null) {

			if (lcname.equalsIgnoreCase("production")) {

				lcname = SystemConstants.JNDI_DB_LOOKUP_STRING;
			} else if (lcname.equalsIgnoreCase("staging")) {

				lcname = SystemConstants.JNDI_DB_LOOKUP_STRING + "_staging";
			}

			try {
				final InitialContext ic = new InitialContext();
				DataSource dataSource = (DataSource) ic.lookup(lcname);
				final java.sql.Connection c = dataSource.getConnection();
				Logger.log("ConnectionFactory.getConnection() SUCCESSFUL JNDI connection: " + lcname + ".");
				return dataSource;

			} catch (final Exception e) {
				Logger.warn("ConnectionFactory.getConnection() failed to get JNDI connection: " + lcname + ". "
						+ e.toString() + " Falling back to non JNDI connection.");

				ds.setServerName(ConnectionFactory.sourceURL);
				ds.setPassword(ConnectionFactory.password);
				p.setUrl(ConnectionFactory.sourceURL);

			}

		} else {
			ds.setServerName(ConnectionFactory.backupURL);
			ds.setPassword(ConnectionFactory.backupPassword);
			p.setUrl(ConnectionFactory.backupURL);
			p.setPassword(ConnectionFactory.backupPassword);
		}

		ds.setPort(ConnectionFactory.sourcePort);
		ds.setDatabaseName(ConnectionFactory.dbName);
		ds.setUser(ConnectionFactory.userName);

		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername(ConnectionFactory.userName);

		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

		// wrap and allow for lazy queue fun
		dsx.setPoolProperties(p);

		dsx.setFairQueue(true);
		dsx.setDataSource(ds);

		return dsx;
	}

	/**
	 * Close the ResultSet
	 *
	 * @param rs
	 *            ResultSet
	 */
	public static void close(ResultSet rs) {

		try {
			rs.close();
		} catch (final SQLException e) {
			System.err.println("ERROR: Unable to close Result Set");
			System.err.println(e.getMessage());
		} // end try-catch block

	} // end method close

	/**
	 * Close statement object
	 *
	 * @param stmt
	 *            Statement
	 */
	public static void close(Statement stmt) {

		try {

			stmt.close();

		} catch (final SQLException e) {

			System.err.println("ERROR: Unable to close Statement");
			System.err.println(e.getMessage());

		} // end try-catch block

	} // end method close

	/**
	 * Close connection
	 *
	 * @param conn
	 *            Connection
	 */
	public static void close(Connection conn) {

		try {

			conn.close();
			conn = null;

		} catch (final SQLException e) {

			io.starter.ignite.util.Logger.log("ERROR: Unable to close Statement");
			io.starter.ignite.util.Logger.log(e.getMessage());

		} // end try-catch block

	} // end method close

} // end class ConnectionFactory
