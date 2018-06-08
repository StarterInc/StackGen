package io.starter.ignite.generator.react;

import io.starter.ignite.security.securefield.SecureField;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains and initializes Redux template Mapping info for a passed in
 * DataObject. <br/>
 * Example Redux State Template Code:<code>
 * 
	 
	 {{#objects}}
		 {{objectname}}Info:{
			 {{#variables}}
			 	{{variablename}}: '{{variableval}}',
			  {{/variables}}
		 },
		{{/objects}}
		
	</code>
 * 
 * @author john
 *
 */
public class AppEntityObject {

	private static final Class<SecureField> SECURE_ANNOTATION_CLASS = SecureField.class;
	private static final Class<JsonProperty> FIELD_ANNOTATION_CLASS = JsonProperty.class;
	private static final Class<ApiModelProperty> ANNOTATION_CLASS = ApiModelProperty.class;

	public String appname;
	public String objectname;
	public String objectname_upper;

	List<Variable> variables = new ArrayList<Variable>();

	static class Variable {

		public String variableval;
		public String variablename;

		Variable(String variablename, String variablebal) {
			this.variablename = variablename;
			this.variableval = variablebal;
		}

		public String toString() {
			return this.variablename + " : " + this.variableval;
		}
	}

	/**
	 * set values from data object class
	 * 
	 * @param s
	 */
	private void processMethod(Method s) {

		SecureField fa = (SecureField) s.getAnnotation(SECURE_ANNOTATION_CLASS);

		JsonProperty jf = (JsonProperty) s
				.getAnnotation(FIELD_ANNOTATION_CLASS);

		ApiModelProperty apia = (ApiModelProperty) s
				.getAnnotation(ANNOTATION_CLASS);

		String val = null;
		if (jf != null)
			val = jf.value();

		if (!apia.hidden() && val != null) {
			io.starter.ignite.util.Logger.log("Processing Field: " + s.toGenericString()
					+ " :" + val);
			variables.add(new Variable(val, apia.example()));
		} else {
			System.err.println("Skipping Invalid Field: " + s.toGenericString()
					+ " :" + val);
		}
	}

	/**
	 * Wrap a class with our JSON Redux state templated output
	 * 
	 * @param cx
	 */
	public AppEntityObject(String app, Class<?> cx) {

		appname = app;
		objectname = cx.getName().substring(cx.getName().lastIndexOf(".") + 1);
		objectname_upper = objectname.toUpperCase();

		Stream.of(cx.getDeclaredMethods())
				.filter(s -> {
					return (((Method) s).getAnnotation(ANNOTATION_CLASS)) != null;
				})
				.forEach(
						s -> {
							processMethod((Method) s);
							if (!isValid())
								System.err
										.println("WARNING: AppEntityObject is invalid: "
												+ s.toString());
						});

	}

	/**
	 * sanity check and ensure compliance with reality
	 * 
	 * @return
	 */
	public boolean isValid() {

		if (appname == null)
			return false;

		if (objectname == null)
			return false;

		if (objectname_upper == null)
			return false;

		return true;
	}

	/**
	 * provide pretty representation
	 */
	public String toString() {
		String sbout = "AppEntityObject: " + this.objectname + "\r\n";
		for (Variable v : variables) {
			sbout += v.toString() + "\r\n";
		}
		return sbout;
	}
}