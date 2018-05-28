/*
 *
 */
package io.starter.ignite.generator;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import io.starter.ignite.generator.Gen;

/**
 * test the app code generator
 */
public class GenTest {

	@Before
	public void setUp() {
		System.err.println("Setting up GenTest");
	}

	@Test
	public void genFilesTest() {
		// generate swqgger api clients
		// (for now just use swaggerhub generated)
		File[] fx = Gen.getFiles();
		junit.framework.Assert.assertNotNull(fx);
	}

}
