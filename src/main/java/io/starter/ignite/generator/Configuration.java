package io.starter.ignite.generator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * javaGenPath
 * REACT_APP_NAME 
 * REACT_EXPORT_FOLDER
 * REACT_TEMPLATE_FOLDER
 * </pre>
 * 
 * @author John McMahon (@TechnoCharms)
 *
 */
public interface Configuration extends SystemConstants {

	public static String		defaultPort						= "8099";

	public static boolean		skipDBGen						= (System
			.getProperty("skipDBGen") != null
					? Boolean.parseBoolean(System.getProperty("skipDBGen"))
					: false);
	public static boolean		skipMybatis						= (System
			.getProperty("skipMybatis") != null
					? Boolean.parseBoolean(System.getProperty("skipMybatis"))
					: false);
	public static boolean		skipBuildGeneratedApp			= (System
			.getProperty("skipBuildGeneratedApp") != null ? Boolean
					.parseBoolean(System.getProperty("skipBuildGeneratedApp"))
					: true);
	public static boolean		overwriteMode					= (System
			.getProperty("overwriteMode") != null
					? Boolean.parseBoolean(System.getProperty("overwriteMode"))
					: true);

	public static boolean		verbose							= (System
			.getProperty("verbose") != null
					? Boolean.parseBoolean(System.getProperty("verbose"))
					: false);
	public static final boolean	debug							= (System
			.getProperty("debug") != null
					? Boolean.parseBoolean(System.getProperty("debug"))
					: false);

	// spring, java, resteasy
	public static final String	swaggerLang						= (System
			.getProperty("swaggerLang") != null
					? System.getProperty("swaggerLang")
					: "spring");

	// spring-boot ,jersey2
	public static final String	swaggerLib						= (System
			.getProperty("swaggerLib") != null
					? System.getProperty("swaggerLib")
					: "spring-boot");

	public static String		artifactId						= (System
			.getProperty("artifactId") != null
					? System.getProperty("artifactId")
					: "ignite").toLowerCase();

	public static final String	schemaName						= (System
			.getProperty("schemaName") != null
					? System.getProperty("schemaName")
					: "ignite").toLowerCase();

	public static String		TABLE_NAME_PREFIX				= schemaName
			+ "$";

	// DML section
	public static String		CREATE_TABLE					= "CREATE TABLE";
	public static String		CREATE_TABLE_BEGIN_BLOCK		= "(";
	public static String		CREATE_TABLE_END_BLOCK			= ");";
	public static String		DROP_TABLE						= "DROP TABLE";
	public static String		ALTER_TABLE						= "ALTER TABLE";
	public static boolean		COLUMNS_UPPERCASE				= false;
	public static String		RENAME_TABLE_SUFFIX				= "BK_";

	public static boolean		dbGenDropTable					= (System
			.getProperty("dbGenDropTable") != null
					? Boolean.parseBoolean(System.getProperty("dbGenDropTable"))
					: false);
	// end DML section

	public static String		javaGenFolder					= (System
			.getProperty("javaGenFolder") != null
					? System.getProperty("javaGenFolder")
					: "/gen");

	public static String		javaGenPath						= (System
			.getProperty("javaGenPath") != null
					? System.getProperty("javaGenPath")
					: rootFolder)
			+ javaGenFolder;

	public static String		JAVA_GEN_ARCHIVE_FOLDER			= "/archive"
			+ javaGenFolder;

	public static String		javaGenArchivePath				= (System
			.getProperty("javaGenArchivePath") != null
					? System.getProperty("javaGenArchivePath")
					: rootFolder + JAVA_GEN_ARCHIVE_FOLDER);

	public static String		JAVA_GEN_SRC_FOLDER				= javaGenPath
			+ "/src/main/java";
	public static File			JAVA_GEN_SRC					= new File(
			JAVA_GEN_SRC_FOLDER);

	public static String		JAVA_GEN_RESOURCES_FOLDER		= javaGenPath
			+ "/src/main/resources";

	public static String		SOURCE_MAIN						= "/src/main";
	public static String		SOURCE_MAIN_JAVA				= SOURCE_MAIN
			+ "/java";
	public static String		SOURCE_RESOURCES				= "/src/resources";
	public static String		PLUGIN_FOLDER					= rootFolder
			+ SOURCE_RESOURCES + "/plugins";

	public static final boolean	DISABLE_DATA_FIELD_ASPECT		= true;
	public static final boolean	DISABLE_SECURE_FIELD_ASPECT		= false;

	// ## SwaggerGen OPEN API
	public static String		artifactVersion					= (System
			.getProperty("artifactVersion") != null
					? System.getProperty("artifactVersion")
					: "1.0.1");

	public static String		ADD_GEN_CLASS_NAME				= "Service";

	public static String		ORG_PACKAGE						= "io.starter."
			+ artifactId;

	public static String		IGNITE_MODEL_PACKAGE			= ORG_PACKAGE
			+ ".model";

	public static String		API_MODEL_PACKAGE				= ORG_PACKAGE
			+ ".model";

	public static String		SPEC_LOCATION					= rootFolder
			+ SOURCE_RESOURCES + "/openapi_specs/";

	public static String		PLUGIN_SPEC_LOCATION			= SPEC_LOCATION
			+ "plugins/";

	public static String		CONFIG_FILE						= rootFolder
			+ SOURCE_RESOURCES + "/swagger_config.json";

	public static String		API_PACKAGE						= ORG_PACKAGE
			+ ".api";

	public static String		MODEL_PACKAGE					= ORG_PACKAGE
			+ ".model";

	public static String		MODEL_DAO_PACKAGE				= MODEL_PACKAGE
			+ ".dao";

	public static String		INVOKER_PACKAGE					= ORG_PACKAGE
			+ ".invoker";

	public static String		LONG_DATE_FORMAT				= "MMM/d/yyyy HH:mm:ss Z";

	static SimpleDateFormat		DATE_FORMAT						= new SimpleDateFormat(
			LONG_DATE_FORMAT);

	public static String		PACKAGE_DIR						= "/io/starter/"
			+ artifactId;

	public static String		MODEL_PACKAGE_DIR				= PACKAGE_DIR
			+ "/model/";

	public static String		API_PACKAGE_DIR					= PACKAGE_DIR
			+ "/api/";

	public static String		MODEL_DAO_PACKAGE_DIR			= PACKAGE_DIR
			+ "/model/dao/";

	public static String		MODEL_CLASSES					= JAVA_GEN_SRC_FOLDER
			+ MODEL_PACKAGE_DIR;

	public static String		MODEL_DAO_CLASSES				= JAVA_GEN_SRC_FOLDER
			+ MODEL_DAO_PACKAGE_DIR;

	public static String		API_CLASSES						= JAVA_GEN_SRC_FOLDER
			+ API_PACKAGE_DIR;

	// ## Mybatis
	public static int			DB_TIMEOUT						= 10000;

	public static final String	TIMEZONE_OFFSET					= "-08:00";
	public static final String	MYBATIS_COL_ENUM_FLAG			= "ENUM";
	public String				ANNOTATAION_CLASS				= "io.starter.ignite.security.securefield.SecureField";

	public static String		SQL_MAPS_PATH					= "io/starter/"
			+ artifactId + "/sqlmaps/";

	// TODO: fix experimental use case
	public static boolean		MYBATIS_CASE_SENSITIVE_FIX		= false;

	public static final String	MYBATIS_GEN_CONFIG				= System
			.getProperty("user.dir")
			+ "/src/resources/templates/MyBatisGeneratorConfig.xml";

	public static final String	MYBATIS_GEN_CONFIG_OUT			= System
			.getProperty("user.dir")
			+ "/src/resources/MyBatisGeneratorConfig.xml";

	public static final String	MYBATIS_CONFIG					= System
			.getProperty("user.dir")
			+ "/src/resources/templates/MyBatisConfig.xml";

	public static final String	MYBATIS_CONFIG_OUT				= System
			.getProperty("user.dir") + javaGenFolder
			+ "/src/main/resources/MyBatisConfig.xml";

	// ## React
	public static String		REACT_APP_NAME					= (System
			.getProperty("REACT_APP_NAME") != null
					? System.getProperty("REACT_APP_NAME")
					: "MyLittlePony");

	public static String		REACT_TEMPLATE_APP_FOLDER		= rootFolder
			+ SOURCE_MAIN + "/react/" + artifactId + "App/";

	public static String		REACT_TEMPLATE_FOLDER			= (System
			.getProperty("REACT_TEMPLATE_FOLDER") != null
					? System.getProperty("REACT_TEMPLATE_FOLDER")
					: rootFolder + SOURCE_MAIN + "/react/template/");

	// external React Project Path
	public static String		REACT_EXPORT_FOLDER				= (System
			.getProperty("REACT_EXPORT_FOLDER") != null
					? System.getProperty("REACT_EXPORT_FOLDER")
					: rootFolder + "/REACT_EXPORT");

	public static String		REACT_APP_OUTPUT_FOLDER			= rootFolder
			+ "/tmp/react/";

	public static List<String>	SKIP_LIST						= new ArrayList<>(
			Arrays.asList(".class"));

	public static List<String>	FOLDER_SKIP_LIST				= new ArrayList<>(
			Arrays.asList(javaGenFolder, "org", "swagger", "node_modules"));

	// ## WEB
	// output generated WP PHP code here
	public static String		WP_PLUGIN_ROOT					= SOURCE_MAIN
			+ "/src/main/wp";

	// output the web content here (including JSP)
	public static String		WEB_ROOT						= SOURCE_MAIN
			+ "/webapp";

	// output the javascript here
	public static String		WEB_JS_ROOT						= SOURCE_MAIN
			+ "/webapp/js";
	public static String		SPRING_DELEGATE					= "ApiDelegate";

	public static String		IGNITE_GEN_MODEL_ENHANCEMENTS	= "igniteGenerateModelEnhancements";
	public static String		IGNITE_GEN_MODEL_CRUD_OPS		= "igniteGenerateCRUDOps";

	public static String		IGNITE_GEN_REST_PATH_PREFIX		= "data/";
	public static String		LINE_FEED						= "\r\n";

	public static int			DB_ENCRYPTED_COLUMN_MULTIPLIER	= 5;

}
