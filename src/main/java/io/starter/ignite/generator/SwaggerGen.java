package io.starter.ignite.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
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
	CodegenConfigurator				configurator;
	private JSONObject				configObj;
	private List<SwaggerGen>		pluginSwaggers	= new ArrayList<SwaggerGen>();

	/**
	 * Create and initialize a new SwaggerGen from a JSON config object
	 * 
	 * @param inputSpec
	 *            JSONObject containing config data
	 */
	public SwaggerGen(JSONObject config) {
		this.configObj = config;

		this.configurator = getConfig(SPEC_LOCATION
				+ config.getString("schemaFile"));
		logger.info("Create Swagger Client Apis for:" + config);
	}

	/**
	 * Create and initialize a new SwaggerGen
	 * 
	 * @param inputSpec
	 *            filename of spec (file in templateDirectory)
	 */
	public SwaggerGen(String spec) {
		this.configurator = getConfig(spec);
		logger.info("Create Swagger Client Apis for:" + spec);
	}

	/**
	 * Create and initialize a new SwaggerGen
	 * 
	 * @param inputSpec
	 *            spe file in templateDirectory
	 */
	public SwaggerGen(File spec) {
		this.configurator = CodegenConfigurator.fromFile(CONFIG_FILE);
		logger.info("Create Swagger Client Apis for:" + spec);
	}

	/**
	 * Get a configuration, load spec
	 * 
	 * @param spec
	 * @param configurator
	 */
	private CodegenConfigurator getConfig(String spec) {
		CodegenConfigurator conf = new CodegenConfigurator();

		setStaticConfiguration(spec, conf);

		// main output type
		// (ie: spring, jersey2)
		conf.setLang(getVal("swaggerLang", swaggerLang));

		// the JSON serialization library to use
		// (ie: jersey2, resteasy, resttemplate)
		conf.setLibrary(getVal("swaggerLib", swaggerLib));
		conf.setOutputDir(getVal("genOutputFolder", genOutputFolder));

		conf.setApiPackage(getVal("API_PACKAGE", API_PACKAGE));
		conf.setModelPackage(getVal("API_MODEL_PACKAGE", API_MODEL_PACKAGE));
		conf.setInvokerPackage(getVal("INVOKER_PACKAGE", INVOKER_PACKAGE));
		String gid = orgPackage.substring(0, orgPackage.length() - 1);
		conf.setGroupId(gid);

		conf.setArtifactId(getVal("artifactId", artifactId));
		conf.setArtifactVersion(getVal("artifactVersion", artifactVersion));

		return conf;
	}

	/**
	 * @param spec
	 * @param conf
	 */
	private void setStaticConfiguration(String spec, CodegenConfigurator conf) {

		conf.setVerbose(verbose);
		conf.addDynamicProperty("dynamic-html", "true");
		conf.addDynamicProperty("dateLibrary", "java8");

		// whether to enhance REST api with default Object CRUD
		conf.addAdditionalProperty(IGNITE_GEN_MODEL_CRUD_OPS, "true");
		conf.addAdditionalProperty(IGNITE_GEN_MODEL_ENHANCEMENTS, "true");

		// app config
		conf.setAuth("oauth");
		conf.setInputSpec(spec);
		conf.addAdditionalProperty("CORSMapping", CORSMapping);

		// github
		conf.setGitRepoId(Configuration.gitRepoId);
		conf.setGitUserId(Configuration.gitUserId);

		// locations
		conf.setTemplateDir(getVal("SPEC_LOCATION", SPEC_LOCATION));

		// server info
		conf.addDynamicProperty("serverHost", Configuration.defaultHostname);
		conf.addDynamicProperty("serverPort", Configuration.defaultPort);

		// company info
		conf.addDynamicProperty("developerName", Configuration.developerName);
		conf.addDynamicProperty("developerEmail", Configuration.developerEmail);
		conf.addDynamicProperty("developerOrganization", Configuration.developerOrganization);
		conf.addDynamicProperty("developerOrganizationUrl", Configuration.developerOrganizationUrl);

		// SPRING properties
		conf.addAdditionalProperty("java8", "true");
		conf.addAdditionalProperty("delegatePattern", "true");
		conf.addAdditionalProperty("asynch", "true");
		conf.addAdditionalProperty("useBeanValidation", "true");
		conf.addAdditionalProperty(CodegenConstants.REMOVE_OPERATION_ID_PREFIX, "true");
	}

	/**
	 * JSONObject overrides
	 * Sysprops
	 * 
	 * @param swaggerlang
	 * @param swaggerlang2 
	 * @return
	 */
	private String getVal(String fieldName, String systemVal) {
		if (this.configObj != null) {
			if (this.configObj.has(fieldName)) {
				String v = this.configObj.getString(fieldName);
				if (v != null)
					return v;
			}
		}
		return systemVal;
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
