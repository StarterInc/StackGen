package io.starter.stackgen.web;

import io.starter.ignite.generator.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * CLI Entrypoint
 *
 * @author john
 */
public class StackGenApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);

        // launch the app
        ConfigurableApplicationContext context = app.run(args);
    }

}