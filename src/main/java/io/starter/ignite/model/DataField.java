/**
 * 
 */
package io.starter.ignite.model;

/* ##LICENSE## */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable StackGen Data operations on a field.
 * 
 *  
 *  @DataField
 *  String applicationData
 *  
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 * 
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataField {
	
	// TODO: implement     @Cacheable(value = ORGANIZATION_DETAILS_CACHE, key = "#organization.uuid")

	public String value() default "";

}