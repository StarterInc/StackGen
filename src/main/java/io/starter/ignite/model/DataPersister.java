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
		System.out.println("DataPersister.read: " + valueOf);
		return valueOf;
	}

	public static String persist(String clearTextValue) {
		System.out.println("DataPersister.persist: " + clearTextValue);
		return clearTextValue;
	}
}
