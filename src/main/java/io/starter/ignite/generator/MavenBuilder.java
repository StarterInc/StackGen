/**
 * 
 */
package io.starter.ignite.generator;

import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author john mcmahon
 *
 */
public class MavenBuilder implements Configuration {

	protected static final Logger logger = LoggerFactory
			.getLogger(MavenBuilder.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MavenBuilder.build();
	}

	/**
	 * run the Maven build
	 */
	public static void build() {
		logger.debug("========= BEGIN MavenBuilder ========= : "
				+ JAVA_GEN_PATH);

		MavenCli cli = new MavenCli();
		System.setProperty("maven.multiModuleProjectDirectory", "true");
		System.setProperty("skipTests", "false");

		// TODO: use spring-boot
		try {
			cli.doMain(new String[] { "clean",
					"install" }, JAVA_GEN_PATH, System.out, System.out);
			logger.debug("========= END MavenBuilder =========");
		} catch (Exception e) {
			logger.warn("Could not build: " + e);
		}
	}

}
