package io.starter.ignite.security.securefield;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.starter.ignite.security.crypto.EncryptionUtil;
import io.starter.stackgentest.model.User;

/**
 * Annotation to enable security on a field.
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 * @see http://docs.stackgen.io/docs/security/secure-field.html
 *
 */
public class SecureFieldTest {

	@Before
	public void setup() throws NoSuchAlgorithmException {
		// transient encryption key
		final String stx = EncryptionUtil.generateKey();

		System.setProperty("starterIgniteSecureKey", stx);
	}

	@Test
	@Ignore(value = "TODO: finish mock definition and asserts")
	public void testSecureFieldGet() throws Throwable {
		// @Around(FIELD_SET)
//		public Object setSecureField(ProceedingJoinPoint pjp) throws Throwable {
		final SecureFieldAspect sfa = new SecureFieldAspect();

		final Object mockTargetObject = new String("TODO");
		final ProceedingJoinPoint mockPjp = Mockito.mock(ProceedingJoinPoint.class);
		Mockito.when(mockPjp.getTarget()).thenReturn(mockTargetObject);

		sfa.setSecureField(mockPjp);
	}

	@Test
	public void testPasswordMatches() {
		final String p = "password";

		final User u = new User();
		u.setPassword(p); // should encrypt

		final BCryptPasswordEncoder bcp = new BCryptPasswordEncoder();

		// should always return crypted
		Assert.assertTrue("Supplying valid password should return a match", bcp.matches(p, u.getPassword()));
	}

	@Test
	public void testPasswordSalts()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final String p = "password";

		final User u = new User();
		u.setPassword(p); // should encrypt

		final User u2 = new User();
		u2.setPassword(p); // should encrypt

		// should always return crypted
		Assert.assertNotEquals("No 2 Passwords should encrypt the same", u2.getPassword(), u.getPassword());
	}

	@Test
	public void testPasswordCrypt()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final String p = "password";
		// String bcp = "uA5ExP+g6My/69Bi/67DItEHa2k3CMOrihumqkXo9V0=";
		final User u = new User();
		u.setPassword(p); // should encrypt

		final Field f = u.getClass().getField("password");
		final String pCheck = f.get(u).toString();
		System.out.println("USER PASS: " + u.getPassword() + ":" + pCheck);

		final String up = u.getPassword();

		// should be encrypted
		Assert.assertEquals(pCheck, up);

		// should always return crypted
		Assert.assertNotEquals(p, up);
	}

	@Test
	public void testEncryptedValue()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final String nm = "James";
		final User u = new User();
		u.setLastName(nm);
		final Field f = u.getClass().getField("lastName");
		final String lnameCheck = f.get(u).toString();
		System.out.println("USER LASTNAME: " + u.getLastName() + ":" + lnameCheck);

		final String nmx = u.getLastName();
		Assert.assertNotEquals(nmx, lnameCheck);
		Assert.assertEquals(nm, u.getLastName());
	}
}