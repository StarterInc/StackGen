package io.starter.ignite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAutoConfiguration
@EnableAdminServer
@SpringBootApplication
public class IgniteApplication {

	public static void main(String[] args) {
		// IGNITE GENERATOR
		SpringApplication.run(IgniteApplication.class, args);
	}
}
