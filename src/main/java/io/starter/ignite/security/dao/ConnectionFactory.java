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
 * <p>
 * Title: ConnectionFactory.java
 * </p>
 *
 * @version 1.5
 */
public class ConnectionFactory {

	protected static final Logger		logger			= LoggerFactory
			.getLogger(ConnectionFactory.class);

	// Connect to the data storage
	private static int					sourcePort		= 3306;
	private static String				driverName		= "com.mysql.jdbc.Driver";
	private static String				dbName			= SystemConstants.dbName;
	private static String				sourceURL		= SystemConstants.dbUrl;
	private static String				userName		= SystemConstants.dbUser;
	private static String				password		= SystemConstants.dbPassword;
	private static String				backupURL		= SystemConstants.dbUrl;
	private static String				backupPassword	= SystemConstants.dbPassword;

	// Call the private constructor to initialize the
	// DriverManager
	@SuppressWarnings("unused")
	private static ConnectionFactory	ref				= new ConnectionFactory();

	public static String toConfigString() {
		return "ConnectionFactory v." + SystemConstants.IGNITE_MAJOR_VERSION
				+ "." + SystemConstants.IGNITE_MINOR_VERSION
				+ Configuration.LINE_FEED + "Settings:" + Configuration.LINE_FEED
				+ "=========" + Configuration.LINE_FEED + driverName
				+ Configuration.LINE_FEED + sourceURL + Configuration.LINE_FEED
				+ dbName + Configuration.LINE_FEED + userName;
	}

	/**
	 * Private default constructor No outside objects can create an object of this
	 * class This constructor initializes the DriverManager by loading the driver
	 * for the database
	 */
	private ConnectionFactory() {

		logger.info("ConnectionFactory: initializing:"
				+ ConnectionFactory.driverName);

		try {

			Class.forName(ConnectionFactory.driverName);
			logger.info("ConnectionFactory: Got JDBC class "
					+ ConnectionFactory.driverName + " OK!");
		} catch (final ClassNotFoundException e) {

			logger.error("ConnectionFactory: Exception loading driver class: "
					+ e.toString());
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
		return DriverManager.getConnection("jdbc:mysql://"+sourceURL+"/"+dbName+"?" +
                "user="+userName+"&password="+password);

	}

	/**
	 * wrap a datasource with a tomcat jdbc pool Connection con = null;
	 *
	 * TODO: implement FUTURE connections try { Future<Connection> future =
	 * datasource.getConnectionAsync(); while (!future.isDone()) {
	 * logger.info( "Connection is not yet available. Do some
	 * background work"); try { Thread.sleep(100); //simulate work }catch
	 * (InterruptedException x) { Thread.currentThread().interrupt(); } } con =
	 * future.get(); //should return instantly Statement st = con.createStatement();
	 * ResultSet rs = st.executeQuery("select * from user");
	 *
	 * @return
	 */
	public static DataSource getDataSource() {
	    		
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
				logger.info("ConnectionFactory.getConnection() SUCCESSFUL JNDI connection: "
						+ lcname + ": connection ready: " + !c.isClosed());
				return dataSource;

			} catch (final Exception e) {
				logger.info("ConnectionFactory.getConnection() failed to get JNDI connection: "
						+ lcname + ". " + e.toString()
						+ " Falling back to non JNDI connection.");

				dsx.setUrl(ConnectionFactory.sourceURL);
				dsx.setPassword(ConnectionFactory.password);
				p.setUrl(ConnectionFactory.sourceURL);

			}

		} else {
			dsx.setUrl(ConnectionFactory.backupURL);
			dsx.setPassword(ConnectionFactory.backupPassword);
			p.setUrl(ConnectionFactory.backupURL);
			p.setPassword(ConnectionFactory.backupPassword);
		}

		// dsx.setPort(ConnectionFactory.sourcePort);
		Properties dbProperties = new Properties();
		dbProperties.put("dbName", dbName);
		dbProperties.put("port", sourcePort);
		
		dsx.setDbProperties(dbProperties);
		
		dsx.setUsername(ConnectionFactory.userName);

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
		// dsx.setDataSource(dsx);
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
			logger.error("ERROR: Unable to close Result Set");
			logger.error(e.getMessage());
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

			logger.error("ERROR: Unable to close Statement");
			logger.error(e.getMessage());

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

			logger.info("ERROR: Unable to close Statement");
			logger.info(e.getMessage());

		} // end try-catch block

	} // end method close

} // end class ConnectionFactory
