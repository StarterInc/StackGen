package io.starter.security.crypto;

/* ##LICENSE## */

import java.lang.reflect.Field;

/**
 * Utilities for working with the encrypted data
 * 
 * @author John McMahon (@TechnoCharms)
 *
 */
public class EncryptionUtils {

	/**
	 * get the Encrypted value stored in the object -- useful for queries
	 * 
	 */
	public static String getEncryptedFieldVal(Object ob, String fieldName)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {

		Field fx = ob.getClass().getDeclaredField(fieldName);
		boolean accessible = fx.isAccessible();
		fx.setAccessible(true);
		String encryptedValue = String.valueOf(fx.get(ob));
		fx.setAccessible(accessible);

		return encryptedValue;
	}
}
