package io.starter.ignite.openapi;

import java.io.File;
import java.io.FilenameFilter;

import org.json.JSONObject;

import io.starter.ignite.generator.JavaGen;
import io.starter.ignite.generator.Main;

public class OpenAPISerializer {

	public static void main(String[] args) throws ClassNotFoundException {
		// for now
		// load folder of classfiles

		// iterate for each class make a JSON model
		String[] modelFiles = getModelFiles();

		for (String f : modelFiles) {
			Class c = Class.forName(f);
			writeOut(c);

		}

	}

	private static void writeOut(Class c) {
		JSONObject o = getJSON(c);

	}

	private static JSONObject getJSON(Class c) {
		JSONObject job = new JSONObject(c);
		return job;
	}

	static String[] getModelFiles() {
		File modelDir = new File(Main.API_MODEL_CLASSES);
		String[] modelFiles = modelDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.contains("Example"))
					return false;
				if (name.contains(JavaGen.ADD_GEN_CLASS_NAME))
					return false;
				return name.toLowerCase().endsWith(".java");
			}
		});
		return modelFiles;
	}
}
