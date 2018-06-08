/**
 * 
 */
package io.starter.ignite.model;

/**
 * @author john
 *
 */
public class DataPersister {

	public static Object read(String valueOf) {
		io.starter.ignite.util.Logger.log("DataPersister.read: " + valueOf);
		return valueOf;
	}

	public static String persist(String clearTextValue) {
		io.starter.ignite.util.Logger.log("DataPersister.persist: " + clearTextValue);
		return clearTextValue;
	}
}
