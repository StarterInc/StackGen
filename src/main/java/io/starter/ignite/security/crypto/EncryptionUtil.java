package io.starter.ignite.security.crypto;

/* ##LICENSE## */

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.starter.ignite.util.SystemConstants;

/**
 * Utilities for working with the encrypted data
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class EncryptionUtil implements SystemConstants {

	/**
	 * get the Encrypted value stored in the object -- useful for queries
	 * 
	 */
	public static String getEncryptedFieldVal(Object ob, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Field fx = ob.getClass().getDeclaredField(fieldName);
		boolean accessible = fx.isAccessible();
		fx.setAccessible(true);
		String encryptedValue = String.valueOf(fx.get(ob));
		fx.setAccessible(accessible);

		return encryptedValue;
	}

	public static String generateKey() throws NoSuchAlgorithmException {

		// create new key
		KeyGenerator keyGenerator = KeyGenerator
				.getInstance(KEYGEN_INSTANCE_NAME);
		keyGenerator.init(KEY_SIZE);

		SecretKey secretKey = keyGenerator.generateKey();
		// get base64 encoded version of the key
		String encodedKey = Base64.getEncoder()
				.encodeToString(secretKey.getEncoded());

		return encodedKey;
	}

}
