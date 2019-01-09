package io.starter.ignite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import io.starter.ignite.util.ASCIIArtPrinter;

@EnableAutoConfiguration
@EnableAdminServer
@SpringBootApplication
public class IgniteApplication {

	public static void main(String[] args) {
		// IGNITE GENERATOR
		System.out.println(ASCIIArtPrinter.print());
		System.out.println();

		SpringApplication.run(IgniteApplication.class, args);
	}
}
