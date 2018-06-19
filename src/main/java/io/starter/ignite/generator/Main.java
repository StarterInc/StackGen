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

		System.out.print(ASCIIArtPrinter.print());
		System.out.println();
		System.out.println();
		io.starter.ignite.util.Logger.log("Starting Main...");

		try {

			// Clear out the gen and
			initOutputFolders();

			// generate swqgger api clients
			SwaggerGen swaggerGen = new SwaggerGen("StarterIgnite.yml");
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
