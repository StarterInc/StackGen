package io.starter.ignite.generator;

import io.starter.ignite.util.ASCIIArtPrinter;

import java.io.File;

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
 * @author john
 *
 */

public class Main implements Configuration {

	public static void main(String[] args) {

		System.out.println(ASCIIArtPrinter.print());

		System.out.println("Starting Main...");

		try {

			// Clear out the gen and
			initOutputFolders();

			// generate swqgger api clients
			SwaggerGen swaggerGen = new SwaggerGen("StarterIgnite.yml");
			JavaGen.compile(MODEL_PACKAGE_DIR);

			// System.exit(0);
			
			// generate corresponding DML
			// statements to create a JDBC database
			// execute DB creation, connect and test
			DBGen.createDatabaseTablesFromModelFolder();

			// generate MyBatis client classes
			// XML configuration file
			MyBatisGen.createMyBatisFromModelFolder();

			// annotated wrapper with encryption,
			// logging, annotations
			JavaGen.compile(MODEL_PACKAGE_DIR);

			// create wrapper classes which
			// delegates calls to/from api to the mybatis entity
			JavaGen.generateClassesFromModelFolder();

			// generate React Redux apps
			ReactGen.generateReactNativeFromAppFolder();

			System.out.println("Main Complete.");
		} catch (Exception e) {
			System.err.println("Exception during App Generation: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void initOutputFolders() {
		File genDir = new File(Configuration.JAVA_GEN_FOLDER);
		if (genDir.exists()) {
			genDir.renameTo(new File(Configuration.JAVA_GEN_FOLDER + "." + System.currentTimeMillis()));
			genDir = new File(Configuration.JAVA_GEN_FOLDER);
			genDir.mkdirs();
		}

		boolean outputDir = new File(Configuration.OUTPUT_DIR + "/src/resources/MyBatis_SQL_Maps").mkdirs();
		if (!outputDir) {
			System.err.println("Could not init: " + outputDir + ". Exiting.");
		}
	}
}
