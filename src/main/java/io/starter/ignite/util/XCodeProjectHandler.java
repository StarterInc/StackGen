package io.starter.ignite.util;

/**
 * TODO: Experimental Feature
 * 
 * Handle the modification of XCODE project files (unzip, edit, rezip)
 * 
 * @author John McMahon (@TechnoCharms)
 *
 */
public class XCodeProjectHandler {

	private static final String XCODE_PROJECT_FILE_EXTENSION = ".xcodeproj";

	public static void main(String[] args) {
		// open the .xcodeproj file
		args = new String[1];
		args[0] = "MyLittlePony" + XCODE_PROJECT_FILE_EXTENSION;
	}

}
