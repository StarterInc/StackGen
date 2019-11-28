/*
 *
 */
package io.starter.ignite.generator;

import static org.junit.Assert.assertNotNull;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * test the app code generator
 */
public class SwaggerGeneratorIT implements Configuration {

	protected static final Logger	logger			= LoggerFactory
			.getLogger(SwaggerGeneratorIT.class);

	String							inputSpecFile	= SPEC_LOCATION
			+ "trade_automator.yml",

			pluginSpecFile1 = PLUGIN_SPEC_LOCATION
					+ "ignite/domains/ecommerce/eStore.yml",

			pluginSpecFile2 = PLUGIN_SPEC_LOCATION + "location_services.yml";

	// experimental
	// pluginSpecFile3 = PLUGIN_SPEC_LOCATION +
	// "/ignite/ecommerce.yml";

	@Before
	public void setUp() {
		logger.error("Setting up Starter Ignite Generator Integration Test");
	}

	@Test
	@Ignore(value = "should not be run as a unit test")
	public void swaggerGenJSONConfig() {

		String inputJSON = "{\n"
				+ "  \"adminServerHost\": \"host name of the admin server\",\n"
				+ "  \"adminServerPort\": \"port of the admin server\",\n"
				+ "  \"artifactId\": \"ignite\",\n"
				+ "  \"createdDate\": \"2019-01-13T23:52:04.918Z\",\n"
				+ "  \"dbGenDropTable\": \"true\",\n"
				+ "  \"dbHostName\": \"db.mycompany.rds.us-west-2.rds.amazonaws.com\",\n"
				+ "  \"dbName\": \"ignite\",\n"
				+ "  \"dbPassword\": \"ABDCEDF\",\n"
				+ "  \"dbUsername\": \"igniteuser\",\n"
				+ "  \"hostName\": \"localhost\",\n"
				+ "  \"hostPort\": \"6969\",\n" + "  \"id\": \"0\",\n"
				+ "  \"starterIgniteSecureKey\": \"=W34sdcwdsfwC34=\",\n"
				+ "  \"keySpec\": \"{keyOwner:111, keySource:'session | system'}\",\n"
				+ "  \"keyVersion\": \"0\",\n"
				+ "  \"modifiedDate\": \"2019-01-13T23:52:04.918Z\",\n"
				+ "  \"mybatisJava\": \"gen/src/main/java/io/starter/ignite/model/\",\n"
				+ "  \"mybatisMain\": \"gen/src/\",\n"
				+ "  \"name\": \"My Microservice API\",\n"
				+ "  \"ownerId\": \"0\",\n"
				+ "  \"schemaData\": \"Complete OpenAPI Schema Contents...\",\n"
				+ "  \"schemaName\": \"starter\",\n"
				+ "  \"status\": \"available\"\n" + "}";
		JSONObject job = new JSONObject(inputJSON);

		SwaggerGen swaggerGen = new SwaggerGen(job);
		assertNotNull(swaggerGen.generate());
	}

	@Test
	public void swaggerGen() {
		SwaggerGen swaggerGen = new SwaggerGen(
				SPEC_LOCATION + "trade_automator.yml");
		assertNotNull(swaggerGen.generate());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAppGen() throws Exception {
		Main.main(null);
	}

}
