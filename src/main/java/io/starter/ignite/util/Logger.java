/**
 * 
 */
package io.starter.ignite.util;

import org.slf4j.LoggerFactory;

/**
 * the usual logging stuff
 * 
 * @author John McMahon Copyright 2013-2018 Starter Inc., all rights reserved.
 * 
 */
public class Logger implements SystemConstants {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SystemConstants.class);

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
