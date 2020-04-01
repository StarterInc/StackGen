package io.starter.ignite.generator.swagger;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.codegen.CliOption;
import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.ClientOpts;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.auth.AuthParser;
import io.swagger.codegen.config.CodegenConfigurator;
import io.swagger.models.Swagger;
import io.swagger.models.auth.AuthorizationValue;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * A class that contains all codegen configuration properties a user would want
 * to manipulate. An instance could be created by deserializing a JSON file or
 * being populated from CLI or Maven plugin parameters. It also has a
 * convenience method for creating a ClientOptInput class which is THE object
 * DefaultGenerator.java needs to generate code.
 */
public class StackGenCodegenConfigurator extends CodegenConfigurator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Logger LOGGER = LoggerFactory.getLogger(CodegenConfigurator.class);

	private String lang;
	private String inputSpec;
	private String outputDir;
	private boolean verbose;
	private boolean skipOverwrite;
	private boolean removeOperationIdPrefix;
	private String templateDir;
	private String auth;
	private String apiPackage;
	private String modelPackage;
	private String invokerPackage;
	private String modelNamePrefix;
	private String modelNameSuffix;
	private String groupId;
	private String artifactId;
	private String artifactVersion;
	private String library;
	private String ignoreFileOverride;
	private Map<String, String> systemProperties = new HashMap<String, String>();
	private Map<String, String> instantiationTypes = new HashMap<String, String>();
	private Map<String, String> typeMappings = new HashMap<String, String>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private Map<String, String> importMappings = new HashMap<String, String>();
	private Set<String> languageSpecificPrimitives = new HashSet<String>();
	private Map<String, String> reservedWordMappings = new HashMap<String, String>();

	private String gitUserId = "GIT_USER_ID";
	private String gitRepoId = "GIT_REPO_ID";
	private String releaseNote = "Initial Commit";
	private String httpUserAgent;

	private final Map<String, Object> dynamicProperties = new HashMap<String, Object>(); // the map that holds the
																							// JsonAnySetter/JsonAnyGetter
																							// values

	public StackGenCodegenConfigurator() {
        this.setOutputDir(".");
    }

	@Override
	public CodegenConfigurator setLang(String lang) {
		this.lang = lang;
		return this;
	}

	@Override
	public CodegenConfigurator setInputSpec(String inputSpec) {
		this.inputSpec = inputSpec;
		return this;
	}

	@Override
	public String getInputSpec() {
		return inputSpec;
	}

	@Override
	public String getOutputDir() {
		return outputDir;
	}

	@Override
	public CodegenConfigurator setOutputDir(String outputDir) {
		this.outputDir = toAbsolutePathStr(outputDir);
		return this;
	}

	@Override
	public String getModelPackage() {
		return modelPackage;
	}

	@Override
	public CodegenConfigurator setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
		return this;
	}

	@Override
	public String getModelNamePrefix() {
		return modelNamePrefix;
	}

	@Override
	public CodegenConfigurator setModelNamePrefix(String prefix) {
		this.modelNamePrefix = prefix;
		return this;
	}

	@Override
	public boolean getRemoveOperationIdPrefix() {
		return removeOperationIdPrefix;
	}

	@Override
	public CodegenConfigurator setRemoveOperationIdPrefix(boolean removeOperationIdPrefix) {
		this.removeOperationIdPrefix = removeOperationIdPrefix;
		return this;
	}

	@Override
	public String getModelNameSuffix() {
		return modelNameSuffix;
	}

	@Override
	public CodegenConfigurator setModelNameSuffix(String suffix) {
		this.modelNameSuffix = suffix;
		return this;
	}

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	@Override
	public CodegenConfigurator setVerbose(boolean verbose) {
		this.verbose = verbose;
		return this;
	}

	@Override
	public boolean isSkipOverwrite() {
		return skipOverwrite;
	}

	@Override
	public CodegenConfigurator setSkipOverwrite(boolean skipOverwrite) {
		this.skipOverwrite = skipOverwrite;
		return this;
	}

	@Override
	public String getLang() {
		return lang;
	}

	@Override
	public String getTemplateDir() {
		return templateDir;
	}

	@Override
	public CodegenConfigurator setTemplateDir(String templateDir) {
		File f = new File(templateDir);

		// check to see if the folder exists
		if (!(f.exists() && f.isDirectory())) {
			throw new IllegalArgumentException("Template directory " + templateDir + " does not exist.");
		}

		this.templateDir = f.getAbsolutePath();
		return this;
	}

	@Override
	public String getAuth() {
		return auth;
	}

	@Override
	public CodegenConfigurator setAuth(String auth) {
		this.auth = auth;
		return this;
	}

	@Override
	public String getApiPackage() {
		return apiPackage;
	}

	@Override
	public CodegenConfigurator setApiPackage(String apiPackage) {
		this.apiPackage = apiPackage;
		return this;
	}

	@Override
	public String getInvokerPackage() {
		return invokerPackage;
	}

	@Override
	public CodegenConfigurator setInvokerPackage(String invokerPackage) {
		this.invokerPackage = invokerPackage;
		return this;
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	@Override
	public CodegenConfigurator setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	@Override
	public String getArtifactId() {
		return artifactId;
	}

	@Override
	public CodegenConfigurator setArtifactId(String artifactId) {
		this.artifactId = artifactId;
		return this;
	}

	@Override
	public String getArtifactVersion() {
		return artifactVersion;
	}

	@Override
	public CodegenConfigurator setArtifactVersion(String artifactVersion) {
		this.artifactVersion = artifactVersion;
		return this;
	}

	@Override
	public Map<String, String> getSystemProperties() {
		return systemProperties;
	}

	@Override
	public CodegenConfigurator setSystemProperties(Map<String, String> systemProperties) {
		this.systemProperties = systemProperties;
		return this;
	}

	@Override
	public CodegenConfigurator addSystemProperty(String key, String value) {
		this.systemProperties.put(key, value);
		return this;
	}

	@Override
	public Map<String, String> getInstantiationTypes() {
		return instantiationTypes;
	}

	@Override
	public CodegenConfigurator setInstantiationTypes(Map<String, String> instantiationTypes) {
		this.instantiationTypes = instantiationTypes;
		return this;
	}

	@Override
	public CodegenConfigurator addInstantiationType(String key, String value) {
		this.instantiationTypes.put(key, value);
		return this;
	}

	@Override
	public Map<String, String> getTypeMappings() {
		return typeMappings;
	}

	@Override
	public CodegenConfigurator setTypeMappings(Map<String, String> typeMappings) {
		this.typeMappings = typeMappings;
		return this;
	}

	@Override
	public CodegenConfigurator addTypeMapping(String key, String value) {
		this.typeMappings.put(key, value);
		return this;
	}

	@Override
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	@Override
	public CodegenConfigurator setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
		return this;
	}

	@Override
	public CodegenConfigurator addAdditionalProperty(String key, Object value) {
		this.additionalProperties.put(key, value);
		return this;
	}

	@Override
	public Map<String, String> getImportMappings() {
		return importMappings;
	}

	@Override
	public CodegenConfigurator setImportMappings(Map<String, String> importMappings) {
		this.importMappings = importMappings;
		return this;
	}

	@Override
	public CodegenConfigurator addImportMapping(String key, String value) {
		this.importMappings.put(key, value);
		return this;
	}

	@Override
	public Set<String> getLanguageSpecificPrimitives() {
		return languageSpecificPrimitives;
	}

	@Override
	public CodegenConfigurator setLanguageSpecificPrimitives(Set<String> languageSpecificPrimitives) {
		this.languageSpecificPrimitives = languageSpecificPrimitives;
		return this;
	}

	@Override
	public CodegenConfigurator addLanguageSpecificPrimitive(String value) {
		this.languageSpecificPrimitives.add(value);
		return this;
	}

	@Override
	public String getLibrary() {
		return library;
	}

	@Override
	public CodegenConfigurator setLibrary(String library) {
		this.library = library;
		return this;
	}

	@Override
	public String getGitUserId() {
		return gitUserId;
	}

	@Override
	public CodegenConfigurator setGitUserId(String gitUserId) {
		this.gitUserId = gitUserId;
		return this;
	}

	@Override
	public String getGitRepoId() {
		return gitRepoId;
	}

	@Override
	public CodegenConfigurator setGitRepoId(String gitRepoId) {
		this.gitRepoId = gitRepoId;
		return this;
	}

	@Override
	public String getReleaseNote() {
		return releaseNote;
	}

	@Override
	public CodegenConfigurator setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
		return this;
	}

	@Override
	public String getHttpUserAgent() {
		return httpUserAgent;
	}

	@Override
	public CodegenConfigurator setHttpUserAgent(String httpUserAgent) {
		this.httpUserAgent = httpUserAgent;
		return this;
	}

	@Override
	public Map<String, String> getReservedWordsMappings() {
		return reservedWordMappings;
	}

	@Override
	public CodegenConfigurator setReservedWordsMappings(Map<String, String> reservedWordsMappings) {
		this.reservedWordMappings = reservedWordsMappings;
		return this;
	}

	@Override
	public CodegenConfigurator addAdditionalReservedWordMapping(String key, String value) {
		this.reservedWordMappings.put(key, value);
		return this;
	}

	@Override
	public String getIgnoreFileOverride() {
		return ignoreFileOverride;
	}

	@Override
	public CodegenConfigurator setIgnoreFileOverride(final String ignoreFileOverride) {
		this.ignoreFileOverride = ignoreFileOverride;
		return this;
	}

	@Override
	public ClientOptInput toClientOptInput() {

		Validate.notEmpty(lang, "language must be specified");
		Validate.notEmpty(inputSpec, "input spec must be specified");

		setVerboseFlags();
		setSystemProperties();

		CodegenConfig config = StackGenCodegenConfigLoader.forName(lang);

		config.setInputSpec(inputSpec);
		config.setOutputDir(outputDir);
		config.setSkipOverwrite(skipOverwrite);
		config.setIgnoreFilePathOverride(ignoreFileOverride);
		config.setRemoveOperationIdPrefix(removeOperationIdPrefix);

		config.instantiationTypes().putAll(instantiationTypes);
		config.typeMapping().putAll(typeMappings);
		config.importMapping().putAll(importMappings);
		config.languageSpecificPrimitives().addAll(languageSpecificPrimitives);
		config.reservedWordsMappings().putAll(reservedWordMappings);

		checkAndSetAdditionalProperty(apiPackage, CodegenConstants.API_PACKAGE);
		checkAndSetAdditionalProperty(modelPackage, CodegenConstants.MODEL_PACKAGE);
		checkAndSetAdditionalProperty(invokerPackage, CodegenConstants.INVOKER_PACKAGE);
		checkAndSetAdditionalProperty(groupId, CodegenConstants.GROUP_ID);
		checkAndSetAdditionalProperty(artifactId, CodegenConstants.ARTIFACT_ID);
		checkAndSetAdditionalProperty(artifactVersion, CodegenConstants.ARTIFACT_VERSION);
		checkAndSetAdditionalProperty(templateDir, toAbsolutePathStr(templateDir), CodegenConstants.TEMPLATE_DIR);
		checkAndSetAdditionalProperty(modelNamePrefix, CodegenConstants.MODEL_NAME_PREFIX);
		checkAndSetAdditionalProperty(modelNameSuffix, CodegenConstants.MODEL_NAME_SUFFIX);
		checkAndSetAdditionalProperty(gitUserId, CodegenConstants.GIT_USER_ID);
		checkAndSetAdditionalProperty(gitRepoId, CodegenConstants.GIT_REPO_ID);
		checkAndSetAdditionalProperty(releaseNote, CodegenConstants.RELEASE_NOTE);
		checkAndSetAdditionalProperty(httpUserAgent, CodegenConstants.HTTP_USER_AGENT);

		handleDynamicProperties(config);

		if (isNotEmpty(library)) {
			config.setLibrary(library);
		}

		config.additionalProperties().putAll(additionalProperties);

		ClientOptInput input = new ClientOptInput().config(config);

		final List<AuthorizationValue> authorizationValues = AuthParser.parse(auth);

		Swagger swagger = new SwaggerParser().read(inputSpec, authorizationValues, true);

		input.opts(new ClientOpts()).swagger(swagger);

		return input;
	}

	@Override
	@JsonAnySetter
	public CodegenConfigurator addDynamicProperty(String name, Object value) {
		dynamicProperties.put(name, value);
		return this;
	}

	@Override
	@JsonAnyGetter
	public Map<String, Object> getDynamicProperties() {
		return dynamicProperties;
	}

	private void handleDynamicProperties(CodegenConfig codegenConfig) {
		for (CliOption langCliOption : codegenConfig.cliOptions()) {
			String opt = langCliOption.getOpt();
			if (dynamicProperties.containsKey(opt)) {
				codegenConfig.additionalProperties().put(opt, dynamicProperties.get(opt));
			} else if (systemProperties.containsKey(opt)) {
				codegenConfig.additionalProperties().put(opt, systemProperties.get(opt));
			}
		}
	}

	private void setVerboseFlags() {
		if (!verbose) {
			return;
		}
		LOGGER.info("\nVERBOSE MODE: ON. Additional debug options are injected"
				+ "\n - [debugSwagger] prints the swagger specification as interpreted by the codegen"
				+ "\n - [debugModels] prints models passed to the template engine"
				+ "\n - [debugOperations] prints operations passed to the template engine"
				+ "\n - [debugSupportingFiles] prints additional data passed to the template engine");

		System.setProperty("debugSwagger", "");
		System.setProperty("debugModels", "");
		System.setProperty("debugOperations", "");
		System.setProperty("debugSupportingFiles", "");
	}

	private void setSystemProperties() {
		for (Map.Entry<String, String> entry : systemProperties.entrySet()) {
			System.setProperty(entry.getKey(), entry.getValue());
		}
	}

	private static String toAbsolutePathStr(String path) {
		if (isNotEmpty(path)) {
			return Paths.get(path).toAbsolutePath().toString();
		}

		return path;

	}

	private void checkAndSetAdditionalProperty(String property, String propertyKey) {
		checkAndSetAdditionalProperty(property, property, propertyKey);
	}

	private void checkAndSetAdditionalProperty(String property, String valueToSet, String propertyKey) {
		if (isNotEmpty(property)) {
			additionalProperties.put(propertyKey, valueToSet);
		}
	}

	public static CodegenConfigurator fromFile(String configFile) {

		if (isNotEmpty(configFile)) {
			try {
				return Json.mapper().readValue(new File(configFile), CodegenConfigurator.class);
			} catch (IOException e) {
				LOGGER.error("Unable to deserialize config file: " + configFile, e);
			}
		}
		return null;
	}

}
