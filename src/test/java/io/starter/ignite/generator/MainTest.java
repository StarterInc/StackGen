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
		System.err.println("Setting up generatorTest");
	}

	@Test
	public void swaggerGen() {
		// generate swqgger api clients
		// (for now just use swaggerhub generated)
		SwaggerGen swaggerGen = new SwaggerGen(Configuration.SPEC_LOCATION + "/StarterIgnite.yml");
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
			System.err.println("ReactGen failed: " + e);
		}
	}
}
