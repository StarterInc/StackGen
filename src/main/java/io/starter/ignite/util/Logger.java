/**
 * 
 */
package io.starter.ignite.util;

import java.io.IOException;

/**
 * the usual logging stuff
 * 
 * @author John McMahon Copyright 2013 Starter Inc., all rights reserved.
 * 
 */
public class Logger {

	private static boolean debug = true; // (System.getProperty("production")
											// == null);

	/**
	 * start with the basics
	 * 
	 * @param message
	 */
	public static void log(String message) {
		System.out.println(getTimestamp() + "INFO: " + message);
	}

	public static void debug(String string) {
		if (debug)
			System.out.println(getTimestamp() + "DEBUG: " + string);
	}

	public static void error(String string) {
		System.err.println(getTimestamp() + "ERROR: " + string);

	}

	public static void warn(String string) {
		System.err.println(getTimestamp() + "WARNING: " + string);

	}

	public static String getTimestamp() {
		return "[" + new java.util.Date().toLocaleString() + "] ";
	}

	public void info(String string) {
		System.err.println(getTimestamp() + "INFO: " + string);

	}

	public void error(Exception e) {
		error(e.toString());

	}

}
