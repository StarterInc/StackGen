package io.starter.stackgen.web;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import io.starter.ignite.generator.Main;

/**
 * CLI Entrypoint
 * 
 * @author john
 *
 */
public class StackGenApplication {

	public static void main(String[] args) {
	    SpringApplication app = new SpringApplication(Main.class);
	    
	    // launch the app
	    ConfigurableApplicationContext context = app.run(args);
	}
	
}