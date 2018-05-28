package io.starter.ignite.generator;

import io.swagger.codegen.ClientOptInput;
// import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.config.CodegenConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SwaggerGenerator implements Runnable {

    public static final Logger LOG = LoggerFactory.getLogger(SwaggerGenerator.class);

    private Boolean verbose;

    @Override
    public void run() {

		String configFile = null;
		// attempt to read from config file
        CodegenConfigurator configurator = CodegenConfigurator.fromFile(configFile);

        // if a config file wasn't specified or we were unable to read it
        if (configurator == null) {
            // createa a fresh configurator
            configurator = new CodegenConfigurator();
        }

        final ClientOptInput clientOptInput = configurator.toClientOptInput();

        new DefaultGenerator().opts(clientOptInput).generate();
    }
}
