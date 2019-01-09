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
 * VERBOSE
 * DEBUG
 * SWAGGER_LANG
 * SWAGGER_LIB
 * DBGEN_DROP_TABLE
 * JAVA_GEN_PATH
 * REACT_APP_NAME 
 * REACT_EXPORT_FOLDER
 * REACT_TEMPLATE_FOLDER
 * </pre>
 * 
 * @author John McMahon (@TechnoCharms)
 *
 */
public interface Configuration extends SystemConstants {

	public static boolean		VERBOSE							= (System
			.getProperty("VERBOSE") != null
					? Boolean.parseBoolean(System.getProperty("VERBOSE"))
					: false);
	public static final boolean	DEBUG							= (System
			.getProperty("DEBUG") != null
					? Boolean.parseBoolean(System.getProperty("DEBUG"))
					: false);

	// spring, java, resteasy
	public static final String	SWAGGER_LANG					= (System
			.getProperty("SWAGGER_LANG") != null
					? System.getProperty("SWAGGER_LANG")
					: "spring");

	// spring-boot ,jersey2
	public static final String	SWAGGER_LIB						= (System
			.getProperty("SWAGGER_LIB") != null
					? System.getProperty("SWAGGER_LIB")
					: "spring-boot");

	public static String		ARTIFACT_ID						= (System
			.getProperty("ARTIFACT_ID") != null
					? System.getProperty("ARTIFACT_ID")
					: "ignite").toLowerCase();

	public static final String	SCHEMA_NAME						= (System
			.getProperty("SCHEMA_NAME") != null
					? System.getProperty("SCHEMA_NAME")
					: "ignite").toLowerCase();

	public static String		TABLE_NAME_PREFIX				= SCHEMA_NAME
			+ "$";

	// DML section
	public static String		CREATE_TABLE					= "CREATE TABLE";
	public static String		CREATE_TABLE_BEGIN_BLOCK		= "(";
	public static String		CREATE_TABLE_END_BLOCK			= ");";
	public static String		DROP_TABLE						= "DROP TABLE";
	public static String		ALTER_TABLE						= "ALTER TABLE";
	public static boolean		SETTING_COLUMNS_UPPERCASED		= false;
	public static String		RENAME_TABLE_SUFFIX				= "BK_";

	public static boolean		DROP_EXISTING_TABLES			= (System
			.getProperty("DBGEN_DROP_TABLE") != null ? Boolean
					.parseBoolean(System.getProperty("DBGEN_DROP_TABLE"))
					: false);
	// end DML section

	public static String		JAVA_GEN_ARCHIVE_FOLDER			= "/archive/gen";
	public static String		JAVA_GEN_ARCHIVE_PATH			= (System
			.getProperty("JAVA_GEN_ARCHIVE_PATH") != null
					? System.getProperty("JAVA_GEN_ARCHIVE_PATH")
					: ROOT_FOLDER + JAVA_GEN_ARCHIVE_FOLDER);

	public static String		JAVA_GEN_FOLDER					= "/gen";
	public static String		JAVA_GEN_PATH					= (System
			.getProperty("JAVA_GEN_PATH") != null
					? System.getProperty("JAVA_GEN_PATH")
					: ROOT_FOLDER + JAVA_GEN_FOLDER);

	public static String		JAVA_GEN_SRC_FOLDER				= JAVA_GEN_PATH
			+ "/src/main/java";
	public static File			JAVA_GEN_SRC					= new File(
			JAVA_GEN_SRC_FOLDER);

	public static String		JAVA_GEN_RESOURCES_FOLDER		= JAVA_GEN_PATH
			+ "/src/main/resources";

	public static String		SOURCE_MAIN						= "/src/main";
	public static String		SOURCE_MAIN_JAVA				= SOURCE_MAIN
			+ "/java";
	public static String		SOURCE_RESOURCES				= "/src/resources";
	public static String		PLUGIN_FOLDER					= ROOT_FOLDER
			+ SOURCE_RESOURCES + "/plugins";

	public static final boolean	DISABLE_DATA_FIELD_ASPECT		= true;
	public static final boolean	DISABLE_SECURE_FIELD_ASPECT		= false;

	// ## SwaggerGen OPEN API
	public static String		ARTIFACT_VERSION				= (System
			.getProperty("ARTIFACT_VERSION") != null
					? System.getProperty("ARTIFACT_VERSION")
					: "1.0.1");

	public static String		ADD_GEN_CLASS_NAME				= "Service";

	public static String		ORG_PACKAGE						= "io.starter."
			+ ARTIFACT_ID;

	public static String		IGNITE_MODEL_PACKAGE			= ORG_PACKAGE
			+ ".model";

	public static String		API_MODEL_PACKAGE				= ORG_PACKAGE
			+ ".model";

	public static String		SPEC_LOCATION					= ROOT_FOLDER
			+ SOURCE_RESOURCES + "/openapi_specs/";

	public static String		PLUGIN_SPEC_LOCATION			= SPEC_LOCATION
			+ "plugins/";

	public static String		CONFIG_FILE						= ROOT_FOLDER
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
			+ ARTIFACT_ID;

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
			+ ARTIFACT_ID + "/sqlmaps/";

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
			.getProperty("user.dir") + JAVA_GEN_FOLDER
			+ "/src/main/resources/MyBatisConfig.xml";

	// ## React
	public static String		REACT_APP_NAME					= (System
			.getProperty("REACT_APP_NAME") != null
					? System.getProperty("REACT_APP_NAME")
					: "MyLittlePony");

	public static String		REACT_TEMPLATE_APP_FOLDER		= ROOT_FOLDER
			+ SOURCE_MAIN + "/react/" + ARTIFACT_ID + "App/";

	public static String		REACT_TEMPLATE_FOLDER			= (System
			.getProperty("REACT_TEMPLATE_FOLDER") != null
					? System.getProperty("REACT_TEMPLATE_FOLDER")
					: ROOT_FOLDER + SOURCE_MAIN + "/react/template/");

	// external React Project Path
	public static String		REACT_EXPORT_FOLDER				= (System
			.getProperty("REACT_EXPORT_FOLDER") != null
					? System.getProperty("REACT_EXPORT_FOLDER")
					: ROOT_FOLDER + "/REACT_EXPORT");

	public static String		REACT_APP_OUTPUT_FOLDER			= ROOT_FOLDER
			+ "/tmp/react/";

	public static List<String>	SKIP_LIST						= new ArrayList<>(
			Arrays.asList(".class"));

	public static List<String>	FOLDER_SKIP_LIST				= new ArrayList<>(
			Arrays.asList("gen", "org", "swagger", "node_modules"));

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
	public static String		LINEFEED						= "\r\n";

}
