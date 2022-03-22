package io.starter.ignite.security.securefield;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.starter.StackGenUI.model.User;

/**
 * Annotation to enable security on a field.
 *  
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 * 
 * @see http://docs.stackgen.io/docs/security/secure-field.html
 *
 */
public class SecureFieldTest {

	@Test
	public void testPasswordMatches() {
		String p = "password";
		
		User u = new User();
		u.setPassword(p); // should encrypt
		
		BCryptPasswordEncoder bcp = new BCryptPasswordEncoder();
		
		// should always return crypted
		assertTrue("Supplying valid password should return a match", bcp.matches(p, u.getPassword()));
	}
	
	@Test
	public void testPasswordSalts() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String p = "password";
		
		User u = new User();
		u.setPassword(p); // should encrypt
		
		User u2 = new User();
		u2.setPassword(p); // should encrypt
		
		// should always return crypted
		assertNotEquals("No 2 Passwords should encrypt the same", u2.getPassword(), u.getPassword());
	}
	
	@Test
	public void testPasswordCrypt() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String p = "password";
		// String bcp = "uA5ExP+g6My/69Bi/67DItEHa2k3CMOrihumqkXo9V0=";
		User u = new User();
		u.setPassword(p); // should encrypt
		
		Field f = u.getClass().getField("password");
		String pCheck = f.get(u).toString();
		// System.out.println("USER PASS: " + u.getPassword() + ":" + pCheck);

		String up = u.getPassword();
		
		// should be encrypted
		assertEquals(pCheck, up);

		// should always return crypted
		assertNotEquals(p, up);
	}

	@Test
	public void testEncryptedValue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String nm = "James";
		User u = new User();
		u.setLastName(nm);
		Field f = u.getClass().getField("lastName");
		String lnameCheck = f.get(u).toString();
		System.out.println("USER LASTNAME: " + u.getLastName() + ":"
				+ lnameCheck);

		String nmx = u.getLastName();
		assertNotEquals(nmx, lnameCheck);
		assertEquals(nm, u.getLastName());
	}
}