package io.starter.ignite.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.aspectj.util.FileUtil;
import org.codehaus.plexus.util.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import io.starter.ignite.util.ASCIIArtPrinter;
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
public class Main extends Gen implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		if (args == null) {
			args = new String[0];
		}
		
		SpringApplication app = new SpringApplication(Main.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		ConfigurableApplicationContext context = app.run(args);
	    logger.debug("CLOSING $type Extractor...");
	    context.close();
		
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

	protected void copyStaticFiles(String[][] staticFiles) {
		final File genDir = new File(config.getGenOutputFolder() );
		if (!genDir.exists()) {
			genDir.mkdirs();
		}
		Main.logger.info("Copying static files to folder: " + config.getGenOutputFolder()  + " exists: " + genDir.exists());
		if (genDir.exists()) {
			String projectdir = System.getProperty("user.dir");
			for (final String[] fx : staticFiles) {
				final File fromF = new File( projectdir + fx[0]);
				final File toF = new File(config.getGenOutputFolder()  + fx[1]);
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
		Main.logger.info("Done copying static files to " + config.getGenOutputFolder() );
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
		String inputSpecFile = null;

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

		if (inputSpecFile == null && System.getProperty("schemaFile") != null) {
			inputSpecFile = System.getProperty("schemaFile");
		}
		if (System.getProperty("generateKey") != null) {
			if (args.length == 2) {
				System.out.println("--- Begin Generated Key ---");
				System.out.println(Main.generateEncryptionKey(args[1]));
				System.out.println("--- End Generated Key ---");
			} else {
				System.out.println("Usage:");
				System.out.println(
						"java io.starter.ignite.generator.Main -DgenerateKey=<secretkey> -jar target/stackgen-1.0.1.jar");
			}
			return;
		}
		logger.info("with: " + inputSpecFile + (args != null ? " and args: " + args.toString() : ""));

		try {
			// config = getConfig();
			config.setInputSpec(inputSpecFile);
			generateStack(config);
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
	public void generateApp(JSONObject cfg) throws Exception {

		try {
			config = StackGenConfigurator.configureFromJSON(cfg, config);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Main.logger.error("Copying config values from JSON to Sysprops failed while starting App Generation");
			e.printStackTrace();
		}
		generateStack(config);

	}

	/**
	 * Step by step process to create Stack from spec file
	 *
	 * @param inputSpecFile
	 */
	public void generateStack(StackGenConfigurator cfg) throws Exception {
		logger.debug("Begin StackGen Back End Generation...");
		preFlight(cfg);
		config = cfg;
		// JavaGen initialized first as it handles compilations
		JavaGen jg = new JavaGen(config);
		
		// Clear out the gen and
		if (config.overwriteMode) {
			initOutputFolders();
		}
		logger.debug("Generating initial Model and API Classes...");
		// generate swqgger api clients
		if (!config.skipJavaGen) {

			generateSwagger(cfg);

			reCompileAll(jg);

		} else {
			logger.debug("####### SWAGGER Generation: SKIPPED");
		}

		if (!config.skipDbGen) {
			logger.debug("Generating Database Schemas for Model API...");
			// generate corresponding DML
			// statements to create a JDBC database
			// execute DB creation, connect and test
			new DBGen(config).createDatabaseTablesFromModelFolder();
		}

		// generate MyBatis client classes XML configuration file
		if (!config.skipMybatisGen) {
			reCompileAll(jg);
			logger.debug("Generating MyBatis ORM Beans for Model API...");
			new MyBatisGen(config).createMyBatisFromModelFolder();
		}

		logger.debug("Compiling all java classes...");
		// compile the DataObject Classes
		jg.compile(config.getModelDaoPackageDir());

		logger.debug("Generating StackGen API beans...");
		// delegates calls to/from api to the mybatis entity
		jg.generateClassesFromModelFolder();

		// final compile
		logger.debug("Final compile of all java classes...");
		jg.compile(config.getPackageDir());

		// copy misc files into gen project
		logger.debug("Copying static project files...");
		copyStaticFiles(staticFiles);

		// package the microservice for deployment
		if (!config.skipMavenBuildGeneratedApp) {
			logger.debug("Running Maven packager for deployment...");
			MavenBuilder.build();
		}

		logger.debug("StackGen Back End Generation Complete.");

	}

	private void preFlight(StackGenConfigurator cfg) {
		this.config = cfg;

		System.out.println(ASCIIArtPrinter.print());
		System.out.println();

		String artifactId = System.getProperty("artifactId");
		if(artifactId != null) {
			this.config.setArtifactId(artifactId);
			logger.info("Generating: " + this.config.getArtifactId());
		}
		
		Main.logger.info("Starting Stack Generation");
	}

	private void reCompileAll(JavaGen jg)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		try {
			jg.compile(config.getPackageDir());
			jg.compile(config.getApiPackageDir());
		} catch (final FileNotFoundException x) {
			Main.logger.error("NO INPUT FILES AT: " + config.getPackageDir() + " Exiting");
			return;
		}

		try {
			jg.compile(config.getModelPackageDir() );
		} catch (final Throwable e) {
			// this one fails in regen mode sometimes. ignore
		}
	}

	private void generateSwagger(StackGenConfigurator config) throws IOException {
		logger.info("Generating Swagger Files From Plugins");
		String inputSpecFile = config.getInputSpec();
		List<File> gfiles = null;

		// handle file-based Schemas
		if (inputSpecFile != null && !inputSpecFile.isEmpty()) {
			// append the input spec path if we aren't able to use default
			if (!new File(inputSpecFile).exists()) {
				inputSpecFile = config.getSpecLocation() + inputSpecFile;
				config.setInputSpec(inputSpecFile);
			}
			
			if (config.iteratePluginGen) {
				logger.info("Iterating Swagger Plugins");
				gfiles = iteratePluginsGen(inputSpecFile);
			} else if (config.mergePluginGen) {
				logger.info("Merging Swagger Plugins");
				gfiles = mergePluginsGen(inputSpecFile);
			}
		} else if(config.schemaData != null){
			String generatedSchemaFileName = config.getJavaGenResourcesFolder() + "/stackgen-schema.yml";
			File fout = new File(generatedSchemaFileName);
			if(!fout.canWrite()) {
				fout.mkdirs();
				fout.delete();
			}
			FileUtils.fileWrite(generatedSchemaFileName, config.schemaData);
			
			config.setInputSpec(generatedSchemaFileName);
		}else {
			throw new IgniteException("No Swagger/OpenAPI Schema found for:" + config.getSchemaName());
		}
		logger.info("File Generation");
		gfiles = new SwaggerGen(config).generate();
		Main.logger.info("####### SWAGGER Generated: " + (gfiles != null ? gfiles.size() : " NO ") + " Source Files");
	}

	/**
	 * Merge the Plugin Swaggers into the main Swagger then Run the Swagger gen from
	 * the main Swagger
	 *
	 * @param inputSpecFile of Main Swagger gen
	 * @return
	 */
	private List<File> mergePluginsGen(String inputSpecFile) {
		final SwaggerGen swaggerGen = new SwaggerGen(config.getSpecLocation() + inputSpecFile);

		// iterate the files in the plugins folder
		final String[] fin = getPluginFiles();
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
	private List<File> iteratePluginsGen(String inputSpecFile) {
		final List<File> allGen = new ArrayList<>();
		logger.warn("iterating plugins...");
		final SwaggerGen swaggerGen = new SwaggerGen(inputSpecFile, config);

		// iterate the files in the plugins folder
		final String[] fin = getPluginFiles();
		for (final String fs : fin) {
			final File f = new File(config.PLUGIN_SPEC_LOCATION + fs);
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
	public String[] getPluginFiles() {
		final File pluginDir = new File(config.PLUGIN_SPEC_LOCATION);
		if (!pluginDir.exists()) {
			throw new IllegalStateException("getPluginFiles Failure: no path here " + config.PLUGIN_SPEC_LOCATION);
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
					+ config.PLUGIN_SPEC_LOCATION + ". Check the PLUGIN_SPEC_LOCATION config value.");
		}
		return pluginFiles;
	}

	private void initOutputFolders() throws IOException {
		File genDir = new File(config.getGenOutputFolder() );
		logger.info("Initializing output folder: " + config.getGenOutputFolder()  + " exists: " + genDir.exists());
		final String marker = System.currentTimeMillis() + ".zip";
		if (genDir.exists()) {

			
			final String fx = config.getJavaGenArchivePath() + "/" + marker;
			
			
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
				throw new IgniteException(
						"Could not zip: " + config.getGenOutputFolder()  + " to: " + fx + "\n" + e.getLocalizedMessage());
			}
			Properties sysp = System.getProperties();
			final File ft = new File(sysp.getProperty("java.io.tmpdir") + "/deletedStackGenProjecct-" + marker);
			if (!genDir.renameTo(ft)) {
				throw new IgniteException("Could not delete: " + config.getGenOutputFolder() );
			}

			genDir = new File(config.getGenOutputFolder() );
			genDir.mkdirs();
		}

		final boolean outputDir = new File(config.getGenOutputFolder()  + "/src/").mkdirs();
		if (!outputDir) {
			Main.logger.error("Could not init: " + outputDir + ". Exiting.");
		}
	}

}
