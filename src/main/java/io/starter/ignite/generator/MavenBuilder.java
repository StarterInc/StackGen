/**
 * 
 */
package io.starter.ignite.generator;

import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
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
		logger.info("========= BEGIN MavenBuilder ========= : "
				+ genOutputFolder);

		MavenCli cli = new MavenCli();
		System.setProperty("maven.multiModuleProjectDirectory", "true");
		System.setProperty("skipTests", "false");

		// TODO: use spring-boot
		try {
			cli.doMain(new String[] { "clean",
					"install" }, genOutputFolder, System.out, System.err);
			logger.info("========= END MavenBuilder =========");
			cli.doMain(new String[] { "clean", "install",
					"spring-boot:run" }, genOutputFolder, System.out, System.err);
			logger.info("========= END MavenBuilder =========");
		} catch (Exception e) {
			logger.info("Could not build: " + e);
		}
	}

}
