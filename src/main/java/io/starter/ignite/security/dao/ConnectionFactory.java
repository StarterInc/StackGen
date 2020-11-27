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
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
	private static String dbDriver = SystemConstants.dbDriver;
	private static String dbName = SystemConstants.dbName;
	private static String dbUrl = SystemConstants.dbUrl;
	private static String dbUser = SystemConstants.dbUser;
	private static String dbPassword = SystemConstants.dbPassword;

	private static DataSource dsx = null;
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
	 * Private default constructor 
	 * 
	 * No outside objects can create an object of this
	 * class This constructor initializes the DriverManager 
	 * by loading the driver for the database
	 */
	private ConnectionFactory() {
		ConnectionFactory.logger.info("Initializing:" + ConnectionFactory.dbDriver);
		try {
			Class.forName(ConnectionFactory.dbDriver);
			ConnectionFactory.logger.info("Loaded JDBC Driver class " + ConnectionFactory.dbDriver + " OK!");
		} catch (final ClassNotFoundException e) {

			ConnectionFactory.logger.error("Exception loading Driver class: " + e.toString());
		}
	}

	/**
	 * Get and return a Connection object that can be used to connect to the data
	 * storage
	 *
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnectionTomcat() throws SQLException {
		try {

			Connection cx = instance.getDataSource().getConnection();
			if (!cx.isValid(5000)) {
				dsx = null;
				cx = DriverManager.getConnection(
						ConnectionFactory.dbUrl 
						+ "/" + ConnectionFactory.dbName + "?"
						+ "user=" + ConnectionFactory.dbUser 
						+ "&dbPassword=" + ConnectionFactory.dbPassword);
			}
			return cx;
		} catch (Exception x) {
			dsx = null;
		}
		return null;
	}

	/*
	 * is Hikari even still used?
	private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
 
    static {
    	
        config.setJdbcUrl( dbUrl );
        config.setSchema( dbName );
        config.setUsername( dbUser);
        config.setPassword( dbPassword );
        config.setDriverClassName( dbDriver );
        
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
       
        config.setMinimumIdle(5);
        config.setConnectionTimeout(20 * 000);
        config.setIdleTimeout(1 * 30 * 1000);
        config.setMaxLifetime(1 * 60 * 1000);
        ds = new HikariDataSource( config );
    }
 	*/
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
	
	static ComboPooledDataSource cpds;
	
	public static DataSource getDataSource() {
		
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
