/**
 * 
 */
package io.starter.ignite.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author John McMahon (@TechnoCharms)
 *
 */
public class DataPersister {

	protected static final Logger logger = LoggerFactory
			.getLogger(DataPersister.class);

	public static Object read(String valueOf) {
		logger.debug("DataPersister.read: " + valueOf);
		return valueOf;
	}

	public static String persist(String clearTextValue) {
		logger.debug("DataPersister.persist: " + clearTextValue);
		return clearTextValue;
	}
}
