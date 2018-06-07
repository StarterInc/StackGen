package io.starter.ignite.generator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.starter.ignite.generator.react.AppEntityObject;
import io.starter.ignite.util.SystemConstants;

/**
 * Configuration values
 * 
 * @author john
 *
 */
public interface Configuration extends SystemConstants {

	public static boolean VERBOSE = false;
	public static boolean DEBUG = false;
	public static String ADD_GEN_CLASS_NAME = "Data";
	public static String LONG_DATE_FORMAT = "MMM/d/yyyy HH:mm:ss Z";
	public static String GEN_MODEL_PACKAGE = "io.starter.ignite.model.";
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(LONG_DATE_FORMAT);

	public static String SOURCE_MAIN = "/src/main";
	public static String SOURCE_RESOURCES = "/src/resources";

	public static String JAVA_GEN_FOLDER = ROOT_FOLDER + "/gen"; // = JAVA_SRC_FOLDER + "/gen/";
	public static String JAVA_GEN_SRC_FOLDER = JAVA_GEN_FOLDER + "/src/main/java";
	public static File JAVA_GEN_SRC = new File(JAVA_GEN_SRC_FOLDER);

	public static String MODEL_PACKAGE_DIR = "/io/starter/ignite/model/";
	public static String API_MODEL_CLASSES = JAVA_GEN_SRC_FOLDER + MODEL_PACKAGE_DIR;

	// ## SwaggerGen OPEN API
	public static String SPEC_LOCATION = ROOT_FOLDER + SOURCE_RESOURCES + "/openapi_specs/";
	public static String CONFIG_FILE = ROOT_FOLDER + SOURCE_RESOURCES + "/swagger_config.json";
	public static String OUTPUT_DIR = JAVA_GEN_FOLDER;

	public static String ORG = "io.starter";
	public static String API_PACKAGE = ORG + ".ignite.api";
	public static String ARTIFACT_ID = ORG + ".ignite";
	public static String MODEL_PACKAGE = ORG + ".ignite.model";
	public static String INVOKER_PACKAGE = ORG + ".ignite.invoker";

	// ## Mybatis
	public static int DB_TIMEOUT = 10000;
	public static String MYBATIS_MODEL_CLASSES = API_MODEL_CLASSES;

	public static final String MYBATIS_GEN_CONFIG = System.getProperty("user.dir")
			+ "/src/resources/templates/MyBatisGeneratorConfig.xml";

	public static final String MYBATIS_GEN_CONFIG_OUT = System.getProperty("user.dir")
			+ "/src/resources/MyBatisGeneratorConfig.xml";

	public static final String SCHEMA_NAME = "ignite";

	// ## React
	// TODO: make dynamic
	public static String REACT_APP_NAME = "IgniteReactApp";

	public static String REACT_TEMPLATE_APP_FOLDER = ROOT_FOLDER + SOURCE_MAIN + "/react/template/starter";

	public static String REACT_TEMPLATE_FOLDER = "mlx";

	// external React Project Path
	public static String REACT_EXPORT_FOLDER = ROOT_FOLDER + "/REACT_EXPORT";

	public static String REACT_APP_OUTPUT_FOLDER = ROOT_FOLDER + SOURCE_MAIN + "/react/out/";

	public static String IGNITE_DATAMODEL_PACKAGE = "io.swagger.model.";

	public List<AppEntityObject> REACT_DATA_OBJECTS = new ArrayList<AppEntityObject>();
	public static List<String> SKIP_LIST = new ArrayList<>(Arrays.asList(".class"));
	public static List<String> FOLDER_SKIP_LIST = new ArrayList<>(
			Arrays.asList("gen", "org", "swagger", "node_modules"));

	// ## WEB
	// output generated WP PHP code here
	public static String WP_PLUGIN_ROOT = SOURCE_MAIN + "/src/main/wp";

	// output the web content here (including JSP)
	public static String WEB_ROOT = SOURCE_MAIN + "/webapp";

	// output the javascript here
	public static String WEB_JS_ROOT = SOURCE_MAIN + "/webapp/js";

}
