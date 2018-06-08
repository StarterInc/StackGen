package io.starter.ignite.generator;

import io.swagger.codegen.*;
import io.swagger.codegen.config.CodegenConfigurator;
import java.io.File;
import java.io.FilenameFilter;

/**
 * responsible for generating the Swagger server and clients
 * 
 * @author john
 * 
 */
public class SwaggerGen extends DefaultGenerator implements Configuration{
	
	DefaultGenerator generator = new DefaultGenerator();


	/**
	 * Create and initialize a new SwaggerGen
	 * 
	 * @param inputSpec
	 *            filename of spec (file in templateDirectory)
	 */
	public SwaggerGen(String spec) {

		spec = SPEC_LOCATION + spec;
		
		io.starter.ignite.util.Logger.log("Create Swagger Client Apis for:" + spec);
		// attempt to read from config file
		CodegenConfigurator configurator = CodegenConfigurator.fromFile(CONFIG_FILE);

		// if a config file wasn't specified or we were unable to read it
		if (configurator == null) {
			// createa a fresh configurator
			configurator = new CodegenConfigurator();
		}

		// basic
		configurator.setLibrary("jersey2");
		configurator.setLang("java");
		configurator.setArtifactId(ARTIFACT_ID);
		configurator.setModelPackage(MODEL_PACKAGE);
		configurator.setOutputDir(OUTPUT_DIR);
		configurator.setApiPackage(API_PACKAGE);
		configurator.setArtifactVersion("1.0");
		configurator.setInvokerPackage(INVOKER_PACKAGE);
		configurator.setVerbose(VERBOSE);
		configurator.addDynamicProperty("dynamic-html", "true");
		
		// app config
		configurator.setAuth("oauth");
		configurator.setInputSpec(spec);

		// locations
		configurator.setTemplateDir(SPEC_LOCATION);

		// github
		configurator.setGitRepoId("StarterIgnite");
		configurator.setGitUserId("Spaceghost69");

		final ClientOptInput clientOptInput = configurator.toClientOptInput();

		new DefaultGenerator().opts(clientOptInput).generate();
	}

	static String[] getModelFiles() {
		File modelDir = new File(Main.API_MODEL_CLASSES);
		String[] modelFiles = modelDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.contains("Example"))
					return false;
				if (name.contains(JavaGen.ADD_GEN_CLASS_NAME))
					return false;
				return name.toLowerCase().endsWith(".java");
			}
		});
		return modelFiles;
	}
}
