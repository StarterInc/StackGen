package io.starter.ignite.generator;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.json.JSONObject;
import org.springframework.util.ReflectionUtils;

import io.starter.ignite.generator.swagger.StackGenCodegenConfigLoader;
import io.starter.ignite.util.SystemConstants;
import io.starter.toolkit.StringTool;
import io.swagger.codegen.CliOption;
import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.ClientOpts;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConfigLoader;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.auth.AuthParser;
import io.swagger.codegen.config.CodegenConfigurator;
import io.swagger.models.Swagger;
import io.swagger.models.auth.AuthorizationValue;
import io.swagger.parser.SwaggerParser;

/**
 * portable sg configs
 * 
 * @author johnmcmahon
 *
 */
public class StackGenConfigurator extends CodegenConfigurator {

	private static final long serialVersionUID = 23423423423L;

	public static String LINE_FEED = "\r\n";
	public String adminServiceURL = (System.getProperty("adminServiceURL") != null
			? System.getProperty("adminServiceURL")
			: "http://localhost:8099/");

	public String dbUser = (System.getProperty("dbUser") != null ? System.getProperty("dbUser") : "stackgen");
	public String dbPassword = (System.getProperty("dbPassword") != null ? System.getProperty("dbPassword")
			: "password");
	public String dbName = (System.getProperty("dbName") != null ? System.getProperty("dbName") : "mystack");
	public String dbUrl = (System.getProperty("dbUrl") != null ? System.getProperty("dbUrl")
			: "jdbc:mysql://mydb.myco.com");

	public String defaultHostname = (System.getProperty("defaultHostname") != null
			? System.getProperty("defaultHostname")
			: "localhost");

	public String defaultPort = (System.getProperty("defaultPort") != null ? System.getProperty("defaultPort")
			: "8100");

	public String gitRepoId = (System.getProperty("gitRepoId") != null ? System.getProperty("gitRepoId") : "StackGen");

	public String gitUserId = (System.getProperty("gitUserId") != null ? System.getProperty("gitUserId")
			: "spaceghost69");

	public String developerOrganizationUrl = (System.getProperty("developerOrganizationUrl") != null
			? System.getProperty("developerOrganizationUrl")
			: "http://starter.io");

	public String developerName = (System.getProperty("developerName") != null ? System.getProperty("developerName")
			: "Stack Dev");

	public String developerEmail = (System.getProperty("developerEmail") != null ? System.getProperty("developerEmail")
			: "info@stackgen.io");

	public String developerOrganization = (System.getProperty("developerOrganization") != null
			? System.getProperty("developerOrganization")
			: "Starter Inc.");

	/**
	 * set the value of allowed CORS request paths
	 */
	public String getCORSMapping() {
		return (System.getProperty("CORSMapping") != null ? System.getProperty("CORSMapping") : "*/**");
	}

	public String getCORSOrigins() {
		return (System.getProperty("CORSMapping") != null ? System.getProperty("CORSOrigins") : "localhost");
	}

	// TODO: these 2 are unused
	public String getAdminUser() {
		return (System.getProperty("adminUser") != null ? System.getProperty("adminUser") : "admin");
	}

	public String getAdminPassword() {
		return (System.getProperty("adminPassword") != null ? System.getProperty("adminPassword") : "ch@ng3m3");
	}

	boolean skipJavaGen = (System.getProperty("skipJavaGen") != null
			? Boolean.parseBoolean(System.getProperty("skipJavaGen"))
			: false);

	boolean skipDbGen = (System.getProperty("skipDbGen") != null ? Boolean.parseBoolean(System.getProperty("skipDbGen"))
			: false);

	boolean skipReactGen = (System.getProperty("skipReactGen") != null
			? Boolean.parseBoolean(System.getProperty("skipReactGen"))
			: false);

	boolean skipMybatisGen = (System.getProperty("skipMybatisGen") != null
			? Boolean.parseBoolean(System.getProperty("skipMybatisGen"))
			: false);

	boolean skipMavenBuildGeneratedApp = (System.getProperty("skipMavenBuildGeneratedApp") != null
			? Boolean.parseBoolean(System.getProperty("skipMavenBuildGeneratedApp"))
			: true);

	boolean overwriteMode = (System.getProperty("overwriteMode") != null
			? Boolean.parseBoolean(System.getProperty("overwriteMode"))
			: true);

	boolean iteratePluginGen = (System.getProperty("iteratePluginGen") != null
			? Boolean.parseBoolean(System.getProperty("iteratePluginGen"))
			: true);

	boolean mergePluginGen = (System.getProperty("mergePluginGen") != null
			? Boolean.parseBoolean(System.getProperty("mergePluginGen"))
			: false);

	boolean verbose = (System.getProperty("verbose") != null ? Boolean.parseBoolean(System.getProperty("verbose"))
			: false);

	boolean debug = (System.getProperty("debug") != null ? Boolean.parseBoolean(System.getProperty("debug")) : false);

	// spring, java, resteasy
	public String swaggerLang = (System.getProperty("swaggerLang") != null ? System.getProperty("swaggerLang")
			: "stackgen-java-spring");

	// spring-boot ,jersey2
	public String swaggerLib = (System.getProperty("swaggerLib") != null ? System.getProperty("swaggerLib")
			: "spring-boot");

//	private String artifactid = "artifactid";
//	public String getArtifactId() { return artifactid) }

	// DML/DB section
	private String schemaName = null;

	public String getSchemaName() { 
		if(schemaName != null) { 
			return schemaName;
		}
		return System.getProperty("schemaName") != null ? System.getProperty("schemaName") : "schemaName";
	}

	public void setSchemaName(String sn) { 
		this.schemaName = sn;
	}

	public String getTableNamePrefix() {
		return getSchemaName() + "$";
	}

	// default is lowercase, this forces uppercase
	public boolean columnsUpperCase = (System.getProperty("columnsUpperCase") != null
			? Boolean.parseBoolean(System.getProperty("columnsUpperCase"))
			: false);

	boolean dbGenDropTable = (System.getProperty("dbGenDropTable") != null
			? Boolean.parseBoolean(System.getProperty("dbGenDropTable"))
			: false);

	// end DML section
	String javaGenFolderName = "/gen";

	public void setJavaGenFolderName(String gn) {
		javaGenFolderName = gn;
	}

	public String getJavaGenFolderName() {
		return (System.getProperty("javaGenFolderName") != null ? System.getProperty("javaGenFolderName")
				: javaGenFolderName);
	}

	public String getGenOutputFolder() {
		return (System.getProperty("genOutputFolder") != null ? System.getProperty("genOutputFolder")
				: SystemConstants.rootFolder) + getJavaGenFolderName();
	}

	public String getJavaGenArchiveFolder() {
		return "/archive" + getJavaGenFolderName();
	}

	public String getJavaGenArchivePath() {
		return (System.getProperty("javaGenArchivePath") != null ? System.getProperty("javaGenArchivePath")
				: SystemConstants.rootFolder + getJavaGenArchiveFolder());
	}

	public String getSourceMain() {
		return (System.getProperty("SOURCE_MAIN") != null ? System.getProperty("SOURCE_MAIN")
				: SystemConstants.rootFolder) + "/src/main";
	}

	public String getSourceMainJava() {
		return getSourceMain() + "/java";
	}

	public String getSourceResources() {
		return (System.getProperty("sourceResources") != null ? System.getProperty("sourceResources")
				: "/src/resources");
	}

	// this is the source folder for any classes in the generator itself...
	public String getJavaGenSourceFolder() {
		return getGenOutputFolder() + "/src/main/java";
	}

	public String getJavaGenResourcesFolder() {
		return getGenOutputFolder() + getSourceResources();
	}

	public File JAVA_GEN_SRC = new File(getJavaGenSourceFolder());

	boolean DISABLE_DATA_FIELD_ASPECT = true;
	boolean DISABLE_SECURE_FIELD_ASPECT = false;

	// ## SwaggerGen OPEN API
	public String getArtifactVersion() {
		return (System.getProperty("artifactVersion") != null ? System.getProperty("artifactVersion")
				: "1.0.1-SNAPSHOT");
	}

	public String ADD_GEN_CLASS_NAME = "Service";
	public String LONG_DATE_FORMAT = "MMM/d/yyyy HH:mm:ss Z";
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(LONG_DATE_FORMAT);

	public String getOrgPackage() {
		return (System.getProperty("orgPackage") != null ? System.getProperty("orgPackage") : "io.starter.");
	}

	public String getOrgFolder() {
		return (System.getProperty("orgFolder") != null ? System.getProperty("orgFolder") : "io/starter/");
	}

	public String getSpecLocation() {
		return SystemConstants.rootFolder + getSourceResources() + "/openapi_specs/";
	}

	public String PLUGIN_SPEC_LOCATION = getSpecLocation() + "plugins/";

	public String getIgniteModelPackage() {
		return getOrgPackage() + getArtifactId() + ".model";
	}

	public String getApiModelPackage() {
		return getOrgPackage() + getArtifactId() + ".model";
	}

	public String getApiPackage() {
		return getOrgPackage() + getArtifactId() + ".api";
	}

	public String getModelPackage() {
		return getOrgPackage() + getArtifactId() + ".model";
	}

	public String getModelDaoPackage() {
		return getModelPackage() + ".dao";
	}

	public String getInvokerPackage() {
		return getOrgPackage() + getArtifactId() + ".invoker";
	}

	public String getPackageDir() {
		return getOrgFolder() + getArtifactId();
	}

	public String getModelPackageDir() {
		return getPackageDir() + "/model/";
	}

	public String getApiPackageDir() {
		return getPackageDir() + "/api/";
	}

	public String getModelDaoPackageDir() {
		return getPackageDir() + "/model/dao/";
	}

	public String getModelClasses() {
		return getJavaGenSourceFolder() + "/" + getModelPackageDir();
	}

	// ## Mybatis
	int DB_TIMEOUT = 10000;

	public String TIMEZONE_OFFSET = (System.getProperty("TIMEZONE_OFFSET") != null
			? System.getProperty("TIMEZONE_OFFSET")
			: "-08:00");

	public String getSqlMapsPath() {
		return getOrgFolder() + getArtifactId().replace(".", "/") + "/model/dao/";
	}

	public String getMybatisGenConfigTemplate() {
		return SystemConstants.rootFolder + getSourceResources() + "/templates/MyBatisGeneratorConfig.xml";
	}

	public String getMybatisGenConfigOut() {
		return getGenOutputFolder() + getSourceResources() + "/MyBatisGeneratorConfig.xml";
	}

	public String getMybatisConfigTemplate() {
		return SystemConstants.rootFolder + getSourceResources() + "/templates/MyBatisConfig.xml";
	}

	public String getMybatisConfigOut() {
		return getGenOutputFolder() + getJavaGenFolderName() + "/src/main/resources/MyBatisConfig.xml";
	}

	public String MYBATIS_COL_ENUM_FLAG = "ENUM";

	List<String> FOLDER_SKIP_LIST = new ArrayList<>(
			Arrays.asList(getJavaGenFolderName(), "org", "swagger", "node_modules"));

	public String SPRING_DELEGATE = "ApiDelegate";

	public String IGNITE_GEN_MODEL_ENHANCEMENTS = "igniteGenerateModelEnhancements";
	public String IGNITE_GEN_MODEL_CRUD_OPS = "igniteGenerateCRUDOps";

	public String IGNITE_GEN_REST_PATH_PREFIX = "data/";

	int DB_ENCRYPTED_COLUMN_MULTIPLIER = 5;

	public String language = "en"; // language
	public String country = "US"; // country

	static String[] RESERVED_WORD_LIST = { "ApiResponse" };

	public String GENERATED_TEXT_BLOCK = "Starter StackGen 'JavaGen' Generated";

	// the actual Schema contents (used if this swagger is not loading from a
	// file...)
	public String schemaData = null;

	private String releaseNote;

	private String templateDir;

	private String auth;

	// public String rootFolder;
	/**
	 * App-wide utility method for checking against list of reserved words
	 *
	 * @param the string to check
	 * @return whether the string is in the reserved word list (case insensitive)
	 */
	public static boolean checkReservedWord(String k) {
		for (final String x : RESERVED_WORD_LIST) {
			if (x.equalsIgnoreCase(k)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * utility method for setting config values from a JSON object
	 * 
	 * @param config2
	 *
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	static StackGenConfigurator configureFromJSON(final JSONObject config)
			throws IllegalArgumentException, IllegalAccessException {
		return configureFromJSON(config, null);

	}

	/**
	 * utility method for setting config values from a JSON object
	 *
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	static StackGenConfigurator configureFromJSON(final JSONObject config, StackGenConfigurator cfgx)
			throws IllegalArgumentException, IllegalAccessException {
		final String[] names = JSONObject.getNames(config);

		// init if null
		if (cfgx == null) {
			cfgx = new StackGenConfigurator();
		}

		for (final String fx : names) {
			try {
				Object o = config.get(fx);
				if (o != null)
					cfgx.set(fx, o);
			} catch (final Exception e) {
				System.err.println("configure from json failed to set: "  + e);
			}
		}
		return cfgx;
	}

	public static String[] getPropertyNames() {
		Field[] fx = StackGenConfigurator.class.getDeclaredFields();
		List<String> pnx = new ArrayList<String>();
		for (Field f : fx) {
			pnx.add(f.getName());
		}
		String[] strx = new String[pnx.size()];
		for (int x = 0; x < strx.length; x++) {
			strx[x] = pnx.get(x);
		}
		return strx;
	}

	public static String[] getMethodPropertyNames() {
		Method[] mx = StackGenConfigurator.class.getDeclaredMethods();
		List<String> pnx = new ArrayList<String>();
		for (Method m : mx) {
			if (m.getName().startsWith("get")) {
				pnx.add(m.getName());
			}
		}
		String[] strx = new String[pnx.size()];
		for (int x = 0; x < strx.length; x++) {
			if (pnx.get(x).length() > 3) {
				strx[x] = pnx.get(x).substring(3);
			} else {
				strx[x] = "none";
			}
		}
		return strx;
	}

	public Object callGet(String m) {
		try {
			String mn = "get" + StringTool.getUpperCaseFirstLetter(m);
			Method fx = ReflectionUtils.findMethod(getClass(), mn);
			return fx.invoke(this);
		} catch (Exception x) {
			System.out.println("No value for: " + m);
			return null;
		}
	}

	public Object get(String pname) throws Exception {
		try {
			Field fx = ReflectionUtils.findField(getClass(), pname);
			return fx.get(this);
		} catch (Exception x) {
			System.out.println("No value for: " + pname);
			return null;
		}
	}

	private void set(String fn, Object object) throws Exception {
		Field fx = ReflectionUtils.findField(getClass(), fn);
		if(fx != null) {
			fx.setAccessible(true);
			fx.set(this, object);
		}
		
	}

	@Override
	public String toString() {
		String[] pn = getPropertyNames();
		String ret = "";
		for (String p : pn) {
			try {
				ret += p + ":" + get(p);
				ret += LINE_FEED;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	/**
	 * load custom Generators
	 */
	@Override
	public ClientOptInput toClientOptInput() {

		// ClientOptInput clx = super.toClientOptInput();

		CodegenConfig generator = StackGenCodegenConfigLoader.forName(getLang());

		generator.setInputSpec(getInputSpec());
		generator.setOutputDir(getOutputDir());
		generator.setSkipOverwrite(isSkipOverwrite());
		generator.setIgnoreFilePathOverride(getIgnoreFileOverride());
		generator.setRemoveOperationIdPrefix(getRemoveOperationIdPrefix());

		generator.instantiationTypes().putAll(getInstantiationTypes());
		generator.typeMapping().putAll(getTypeMappings());
		generator.importMapping().putAll(getImportMappings());
		generator.languageSpecificPrimitives().addAll(getLanguageSpecificPrimitives());

		checkAndSetAdditionalProperty(this.getApiPackage(), CodegenConstants.API_PACKAGE);
		checkAndSetAdditionalProperty(this.getModelPackage(), CodegenConstants.MODEL_PACKAGE);
		checkAndSetAdditionalProperty(this.getInvokerPackage(), CodegenConstants.INVOKER_PACKAGE);
//         checkAndSetAdditionalProperty(this.GRO, CodegenConstants.GROUP_ID);
		checkAndSetAdditionalProperty(this.getArtifactId(), CodegenConstants.ARTIFACT_ID);
		checkAndSetAdditionalProperty(this.getArtifactVersion(), CodegenConstants.ARTIFACT_VERSION);
		checkAndSetAdditionalProperty(this.templateDir, CodegenConstants.TEMPLATE_DIR);
//        checkAndSetAdditionalProperty(modelNamePrefix, CodegenConstants.MODEL_NAME_PREFIX);
//        checkAndSetAdditionalProperty(modelNameSuffix, CodegenConstants.MODEL_NAME_SUFFIX);
		checkAndSetAdditionalProperty(gitUserId, CodegenConstants.GIT_USER_ID);
		checkAndSetAdditionalProperty(gitRepoId, CodegenConstants.GIT_REPO_ID);
		checkAndSetAdditionalProperty(this.releaseNote, CodegenConstants.RELEASE_NOTE);
		// checkAndSetAdditionalProperty(this.httpUserAgent,
		// CodegenConstants.HTTP_USER_AGENT);

		handleDynamicProperties(generator);

		if (isNotEmpty(this.swaggerLib)) {
			generator.setLibrary(this.swaggerLib);
		}

		generator.additionalProperties().putAll(this.getAdditionalProperties());

		ClientOptInput input = new ClientOptInput().config(generator);

		final List<AuthorizationValue> authorizationValues = AuthParser.parse(auth);

		Swagger swagger = new SwaggerParser().read(getInputSpec(), authorizationValues, true);

		input.opts(new ClientOpts()).swagger(swagger);

		return input;
	}

	private void checkAndSetAdditionalProperty(String property, String propertyKey) {
		checkAndSetAdditionalProperty(property, property, propertyKey);
	}

	private void checkAndSetAdditionalProperty(String property, String valueToSet, String propertyKey) {
		if (isNotEmpty(property)) {
			getAdditionalProperties().put(propertyKey, valueToSet);
		}
	}

	void handleDynamicProperties(CodegenConfig codegenConfig) {
		for (CliOption langCliOption : codegenConfig.cliOptions()) {
			String opt = langCliOption.getOpt();
			if (getDynamicProperties().containsKey(opt)) {
				codegenConfig.additionalProperties().put(opt, getDynamicProperties().get(opt));
			} else if (getSystemProperties().containsKey(opt)) {
				codegenConfig.additionalProperties().put(opt, getSystemProperties().get(opt));
			}
		}
	}

}