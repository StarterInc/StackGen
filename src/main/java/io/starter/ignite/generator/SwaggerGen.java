package io.starter.ignite.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.swagger.codegen.v3.Generator;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.swagger.languages.IgniteGenerator;

import io.swagger.codegen.v3.ClientOptInput;
import io.swagger.codegen.v3.CodegenConstants;

/**
 * responsible for generating the Swagger server and clients
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 * 
 */
public class SwaggerGen extends Gen {

	private static final Logger logger = LoggerFactory.getLogger(SwaggerGen.class);

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
		this(new File(  (StackGenConfigurator.getSpecLocation() != null ? StackGenConfigurator.getSpecLocation() : "") + 
						( cfg.getString("schemaFile") != null 
						? cfg.getString("schemaFile") 
						: "")
				 ));
	}

	/**
	 * Create and initialize a new SwaggerGen
	 * 
	 * @param spec filename of spec (file in templateDirectory)
	 */
	public SwaggerGen(String spec) {
		config = getConfig(spec);
		setStaticConfiguration();
	}
	
	/**
	 * Create and initialize a new SwaggerGen from a YAML or JSON spec file
	 * 
	 * @param inputSpec spe file in templateDirectory
	 */

	public SwaggerGen(File spec) {
		super();
		try {
			//  YAML
			config=getConfig(spec.getPath());
		}catch(Exception e) {
			// JSON
			config = (StackGenConfigurator) StackGenConfigurator.fromFile(spec.getPath());
		}
		setStaticConfiguration();
		if(config==null) {
			throw new RuntimeException("SwaggerGen config cannot be null after init");
		}
		logger.debug("Initializing Generator for: [ " + config.getArtifactId() + ".v" + config.getArtifactVersion() + " ]");
	}

	/**
	 * Get a configuration, load spec
	 * 
	 * @param spec
	 * @param configurator
	 */
	private StackGenConfigurator getConfig(String spec) {		
		if(spec.startsWith("/var/") || spec.startsWith("/tmp/")){
			; // allow temp files
		} else if (!spec.contains(StackGenConfigurator.getSpecLocation())) {
			spec = StackGenConfigurator.getSpecLocation() + spec;
		}
		if(config == null) {
			config = new StackGenConfigurator();
		}
		config.setInputSpec(spec);
		setStaticConfiguration();
		return config;
	}

	/**
	 *
	 */
	private void setStaticConfiguration() {

		// config.setVerbose(verbose);

		// main output type
		// (ie: spring, jersey2)
		config.setLang(getVal("swaggerLang", config.swaggerLang));

		// the JSON serialization library to use
		// (ie: jersey2, resteasy, resttemplate)
		config.setLibrary(getVal("swaggerLib", "spring-boot"));

		// config.setReleaseNote("SET RELEASE NOTES", config.getReleaseNote());

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

		// SG library version
		config.addAdditionalProperty("StackGenVersion", config.getStackGenVersion());

		// github
		config.setGitRepoId(config.gitRepoId);
		config.setGitUserId(config.gitUserId);
		
		// locations
		// Don't do this here... config.setTemplateDir(config.getOutputDir() + "/templates/" + config.getLibrary());

		// server info
		config.addDynamicProperty("serverHost", config.defaultHostname);
		config.addDynamicProperty("serverPort", config.defaultPort);

		// company info
		config.addDynamicProperty("developerName", config.developerName);
		config.addDynamicProperty("developerEmail", config.developerEmail);
		config.addDynamicProperty("developerOrganization", config.developerOrganization);
		config.addDynamicProperty("developerOrganizationUrl", config.developerOrganizationUrl);
		config.addDynamicProperty("infoUrl", config.developerOrganizationUrl + "/info");

		// SPRING properties
		config.addAdditionalProperty("java8", "true");
		config.addAdditionalProperty("delegatePattern", "true");
		config.addAdditionalProperty("asynch", "true");
		config.addAdditionalProperty("useDelegateValidation", "true");
		config.addAdditionalProperty("useBeanValidation", "true");
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

	public Generator preGen() {
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
	 */
	public ClientOptInput mergePluginSwaggers() {
		try {
			final ClientOptInput clientOptInput = config.toClientOptInput();

			// merge swagger
			OpenAPI x = clientOptInput.getOpenAPI();
			for (SwaggerGen t : pluginSwaggers) {
				try {
					logger.info("Merging plugin swagger: " + t);
					OpenAPI s = t.config.toClientOptInput().getOpenAPI();
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
	void mergeSwagger(OpenAPI plugin, OpenAPI target) {
		Components definitions = plugin.getComponents();
		Components targetComponents = target.getComponents();
		targetComponents.getSchemas().putAll(definitions.getSchemas());
		targetComponents.getCallbacks().putAll(definitions.getCallbacks());
		targetComponents.getExamples().putAll(definitions.getExamples());
		targetComponents.getExtensions().putAll(definitions.getExtensions());
		targetComponents.getHeaders().putAll(definitions.getHeaders());
		targetComponents.getLinks().putAll(definitions.getLinks());targetComponents.getExtensions().putAll(definitions.getExtensions());
		targetComponents.getParameters().putAll(definitions.getParameters());
		targetComponents.getResponses().putAll(definitions.getResponses());
		targetComponents.getRequestBodies().putAll(definitions.getRequestBodies());
		targetComponents.getSecuritySchemes().putAll(definitions.getSecuritySchemes());
	}
}
