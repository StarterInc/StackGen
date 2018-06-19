/*
 *
 */
package io.starter.ignite.generator;

import org.junit.Ignore;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * test the app code generator
 */
public class MainTest {

	@Before
	public void setUp() {
		io.starter.ignite.util.Logger.error("Setting up generatorTest");
	}

	@Test
	@Ignore
	public void swaggerGen() {
		// generate swqgger api clients
		// (for now just use swaggerhub generated)
		SwaggerGen swaggerGen = new SwaggerGen("StarterIgnite.yml");
		assertNotNull(swaggerGen.generate());
	}

	@Test
	public void testAppGen() {
		Main.main(null);
	}

	@Test
	public void testGenerateReact() {
		try {
			ReactGen.main(null);
		} catch (Exception e) {
			io.starter.ignite.util.Logger.error("ReactGen failed: " + e);
		}
	}
}
