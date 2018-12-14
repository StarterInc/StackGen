package io.starter.ignite.generator;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.util.ASCIIArtPrinter;

/**
 * generate an app from swagger YAML
 *
 * <pre>
 *   - YAML -> Swagger client
 *   - Swagger Client -> entity classes
 *   - Swagger Client -> DML for database
 *   - Swagger CLient -> React-native JS screens
 * </pre>
 * 
 * @author John McMahon (@TechnoCharms) mcmahon
 *
 */

public class Main implements Configuration {

	private static boolean			skipDBGen		= false;
	private static boolean			skipMybatis		= false;
	private static boolean			skipReactGen	= true;

	protected static final Logger	logger			= LoggerFactory
			.getLogger(Main.class);

	public static void main(String[] args) {

		String inputSpecFile = "chainring_api_v1.yml";
		// String inputSpecFile = "StarterIgnite.yml";

		// check to see if the String array is empty
		if (args == null || args.length == 0) {
			System.out.println("No command line arguments Usage:");
			System.out.println();
			System.out
					.println("java io.starter.ignite.generator.Main <input.yml> -D<option_name>=<option_value> ... ");
		} else {
			// For each String in the String array
			// print out the String.
			for (String argument : args) {
				if (argument.toLowerCase().endsWith(".yml")
						|| argument.toLowerCase().endsWith(".yml")) {
					inputSpecFile = argument;
				} else if (argument.contains("=")) {

					int p = argument.indexOf("=");
					String narg = argument.substring(0, argument.indexOf(p));
					String varg = argument.substring(argument.indexOf(p));
					System.setProperty(narg, varg);

				}
			}
		}

		System.out.println(ASCIIArtPrinter.print());
		System.out.println();
		System.out.println();
		logger.debug("Starting App Generation");
		logger.debug("with: " + inputSpecFile
				+ (args != null ? " and args: " + args.toString() : ""));
		try {

			// Clear out the gen and
			initOutputFolders();

			// generate swqgger api clients
			SwaggerGen swaggerGen = new SwaggerGen(inputSpecFile);
			logger.debug("Generated: " + swaggerGen.generate().size()
					+ " Source Files");
			JavaGen.compile(MODEL_PACKAGE_DIR);

			if (!skipDBGen) {
				// generate corresponding DML
				// statements to create a JDBC database
				// execute DB creation, connect and test
				DBGen.createDatabaseTablesFromModelFolder();
			}

			// generate MyBatis client classes
			// XML configuration file
			if (!skipMybatis) {
				MyBatisGen.createMyBatisFromModelFolder();
			}

			JavaGen.compile(MODEL_DAO_PACKAGE_DIR);

			// annotated wrapper with encryption,
			// logging, annotations
			JavaGen.compile(MODEL_PACKAGE_DIR);

			// create wrapper classes which
			// delegates calls to/from api to the mybatis entity
			JavaGen.generateClassesFromModelFolder();

			// package the microservice for deployment
			MavenBuilder.build();

			// generate React Redux apps
			if (!skipReactGen) {
				ReactGen.generateReactNative();
			}

			logger.debug("App Generation Complete.");
		} catch (Exception e) {
			logger.error("Exception during App Generation: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void initOutputFolders() {
		File genDir = new File(JAVA_GEN_PATH);
		logger.debug("Initializing output folder: " + JAVA_GEN_PATH
				+ " exists: " + genDir.exists());
		if (genDir.exists()) {
			String fx = ".." + JAVA_GEN_ARCHIVE_FOLDER + "."
					+ System.currentTimeMillis();
			logger.error("Output Folder: " + JAVA_GEN_PATH
					+ " exists. Renaming to: " + fx);
			genDir.renameTo(new File(fx));
		}

		/*
		 * boolean outputDir = new File(Configuration.OUTPUT_DIR
		 * + "/src/main/resources/MyBatis_SQL_Maps").mkdirs();
		 * if (!outputDir) {
		 * logger.error("Could not init: " + outputDir +
		 * ". Exiting.");
		 * }
		 */
	}
}
