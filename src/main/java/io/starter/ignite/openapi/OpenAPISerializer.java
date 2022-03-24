package io.starter.ignite.openapi;

import org.json.JSONObject;

import io.starter.ignite.generator.StackGenConfigurator;
import io.starter.ignite.generator.Gen;

/**
 * Creates an OpenAPI Spec fragment from Java classes
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class OpenAPISerializer {

	public static void main(String[] args) throws ClassNotFoundException {
		// for now
		// load folder of classfiles

		// iterate for each class make a JSON model
		StackGenConfigurator config = new StackGenConfigurator();
		String[] modelFiles = new Gen(config).getModelFileNames();

		for (String f : modelFiles) {
			Class<?> c = Class.forName(f);
			writeOut(c);

		}

	}

	private static void writeOut(Class<?> c) {
		JSONObject o = getJSON(c);

	}

	private static JSONObject getJSON(Class<?> c) {
		JSONObject job = new JSONObject(c);
		return job;
	}

}
