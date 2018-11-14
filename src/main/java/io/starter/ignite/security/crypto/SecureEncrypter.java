package io.starter.ignite.security.crypto;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.shiro.crypto.hash.Sha256Hash;

/* ##LICENSE## */

import io.starter.ignite.util.Logger;
import io.starter.ignite.util.SystemConstants;

/**
 * JCE implementation for encrypting fields
 * 
 * Get the key from secure file.
 * 
 * @author john
 *
 */
public class SecureEncrypter implements SystemConstants {

	private static byte[]		iv	= null;
	private static KeyGenerator	keyGenerator;
	private static SecretKey	secretKey;
	private static Cipher		cipher;

	/**
	 * Test the SecureEncryption functionality
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String cleartext = "AES Symmetric Encryption Decryption";
		Logger.debug("Plain Text Before Encryption: " + cleartext);

		String ciphertext = SecureEncrypter.encrypt(cleartext);
		Logger.debug("Encrypted Text After Encryption: " + ciphertext);

		String decryptedText = SecureEncrypter.decrypt(ciphertext);
		Logger.debug("Decrypted Text After Decryption: " + decryptedText);
	}

	/**
	 * Handles Secure Encryption
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	private static void init() throws Exception {
		Logger.warn("SecureEncrypter init: " + SECURE_KEY_PROPERTY
				+ " property is set: " + (SECRET_KEY != null));
		if (SECRET_KEY == null) {
			throw new RuntimeException(
					"SecureEncrypter Initialization Failure: "
							+ SECURE_KEY_PROPERTY + " property is not set.");
		}
		keyGenerator = KeyGenerator.getInstance("AES");
		Logger.debug("SecureEncrypter init: Crypto Provider ["
				+ keyGenerator.getProvider().getName() + "]");
		keyGenerator.init(KEY_SIZE);
		cipher = Cipher.getInstance(CIPHER_NAME);
		secretKey = keyGenerator.generateKey();
		iv = new byte[16];
		iv = Arrays.copyOf(SECRET_KEY.getBytes(), 16);
	}

	/**
	 * Returns a 256 big secure hash (Shiro implementation)
	 * 
	 * @param e
	 * @return
	 */
	public static String secureHash(String e) {

		Sha256Hash sha256Hash = new Sha256Hash(e);
		return sha256Hash.toHex();
	}

	/**
	 * Encrypt a String
	 * 
	 * @param cleartext
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String cleartext) throws Exception {
		if (keyGenerator == null) {
			init();
		}
		byte[] cleartextByte = cleartext.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
		byte[] encryptedByte = cipher.doFinal(cleartextByte);
		Base64.Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(encryptedByte);
	}

	/**
	 * Decrypt a String
	 * 
	 * @param ciphertext
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String ciphertext) throws Exception {
		if (keyGenerator == null) {
			init();
		}
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] ciphertextByte = decoder.decode(ciphertext);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		byte[] decryptedByte = cipher.doFinal(ciphertextByte);
		return new String(decryptedByte);
	}
}