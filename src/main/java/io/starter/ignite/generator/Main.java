package io.starter.ignite.generator;

import java.io.File;
import java.io.FilenameFilter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.util.ASCIIArtPrinter;

/**
 * <h2>Generates an app from a Swagger Spec</h2>
 *
 * Features the ability to merge spec files from a templates 
 * directory as well as auto-creation of CRUD methods hooked 
 * up to Ignite Data Objects.
 * 	
 * <pre>
 *   - YAML -> Swagger client
 *   - Swagger Client -> entity classes
 *   - Swagger Client -> DML for database
 *   - Swagger CLient -> React-native JS screens
 * </pre>
 * 
 * <h2>NOTE: Generate a SecureField key</h2>
 *<pre>
 *	java io.starter.ignite.generator.Main -Dio.starter.generateKey=<secretkey> -jar StarterIgnite-1.0.1.jar
 *</pre>
 *
 * @author John McMahon (@TechnoCharms)
 *
 */

public class Main implements Configuration {

	private static boolean			skipDBGen				= false;
	private static boolean			skipMybatis				= false;
	private static boolean			skipBuildGeneratedApp	= true;

	protected static final Logger	logger					= LoggerFactory
			.getLogger(Main.class);

	public static void main(String[] args) throws Exception {

		String inputSpecFile = "trade_automator.yml"; // "chainring_api_v1.yml";
														// // "simple_cms.yml";
		// String inputSpecFile = "StarterIgnite.yml";

		// check to see if the String array is empty
		if (args == null || args.length == 0) {
			logger.info("No command line arguments Usage:");
			System.out.println();
			System.out
					.println("java io.starter.ignite.generator.Main <input.yml> -D<option_name>=<option_value> ... ");
		} else {
			// For each String in the String array
			// print out the String.
			for (String argument : args) {
				if (argument.toLowerCase().endsWith(".yml")
						|| argument.toLowerCase().endsWith(".json")) {
					inputSpecFile = argument;
				} else if (argument.contains("=")) {

					int p = argument.indexOf("=");
					String narg = argument.substring(0, argument.indexOf(p));
					String varg = argument.substring(argument.indexOf(p));
					System.setProperty(narg, varg);

				}
			}
		}

		if (System.getProperty("io.starter.generateKey") != null) {
			if (args.length == 2) {
				System.out.println("--- Begin Generated Key ---");
				System.out.println(generateEncryptionKey(args[1]));
				System.out.println("-- End Generated Key ---");
			} else {
				System.out.println("Usage:");
				System.out
						.println("java io.starter.ignite.generator.Main -Dio.starter.generateKey=<secretkey> -jar StarterIgnite-1.0.1.jar");
			}
			return;
		}
		logger.info("with: " + inputSpecFile
				+ (args != null ? " and args: " + args.toString() : ""));

		generateApp(inputSpecFile);
	}

	/**
	 * Step by step process to create Ignite app from spec file
	 * 
	 * @param inputSpecFile
	 */
	public static void generateApp(String inputSpecFile) {
		System.out.println(ASCIIArtPrinter.print());
		System.out.println();
		logger.info("Starting App Generation");

		try {

			// Clear out the gen and
			initOutputFolders();

			// generate swqgger api clients
			SwaggerGen swaggerGen = new SwaggerGen(
					SPEC_LOCATION + inputSpecFile);

			// iterate the files in the plugins folder
			File[] fin = Main.getPluginFiles();
			for (File f : fin) {
				logger.info("Installing Plugin: " + f.getName());
				SwaggerGen pluginSwag = new SwaggerGen(f.getAbsolutePath());
				swaggerGen.addSwagger(pluginSwag);
			}

			logger.info("####### SWAGGER Generated: "
					+ swaggerGen.generate().size() + " Source Files");

			JavaGen.compile(PACKAGE_DIR);
			JavaGen.compile(API_PACKAGE_DIR);
			JavaGen.compile(MODEL_PACKAGE_DIR);

			if (!skipDBGen) {
				// generate corresponding DML
				// statements to create a JDBC database
				// execute DB creation, connect and test
				DBGen.createDatabaseTablesFromModelFolder();
			}

			// generate MyBatis client classes XML configuration file
			if (!skipMybatis) {
				MyBatisGen.createMyBatisFromModelFolder();
			}

			// compile the DataOgbject Classes
			JavaGen.compile(PACKAGE_DIR);

			// delegates calls to/from api to the mybatis entity
			JavaGen.generateClassesFromModelFolder();

			// copy misc files into gen project
			copyStaticFiles();

			// package the microservice for deployment
			if (!skipBuildGeneratedApp) {
				MavenBuilder.build();
			}

			logger.info("App Generation Complete.");
		} catch (Exception e) {
			logger.error("Exception during App Generation: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * generates an encryption key suitable for use by the SecureField encryption algorithm
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
	 * @return
	 */
	static File[] getPluginFiles() {
		ArrayList<File> schemaList = new ArrayList<File>();
		File pluginDir = new File(Configuration.PLUGIN_FOLDER);
		File[] pfx = pluginDir.listFiles();
		for (File f : pfx) {
			File[] z = getSchemaFiles(f);
			for (File t : z)
				schemaList.add(t);
		}
		return schemaList.toArray(new File[schemaList.size()]);
	}

	private static File[] getSchemaFiles(File f) {
		File[] pluginFiles = f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".yml")
						|| name.toLowerCase().endsWith(".json");
			}
		});
		return pluginFiles;
	}

	private static void initOutputFolders() {
		File genDir = new File(JAVA_GEN_PATH);
		logger.info("Initializing output folder: " + JAVA_GEN_PATH + " exists: "
				+ genDir.exists());
		if (genDir.exists()) {

			if (!genDir.renameTo(new File(JAVA_GEN_ARCHIVE_PATH + "."
					+ System.currentTimeMillis()))) {
				throw new IgniteException("Could not rename: " + JAVA_GEN_PATH);
			}

			genDir = new File(JAVA_GEN_PATH);
			genDir.mkdirs();
		}

		boolean outputDir = new File(Configuration.JAVA_GEN_PATH + "/src/")
				.mkdirs();
		if (!outputDir) {
			logger.error("Could not init: " + outputDir + ". Exiting.");
		}
	}

	/**
	 * a list of file paths to copy
	 * relative to project root
	 */
	private static String[][] staticFiles = {
			{ "/src/resources/templates/application.yml",
					"/src/main/resources/application.yml" },
			{ "/logs/logfile_placeholder.txt",
					"/logs/logfile_placeholder.txt" },
			{ "/src/main/java/io/starter/spring/boot/starter-ignite-banner.txt",
					"/src/main/java/io/starter/spring/boot/starter-ignite-banner.txt" } };

	private static void copyStaticFiles() {
		File genDir = new File(JAVA_GEN_PATH);
		logger.info("Copying static files to folder: " + JAVA_GEN_PATH
				+ " exists: " + genDir.exists());
		if (genDir.exists()) {
			for (String[] fx : staticFiles) {
				File fromF = new File(ROOT_FOLDER + fx[0]);
				File toF = new File(JAVA_GEN_PATH + fx[1]);
				if (fromF.exists()) {
					logger.info("Copying static file : " + fromF + " to: "
							+ toF);

					if (!toF.exists()) { // prep dest folder
						toF.mkdirs();
						toF.delete();
					}
					try {
						FileUtils.copyFile(fromF, toF);
					} catch (Exception e) {
						logger.error("Could not copy static file. "
								+ e.toString());
					}
				} else {
					logger.warn("Missing expected static file : "
							+ fromF.getAbsolutePath());
				}
			}
		}
		logger.info("Done copying static files to " + JAVA_GEN_PATH);
	}
}
