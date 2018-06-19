/**
 * 
 */
package io.starter.ignite.generator;

import org.apache.maven.cli.MavenCli;

/**
 * 
 * @author john mcmahon
 *
 */
public class MavenBuilder implements Configuration {

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
		io.starter.ignite.util.Logger.log("========= BEGIN MavenBuilder ========= : " + JAVA_GEN_FOLDER);

		MavenCli cli = new MavenCli();
		System.setProperty("maven.multiModuleProjectDirectory", "true");
		System.setProperty("skipTests", "true");
		cli.doMain(new String[] { "clean", "install" }, JAVA_GEN_FOLDER, System.out, System.out);
		io.starter.ignite.util.Logger.log("========= END MavenBuilder =========");
	}

}
