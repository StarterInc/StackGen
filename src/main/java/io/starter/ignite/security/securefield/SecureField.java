package io.starter.ignite.security.securefield;

/* ##LICENSE## */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * Annotation to enable security on a field.
 * 
 * We leverage the Spring Security encryption utils for password encryption
 *	https://docs.spring.io/spring-security/site/docs/4.2.11.RELEASE/apidocs/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html
 * 	
 * 
 *  @SecureField
 *  String valuableData
 *  
 * @author John McMahon (@TechnoCharms)
 * 
 * @see http://docs.stackgen.io/docs/security/secure-field.html
 *
 */
public @interface SecureField {

	public enum Type {
		SYMMETRIC, HASHED, HYBRID_SYMMETRIC_HASHED, PKI
	}

	// strength for HASHED (passwords) the log rounds to use,
	// between 4 and 31
	// for SYMMETRICAL this is 256 or 512

	Type type() default Type.SYMMETRIC;

	// whether to bypass security
	public boolean enabled() default true;

	int strength() default 10;

}