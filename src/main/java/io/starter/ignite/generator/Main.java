package io.starter.ignite.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.util.FileUtil;
import org.codehaus.plexus.util.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import io.starter.ignite.util.ASCIIArtPrinter;
import io.starter.ignite.util.SystemConstants;
import io.starter.ignite.util.ZipFileWriter;

/**
 * <h2>Generates an app from a Swagger Spec</h2>
 *
 * Features the ability to merge spec files from a templates directory as well
 * as auto-creation of CRUD methods hooked up to Ignite Data Objects.
 *
 * <pre>
 *   - YAML -> Swagger client
 *   - Swagger Client -> entity classes
 *   - Swagger Client -> DML for database
 *   - Swagger CLient -> React-native JS screens
 * </pre>
 *
 * <h2>NOTE: Generate a SecureField key</h2>
 *
 * <pre>
 *	java io.starter.ignite.generator.Main -Dio.starter.generateKey=<secretkey> -jar StarterIgnite-1.0.1.jar
 * </pre>
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
// @SpringBootApplication
// @Order(Ordered.LOWEST_PRECEDENCE) // run before ReactGenerator
public class Main implements Configuration, CommandLineRunner {

	protected static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		if (args == null) {
			args = new String[0];
		}
		SpringApplication.run(Main.class, args);
	}

	/**
	 * a list of file paths to copy relative to project root
	 */
	protected static String[][] staticFiles = {

			{ "/src/resources/templates/log4j.properties", "/src/main/resources/log4j.properties" },

			{ "/logs/logfile_placeholder.txt", "/logs/logfile_placeholder.txt" },

			{ "/src/main/java/io/starter/spring/boot/stackgen-pro.txt",
					"/src/main/java/io/starter/spring/boot/stackgen-pro.txt" },

			{ "/src/test/java/io/starter/stackgentest/model/User.java",
					"/src/test/java/io/starter/stackgentest/model/User.java" },

			{ "/src/test/java/io/starter/ignite/security/securefield/SecureFieldTest.java",
					"/src/test/java/io/starter/ignite/security/securefield/SecureFieldTest.java" } };

	protected static void copyStaticFiles(String[][] staticFiles) {
		final File genDir = new File(Configuration.genOutputFolder);
		if (!genDir.exists()) {
			genDir.mkdirs();
		}
		Main.logger.info(
				"Copying static files to folder: " + Configuration.genOutputFolder + " exists: " + genDir.exists());
		if (genDir.exists()) {
			for (final String[] fx : staticFiles) {
				final File fromF = new File(SystemConstants.rootFolder + fx[0]);
				final File toF = new File(Configuration.genOutputFolder + fx[1]);
				if (fromF.exists()) {
					Main.logger.info("Copying static file : " + fromF + " to: " + toF);

					if (!toF.exists()) { // prep dest folder
						toF.mkdirs();
						toF.delete();
					}
					try {
						FileUtils.copyFile(fromF, toF);
					} catch (final Exception e) {
						Main.logger.error("Could not copy static file. " + e.toString());
					}
				} else {
					Main.logger.warn("Missing expected static file : " + fromF.getAbsolutePath());
				}
			}
		}
		Main.logger.info("Done copying static files to " + Configuration.genOutputFolder);
	}

	/**
	 * ensure existence
	 *
	 * @throws IOException
	 */
	public static void checkLog4j() throws IOException {
		final String udir = System.getProperty("user.dir");
		final File log4j = new File(udir + "/log4j.properties");
		if (!log4j.exists()) {
			final File copyLog4j = new File(udir + "/src/resources/log4j.properties");
			FileUtil.copyFile(copyLog4j, log4j);
		}
	}

	@Override
	public void run(String... args) throws IllegalArgumentException, IllegalAccessException, NoSuchAlgorithmException {
		Configuration.copyConfigurationToSysprops();
		String inputSpecFile = "simple_cms.yml";

		// check to see if the String array is empty

		// copy any args to sysprops
		for (final String argument : args) {
			if (argument.toLowerCase().endsWith(".yml") || argument.toLowerCase().endsWith(".json")) {
				inputSpecFile = argument;
			} else if (argument.contains("=")) {
				final int p = argument.indexOf("=");
				try {
					final String narg = argument.substring(0, argument.indexOf(p));
					final String varg = argument.substring(argument.indexOf(p));
					System.setProperty(narg, varg);
				} catch (final Exception e) {
					Main.logger.warn("Could not set property from arg: " + argument + " due to: " + e.toString());
				}
			}
		}

		if (System.getProperty("schemaFile") != null) {
			inputSpecFile = System.getProperty("schemaFile");
		}
		if (System.getProperty("generateKey") != null) {
			if (args.length == 2) {
				System.out.println("--- Begin Generated Key ---");
				System.out.println(Main.generateEncryptionKey(args[1]));
				System.out.println("--- End Generated Key ---");
			} else {
				System.out.println("Usage:");
				System.out.println("java io.starter.ignite.generator.Main -DgenerateKey=<secretkey>");
			}
			return;
		}
		Main.logger.info("with: " + inputSpecFile + (args != null ? " and args: " + args.toString() : ""));

		try {
			Main.generateStack(inputSpecFile);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new IgniteException("STACKGEN FAILURE: " + e.toString());
		}
	}

	/**
	 * Create and initialize a new SwaggerGen from a JSON config object
	 *
	 * @param inputSpec JSONObject containing config data
	 * @return
	 */
	public static void generateApp(JSONObject config) throws Exception {

		try {
			Configuration.copyJSONConfigToSysprops(config);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Main.logger
					.error("Copying Configuration values from JSON to Sysprops failed while starting App Generation");
			e.printStackTrace();
		}
		Main.generateStack(config.getString("schemaFile"));

	}

	/**
	 * Step by step process to create Stack from spec file
	 *
	 * @param inputSpecFile
	 */
	public static void generateStack(String inputSpecFile) throws Exception {
		System.out.println(ASCIIArtPrinter.print());
		System.out.println();

		Main.logger.info("Starting App Generation");

		// Clear out the gen and
		if (Configuration.overwriteMode) {
			Main.initOutputFolders();
		}

		// generate swqgger api clients
		if (!Configuration.skipJavaGen) {

			generateSwagger(inputSpecFile);

			reCompileAll();

		} else {
			Main.logger.info("####### SWAGGER Generation: SKIPPED");
		}

		if (!Configuration.skipDbGen) {
			// generate corresponding DML
			// statements to create a JDBC database
			// execute DB creation, connect and test
			DBGen.createDatabaseTablesFromModelFolder();
		}

		// generate MyBatis client classes XML configuration file
		if (!Configuration.skipMybatisGen) {
			MyBatisGen.createMyBatisFromModelFolder();
		}

		// compile the DataObject Classes
		JavaGen.compile(Configuration.MODEL_DAO_PACKAGE_DIR);

		// JavaGen.compile(Configuration.PACKAGE_DIR);

		// delegates calls to/from api to the mybatis entity
		JavaGen.generateClassesFromModelFolder();

		// final compile
		JavaGen.compile(Configuration.PACKAGE_DIR);

		// copy misc files into gen project
		Main.copyStaticFiles(Main.staticFiles);

		// package the microservice for deployment
		if (!Configuration.skipMavenBuildGeneratedApp) {
			MavenBuilder.build();
		}

		Main.logger.info("Backend Stack Generation Complete.");

	}

	private static void reCompileAll()
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		try {
			JavaGen.compile(Configuration.PACKAGE_DIR);
			JavaGen.compile(Configuration.API_PACKAGE_DIR);
		} catch (final FileNotFoundException x) {
			Main.logger.error("NO INPUT FILES AT: " + Configuration.PACKAGE_DIR + " Exiting");
			return;
		}

		try {
			JavaGen.compile(Configuration.MODEL_PACKAGE_DIR);
		} catch (final Throwable e) {
			// this one fails in regen mode sometimes. ignore
		}
	}

	private static void generateSwagger(String inputSpecFile) {
		List<File> gfiles = null;
		if (Configuration.iteratePluginGen) {
			gfiles = Main.iteratePluginsGen(inputSpecFile);
		} else if (Configuration.mergePluginGen) {
			gfiles = Main.mergePluginsGen(inputSpecFile);
		} else {
			final SwaggerGen swaggerGen = new SwaggerGen(Configuration.SPEC_LOCATION + inputSpecFile);

			gfiles = swaggerGen.generate();
		}
		Main.logger.info("####### SWAGGER Generated: " + (gfiles != null ? gfiles.size() : " NO ") + " Source Files");
	}

	/**
	 * Merge the Plugin Swaggers into the main Swagger then Run the Swagger gen from
	 * the main Swagger
	 *
	 * @param inputSpecFile of Main Swagger gen
	 * @return
	 */
	private static List<File> mergePluginsGen(String inputSpecFile) {
		final SwaggerGen swaggerGen = new SwaggerGen(Configuration.SPEC_LOCATION + inputSpecFile);

		// iterate the files in the plugins folder
		final String[] fin = Main.getPluginFiles();
		if (fin != null) {
			for (final String fs : fin) {
				final File f = new File(fs);
				Main.logger.info("Generating Plugin Schema: " + fs);
				final SwaggerGen pluginSwag = new SwaggerGen(f.getAbsolutePath());
				swaggerGen.addSwagger(pluginSwag);
			}
		}
		return swaggerGen.generate();
	}

	/**
	 * Iterate and run the Plugin Swagger gens, finally Run the Main Swagger gen
	 *
	 * @param inputSpecFile of Main Swagger gen
	 */
	private static List<File> iteratePluginsGen(String inputSpecFile) {
		final List<File> allGen = new ArrayList<>();
		final SwaggerGen swaggerGen = new SwaggerGen(Configuration.SPEC_LOCATION + inputSpecFile);

		// iterate the files in the plugins folder
		final String[] fin = Main.getPluginFiles();
		for (final String fs : fin) {
			final File f = new File(Configuration.PLUGIN_SPEC_LOCATION + fs);
			Main.logger.info("Generating Plugin: " + f.getName());
			final SwaggerGen pluginSwag = new SwaggerGen(f.getAbsolutePath());
			final List<File> fxs = pluginSwag.generate();
			allGen.addAll(fxs);
		}
		allGen.addAll(swaggerGen.generate());
		Main.logger.info("####### SWAGGER Generated: " + allGen.size() + " Source Files");
		return allGen;
	}

	/**
	 * generates an encryption key suitable for use by the SecureField encryption
	 * algorithm
	 *
	 * @param salt
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @see io.starter.ignite.security.securefield.SecureField
	 */
	public static String generateEncryptionKey(String salt) throws NoSuchAlgorithmException {
		return io.starter.ignite.security.crypto.EncryptionUtil.generateKey();
	}

	/**
	 * get YML or JSON Schema files from the plugins dir
	 *
	 * for now we go one level deep only
	 *
	 * @return array of schema files
	 */
	public static String[] getPluginFiles() {
		final File pluginDir = new File(Configuration.PLUGIN_SPEC_LOCATION);
		if (!pluginDir.exists()) {
			throw new IllegalStateException(
					"getPluginFiles Failure: no path here " + Configuration.PLUGIN_SPEC_LOCATION);
		}
		final String[] pluginFiles = pluginDir.list((dir, name) -> {
			if (name.toLowerCase().endsWith(".json")) {
				return true;
			}
			if (name.toLowerCase().endsWith(".yml") || name.toLowerCase().endsWith(".yaml")) {
				return true;
			}

			// otherwise
			return false;
		});

		if (pluginFiles != null && pluginFiles.length < 1) {
			throw new IllegalStateException("Gen.getPluginFiles Failure: no plugin schemas found: "
					+ Configuration.PLUGIN_SPEC_LOCATION + ". Check the PLUGIN_SPEC_LOCATION config value.");
		}
		return pluginFiles;
	}

	private static void initOutputFolders() throws IOException {
		File genDir = new File(Configuration.genOutputFolder);
		Main.logger
				.info("Initializing output folder: " + Configuration.genOutputFolder + " exists: " + genDir.exists());
		final String marker = System.currentTimeMillis() + ".zip";
		if (genDir.exists()) {
			final String fx = Configuration.javaGenArchivePath + "/" + marker;
			final File toF = new File(fx);
			if (!toF.exists()) {
				toF.mkdirs();
				toF.delete();
				if (!toF.createNewFile()) {
					throw new IgniteException("Could not create zip file: " + fx);
				}
			}

			final ZipFileWriter zfw = new ZipFileWriter();
			try {
				zfw.zip(genDir, fx);
			} catch (final Exception e) {
				throw new IgniteException("Could not zip: " + Configuration.genOutputFolder + " to: " + fx + "\n"
						+ e.getLocalizedMessage());
			}

			final File ft = new File(System.getProperty("tmp.dir") + "deletedStackGenProjecct-" + marker);
			if (!genDir.renameTo(ft)) {
				throw new IgniteException("Could not delete: " + Configuration.genOutputFolder);
			}

			genDir = new File(Configuration.genOutputFolder);
			genDir.mkdirs();
		}

		final boolean outputDir = new File(Configuration.genOutputFolder + "/src/").mkdirs();
		if (!outputDir) {
			Main.logger.error("Could not init: " + outputDir + ". Exiting.");
		}
	}

}
