package io.starter.ignite.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.swagger.IgniteGenerator;
import io.starter.ignite.util.SystemConstants;
import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.CodegenConstants;
import io.swagger.models.Model;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;

/**
 * responsible for generating the Swagger server and clients
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 * 
 */
public class SwaggerGen extends Gen {

	protected static final Logger logger = LoggerFactory.getLogger(SwaggerGen.class);

	IgniteGenerator generator = new IgniteGenerator(config);

	/**
	 * @return the generator
	 */
	public IgniteGenerator getGenerator() {
		return generator;
	}

	/**
	 * @param generator the generator to set
	 */
	public void setGenerator(IgniteGenerator generator) {
		this.generator = generator;
	}

	/**
	 * @return the configObj
	 */
	public JSONObject getConfigObj() {
		return configObj;
	}

	/**
	 * @param configObj the configObj to set
	 */
	public void setConfigObj(JSONObject configObj) {
		this.configObj = configObj;
	}

	/**
	 * @return the pluginSwaggers
	 */
	public List<SwaggerGen> getPluginSwaggers() {
		return pluginSwaggers;
	}

	/**
	 * @param pluginSwaggers the pluginSwaggers to set
	 */
	public void setPluginSwaggers(List<SwaggerGen> pluginSwaggers) {
		this.pluginSwaggers = pluginSwaggers;
	}

	// the configObj is basically the
	// command line stackgen values only
	private JSONObject configObj;

	List<SwaggerGen> pluginSwaggers = new ArrayList<SwaggerGen>();

	public SwaggerGen(StackGenConfigurator cfg) {
		super(cfg);
		setStaticConfiguration();
	}

	/**
	 * Create and initialize a new SwaggerGen from a JSON config object
	 * 
	 * @param inputSpec JSONObject containing config data
	 */
	public SwaggerGen(JSONObject cfg) {
		super();
		this.configObj = cfg;

		config = getConfig(config.getSpecLocation() + cfg.getString("schemaFile"));

		logger.info("Configured StackGen Schema for:" + config.schemaName);
	}

	/**
	 * Create and initialize a new SwaggerGen
	 * 
	 * @param inputSpec filename of spec (file in templateDirectory)
	 */
	public SwaggerGen(String spec) {
		super();
		config = getConfig(spec);
		logger.info("StackGen Schema Initialized: " + config.schemaName);
	}

	/**
	 * Create and initialize a new SwaggerGen
	 * 
	 * @param inputSpec spe file in templateDirectory
	 */
	public SwaggerGen(File spec) {
		super();
		config = (StackGenConfigurator) StackGenConfigurator.fromFile(spec.getPath());
		logger.info("Create Swagger Client Apis for:" + spec);
	}

	/**
	 * Get a configuration, load spec
	 * 
	 * @param spec
	 * @param configurator
	 */
	private StackGenConfigurator getConfig(String spec) {
		setStaticConfiguration();
		if (!spec.contains(config.getTemplateDir())) {
			spec = config.getTemplateDir() + "/" + spec;
		}
		config.setInputSpec(spec);
		return config;
	}

	/**
	 * @param spec
	 * @param config
	 */
	private void setStaticConfiguration() {

		// config.setVerbose(verbose);

		// main output type
		// (ie: spring, jersey2)
		config.setLang(getVal("swaggerLang", config.swaggerLang));

		// the JSON serialization library to use
		// (ie: jersey2, resteasy, resttemplate)
		config.setLibrary(getVal("swaggerLib", "spring-boot"));

		// config.setReleaseNote(getVal("description"), description);

		config.setOutputDir(getVal("genOutputFolder", config.getGenOutputFolder() ));

		config.setApiPackage(getVal("API_PACKAGE", config.getApiPackage()));
		config.setModelPackage(getVal("API_MODEL_PACKAGE", config.getApiModelPackage()));
		config.setInvokerPackage(getVal("INVOKER_PACKAGE", config.getInvokerPackage()));
		String gid = config.getOrgPackage().substring(0, config.getOrgPackage().length() - 1);
		config.setGroupId(gid);

		config.setArtifactId(getVal("artifactId", config.getArtifactId()));
		config.setArtifactVersion(getVal("artifactVersion", config.getArtifactVersion()));

		config.addDynamicProperty("dynamic-html", "true");
		config.addDynamicProperty("dateLibrary", "java8");

		// whether to enhance REST api with default Object CRUD
		config.addAdditionalProperty(config.IGNITE_GEN_MODEL_CRUD_OPS, "true");
		config.addAdditionalProperty(config.IGNITE_GEN_MODEL_ENHANCEMENTS, "true");

		// app config
		config.setAuth("oauth");
		config.addAdditionalProperty("CORSMapping", config.getCORSMapping());
		config.addAdditionalProperty("CORSOrigins", config.getCORSOrigins());

		// github
		config.setGitRepoId(config.gitRepoId);
		config.setGitUserId(config.gitUserId);

		// locations
		config.setTemplateDir(getVal("SPEC_LOCATION", config.getSpecLocation()));

		// server info
		config.addDynamicProperty("serverHost", config.defaultHostname);
		config.addDynamicProperty("serverPort", config.defaultPort);

		// company info
		config.addDynamicProperty("developerName", config.developerName);
		config.addDynamicProperty("developerEmail", config.developerEmail);
		config.addDynamicProperty("developerOrganization", config.developerOrganization);
		config.addDynamicProperty("developerOrganizationUrl", config.developerOrganizationUrl);

		// SPRING properties
		config.addAdditionalProperty("java8", "true");
		config.addAdditionalProperty("delegatePattern", "true");
		config.addAdditionalProperty("asynch", "true");
		config.addAdditionalProperty("useDelegateValidation", "true");
		config.addAdditionalProperty(CodegenConstants.REMOVE_OPERATION_ID_PREFIX, "true");
	}

	/**
	 * JSONObject overrides Sysprops
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

	public io.swagger.codegen.Generator preGen() {
		final ClientOptInput clientOptInput = mergePluginSwaggers();
		return generator.opts(clientOptInput);
	}

	public List<File> generate() {
		return preGen().generate();
	}

	/**
	 * Merge all of the loaded plugin swagger specs into this one
	 * 
	 * returns a ready-to-run config
	 * 
	 * @see addSwagger(SwaggerGen x)
	 */
	ClientOptInput mergePluginSwaggers() {
		try {
			final ClientOptInput clientOptInput = config.toClientOptInput();

			// merge swagger
			Swagger x = clientOptInput.getSwagger();
			for (SwaggerGen t : pluginSwaggers) {
				try {
					logger.info("Merging plugin swagger: " + t);
					Swagger s = t.config.toClientOptInput().getSwagger();
					if (s != null)
						mergeSwagger(s, x);
				} catch (Throwable e) {
					logger.warn("Merging plugin swagger " + t + " failed: " + e);
				}
			}
			return clientOptInput;
		} catch (Exception e) {
			throw e;
		}
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
