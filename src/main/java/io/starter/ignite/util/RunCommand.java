package io.starter.ignite.util;

import io.starter.ignite.generator.Configuration;
import io.starter.ignite.generator.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// class StreamGobbler omitted for brevity
public class RunCommand {

	// whitelist the commands this thing will run
	private static List<String> WHITE_LIST = new ArrayList<>(Arrays.asList(
			"ls", "npm", "react-native", "javac"));

	private static String cmd = "react-native";

	public static void main(String args[]) {

		args = new String[4];

		args[0] = "/usr/local/bin/react-native";
		args[1] = "init";
		args[2] = "${MYBATIS_MAIN}main/react/" + Configuration.REACT_APP_NAME;
		args[3] = "--verbose";

		// args[1] =
		// "cd ${MYBATIS_MAIN}main/react/MyLittlePony";
		// args[2] = "/usr/local/bin/react-native run-ios";

		if (args.length < 1) {
			io.starter.ignite.util.Logger.log("USAGE: java TestExec \"cmd\"");
			System.exit(1);
		}

		try {
			String cmd = args[0];
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(args);

			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(
					proc.getErrorStream(), "ERR");

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream(), "OUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = proc.waitFor();
			io.starter.ignite.util.Logger.log("ExitValue: " + exitVal);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void runSafe(String string, String[] cmdarray)
			throws IOException {
		System.err.println("Running: " + string);
		// check to see if the command is in our list of safe commands
		if (!WHITE_LIST.contains(cmd.toLowerCase())) {
			System.err.println("RunCommand could not run: " + cmd
					+ ".  Command not whitelisted.");
			return;
		}

		ProcessBuilder builder = new ProcessBuilder(cmdarray);
		builder.directory(new File(Main.JAVA_GEN_SRC_FOLDER).getAbsoluteFile()); // root
		builder.redirectErrorStream(true);
		builder.start();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void mainX(String[] args) throws IOException {
		// test
		String sourcepath = Main.JAVA_GEN_SRC_FOLDER
				+ "/gen/io/starter/ignite/model/";
		io.starter.ignite.util.Logger.log("RunCommand Compiling: " + sourcepath);
		String[] cmdarray = null;
		if (args != null)
			// {
			cmdarray = args;
		// } else {
		cmdarray = new String[] { "javac", sourcepath + "User.java",
				"-sourcepath", sourcepath, "-classpath",
				System.getProperty("java.class.path") };
		// }
		runSafe(cmdarray[0], cmdarray);
	}

}