package io.starter.ignite.generator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.config.CodegenConfigurator;

/**
 * responsible for generating the Swagger server and clients
 * 
 * @author john
 * 
 */
public class SwaggerGen extends DefaultGenerator implements Configuration {

	protected static final Logger	logger			= LoggerFactory
			.getLogger(SwaggerGen.class);

	DefaultGenerator				generator		= new DefaultGenerator();
	CodegenConfigurator				configurator	= CodegenConfigurator
			.fromFile(CONFIG_FILE);

	/**
	 * Create and initialize a new SwaggerGen
	 * 
	 * @param inputSpec
	 *            filename of spec (file in templateDirectory)
	 */
	public SwaggerGen(String spec) {

		spec = SPEC_LOCATION + spec;

		logger.debug("Create Swagger Client Apis for:" + spec);
		// attempt to read from config file

		// if a config file wasn't specified or we were unable to
		// read it
		if (configurator == null) {
			// createa a fresh configurator
			configurator = new CodegenConfigurator();
		}

		// basic
		configurator.setLibrary("jersey2"); // ("resttemplate"); // //
											// jersey2
		configurator.setLang("java");
		configurator.setArtifactId(ARTIFACT_ID);
		configurator.setModelPackage(MODEL_PACKAGE);
		configurator.setOutputDir(OUTPUT_DIR);
		configurator.setApiPackage(API_PACKAGE);
		configurator.setArtifactVersion("1.0");
		configurator.setInvokerPackage(INVOKER_PACKAGE);
		configurator.setVerbose(VERBOSE);
		configurator.addDynamicProperty("dynamic-html", "true");
		configurator.addDynamicProperty("dateLibrary", "java8");

		// company info
		configurator.addDynamicProperty("developerName", "Starter Inc.");
		configurator.addDynamicProperty("developerEmail", "info@starter.io");
		configurator.addDynamicProperty("developerName", "Starter Inc.");
		configurator
				.addDynamicProperty("developerOrganization", "Starter Inc.");
		configurator
				.addDynamicProperty("developerOrganizationUrl", "http://starter.io");

		// app config
		configurator.setAuth("oauth");
		configurator.setInputSpec(spec);

		// locations
		configurator.setTemplateDir(SPEC_LOCATION);

		// github
		configurator.setGitRepoId("StarterIgnite");
		configurator.setGitUserId("Spaceghost69");

	}

	@Override
	public List<File> generate() {
		final ClientOptInput clientOptInput = configurator.toClientOptInput();
		return new DefaultGenerator().opts(clientOptInput).generate();
	}

	static String[] getModelFiles() {
		File modelDir = new File(Main.API_MODEL_CLASSES);
		String[] modelFiles = modelDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().contains("example"))
					return false;
				if (name.toLowerCase().contains("mapper"))
					return false;
				if (name.contains(JavaGen.ADD_GEN_CLASS_NAME))
					return false;
				return name.toLowerCase().endsWith(".java");
			}
		});
		return modelFiles;
	}
}
