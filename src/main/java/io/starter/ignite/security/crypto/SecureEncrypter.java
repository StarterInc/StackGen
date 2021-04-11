package io.starter.ignite.security.crypto;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.util.SystemConstants;

/**
 * JCE implementation for encrypting fields
 * 
 * Get the key from System properties (i.e.: command line)
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class SecureEncrypter implements SystemConstants {

	protected static final Logger	logger	= LoggerFactory
			.getLogger(SecureEncrypter.class);

	private static KeyGenerator		keyGenerator;
	private static SecretKey		secretKey;
	private static Cipher			cipher;
	private static SecureRandom		randomSecureRandom;

	/**
	 * Test the SecureEncryption functionality
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		logger.info("Generate Encryption Key: " + EncryptionUtil.generateKey());

		String cleartext = "AES Symmetric Encryption Decryption";
		logger.info("Plain Text Before Encryption: " + cleartext);

		String ciphertext = SecureEncrypter.encrypt(cleartext);
		logger.info("Encrypted Text After Encryption: " + ciphertext);

		String decryptedText = SecureEncrypter.decrypt(ciphertext);
		logger.info("Decrypted Text After Decryption: " + decryptedText);
	}

	public static SecretKey getKeyFromBytes(byte[] b) {
		// decode the base64 encoded string
		byte[] decodedKey = Base64.getDecoder().decode(b);
		// rebuild key using SecretKeySpec
		SecretKey originalKey = new SecretKeySpec(decodedKey, 0,
				decodedKey.length, KEYGEN_INSTANCE_NAME);

		return originalKey;
	}

	/**
	 * Handles Secure Encryption
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	private static void init() throws Exception {
		if (SECRET_KEY == null) {
			throw new RuntimeException(
					"SecureEncrypter Initialization Failure: "
							+ SECURE_KEY_PROPERTY + " property is not set.");
		}
		try {
			secretKey = getKeyFromBytes(SECRET_KEY.getBytes());
		}catch(Exception e){
			throw new RuntimeException(
					"SecureEncrypter Initialization Failure: "
							+ SECURE_KEY_PROPERTY + " key is invalid:" + e.toString());
		}
		cipher = Cipher.getInstance(CIPHER_NAME);
		randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
	}

	/**
	 * Returns a 256 big secure hash (Shiro implementation)
	 * 
	 * @param e
	 * @return
	 */
	private static String secureHash(String e) {
		return DigestUtils.sha256Hex(e);
	}

	/**
	 * Encrypt a String
	 * 
	 * @param cleartext
	 * @return
	 * @throws Exception
	 */
	public synchronized static String encrypt(String cleartext) throws Exception {
		if(cleartext == null){
			return null;
		}
		if (keyGenerator == null) {
			init();
		}
		byte[] cleartextByte = cleartext.getBytes();

		byte[] iv = new byte[cipher.getBlockSize()];
		randomSecureRandom.nextBytes(iv);

		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
		byte[] encryptedByte = cipher.doFinal(cleartextByte);

		byte[] encryptedByteWithIV = new byte[encryptedByte.length + 16];
		System.arraycopy(encryptedByte, 0, encryptedByteWithIV, 16, encryptedByte.length);

		System.arraycopy(iv, 0, encryptedByteWithIV, 0, iv.length);

		Base64.Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(encryptedByteWithIV);
	}

	/**`
	 * Decrypt a String
	 * 
	 * @param ciphertext
	 * @return
	 * @throws Exception
	 */
	public synchronized static String decrypt(String ciphertext) throws Exception {
		if (keyGenerator == null) {
			init();
		}
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] ciphertextByte = decoder.decode(ciphertext);

		byte[] iv = Arrays.copyOfRange(ciphertextByte, 0, 16);
		byte[] decb = Arrays
				.copyOfRange(ciphertextByte, 16, ciphertextByte.length);

		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

		byte[] decryptedByte = cipher.doFinal(decb);
		return new String(decryptedByte);
	}
}