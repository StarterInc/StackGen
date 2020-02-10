package io.starter.ignite.security.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.Configuration;
import io.starter.ignite.util.SystemConstants;

/**
 *
 * get connections using MyBatisConfig.xml settings
 *
 * @version 1.5
 */
public class ConnectionFactory {

	protected static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

	// Connect to the data storage
	private static int sourcePort = 3306;
	private static String driverName = SystemConstants.dbDriver;
	private static String dbName = SystemConstants.dbName;
	private static String sourceURL = SystemConstants.dbUrl;
	private static String userName = SystemConstants.dbUser;
	private static String password = SystemConstants.dbPassword;
	private static String backupURL = SystemConstants.dbUrl;
	private static String backupPassword = SystemConstants.dbPassword;

	private static DataSource dsx = null;

	// Call the private constructor to initialize the
	// DriverManager
	@SuppressWarnings("unused")
	public static ConnectionFactory instance = new ConnectionFactory();

	public static String toConfigString() {
		return "ConnectionFactory v." + SystemConstants.IGNITE_MAJOR_VERSION + "."
				+ SystemConstants.IGNITE_MINOR_VERSION + Configuration.LINE_FEED + "Settings:" + Configuration.LINE_FEED
				+ "=========" + Configuration.LINE_FEED + ConnectionFactory.driverName + Configuration.LINE_FEED
				+ ConnectionFactory.sourceURL + Configuration.LINE_FEED + ConnectionFactory.dbName
				+ Configuration.LINE_FEED + ConnectionFactory.userName;
	}

	/**
	 * Private default constructor No outside objects can create an object of this
	 * class This constructor initializes the DriverManager by loading the driver
	 * for the database
	 */
	private ConnectionFactory() {

		ConnectionFactory.logger.info("ConnectionFactory: initializing:" + ConnectionFactory.driverName);

		try {

			Class.forName(ConnectionFactory.driverName);
			ConnectionFactory.logger.info("ConnectionFactory: Got JDBC class " + ConnectionFactory.driverName + " OK!");
		} catch (final ClassNotFoundException e) {

			ConnectionFactory.logger.error("ConnectionFactory: Exception loading driver class: " + e.toString());
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
		final Connection cx = DriverManager.getConnection(ConnectionFactory.sourceURL + "/" + ConnectionFactory.dbName
				+ "?" + "user=" + ConnectionFactory.userName + "&password=" + ConnectionFactory.password);
		if (!cx.isValid(5000)) {
			return dsx.getConnection();
		}
		return cx;

	}

	/**
	 * wrap a datasource with a tomcat jdbc pool Connection con = null;
	 *
	 * TODO: implement FUTURE connections try { Future<Connection> future =
	 * datasource.getConnectionAsync(); while (!future.isDone()) { logger.info(
	 * "Connection is not yet available. Do some background work"); try {
	 * Thread.sleep(100); //simulate work }catch (InterruptedException x) {
	 * Thread.currentThread().interrupt(); } } con = future.get(); //should return
	 * instantly Statement st = con.createStatement(); ResultSet rs =
	 * st.executeQuery("select * from user");
	 *
	 * @return
	 */
	public DataSource getDataSource() {

		if (dsx != null) {
			return dsx;
		} else {
			dsx = new org.apache.tomcat.jdbc.pool.DataSource();
		}

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
				final DataSource dataSource = (DataSource) ic.lookup(lcname);
				final java.sql.Connection c = dataSource.getConnection();
				ConnectionFactory.logger.info("ConnectionFactory.getConnection() SUCCESSFUL JNDI connection: " + lcname
						+ ": connection ready: " + !c.isClosed());
				return dataSource;

			} catch (final Exception e) {
				ConnectionFactory.logger.info("ConnectionFactory.getConnection() failed to get JNDI connection: "
						+ lcname + ". " + e.toString() + " Falling back to non JNDI connection.");

				((org.apache.tomcat.jdbc.pool.DataSource) dsx)
						.setUrl(ConnectionFactory.sourceURL + "/" + ConnectionFactory.dbName);
				((org.apache.tomcat.jdbc.pool.DataSource) dsx).setPassword(ConnectionFactory.password);
				p.setUrl(ConnectionFactory.sourceURL + "/" + ConnectionFactory.dbName);

			}

		} else {
			((org.apache.tomcat.jdbc.pool.DataSource) dsx)
					.setUrl(ConnectionFactory.backupURL + "/" + ConnectionFactory.dbName);
			((org.apache.tomcat.jdbc.pool.DataSource) dsx).setPassword(ConnectionFactory.backupPassword);
			p.setUrl(ConnectionFactory.backupURL + "/" + ConnectionFactory.dbName);
			p.setPassword(ConnectionFactory.backupPassword);
		}

		// dsx.setPort(ConnectionFactory.sourcePort);
		final Properties dbProperties = new Properties();
		dbProperties.put("dbName", ConnectionFactory.dbName);
		dbProperties.put("port", ConnectionFactory.sourcePort);

		((org.apache.tomcat.jdbc.pool.DataSource) dsx).setDbProperties(dbProperties);
		((org.apache.tomcat.jdbc.pool.DataSource) dsx).setUsername(ConnectionFactory.userName);

		p.setDriverClassName(driverName);
		p.setUsername(ConnectionFactory.userName);

		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);

		p.setTimeBetweenEvictionRunsMillis(5000);

		p.setMaxActive(100);
		p.setInitialSize(10);

		p.setMaxWait(10000);

		p.setLogAbandoned(false);
		p.setRemoveAbandoned(true);
		p.setRemoveAbandonedTimeout(600);

		p.setMinEvictableIdleTimeMillis(60000);
		p.setMinIdle(10);

		// crucial to avoid abandoned PooledConnection errors.
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer");

		// wrap and allow for lazy queue fun
		((org.apache.tomcat.jdbc.pool.DataSource) dsx).setPoolProperties(p);

		((org.apache.tomcat.jdbc.pool.DataSource) dsx).setFairQueue(true);
		// dsx.setDataSource(dsx);
		return dsx;
	}

	/**
	 * Close the ResultSet
	 *
	 * @param rs ResultSet
	 */
	public static void close(ResultSet rs) {

		try {
			rs.close();
		} catch (final SQLException e) {
			ConnectionFactory.logger.error("ERROR: Unable to close Result Set");
			ConnectionFactory.logger.error(e.getMessage());
		} // end try-catch block

	} // end method close

	/**
	 * Close statement object
	 *
	 * @param stmt Statement
	 */
	public static void close(Statement stmt) {

		try {

			stmt.close();

		} catch (final SQLException e) {

			ConnectionFactory.logger.error("ERROR: Unable to close Statement");
			ConnectionFactory.logger.error(e.getMessage());

		} // end try-catch block

	} // end method close

	/**
	 * Close connection
	 *
	 * @param conn Connection
	 */
	public static void close(Connection conn) {

		try {

			conn.close();
			conn = null;

		} catch (final SQLException e) {

			ConnectionFactory.logger.info("ERROR: Unable to close Statement");
			ConnectionFactory.logger.info(e.getMessage());

		} // end try-catch block

	} // end method close

	public void setDataSource(DataSource dataSource) {
		dsx = dataSource;
	}

} // end class ConnectionFactory
