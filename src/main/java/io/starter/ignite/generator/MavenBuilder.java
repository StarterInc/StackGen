/**
 * 
 */
package io.starter.ignite.generator;

/**
 * 
 * @author john
 *
 */
public class MavenBuilder implements Configuration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MavenCli cli = new MavenCli();
		cli.doMain(new String[]{"clean", "install"}, JAVA_GEN_FOLDER, System.out, System.out);
	}

}
