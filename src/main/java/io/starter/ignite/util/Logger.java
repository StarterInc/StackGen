/**
 * 
 */
package io.starter.ignite.util;

import org.apache.log4j.PropertyConfigurator;

/**
 * the usual logging stuff
 * 
 * @author John McMahon Copyright 2013 Starter Inc., all rights reserved.
 * 
 */
public class Logger implements SystemConstants{

	static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);

	// PropertiesConfigurator is used to configure logger from properties file
	static {
		// URL lc =
		// Logger.class.getClassLoader().getResource("resources/log4j.properties");
		String lc = ROOT_FOLDER+ "/src/resources/log4j.properties";
		PropertyConfigurator.configure(lc);
	}

	/**
	 * start with the basics
	 *
	 * @param message
	 */
	public static void log(String message) {
		logger.info(message);
	}

	public static void debug(String string) {
		logger.debug(string);
	}

	public static void error(String string) {
		logger.error(string);
	}

	public static void warn(String string) {
		logger.warn(string);

	}

	public static void error(Exception e) {
		error(e.getMessage());
		e.printStackTrace();
	}

}
