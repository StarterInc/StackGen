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
 * @author John McMahon (@TechnoCharms)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

// can use in method only.
public @interface DataField {

}
