package io.starter.ignite.security.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
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
	private static String driverName = SystemConstants.dbDriver;
	private static String dbName = SystemConstants.dbName;
	private static String sourceURL = SystemConstants.dbUrl;
	private static String userName = SystemConstants.dbUser;
	private static String password = SystemConstants.dbPassword;

	private static DataSource dsx = null;
	static String LINE_FEED = "\r\n";

	@SuppressWarnings("unused")
	public final static ConnectionFactory instance = new ConnectionFactory();

	public static String toConfigString() {
		return "ConnectionFactory v." + SystemConstants.IGNITE_MAJOR_VERSION + "."
				+ SystemConstants.IGNITE_MINOR_VERSION + LINE_FEED + "Settings:" + LINE_FEED + "=========" + LINE_FEED
				+ ConnectionFactory.driverName + LINE_FEED + ConnectionFactory.sourceURL + LINE_FEED
				+ ConnectionFactory.dbName + LINE_FEED + ConnectionFactory.userName;
	}

	/**
	 * Private default constructor 
	 * 
	 * No outside objects can create an object of this
	 * class This constructor initializes the DriverManager 
	 * by loading the driver for the database
	 */
	private ConnectionFactory() {
		ConnectionFactory.logger.info("Initializing:" + ConnectionFactory.driverName);
		try {
			Class.forName(ConnectionFactory.driverName);
			ConnectionFactory.logger.info("Got JDBC class " + ConnectionFactory.driverName + " OK!");
		} catch (final ClassNotFoundException e) {

			ConnectionFactory.logger.error("Exception loading driver class: " + e.toString());
		}
	}

	/**
	 * Get and return a Connection object that can be used to connect to the data
	 * storage
	 *
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		try {

			Connection cx = instance.getDataSource().getConnection();
			if (!cx.isValid(5000)) {
				dsx = null;
				cx = DriverManager.getConnection(ConnectionFactory.sourceURL + "/" + ConnectionFactory.dbName + "?"
						+ "user=" + ConnectionFactory.userName + "&password=" + ConnectionFactory.password);
			}
			return cx;
		} catch (Exception x) {
			dsx = null;
		}
		return null;
	}

	static ComboPooledDataSource cpds;
	public static DataSource getDataSource() {
		
		if(cpds != null) {
			return cpds;
		}
		// logger.warn("Creating new ComboPooledDataSource");
		cpds = new ComboPooledDataSource();
		
		try {
			cpds.setDriverClass(driverName); // loads the jdbc driver
		} catch (Exception e) {
			System.err.println("Could not set Driver Class: " + driverName);
		}
		cpds.setJdbcUrl(sourceURL + "/" + dbName);
		cpds.setUser(userName);
		cpds.setPassword(password);

		// the settings below are optional -- c3p0 can work with defaults
		// cpds.setMinPoolSize(10);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(100);
		return cpds;
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

			logger.info("ERROR: Unable to close Statement");
			logger.error(e.getMessage());

		} // end try-catch block

	} // end method close

	public void setDataSource(DataSource dataSource) {
		dsx = dataSource;
	}

} // end class ConnectionFactory
