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
public class MainTest {

	protected static final Logger logger = LoggerFactory
			.getLogger(MainTest.class);

	@Before
	public void setUp() {
		logger.error("Setting up generatorTest");
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
}
