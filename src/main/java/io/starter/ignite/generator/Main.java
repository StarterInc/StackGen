package io.starter.ignite.generator;

import java.io.File;

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
 * @author john mcmahon
 *
 */

public class Main implements Configuration {

	private static boolean skipDBGen = false;

	public static void main(String[] args) {
		String inputSpecFile = "StarterIgnite.yml";

		// check to see if the String array is empty
		if (args == null || args.length == 0) {
			System.out.println("No command line arguments Usage:");
			System.out.println();
			System.out.println("io.starter.ignite.generator.Main <input.yml> -P<NAME>=<VALUE> ... ");
		} else {
			// For each String in the String array
			// print out the String.
			for (String argument : args) {
				if (argument.toLowerCase().endsWith(".yml") || argument.toLowerCase().endsWith(".yml")) {
					inputSpecFile = argument;
				} else if (argument.contains("=")) {

					int p = argument.indexOf("=");
					String narg = argument.substring(0, argument.indexOf(p));
					String varg = argument.substring(argument.indexOf(p));
					System.setProperty(narg, varg);

				}
			}
		}

		System.out.print(ASCIIArtPrinter.print());
		System.out.println();
		System.out.println();
		io.starter.ignite.util.Logger.log("Starting Main...");
		io.starter.ignite.util.Logger
				.log("with: " + inputSpecFile + (args != null ? " and args: " + args.toString() : ""));
		try {

			// Clear out the gen and
			initOutputFolders();

			// generate swqgger api clients
			SwaggerGen swaggerGen = new SwaggerGen(inputSpecFile);
			io.starter.ignite.util.Logger.log("Generated: " + swaggerGen.generate().size() + " Source Files");
			JavaGen.compile(MODEL_PACKAGE_DIR);

			if (!skipDBGen) {
				// generate corresponding DML
				// statements to create a JDBC database
				// execute DB creation, connect and test
				DBGen.createDatabaseTablesFromModelFolder();
			}

			// generate MyBatis client classes
			// XML configuration file
			MyBatisGen.createMyBatisFromModelFolder();

			// annotated wrapper with encryption,
			// logging, annotations
			JavaGen.compile(MODEL_PACKAGE_DIR);

			// create wrapper classes which
			// delegates calls to/from api to the mybatis entity
			JavaGen.generateClassesFromModelFolder();

			// package the microservice for deployment
			MavenBuilder.build();

			// generate React Redux apps
			ReactGen.generateReactNative();

			io.starter.ignite.util.Logger.log("Main Complete.");
		} catch (Exception e) {
			io.starter.ignite.util.Logger.error("Exception during App Generation: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void initOutputFolders() {
		File genDir = new File(JAVA_GEN_FOLDER);
		io.starter.ignite.util.Logger
				.error("Initializing output folder: " + JAVA_GEN_FOLDER + " exists: " + genDir.exists());
		if (genDir.exists()) {
			String fx = JAVA_GEN_FOLDER + "." + System.currentTimeMillis();
			io.starter.ignite.util.Logger.error("Output Folder: " + JAVA_GEN_FOLDER + " exists. Renaming to: " + fx);
			genDir.renameTo(new File(fx));
			genDir = new File(JAVA_GEN_FOLDER);
			genDir.mkdirs();
		}

		boolean outputDir = new File(Configuration.OUTPUT_DIR + "/src/resources/MyBatis_SQL_Maps").mkdirs();
		if (!outputDir) {
			io.starter.ignite.util.Logger.error("Could not init: " + outputDir + ". Exiting.");
		}
	}
}
