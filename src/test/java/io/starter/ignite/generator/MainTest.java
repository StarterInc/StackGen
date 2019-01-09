/*
 *
 */
package io.starter.ignite.generator;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * test the app code generator
 */
public class MainTest implements Configuration {

	protected static final Logger	logger			= LoggerFactory
			.getLogger(MainTest.class);

	String							inputSpecFile	= SPEC_LOCATION
			+ "trade_automator.yml",

			pluginSpecFile1 = PLUGIN_SPEC_LOCATION + "ignite/eStore.yml",

			pluginSpecFile2 = PLUGIN_SPEC_LOCATION + "location_services.yml";

	// experimental
	// pluginSpecFile3 = PLUGIN_SPEC_LOCATION +
	// "/ignite/ecommerce.yml";

	@Before
	public void setUp() {
		logger.error("Setting up Starter Ignite Generator Tests");
	}

	@Test
	@Ignore(value = "should not be run as a unit test")
	public void swaggerGen() {
		SwaggerGen swaggerGen = new SwaggerGen(
				SPEC_LOCATION + "trade_automator.yml");
		assertNotNull(swaggerGen.generate());
	}

	@Test
	@Ignore(value = "should not be run as a unit test")
	public void testAppGen() throws Exception {
		Main.main(null);
	}

	@Test
	public void swaggerPluginMerge() {
		SwaggerGen swaggerGen = new SwaggerGen(inputSpecFile),
				gx1 = new SwaggerGen(pluginSpecFile1),
				gx2 = new SwaggerGen(pluginSpecFile2);
		// gx3 = new SwaggerGen(pluginSpecFile3); experimental
		// partial input

		swaggerGen.addSwagger(gx1);
		swaggerGen.addSwagger(gx2);
		// swaggerGen.addSwagger(gx3); experimental

		assertNotNull(swaggerGen.mergePluginSwaggers());

	}

	@Test
	public void swaggerPluginMergeGenerate() {
		SwaggerGen swaggerGen = new SwaggerGen(inputSpecFile),
				gx1 = new SwaggerGen(pluginSpecFile1),
				gx2 = new SwaggerGen(pluginSpecFile2);
		// gx3 = new SwaggerGen(pluginSpecFile3); experimental
		// partial input

		swaggerGen.addSwagger(gx1);
		swaggerGen.addSwagger(gx2);
		// swaggerGen.addSwagger(gx3); experimental

		// TODO: mock this assertNotNull(swaggerGen.generate());

	}
}
