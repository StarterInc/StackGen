package io.starter.ignite.generator.annotations;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds and manipulates data of a model property.
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StackgenModelProperty {

	/**
	 * the minimum size of the data
	 */
	int minLength() default 0;

	/**
	 * the maximum size of the data
	 */
	int maxLength() default 256;

	/**
	 * the minimum value of the data
	 */
	double minimum() default Double.MIN_VALUE;

	/**
	 * the maximum value of the data
	 */
	double maximum() default Double.MAX_VALUE;

	/**
	 * whether to encrypt the field with @SecureField
	 * 
	 * @see @io.starter.ignite.security.SecureField
	 * @return
	 */
	boolean secureField() default false;

	/**
	 * whether to tag the field with @DataField
	 * 
	 * @see @io.starter.ignite.model.DataField
	 * @return
	 */
	String dataField() default "";

	/**
	 * A brief description of this property.
	 */
	String value() default "";

	/**
	 * Allows overriding the name of the property.
	 *
	 * @return the overridden property name
	 */
	String name() default "";

	/**
	 * Limits the acceptable values for this parameter.
	 * <p>
	 * There are three ways to describe the allowable values:
	 * <ol>
	 * <li>To set a list of values, provide a comma-separated list.
	 * For example: {@code first, second, third}.</li>
	 * <li>To set a range of values, start the value with "range", and surrounding by square
	 * brackets include the minimum and maximum values, or round brackets for exclusive minimum and maximum values.
	 * For example: {@code range[1, 5]}, {@code range(1, 5)}, {@code range[1, 5)}.</li>
	 * <li>To set a minimum/maximum value, use the same format for range but use "infinity"
	 * or "-infinity" as the second value. For example, {@code range[1, infinity]} means the
	 * minimum allowable value of this parameter is 1.</li>
	 * </ol>
	 */
	String allowableValues() default "";

	/**
	 * Allows for filtering a property from the API documentation. See io.swagger.core.filter.SwaggerSpecFilter.
	 */
	String access() default "";

	/**
	 * Currently not in use.
	 */
	String notes() default "";

	/**
	 * The data type of the parameter.
	 * <p>
	 * This can be the class name or a primitive. The value will override the data type as read from the class
	 * property.
	 */
	String dataType() default "";

	/**
	 * Specifies if the underlying value should be unique in database
	 */
	boolean uniqueValues() default false;

	/**
	 * Specifies if the parameter is required or not.
	 */
	boolean required() default false;

	/**
	 * Allows explicitly ordering the property in the model.
	 */
	int position() default 0;

	/**
	 * Allows a model property to be hidden in the Swagger model definition.
	 */
	boolean hidden() default false;

	/**
	 * A sample value for the property.
	 */
	String example() default "";

	/**
	 * Allows a model property to be designated as read only.
	 *
	 * @deprecated As of 1.5.19, replaced by {@link #accessMode()}
	 *
	 */
	@Deprecated
	boolean readOnly() default false;

	/**
	 * Allows to specify the access mode of a model property (AccessMode.READ_ONLY, READ_WRITE)
	 *
	 * @since 1.5.19
	 */
	StackgenModelProperty.AccessMode accessMode() default StackgenModelProperty.AccessMode.AUTO;


	/**
	 * Specifies a reference to the corresponding type definition, overrides any other metadata specified
	 */

	String reference() default "";

	/**
	 * Allows passing an empty value
	 *
	 * @since 1.5.11
	 */
	boolean allowEmptyValue() default false;

	/**
	 * @return an optional array of extensions
	 */
	Extension[] extensions() default @Extension(properties = @ExtensionProperty(name = "", value = ""));

	enum AccessMode {
		AUTO,
		READ_ONLY,
		READ_WRITE;
	}
}

