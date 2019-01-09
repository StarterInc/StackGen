package io.starter.ignite.security.securefield;

/* ##LICENSE## */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
// can use in method only.
/**
 * Annotation to enable security on a field.
 * 
 * 
 *  @SecureField
 *  String valuableData
 *  
 * @author John McMahon (@TechnoCharms)
 * 
 * @see http://ignite.starter.io/securefield
 *
 */
public @interface SecureField {

	// whether to bypass security
	public boolean enabled() default true;

}