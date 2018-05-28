/*
 *
 */
package io.starter.ignite;


import io.starter.ignite.generator.*;

import org.junit.Ignore;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * test the app code generator
 */
public class GeneratorTest
{

	@Before
	public void setUp()
	{
        System.err.println("Setting up generatorTest");
	}
	
	@Test
	public void swaggerGen() {
		// generate swqgger api clients
		// (for now just use swaggerhub generated)
		SwaggerGen swaggerGen = new SwaggerGen("StarterIgnite.yml");
	}

	@Test
	@Ignore
	public void testAppGen()
	{
        Main.main(null);
	}
	
	@Test
//	@Ignore
	public void testGenerateReact()
	{
	    try{
            ReactGen.main(null);
        }catch(Exception e){
            System.err.println("ReactGen failed: " + e);
        }
	}
}
