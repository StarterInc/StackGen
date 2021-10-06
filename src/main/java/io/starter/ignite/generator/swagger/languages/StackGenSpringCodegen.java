package io.starter.ignite.generator.swagger.languages;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import com.github.jknack.handlebars.Handlebars;
import io.starter.toolkit.StringTool;
import io.swagger.codegen.v3.*;
import io.swagger.codegen.v3.templates.TemplateEngine;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import io.swagger.codegen.languages.SpringCodegen;
import io.swagger.codegen.languages.features.BeanValidationFeatures;
 import io.swagger.codegen.languages.features.OptionalFeatures;

/**
 * customized Spring CodeGen for StackGen
 *
 *
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class StackGenSpringCodegen extends SpringCodegen implements CodegenConfig, BeanValidationFeatures, OptionalFeatures {

	protected static final Logger logger = LoggerFactory.getLogger(StackGenSpringCodegen.class);

	public StackGenSpringCodegen() {
		super();
	}

	@Override
	public String getName() {
		return "stackgen-java-spring";
	}

	@Override
	public String getHelp() {
		return "Generates a Java SpringBoot StackGen Service.";
	}

    @Override
    public String apiDocFileFolder() {
        return (outputFolder + "/" + apiDocPath).replace('/', File.separatorChar);
    }

	@Override
	public String customTemplateDir() {
		return null;
	}

	@Override
	public String getTemplateVersion() {
		return null;
	}

	@Override
    public String modelDocFileFolder() {
        return (outputFolder + "/" + modelDocPath).replace('/', File.separatorChar);
    }

	@Override
	public String getTypeDeclaration(Schema schema) {
		return null;
	}

	@Override
	public void processOpts() {

		// fix template dir to match our SG template project paths
		super.templateDir = super.templateDir + "/src/main/" + super.library;
		logger.info("Processing template files at: " + super.templateDir);
		super.processOpts();
		// add doc templates
		apiTemplateFiles.put("ApiClient.mustache", ".java");
        modelDocTemplateFiles.put("model_doc.mustache", ".md");
        apiDocTemplateFiles.put("api_doc.mustache", ".md");

		// TODO: add model test
		// modelTestTemplateFiles.put("modelTest.mustache", "Test.java");

		apiTestTemplateFiles.put("apiTest.mustache", ".java");

        // add lambda for mustache templates
 		additionalProperties.put("lambdaAddSecurityAnnotations", new Mustache.Lambda() {
 			@Override
 			public void execute(Template.Fragment fragment, Writer writer) throws IOException {
 				String checkOp = fragment.execute();
 				
 				String targetClassname = StackGenSpringCodegen.this.modelPackage() + "." + ((io.swagger.codegen.CodegenOperation)fragment.context()).baseName + "Service";
 				
 				switch(checkOp) {
 					case "update":
						writer.write("@PreAuthorize(\"hasPermission(#id2, 'update')\")");
						break;
 					case "delete":
 						writer.write("@PreAuthorize(\"hasPermission(#id, '"+targetClassname+"', 'delete')\")");
 						break;						
 					case "load":
 						writer.write("@PostFilter(\"hasPermission(filterObject, 'load')\")");
 						break;
 					case "list":
 						writer.write("@PostFilter(\"hasPermission(filterObject, 'list')\")");
 						break;
 					// case "insert":
 						// writer.write("@PreAuthorize(\"hasPermission(filterObject, 'insert')\")");
 						// break;
 						
 				}
 			}
 		});
		additionalProperties.put("lambdaEscapeDoubleQuote", new Mustache.Lambda() {
			@Override
			public void execute(Template.Fragment fragment, Writer writer) throws IOException {
				writer.write(fragment.execute().replaceAll("\"", Matcher.quoteReplacement("\\\"")));
			}
		});
		additionalProperties.put("lambdaStripAPIPackage", new Mustache.Lambda() {
			@Override
			public void execute(Template.Fragment fragment, Writer writer) throws IOException {
				writer.write(fragment.execute().replace(".api", ""));
			}
		});
		additionalProperties.put("lambdaStripAPI", new Mustache.Lambda() {
			@Override
			public void execute(Template.Fragment fragment, Writer writer) throws IOException {
				writer.write(fragment.execute().replace("Api", ""));
			}
		});
		additionalProperties.put("lambdaLowerCaseFirstLetter", new Mustache.Lambda() {
			@Override
			public void execute(Template.Fragment fragment, Writer writer) throws IOException {
				String str = fragment.execute();
				if(str.length() > 0){
					str = StringTool.getLowerCaseFirstLetter(str);
				}
				writer.write(str);
			}
		});
		additionalProperties.put("lambdaRemoveLineBreak", new Mustache.Lambda() {
			@Override
			public void execute(Template.Fragment fragment, Writer writer) throws IOException {
				writer.write(fragment.execute().replaceAll("\\r|\\n", ""));
			}
		});
	}

	@Override
	public String generateExamplePath(String s, Operation operation) {
		return null;
	}

	@Override
	public String getInputURL() {
		return null;
	}

	@Override
	public void setInputURL(String s) {

	}

	@Override
	public CodegenModel fromModel(String s, Schema schema) {
		return null;
	}

	@Override
	public CodegenModel fromModel(String s, Schema schema, Map<String, Schema> map) {
		return null;
	}

	@Override
	public CodegenOperation fromOperation(String s, String s1, Operation operation, Map<String, Schema> map, OpenAPI openAPI) {
		return null;
	}

	@Override
	public CodegenOperation fromOperation(String s, String s1, Operation operation, Map<String, Schema> map) {
		return null;
	}

	@Override
	public List<CodegenSecurity> fromSecurity(Map<String, SecurityScheme> map) {
		return null;
	}

	@Override
	public void preprocessOpenAPI(OpenAPI openAPI) {

	}

	@Override
	public void processOpenAPI(OpenAPI openAPI) {

	}

	@Override
	public TemplateEngine getTemplateEngine() {
		return null;
	}

	@Override
	public void addOperationToGroup(String s, String s1, Operation operation, CodegenOperation codegenOperation, Map<String, List<CodegenOperation>> map) {

	}

	@Override
	public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
		super.postProcessModelProperty(model, property);

		// Starter Extensions
		if (property.vendorExtensions.containsKey("x-starter-secureField")) {
			Object o = property.vendorExtensions.containsKey("x-starter-secureField");
			if (o != null) {
				
				// property.isSecure = true;
				Object confs = property.vendorExtensions.get("x-starter-secureField");
				String vx = "@io.starter.ignite.security.securefield.SecureField(enabled=true";
				
				if (confs.toString().toLowerCase().contains("type=hashed")) {
					vx += ", type=io.starter.ignite.security.securefield.SecureField.Type.HASHED";
				} else {
					vx += ", strength=5";
				}
				vx += ")";
				
				property.vendorExtensions.put("secureAnnotation", vx);
				logger.info("Found Starter SecureField Vendor Extension" + vx);
			}
		}

		if (property.vendorExtensions.containsKey("x-starter-dataField")) {
			Object o = property.vendorExtensions.get("x-starter-dataField");
			if (o != null) {
				// "pass on" the settings for processing (ie: aspects)
				String vx = "@io.starter.ignite.model.DataField({{vx}})";
				String st = "";
				
				// handle hidden datafields
				String strx = o.toString(); // .toLowerCase();
				if(strx.indexOf(",") == -1) {
					st = extractEnumConfig(st, strx.trim());
				}else {					
					StringTokenizer tokr = new StringTokenizer(strx, ",");
					while(tokr.hasMoreTokens()){
						String param = tokr.nextToken();
						param.trim();
						st = extractEnumConfig(st, param);
						if(tokr.hasMoreTokens()) {
							st += ",";
						}
					}
				}
				vx = vx.replace("{{vx}}", st);
				property.vendorExtensions.put("dataAnnotation", vx);
				logger.info("Found Starter DataField Vendor Extension" + vx);
			}
		}

		if (serializeBigDecimalAsString) {
			if (property.baseType.equals("BigDecimal")) {
				// we serialize BigDecimal as `string` to avoid precision
				// loss
				property.vendorExtensions.put("extraAnnotation", "@JsonSerialize(using = ToStringSerializer.class)");

				// this requires some more imports to be added for this
				// model...
				model.imports.add("ToStringSerializer");
				model.imports.add("JsonSerialize");
			}
		}

		if (!fullJavaUtil) {
			if ("array".equals(property.containerType)) {
				model.imports.add("ArrayList");
			} else if ("map".equals(property.containerType)) {
				model.imports.add("HashMap");
			}
		}

		if (!BooleanUtils.toBoolean(model.isEnum)) {
			model.imports.add("ApiModel");
		}

		if ("null".equals(property.example)) {
			property.example = null;
		}

		// Add imports for Jackson
		if (!Boolean.TRUE.equals(model.isEnum)) {
			model.imports.add("JsonProperty");

			if (Boolean.TRUE.equals(model.hasEnums)) {
				model.imports.add("JsonValue");
			}
		} else { // enum class
			// Needed imports for Jackson's JsonCreator
			if (additionalProperties.containsKey("jackson")) {
				model.imports.add("JsonCreator");
			}
		}
	}

	@Override
	public void postProcessParameter(CodegenParameter codegenParameter) {

	}

	@Override
	public void addHandlebarHelpers(Handlebars handlebars) {

	}

	@Override
	public List<CodegenArgument> readLanguageArguments() {
		return null;
	}

	@Override
	public List<CodegenArgument> getLanguageArguments() {
		return null;
	}

	@Override
	public void setLanguageArguments(List<CodegenArgument> list) {

	}

	@Override
	public boolean needsUnflattenedSpec() {
		return false;
	}

	@Override
	public void setUnflattenedOpenAPI(OpenAPI openAPI) {

	}

	@Override
	public ISchemaHandler getSchemaHandler() {
		return null;
	}

	private String extractEnumConfig(String st, String param) {
		try {
			StringTokenizer tx = new StringTokenizer(param, "=");
			String nm = tx.nextToken();
			String vxt = tx.nextToken();

			// if we cannot parse as boolean or number, wrap string in quotes
			try {
				Double dx = Double.parseDouble(vxt);
			} catch (Exception t) {
				if (!vxt.equalsIgnoreCase("true")
						&& !vxt.equalsIgnoreCase("false")) {
					vxt = "\"" + vxt + "\"";
				}
			}
			st += nm + "=" + vxt;
		}catch(Exception e){
			logger.warn("Could not get value from StackGen extension: " + param + " : " + e.toString());
		}
		return st;
	}


}
