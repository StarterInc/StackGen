package io.starter.ignite.generator;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.util.SystemConstants;

/**
 * Global Configuration
 *
 * The following values can be set as System properties or on the command line:
 *
 * <pre>
 * verbose
 * debug
 * swaggerLang
 * swaggerLib
 * dbGenDropTable
 * genOutputFolder
 * REACT_APP_NAME
 * REACT_EXPORT_FOLDER
 * REACT_TEMPLATE_FOLDER
 * </pre>
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public interface Configuration extends SystemConstants {

	Logger logger = LoggerFactory.getLogger(Configuration.class);

	String adminServiceURL = (System.getProperty("adminServiceURL") != null ? System.getProperty("adminServiceURL")
			: "http://localhost:8099/");

	String defaultHostname = (System.getProperty("defaultHostname") != null ? System.getProperty("defaultHostname")
			: "localhost");

	String defaultPort = (System.getProperty("defaultPort") != null ? System.getProperty("defaultPort") : "8100");

	String gitRepoId = (System.getProperty("gitRepoId") != null ? System.getProperty("gitRepoId") : "StackGen");

	String gitUserId = (System.getProperty("gitUserId") != null ? System.getProperty("gitUserId") : "spaceghost69");

	String developerOrganizationUrl = (System.getProperty("developerOrganizationUrl") != null
			? System.getProperty("developerOrganizationUrl")
			: "http://starter.io");

	String developerName = (System.getProperty("developerName") != null ? System.getProperty("developerName")
			: "Stack Dev");

	String developerEmail = (System.getProperty("developerEmail") != null ? System.getProperty("developerEmail")
			: "info@stackgen.io");

	String developerOrganization = (System.getProperty("developerOrganization") != null
			? System.getProperty("developerOrganization")
			: "Starter Inc.");

	/**
	 * set the value of allowed CORS request paths
	 */
	String CORSMapping = (System.getProperty("CORSMapping") != null ? System.getProperty("CORSMapping") : "*/**");

	String CORSOrigins = (System.getProperty("CORSMapping") != null ? System.getProperty("CORSOrigins") : "localhost");

	String adminUser = (System.getProperty("adminUser") != null ? System.getProperty("adminUser") : "admin");

	String adminPassword = (System.getProperty("adminPassword") != null ? System.getProperty("adminPassword")
			: "ch@ng3m3");

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
	String swaggerLang = (System.getProperty("swaggerLang") != null ? System.getProperty("swaggerLang") : "spring");

	// spring-boot ,jersey2
	String swaggerLib = (System.getProperty("swaggerLib") != null ? System.getProperty("swaggerLib") : "spring-boot");

	String artifactId = (System.getProperty("artifactId") != null ? System.getProperty("artifactId") : "stackgen");

	String schemaName = (System.getProperty("schemaName") != null ? System.getProperty("schemaName") : "stackgen");

	String TABLE_NAME_PREFIX = Configuration.schemaName + "$";

	// DML/DB section
	// default is lowercase, this forces uppercase
	boolean columnsUpperCase = (System.getProperty("columnsUpperCase") != null
			? Boolean.parseBoolean(System.getProperty("columnsUpperCase"))
			: false);

	boolean dbGenDropTable = (System.getProperty("dbGenDropTable") != null
			? Boolean.parseBoolean(System.getProperty("dbGenDropTable"))
			: false);

	String CREATE_TABLE = "CREATE TABLE";
	String CREATE_TABLE_BEGIN_BLOCK = "(";
	String CREATE_TABLE_END_BLOCK = ");";
	String DROP_TABLE = "DROP TABLE";
	String ALTER_TABLE = "ALTER TABLE";
	String RENAME_TABLE_PREFIX = "BK_";
	String TUPLE_TABLE_SUFFIX = "_idx";

	// end DML section

	String javaGenFolderName = (System.getProperty("javaGenFolderName") != null
			? System.getProperty("javaGenFolderName")
			: "/gen");

	String genOutputFolder = (System.getProperty("genOutputFolder") != null ? System.getProperty("genOutputFolder")
			: SystemConstants.rootFolder) + Configuration.javaGenFolderName;

	String JAVA_GEN_ARCHIVE_FOLDER = "/archive" + Configuration.javaGenFolderName;

	String javaGenArchivePath = (System.getProperty("javaGenArchivePath") != null
			? System.getProperty("javaGenArchivePath")
			: SystemConstants.rootFolder + Configuration.JAVA_GEN_ARCHIVE_FOLDER);

	String SOURCE_MAIN = (System.getProperty("SOURCE_MAIN") != null ? System.getProperty("SOURCE_MAIN")
			: SystemConstants.rootFolder) + "/src/main";

	String JAVA_GEN_SRC_FOLDER = Configuration.genOutputFolder + "/src/main/java";

	File JAVA_GEN_SRC = new File(Configuration.JAVA_GEN_SRC_FOLDER);

	String JAVA_GEN_RESOURCES_FOLDER = Configuration.genOutputFolder + "/resources";

	String PUBLIC_ROOT = Configuration.javaGenFolderName + "/public";

	String SOURCE_MAIN_JAVA = Configuration.SOURCE_MAIN + "/java";

	String SOURCE_RESOURCES = (System.getProperty("sourceResources") != null ? System.getProperty("sourceResources")
			: "/src/resources");

	boolean DISABLE_DATA_FIELD_ASPECT = true;
	boolean DISABLE_SECURE_FIELD_ASPECT = false;

	// ## SwaggerGen OPEN API
	String artifactVersion = (System.getProperty("artifactVersion") != null ? System.getProperty("artifactVersion")
			: "1.0.1-SNAPSHOT");

	String ADD_GEN_CLASS_NAME = "Service";

	String orgPackage = (System.getProperty("orgPackage") != null ? System.getProperty("orgPackage") : "io.starter.");

	String orgFolder = (System.getProperty("orgFolder") != null ? System.getProperty("orgFolder") : "io/starter/");

	String SPEC_LOCATION = SystemConstants.rootFolder + Configuration.SOURCE_RESOURCES + "/openapi_specs/";

	String PLUGIN_SPEC_LOCATION = Configuration.SPEC_LOCATION + "plugins/";

	String CONFIG_FILE = SystemConstants.rootFolder + Configuration.SOURCE_RESOURCES + "/swagger_config.json";

	String IGNITE_MODEL_PACKAGE = Configuration.orgPackage + Configuration.artifactId + ".model";

	String API_MODEL_PACKAGE = Configuration.orgPackage + Configuration.artifactId + ".model";

	String API_PACKAGE = Configuration.orgPackage + Configuration.artifactId + ".api";

	String MODEL_PACKAGE = Configuration.orgPackage + Configuration.artifactId + ".model";

	String MODEL_DAO_PACKAGE = Configuration.MODEL_PACKAGE + ".dao";

	String INVOKER_PACKAGE = Configuration.orgPackage + Configuration.artifactId + ".invoker";

	String LONG_DATE_FORMAT = "MMM/d/yyyy HH:mm:ss Z";

	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Configuration.LONG_DATE_FORMAT);

	String PACKAGE_DIR = Configuration.orgFolder + Configuration.artifactId;

	String MODEL_PACKAGE_DIR = Configuration.PACKAGE_DIR + "/model/";

	String API_PACKAGE_DIR = Configuration.PACKAGE_DIR + "/api/";

	String MODEL_DAO_PACKAGE_DIR = Configuration.PACKAGE_DIR + "/model/dao/";

	String MODEL_CLASSES = Configuration.JAVA_GEN_SRC_FOLDER + "/" + Configuration.MODEL_PACKAGE_DIR;

	String MODEL_DAO_CLASSES = Configuration.JAVA_GEN_SRC_FOLDER + Configuration.MODEL_DAO_PACKAGE_DIR;

	String API_CLASSES = Configuration.JAVA_GEN_SRC_FOLDER + Configuration.API_PACKAGE_DIR;

	// ## Mybatis
	int DB_TIMEOUT = 10000;

	String TIMEZONE_OFFSET = (System.getProperty("TIMEZONE_OFFSET") != null ? System.getProperty("TIMEZONE_OFFSET")
			: "-08:00");

	String MYBATIS_COL_ENUM_FLAG = "ENUM";

	String SQL_MAPS_PATH = Configuration.orgFolder + Configuration.artifactId.replace(".", "/") + "/model/dao/";

	String MYBATIS_GEN_CONFIG_TEMPLATE = SystemConstants.rootFolder + Configuration.SOURCE_RESOURCES
			+ "/templates/MyBatisGeneratorConfig.xml";

	String MYBATIS_GEN_CONFIG_OUT = Configuration.genOutputFolder + Configuration.SOURCE_RESOURCES
			+ "/MyBatisGeneratorConfig.xml";

	String MYBATIS_CONFIG_TEMPLATE = SystemConstants.rootFolder + Configuration.SOURCE_RESOURCES
			+ "/templates/MyBatisConfig.xml";

	String MYBATIS_CONFIG_OUT = Configuration.genOutputFolder + Configuration.javaGenFolderName
			+ "/src/main/resources/MyBatisConfig.xml";

	List<String> FOLDER_SKIP_LIST = new ArrayList<>(
			Arrays.asList(Configuration.javaGenFolderName, "org", "swagger", "node_modules"));

	// ## WEB
	// output generated WP PHP code here
	String WP_PLUGIN_ROOT = Configuration.SOURCE_MAIN + Configuration.SOURCE_MAIN + "/wp";

	// output the web content here (including JSP)
	String WEB_ROOT = Configuration.SOURCE_MAIN + "/webapp";

	// output the javascript here
	String WEB_JS_ROOT = Configuration.SOURCE_MAIN + "/webapp/js";
	String SPRING_DELEGATE = "ApiDelegate";

	String IGNITE_GEN_MODEL_ENHANCEMENTS = "igniteGenerateModelEnhancements";
	String IGNITE_GEN_MODEL_CRUD_OPS = "igniteGenerateCRUDOps";

	String IGNITE_GEN_REST_PATH_PREFIX = "data/";
	String LINE_FEED = "\r\n";

	int DB_ENCRYPTED_COLUMN_MULTIPLIER = 5;

	String language = "en"; // language
	String country = "US"; // country

	String[] RESERVED_WORD_LIST = { "ApiResponse" };

	String GENERATED_TEXT_BLOCK = "Starter StackGen 'JavaGen' Generated";

	/**
	 * App-wide utility method for checking against list of reserved words
	 *
	 * @param the string to check
	 * @return whether the string is in the reserved word list (case insensitive)
	 */
	static boolean checkReservedWord(String k) {
		for (final String x : Configuration.RESERVED_WORD_LIST) {
			if (x.equalsIgnoreCase(k)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * utility method for setting config values from a JSON object
	 *
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	static boolean copyJSONConfigToSysprops(final JSONObject config)
			throws IllegalArgumentException, IllegalAccessException {
		final String[] names = JSONObject.getNames(config);
		for (final String fx : names) {
			try {
				System.setProperty(fx, config.get(fx).toString());
			} catch (final Exception e) {

			}
		}
		return true;
	}

	/**
	 * utility method for setting sysprops values from this Configuration
	 *
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	static boolean copyConfigurationToSysprops() throws IllegalArgumentException, IllegalAccessException {
		final Field[] f = Configuration.class.getFields();
		for (final Field fx : f) {
			try {
				System.setProperty(fx.getName(), fx.get(null).toString());
			} catch (final Exception e) {

			}
		}
		return true;
	}

}
