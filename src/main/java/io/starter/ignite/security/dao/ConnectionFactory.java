package io.starter.ignite.security.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
public class ConnectionFactory implements org.apache.ibatis.datasource.DataSourceFactory{

	protected static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

	// Connect to the data storage
	private static String dbDriver = SystemConstants.dbDriver;
	private static String dbName = SystemConstants.dbName;
	private static String dbUrl = SystemConstants.dbUrl;
	private static String dbUser = SystemConstants.dbUser;
	private static String dbPassword = SystemConstants.dbPassword;

	static String LINE_FEED = "\r\n";

	@SuppressWarnings("unused")
	public final static ConnectionFactory instance = new ConnectionFactory();

	public static String toConfigString() {
		return "ConnectionFactory v." + SystemConstants.IGNITE_MAJOR_VERSION + "."
				+ SystemConstants.IGNITE_MINOR_VERSION + LINE_FEED + "Settings:" + LINE_FEED + "=========" + LINE_FEED
				+ ConnectionFactory.dbDriver + LINE_FEED + ConnectionFactory.dbUrl + LINE_FEED
				+ ConnectionFactory.dbName + LINE_FEED + ConnectionFactory.dbUser;
	}

	/**
	 * This constructor initializes the DriverManager 
	 * by loading the driver for the database
	 */
	public ConnectionFactory() {
		ConnectionFactory.logger.info("Initializing:" + ConnectionFactory.dbDriver);
		try {
			Class.forName(ConnectionFactory.dbDriver);
			ConnectionFactory.logger.info("Loaded JDBC Driver class " + ConnectionFactory.dbDriver + " OK!");
		} catch (final ClassNotFoundException e) {

			ConnectionFactory.logger.error("Exception loading Driver class: " + e.toString());
		}
	}

	
    public static Connection getConnection() throws SQLException {
    	return ConnectionFactory.instance.getDataSource().getConnection();
    }
    
    public static Connection getConnection(
    		String driver, 
    		String url, 
    		String name,
    		String username,
    		String password) throws SQLException {
		cpds = new ComboPooledDataSource();
		
		try {
			cpds.setDriverClass(driver); // loads the jdbc driver
		} catch (Exception e) {
			System.err.println("Could not set Driver Class: " + driver);
		}
		cpds.setJdbcUrl(url + "/" + name);
		cpds.setUser(username);
		cpds.setPassword(password);
    	return cpds.getConnection();
    }
    
    
	
	static ComboPooledDataSource cpds;

	
	public static DataSource getDataSourceInternal() {
		
		if(cpds != null) {
			return cpds;
		}
		// logger.warn("Creating new ComboPooledDataSource");
		cpds = new ComboPooledDataSource();
		
		try {
			cpds.setDriverClass(dbDriver); // loads the jdbc driver
		} catch (Exception e) {
			System.err.println("Could not set Driver Class: " + dbDriver);
		}
		cpds.setJdbcUrl(dbUrl + "/" + dbName);
		cpds.setUser(dbUser);
		cpds.setPassword(dbPassword);

		// the settings below are optional -- c3p0 can work with defaults
		// cpds.setMinPoolSize(10);
		cpds.setAcquireIncrement(5);
		cpds.setMaxIdleTime(1800);
		cpds.setIdleConnectionTestPeriod(300);
		cpds.setTestConnectionOnCheckout(true);
		cpds.setMaxPoolSize(30);
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

	@Override
	public void setProperties(Properties props) {
		if(props.get("driver")!=null) {
			dbDriver = (String) props.get("driver");
		}
		if(props.get("url")!=null) {
			dbUrl = (String) props.get("url");
		}
		if(props.get("username")!=null) {
			dbUser = (String) props.get("username");
		}
		if(props.get("password")!=null) {
			dbPassword = (String) props.get("password");
		}
		if(props.get("auto")!=null) {
			dbDriver = (String) props.get("driver");
		}
	}

	@Override
	public DataSource getDataSource() {
		return ConnectionFactory.getDataSourceInternal();
	}


}
