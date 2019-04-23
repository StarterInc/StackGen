package io.starter.ignite.security.securefield;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

import io.starter.ignite.security.crypto.EncryptionUtil;
import io.starter.stackgentest.model.User;

/**
 * Annotation to enable security on a field.
 *  
 * @author John McMahon (@TechnoCharms)
 * 
 * @see http://docs.stackgen.io/docs/security/secure-field.html
 *
 */
public class SecureFieldTest {

	@Before
	public void setup() throws NoSuchAlgorithmException {
		// transient encryption key
		String stx = EncryptionUtil.generateKey();

		System.setProperty("starterIgniteSecureKey", stx);
	}

	@Test
	public void testPassword() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String p = "password";
		User u = new User();
		u.setPassword(p);
		Field f = u.getClass().getField("password");
		String pCheck = f.get(u).toString();
		System.out.println("USER PASS: " + u.getPassword() + ":" + pCheck);

		assertNotEquals(u.getPassword(), pCheck);
		assertEquals(p, u.getPassword());
	}

	@Test
	public void testEncryptedValue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String nm = "James";
		User u = new User();
		u.setLastName(nm);
		Field f = u.getClass().getField("lastName");
		String fnameCheck = f.get(u).toString();
		System.out.println("USER LASTNAME: " + u.getLastName() + ":"
				+ fnameCheck);

		assertNotEquals(u.getLastName(), fnameCheck);
		assertEquals(nm, u.getLastName());
	}
}