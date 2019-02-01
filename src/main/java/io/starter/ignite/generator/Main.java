package io.starter.ignite.generator;

import java.io.File;
import java.io.FilenameFilter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

	protected static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		Configuration.copyConfigurationToSysprops();
		String inputSpecFile = "simple_cms.yml"; // "trade_automator.yml";
													// //
													// //starter_ignite
													// "chainring_api_v1.yml";
													// // "simple_cms.yml";
		// String inputSpecFile = "StarterIgnite.yml";

		// check to see if the String array is empty
		if (args == null || args.length == 0 || args[0] == null) {
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

		if (System.getProperty("schemaFile") != null) {
			inputSpecFile = System.getProperty("schemaFile");
		}
		if (System.getProperty("io.starter.generateKey") != null) {
			if (args.length == 2) {
				System.out.println("--- Begin Generated Key ---");
				System.out.println(generateEncryptionKey(args[1]));
				System.out.println("--- End Generated Key ---");
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
			if (overwriteMode) {
				initOutputFolders();
			}

			// generate swqgger api clients
			List<File> gfiles = null;
			if (iteratePluginGen) {
				gfiles = iteratePluginsGen(inputSpecFile);
			} else if (mergePluginGen) {
				gfiles = mergePluginsGen(inputSpecFile);
			} else {
				SwaggerGen swaggerGen = new SwaggerGen(
						SPEC_LOCATION + inputSpecFile);
				gfiles = swaggerGen.generate();
			}

			logger.info("####### SWAGGER Generated: "
					+ (gfiles != null ? gfiles : " NO ") + " Source Files");

			JavaGen.compile(PACKAGE_DIR);
			JavaGen.compile(API_PACKAGE_DIR);
			JavaGen.compile(MODEL_PACKAGE_DIR);

			if (!skipDbGen) {
				// generate corresponding DML
				// statements to create a JDBC database
				// execute DB creation, connect and test
				DBGen.createDatabaseTablesFromModelFolder();
			}

			// generate MyBatis client classes XML configuration file
			if (!skipMybatisGen) {
				MyBatisGen.createMyBatisFromModelFolder();
			}

			// compile the DataOgbject Classes
			JavaGen.compile(PACKAGE_DIR);

			// delegates calls to/from api to the mybatis entity
			JavaGen.generateClassesFromModelFolder();

			// copy misc files into gen project
			copyStaticFiles(staticFiles);

			// package the microservice for deployment
			if (!skipMavenBuildGeneratedApp) {
				MavenBuilder.build();
			}

			logger.info("App Generation Complete.");
		} catch (Exception e) {
			logger.error("Exception during App Generation: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Merge the Plugin Swaggers into the main Swagger then Run the Swagger gen from the main Swagger 
	 * 
	 * @param inputSpecFile of Main Swagger gen
	 * @return 
	 */
	private static List<File> mergePluginsGen(String inputSpecFile) {
		SwaggerGen swaggerGen = new SwaggerGen(SPEC_LOCATION + inputSpecFile);

		// iterate the files in the plugins folder
		File[] fin = Main.getPluginFiles();
		if (fin != null) {
			for (File f : fin) {
				logger.info("Installing Plugin: " + f.getName());
				SwaggerGen pluginSwag = new SwaggerGen(f.getAbsolutePath());
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
		List<File> allGen = new ArrayList<File>();
		SwaggerGen swaggerGen = new SwaggerGen(SPEC_LOCATION + inputSpecFile);

		// iterate the files in the plugins folder
		File[] fin = Main.getPluginFiles();
		for (File f : fin) {
			logger.info("Generating Plugin: " + f.getName());
			SwaggerGen pluginSwag = new SwaggerGen(f.getAbsolutePath());
			allGen.addAll(pluginSwag.generate());
		}
		allGen.addAll(swaggerGen.generate());
		logger.info("####### SWAGGER Generated: " + allGen.size()
				+ " Source Files");
		return allGen;
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
		if (pfx != null) {
			for (File f : pfx) {
				File[] z = getSchemaFiles(f);
				for (File t : z)
					schemaList.add(t);
			}
			return schemaList.toArray(new File[schemaList.size()]);
		}
		return null;
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
		File genDir = new File(genOutpuFolder);
		logger.info("Initializing output folder: " + genOutpuFolder
				+ " exists: " + genDir.exists());
		if (genDir.exists()) {
			String fx = javaGenArchivePath + "." + System.currentTimeMillis();
			File toF = new File(fx);
			if (!toF.exists()) {
				toF.mkdirs();
			}
			if (!genDir.renameTo(toF)) {
				throw new IgniteException(
						"Could not rename: " + genOutpuFolder + " to: " + fx);
			}

			genDir = new File(genOutpuFolder);
			genDir.mkdirs();
		}

		boolean outputDir = new File(Configuration.genOutpuFolder + "/src/")
				.mkdirs();
		if (!outputDir) {
			logger.error("Could not init: " + outputDir + ". Exiting.");
		}
	}

	/**
	 * a list of file paths to copy
	 * relative to project root
	 */
	protected static String[][] staticFiles = {
			{ "/src/resources/templates/application.yml",
					"/src/main/resources/application.yml" },
			{ "/src/resources/templates/log4j.properties",
					"/src/main/resources/log4j.properties" },
			{ "/logs/logfile_placeholder.txt",
					"/logs/logfile_placeholder.txt" },
			{ "/src/main/java/io/starter/spring/boot/starter-ignite-banner.txt",
					"/src/main/java/io/starter/spring/boot/starter-ignite-banner.txt" } };

	protected static void copyStaticFiles(String[][] staticFiles) {
		File genDir = new File(genOutpuFolder);
		logger.info("Copying static files to folder: " + genOutpuFolder
				+ " exists: " + genDir.exists());
		if (genDir.exists()) {
			for (String[] fx : staticFiles) {
				File fromF = new File(rootFolder + fx[0]);
				File toF = new File(genOutpuFolder + fx[1]);
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
		logger.info("Done copying static files to " + genOutpuFolder);
	}
}
