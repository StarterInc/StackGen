package io.starter.ignite.generator.react;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.starter.ignite.security.securefield.SecureField;
import io.swagger.annotations.ApiModelProperty;

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

	private static final org.slf4j.Logger			logger					= LoggerFactory
			.getLogger(AppEntityObject.class);

	private static final Class<SecureField>			SECURE_ANNOTATION_CLASS	= SecureField.class;
	private static final Class<JsonProperty>		FIELD_ANNOTATION_CLASS	= JsonProperty.class;
	private static final Class<ApiModelProperty>	ANNOTATION_CLASS		= ApiModelProperty.class;

	public String									appname;
	public String									objectname;
	public String									objectname_upper;

	List<Variable>									variables				= new ArrayList<Variable>();

	static class Variable {

		public String	variableval;
		public String	variablename;

		Variable(String variablename, String variablebal) {
			this.variablename = variablename;
			this.variableval = variablebal;
		}

		@Override
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

		SecureField fa = s.getAnnotation(SECURE_ANNOTATION_CLASS);

		JsonProperty jf = s.getAnnotation(FIELD_ANNOTATION_CLASS);

		ApiModelProperty apia = s.getAnnotation(ANNOTATION_CLASS);

		String val = null;
		if (jf != null)
			val = jf.value();
		else
			val = "";

		if (!apia.hidden() && val != null) {
			logger.debug("Processing Field: " + s.toGenericString() + " :"
					+ val);
			variables.add(new Variable(val, apia.example()));
		} else {
			logger.error("Skipping Invalid Field: " + s.toGenericString() + " :"
					+ val);
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

		Stream.of(cx.getDeclaredMethods()).filter(s -> {
			return (s.getAnnotation(ANNOTATION_CLASS)) != null;
		}).forEach(s -> {
			processMethod(s);
			if (!isValid())
				logger.error("WARNING: AppEntityObject is invalid: "
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
	@Override
	public String toString() {
		String sbout = "AppEntityObject: " + this.objectname + "\r\n";
		for (Variable v : variables) {
			sbout += v.toString() + "\r\n";
		}
		return sbout;
	}
}