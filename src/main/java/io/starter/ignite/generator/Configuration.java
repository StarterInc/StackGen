package io.starter.ignite.generator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.starter.ignite.generator.react.AppEntityObject;
import io.starter.ignite.util.SystemConstants;

/**
 * Global Configuration
 * 
 * The following values can be set as System properties or on the command line:
 * 
 * <pre>
 * JAVA_GEN_PATH
 * REACT_APP_NAME 
 * REACT_EXPORT_FOLDER
 * REACT_TEMPLATE_FOLDER
 * </pre>
 * 
 * @author john
 *
 */
public interface Configuration extends SystemConstants {

	public static boolean			VERBOSE						= !false;
	public static boolean			DEBUG						= !false;

	public static final boolean		DISABLE_DATA_FIELD_ASPECT	= true;
	public static final boolean		DISABLE_SECURE_FIELD_ASPECT	= false;

	public static String			ADD_GEN_CLASS_NAME			= "Data";
	public static String			IGNITE_DATAMODEL_PACKAGE	= "io.starter.ignite.model.";

	public static String			LONG_DATE_FORMAT			= "MMM/d/yyyy HH:mm:ss Z";
	public static String			GEN_MODEL_PACKAGE			= "io.starter.ignite.model.";
	static SimpleDateFormat			DATE_FORMAT					= new SimpleDateFormat(
			LONG_DATE_FORMAT);

	public static String			SOURCE_MAIN					= "/src/main";
	public static String			SOURCE_RESOURCES			= "/src/resources";

	public static String			JAVA_GEN_ARCHIVE_FOLDER		= "/archive/gen";
	public static String			JAVA_GEN_ARCHIVE_PATH		= (System
			.getProperty("JAVA_GEN_ARCHIVE_PATH") != null
					? System.getProperty("JAVA_GEN_ARCHIVE_PATH")
					: ROOT_FOLDER + JAVA_GEN_ARCHIVE_FOLDER);

	public static String			JAVA_GEN_FOLDER				= "/gen";
	public static String			JAVA_GEN_PATH				= (System
			.getProperty("JAVA_GEN_PATH") != null
					? System.getProperty("JAVA_GEN_PATH")
					: ROOT_FOLDER + JAVA_GEN_FOLDER);

	public static String			JAVA_GEN_SRC_FOLDER			= JAVA_GEN_PATH
			+ "/src/main/java";
	public static File				JAVA_GEN_SRC				= new File(
			JAVA_GEN_SRC_FOLDER);

	public static String			MODEL_PACKAGE_DIR			= "/io/starter/ignite/model/";
	public static String			MODEL_DAO_PACKAGE_DIR		= "/io/starter/ignite/model/dao/";

	public static String			API_MODEL_CLASSES			= JAVA_GEN_SRC_FOLDER
			+ MODEL_PACKAGE_DIR;

	// ## SwaggerGen OPEN API
	public static String			SPEC_LOCATION				= ROOT_FOLDER
			+ SOURCE_RESOURCES + "/openapi_specs/";
	public static String			CONFIG_FILE					= ROOT_FOLDER
			+ SOURCE_RESOURCES + "/swagger_config.json";
	public static String			OUTPUT_DIR					= JAVA_GEN_PATH;

	public static String			ORG							= "io.starter.ignite";
	public static String			API_PACKAGE					= ORG + ".api";
	public static String			ARTIFACT_ID					= "ignite";
	public static String			MODEL_PACKAGE				= ORG
			+ ".model";
	public static String			INVOKER_PACKAGE				= ORG
			+ ".invoker";

	// ## Mybatis
	public static int				DB_TIMEOUT					= 10000;
	public static String			MYBATIS_MODEL_CLASSES		= API_MODEL_CLASSES;

	public static final String		MYBATIS_GEN_CONFIG			= System
			.getProperty("user.dir")
			+ "/src/resources/templates/MyBatisGeneratorConfig.xml";

	public static final String		MYBATIS_GEN_CONFIG_OUT		= System
			.getProperty("user.dir")
			+ "/src/resources/MyBatisGeneratorConfig.xml";

	public static final String		MYBATIS_CONFIG				= System
			.getProperty("user.dir")
			+ "/src/resources/templates/MyBatisConfig.xml";

	public static final String		MYBATIS_CONFIG_OUT			= System
			.getProperty("user.dir") + JAVA_GEN_FOLDER
			+ "/src/main/resources/MyBatisConfig.xml";

	public static final String		SCHEMA_NAME					= "ignite";

	// ## React
	public static String			REACT_APP_NAME				= (System
			.getProperty("REACT_APP_NAME") != null
					? System.getProperty("REACT_APP_NAME")
					: "MyLittlePony");

	public static String			REACT_TEMPLATE_APP_FOLDER	= ROOT_FOLDER
			+ SOURCE_MAIN + "/react/IgniteApp/";

	public static String			REACT_TEMPLATE_FOLDER		= (System
			.getProperty("REACT_TEMPLATE_FOLDER") != null
					? System.getProperty("REACT_TEMPLATE_FOLDER")
					: ROOT_FOLDER + SOURCE_MAIN + "/react/template/");

	// external React Project Path
	public static String			REACT_EXPORT_FOLDER			= (System
			.getProperty("REACT_EXPORT_FOLDER") != null
					? System.getProperty("REACT_EXPORT_FOLDER")
					: ROOT_FOLDER + "/REACT_EXPORT");

	public static String			REACT_APP_OUTPUT_FOLDER		= ROOT_FOLDER
			+ "/tmp/react/";

	public List<AppEntityObject>	REACT_DATA_OBJECTS			= new ArrayList<AppEntityObject>();

	public static List<String>		SKIP_LIST					= new ArrayList<>(
			Arrays.asList(".class"));

	public static List<String>		FOLDER_SKIP_LIST			= new ArrayList<>(
			Arrays.asList("gen", "org", "swagger", "node_modules"));

	// ## WEB
	// output generated WP PHP code here
	public static String			WP_PLUGIN_ROOT				= SOURCE_MAIN
			+ "/src/main/wp";

	// output the web content here (including JSP)
	public static String			WEB_ROOT					= SOURCE_MAIN
			+ "/webapp";

	// output the javascript here
	public static String			WEB_JS_ROOT					= SOURCE_MAIN
			+ "/webapp/js";

}
