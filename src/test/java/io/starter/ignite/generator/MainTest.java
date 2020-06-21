/*
 *
 */
package io.starter.ignite.generator;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.List;

import io.swagger.models.properties.Property;

/**
 * test the app code generator
 */
public class MainTest {

	protected static final Logger logger = LoggerFactory.getLogger(MainTest.class);

	String inputSpecFile = "trade_automator.yml", pluginSpecFile1 = "ignite/domains/ecommerce/eStore.yml",
			pluginSpecFile2 = "location_services.yml";

	// experimental
	// pluginSpecFile3 = PLUGIN_SPEC_LOCATION +
	// "/ignite/ecommerce.yml";

	@Before
	public void setUp() {
		logger.error("Setting up Starter Ignite Generator Unit Test");
	}

	@Test
	// @Ignore(value = "should not be run as a unit test")
	public void swaggerGenJSONConfig() {

		JSONObject job = new JSONObject(inputJSON);

		SwaggerGen swaggerGen = new SwaggerGen(job);

		swaggerGen.mergePluginSwaggers();
	}

	
	// @Test
	public void generateAppFromJSON() throws Exception {
		JSONObject job = new JSONObject(inputJSON);
		// job.remove("schemaData");
		// job.remove("schemaFile");
		
		Main generator = new Main();
		generator.generateApp(job);
	}
	
	@Test
	// @Ignore(value = "should not be run as a unit test")
	public void swaggerGenJSONConfigGenerate() {

		JSONObject job = new JSONObject(inputJSON);

		SwaggerGen swaggerGen = new SwaggerGen(job);

		swaggerGen.mergePluginSwaggers();

		List<File> output = swaggerGen.generate();
		org.junit.Assert.assertEquals("there should be 16 output files", output.size(), 17);
		
		
	}

	@Test
	public void swaggerPluginMergeGenerate() {

		for (int i = 0; i < 10; i++) {

			SwaggerGen swaggerGen = new SwaggerGen(inputSpecFile), gx1 = new SwaggerGen("plugins/" + pluginSpecFile1),
					gx2 = new SwaggerGen("plugins/" + pluginSpecFile2);

			// gx3 = new SwaggerGen(pluginSpecFile3); experimental
			// partial input

			swaggerGen.addSwagger(gx1);
			swaggerGen.addSwagger(gx2);
			swaggerGen.mergePluginSwaggers();
			swaggerGen.preGen();

			assertEquals("there should be 2 plugin swagger specs", 2, swaggerGen.pluginSwaggers.size());

			 assertEquals("there should be 17 total swagger models", 17, swaggerGen.generator.getSwagger().getDefinitions().size());

			Property px = swaggerGen.generator.getSwagger().getDefinitions().get("User").getProperties().get("userStatus");

			assertEquals("Check User.userStatus field", "User Status", px.getDescription());

		}

	}

	String inputJSON = "{\n" + "  \"adminServerHost\": \"host name of the admin server\",\n"
			+ "  \"adminServerPort\": \"port of the admin server\",\n" 
			+ "  \"artifactId\": \"ignite\",\n"
			+ "  \"createdDate\": \"2019-01-13T23:52:04.918Z\",\n" 
			+ "  \"dbGenDropTable\": \"true\",\n"
			+ "  \"dbHostName\": \"db.mycompany.rds.us-west-2.rds.amazonaws.com\",\n" 
			+ "  \"dbName\": \"ignite\",\n"
			+ "  \"dbPassword\": \"ABDCEDF\",\n" 
			+ "  \"dbUsername\": \"igniteuser\",\n"
			+ "  \"hostName\": \"localhost\",\n" 
			+ "  \"hostPort\": \"6969\",\n" 
			+ "  \"id\": \"0\",\n"
			+ "  \"starterIgniteSecureKey\": \"=W34sdcwdsfwC34=\",\n"
			+ "  \"keySpec\": \"{keyOwner:111, keySource:'session | system'}\",\n" 
			+ "  \"keyVersion\": \"0\",\n"
//			+ "  \"mybatisJava\": \"gen/src/main/java/io/starter/ignite/model/\",\n"
//			+ "  \"mybatisMain\": \"gen/src/\",\n" 
			+ "  \"name\": \"My Microservice API\",\n"
			+ "  \"ownerId\": \"0\",\n"
			+ "  \"schemaData\": \"swagger: '2.0'  info:   description: Starter StackGen API   version: 1.0.0   title: Starter StackGen API    # put the contact info for your development or API team   contact:     email: john@starter.io   license:     name: AGPL 3.0     url: https://www.gnu.org/licenses/agpl-3.0.html   termsOfService: http://stackgen.io/tos/  servers:   url: https://{username}.stackgen.io:{port}/{basePath}   description: The production API server   variables:     username:       # note! no enum here means it is an open value       default: demo       description: this value is assigned by the service provider     port:       enum:         - '8100'       default: '8100'     basePath:       # open meaning there is the opportunity to use special base paths as assigned by the provider, default is `v2`       default: v2        # tags are used for organizing operations tags: - name: admins   description: Secured Admin-only calls - name: users   description: Operations available to regular users  schemes: - https  paths:   /login:     get:       tags:       - 'user'       summary: 'Logs user into the system'       description: ''       operationId: 'login'       produces:       - 'application/xml'       - 'application/json'       parameters:       - name: 'username'         in: 'query'         description: 'The user name for login'         required: true         type: 'string'       - name: 'password'         in: 'query'         description: 'The password for login in clear text'         required: true         type: 'string'       responses:         200:           description: 'successful operation'           schema:             type: 'string'           headers:             X-Rate-Limit:               type: 'integer'               format: 'int32'               description: 'calls per hour allowed by the user'             X-Expires-After:               type: 'string'               format: 'date-time'               description: 'date in UTC when token expires'         400:           description: 'Invalid username/password supplied'   /signup:     get:       tags:       - 'user'       summary: 'Signup a new user'       description: ''       operationId: 'signup'       produces:       - 'application/xml'       - 'application/json'       parameters: []       responses:         default:           description: 'successful operation'             /logout:     get:       tags:       - 'user'       summary: 'Logs out current logged in user session'       description: ''       operationId: 'logoutUser'       produces:       - 'application/xml'       - 'application/json'       parameters: []       responses:         default:           description: 'successful operation'            securityDefinitions:   automator_auth:     type: 'oauth2'     authorizationUrl: ' https://{username}.stackgen.io:{port}/oauth'     flow: 'implicit'     scopes:       write:items: 'modify owned items'       read:items: 'read owned items'   api_key:     type: 'jwt'     name: 'jwt'     in: 'header'      definitions:   Account:     type: 'object'     properties:       balance:         type: 'number'         format: 'double'         description: 'Account balance point in time'         x-starter-dataField: component=AccountBalance       institutionName:         type: 'string'       accountNumber:         type: 'string'         x-starter-secureField: true       routingNumber:         type: 'string'           status:         type: 'string'         description: 'Account Status'         enum:         - 'ready'         - 'closed'         - 'executed'       verified:         type: boolean         default: false     xml:       name: 'Account'          LedgerEntry:     type: 'object'     properties:       accountId:         type: 'integer'         format: 'int64'       orderId:         type: 'integer'         format: 'int64'       entryValue:         type: 'number'         format: 'double'       institutionName:         type: 'string'         x-starter-secureField: true       description:         type: 'string'       accountBalance:         type: 'number'         format: 'double'           status:         type: 'string'         description: 'Transaction Status'         enum:         - 'ready'         - 'closed'         - 'executed'       transactionType:         type: 'string'         description: 'Transaction Type'         enum:         - 'debit'         - 'credit'         - 'deposit'                 - 'void'         - 'refund'       verified:         type: boolean         default: false     xml:       name: 'LedgerEntry'          Order:     type: 'object'     properties:       tradeId:         type: 'integer'         format: 'int64'       quantity:         type: 'integer'         format: 'int32'       executionDate:         type: 'string'         format: 'date-time'       status:         type: 'string'         description: 'Order Status'         enum:         - 'placed'         - 'pending'         - 'approved'         - 'executed'         - 'declined'       complete:         type: boolean         default: false     xml:       name: 'Order'          Content:     type: object     required:     - name     properties:       id:         type: 'integer'         format: 'int64'       name:         type: string         example: Widget Adapter       data:         type: string         example: full content data         maxLength: 100000             authorId:         type: 'integer'         format: 'int64'       releaseDate:         type:  string         format: 'date-time'       organizationId:         type: 'integer'         format: 'int64'     xml:       name: 'Content'              Organization:     required:     - name     properties:       id:         type: 'integer'         format: 'int64'       name:         type: string         example: ACME Corp Inc.       homePage:         type: string         format: url         example:  https://www.acme-corp.com       phone:         type: string         example: 415-867-5309     xml:       name: 'Organization'                   User:     required:     - username     - password     - email     properties:       firstName:         description: first name of person (encrypted)         type: string         example: Karena         x-starter-secureField: true       lastName:         description: last name of person (encrypted)         type: string         example: Jones         x-starter-secureField: true       governmentId:         description: a 10 digit government ID (encrypted)         type: string         example: 1112233334         maxLength: 10         minLength: 10         x-starter-secureField: true       userName:         type: string         example: Sparky       password:         type: string         example: HardToGuess1980         minLength: 10         # encryption handled by authorization code         x-starter-secureField: true, Type=HASHED, strength=5       fingerprint:         type: string         example: DK$DFSJaraDD         x-starter-secureField: true       homePage:         type: string         format: url         example: https://www.acme-corp.com       email:         description: the main email address for the user  (encrypted)         type: string         format: email         example: wiley.coyote@acme-corp.com         x-starter-secureField: true               social:         type: string         format: url         example: https://twitter.com/StackGen       # Spring Security User fields     # isCredentialsNonExpired       credentialNonExpired:         description: Spring Security User field whether the user's login credentials have expired         type: boolean         default: true        # isAccountEnabled       accountEnabled:         description: Spring Security User field whether the user account is enabled         type: boolean         default: true                  # isAccountNonLocked       accountNonLocked:         description: Spring Security User field whether the user account is locked         type: boolean         default: true               # isAccountNonExpired       accountNonExpired:         description: Spring Security User field whether the user account has expired         type: boolean         default: false              # getAuthorities    # TODO: x-starter-dataField: hidden=true       authorities:          description: Spring Security User representation of the granted authority (or <code>null</code> if the granted authority cannot be expressed as a <code>String</code> with sufficient precision).          type: array          items:            type: string           xml:       name: 'User'          Role:     required:     - name     properties:       id:         type: 'integer'         format: 'int64'       name:         description: Name of the Role         type: string         example: Karena       type:         type: string         example: HardToGuess1980         minLength: 10     xml:       name: 'Role'                Privilege:     required:     - name     properties:       id:         type: 'integer'         format: 'int64'       name:         description: Name of the System Privilege         type: string         example: Read Access to Content       type:         description: Name of the System Privilege         type: 'string'         example: 'insert, read, write, delete, execute, all'         enum:         - 'insert'         - 'read'         - 'write'         - 'delete'         - 'execute'         - 'all'     xml:       name: 'Privilege'   WorkFlows:     required:     - description     - json     properties:       id:         type: 'integer'         format: 'int64'       fingerprint:         type: string         example: DK$DFSJaraDD       sourceType:         type: string         example: user       sourceOwner:         type: string         example: userId:d290f1ee-6c54-4b01-90e6-d701748f0851       json:         type: string         example: Sparkyt         description: this value is the JSON content of the WorkFlow definition       xml:         type: string         example: Sparkyt     xml:       name: 'Workflows'   ContactMethod:             type: object     required:     - name     - ticker     properties:       id:         type: 'integer'         format: 'int64'             messageTypeDescription:         type: string         example: twitter handle       messageType:         type: string         example: email         enum:         - 'github'                 - 'email'         - 'twitter'         - 'mobile'         - 'sms'         - 'facebook'       status:         type: 'string'         description: 'Contact Method Status'         enum:         - 'new'         - 'validated'         - 'closed'         - 'DNC'         - 'ADMIN'       complete:         type: boolean         default: false     xml:       name: 'ContactMethod'   AuditLog:     required:     - sourceType     - json     properties:       id:         type: 'integer'         format: 'int64'       fingerprint:         type: string         example: DK$DFSJaraDD         x-starter-dataField: hidden=true       sourceType:         type: string         example: user         hidden: true       sourceOwner:         type: string         example: userId:1000       json:         type: string         example: Sparkyt         maxLength: 100000         xml:       name: 'AuditLog'   Data:     required:     - sourceType     - json     properties:       id:         type: 'integer'         format: 'int64'       fingerprint:         type: string         example: D234234ER       type:         type: string         example: user       json:         type: string         example: Sparkyt         maxLength: 100000         xml:       name: 'Data'   Category:     type: 'object'     properties:       id:         type: 'integer'         format: 'int64'       name:         type: 'string'     xml:       name: 'Category'   Tag:     type: 'object'     properties:       id:         type: 'integer'         format: 'int64'       name:         type: 'string'     xml:       name: 'Tag'  # Special Object for tracking and instantiating OpenAPI Schema instances    Stack:     description: 'Application API Spec'     type: 'object'     required:     - 'name'     properties:       id:         type: 'integer'         format: 'int64'       category:         $ref: '#/definitions/Category'       name:         type: 'string'         example: 'My New Stack'         description: 'name of the service API displayed in the UI'           adminServerHost:         type: 'string'         example: 'www.mycompany.com'         maxLength: 256         description: 'host (FQDN) name of to connect to the admin server -- do not include 'http(s):' or slashes'       adminServerPort:         type: 'string'         example: '8009'         maxLength: 256          description: 'port of to connect to the admin server'               hostName:         type: 'string'         example: 'localhost'         maxLength: 256          description: 'host of this service (FQDN) -- do not include 'http(s):' or slashes'                 hostPort:         type: 'string'         example: '8099'         maxLength: 5          description: 'port of this service'       orgName:         type: 'string'         example: 'Starter Inc.'         maxLength: 256          description: 'the name of the organization owner of the service used in classpath'       gitUser:         type: string         example: StarterInc         maxLength: 256         description: 'The name of the git USER or ORG to push generated stack code'       gitRepo:         type: string         example: MyStackGenApp         maxLength: 256         description: 'YAML Swagger/OpenAPI Schema'               myBatisMain:         type: 'string'         example: 'gen/src/'         maxLength: 256          description: 'relative path to the MyBatis source files'       myBatisJava:         type: 'string'         example: 'gen/src/main/java/io/starter/StackGen/model/'         maxLength: 256          description: 'path for the MyBatis Java model output files'            starterIgniteSecureKey:         type: 'string'         example: '=W34sdcwdsfwC34='         maxLength: 256          description: 'Secure key used to encrypt the data in the new platform'                            dbUrl:         type: 'string'         example: 'jdbc:mysql//db.myco.com'         maxLength: 256          description: 'database url for the system'                 dbName:         type: 'string'         example: 'StackGenApp1'         maxLength: 256          description: 'db name (schema name) for the system - must exist prior to initialization'                 dbUser:         type: 'string'         example: 'igniteuser'         maxLength: 256          description: 'database user for the system'           dbPassword:         type: 'string'         example: 'hard2Gu3ss'         maxLength: 256          description: 'database password for the system'          x-starter-secureField: true       dbGenDropTable:         type: boolean         example: true          description: 'rename and recreate any pre-existing tables during generation'        schemaName:         type: 'string'         example: 'starter'         maxLength: 256          description: 'Name for the Schema'        schemaData:         type: string         example: Sparkyt         maxLength: 1000000         description: 'YAML Swagger/OpenAPI Schema'             artifactId:         type: 'string'         example: 'StackGen'         maxLength: 256          description: 'database for the system'             status:         type: 'string'         description: 'API status as of last check'         enum:         - 'available'         - 'runtime-error'         - 'compilation-error'         - 'locked'     xml:       name: 'ApiSpec'   Item:     description: 'Products or Services'     type: 'object'     required:     - 'name'     - 'photoUrls'     properties:       id:         type: 'integer'         format: 'int64'       category:         $ref: '#/definitions/Category'       name:         type: 'string'         example: 'doggie'         description: 'name of the item displayed in the store'       photoUrls:         type: 'array'         xml:           name: 'photoUrl'           wrapped: true         items:           type: 'string'       tags:         type: 'array'         xml:           name: 'tag'           wrapped: true         items:           $ref: '#/definitions/Tag'       status:         type: 'string'         description: 'item status in the store'         enum:         - 'available'         - 'pending'         - 'sold'     xml:       name: 'Item'   ApiResponse:     type: 'object'     properties:       id:         type: 'integer'         format: 'int64'       code:         type: 'integer'         format: 'int32'       type:         type: 'string'       message:         type: 'string'  externalDocs:   description: 'Learn more about Starter StackGen'   url: 'https://docs.stackgen.io/'\",\n"
			+ "  \"schemaName\": \"HappyPath\",\n" 
			+ "  \"schemaFile\": \"simple_csat.yml\",\n"
			+ "  \"status\": \"available\"\n" + "}";
}
