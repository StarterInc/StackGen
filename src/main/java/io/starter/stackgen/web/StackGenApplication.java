package io.starter.stackgen.web;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import io.starter.ignite.generator.Main;

/**
 * stubbed app
 * 
 * @author john
 *
 */
public class StackGenApplication {

	public static void main(String[] args) {
	    SpringApplication app;
	    if( !ArrayUtils.contains(args, "--web")){
	        app = new SpringApplication(Main.class);
		    app.setMainApplicationClass(Main.class);
		    app.setWebApplicationType(WebApplicationType.NONE);
	        // args = (String[])ArrayUtils.add(args, "--spring.jpa.hibernate.ddl-auto=create");
		    
	    } else {
	        app = new SpringApplication(StackGenApplication.class);
	    }


	    // launch the app
	    ConfigurableApplicationContext context = app.run(args);
	    
	    // finished so close the context
	    context.close();
	}
	
}