package io.starter.ignite.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.swagger.IgniteGenerator;
import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.config.CodegenConfigurator;
import io.swagger.models.Model;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;

/**
 * responsible for generating the Swagger server and clients
 * 
 * @author John McMahon (@TechnoCharms)
 * 
 */
public class SwaggerGen implements Configuration {

	protected static final Logger	logger			= LoggerFactory
			.getLogger(SwaggerGen.class);

	IgniteGenerator					generator		= new IgniteGenerator();
	CodegenConfigurator				configurator	= CodegenConfigurator
			.fromFile(CONFIG_FILE);

	private List<SwaggerGen>		pluginSwaggers	= new ArrayList<SwaggerGen>();

	/**
	 * Create and initialize a new SwaggerGen
	 * 
	 * @param inputSpec
	 *            filename of spec (file in templateDirectory)
	 */
	public SwaggerGen(String spec) {

		logger.info("Create Swagger Client Apis for:" + spec);

		// read from config file
		if (configurator == null) {
			// createa a fresh configurator
			configurator = new CodegenConfigurator();
		}

		// main output type
		// (ie: spring, jersey2)
		configurator.setLang(SWAGGER_LANG);

		// the JSON serialization library to use
		// (ie: jersey2, resteasy, resttemplate)
		configurator.setLibrary(SWAGGER_LIB);
		configurator.setOutputDir(JAVA_GEN_PATH);

		configurator.setArtifactId(ARTIFACT_ID);
		configurator.setApiPackage(API_PACKAGE);
		configurator.setModelPackage(API_MODEL_PACKAGE);
		configurator.setInvokerPackage(INVOKER_PACKAGE);

		configurator.setArtifactVersion(ARTIFACT_VERSION);
		configurator.setVerbose(VERBOSE);
		configurator.addDynamicProperty("dynamic-html", "true");
		configurator.addDynamicProperty("dateLibrary", "java8");

		// whether to enhance REST api with default Object CRUD
		configurator
				.addAdditionalProperty(Configuration.IGNITE_GEN_MODEL_CRUD_OPS, "true");
		configurator
				.addAdditionalProperty(Configuration.IGNITE_GEN_MODEL_ENHANCEMENTS, "true");

		// company info
		configurator
				.addDynamicProperty("developerName", "John McMahon @TechnoCharms");
		configurator.addDynamicProperty("developerEmail", "info@starter.io");
		configurator
				.addDynamicProperty("developerOrganization", "Starter Inc.");
		configurator
				.addDynamicProperty("developerOrganizationUrl", "http://ignite.starter.io/");

		// SPRING props
		configurator.addAdditionalProperty("java8", "true");
		configurator.addAdditionalProperty("delegatePattern", "true");
		configurator.addAdditionalProperty("asynch", "true");
		configurator.addAdditionalProperty("useBeanValidation", "true");
		configurator
				.addAdditionalProperty(CodegenConstants.REMOVE_OPERATION_ID_PREFIX, "true");

		// app config
		configurator.setAuth("oauth");
		configurator.setInputSpec(spec);

		// locations
		configurator.setTemplateDir(SPEC_LOCATION);

		// github
		configurator.setGitRepoId("StarterIgnite");
		configurator.setGitUserId("Spaceghost69");

	}

	public List<File> generate() {
		final ClientOptInput clientOptInput = mergePluginSwaggers();
		return new IgniteGenerator().opts(clientOptInput).generate();
	}

	/**
	 * Merge all of the loaded plugin swagger specs into this one
	 * 
	 * @see addSwagger(SwaggerGen x)
	 */
	ClientOptInput mergePluginSwaggers() {
		final ClientOptInput clientOptInput = configurator.toClientOptInput();

		// merge swagger
		Swagger x = clientOptInput.getSwagger();
		for (SwaggerGen t : pluginSwaggers) {
			try {
				logger.info("Merging plugin swagger: " + t);
				Swagger s = t.configurator.toClientOptInput().getSwagger();
				if (s != null)
					mergeSwagger(s, x);
			} catch (Throwable e) {
				logger.warn("Merging plugin swagger " + t + " failed: " + e);
			}
		}
		return clientOptInput;
	}

	/**
	 * merge the plugins
	 * 
	 * @param swag
	 */
	void addSwagger(SwaggerGen swag) {
		this.pluginSwaggers.add(swag);
	}

	/**
	 * manual copy of swaggers into target
	 * 
	 * @param plugin
	 * @param target
	 */
	void mergeSwagger(Swagger plugin, Swagger target) {

		List<Scheme> schemes = plugin.getSchemes();
		for (Scheme c : schemes)
			target.addScheme(c);

		List<String> consumes = plugin.getConsumes();
		if (consumes != null)
			for (String c : consumes)
				target.addConsumes(c);

		List<String> produces = plugin.getProduces();
		if (produces != null)
			for (String c : produces)
				target.addProduces(c);

		Map<String, Model> definitions = plugin.getDefinitions();
		if (definitions != null)
			for (String c : definitions.keySet())
				target.addDefinition(c, definitions.get(c));

		Map<String, Parameter> parameters = plugin.getParameters();
		if (parameters != null)
			for (String c : parameters.keySet())
				target.addParameter(c, parameters.get(c));

		// target.addSecurity(securityRequirement);
		List<Tag> tags = plugin.getTags();
		if (tags != null)
			for (Tag c : tags)
				target.addTag(c);
	}

}
