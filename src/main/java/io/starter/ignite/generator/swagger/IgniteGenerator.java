package io.starter.ignite.generator.swagger;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.starter.ignite.generator.Configuration;
import io.starter.ignite.generator.SwaggerGen;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.InlineModelResolver;
import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;

/**
 * Enhance the swagger Code Generation with Starter Ignite features
 * 
 * @author john
 *
 */
public class IgniteGenerator extends DefaultGenerator implements Configuration {

	// Starter Enhancements
	private Boolean				generateStarterCRUDOps				= true;
	private Boolean				generateStarterModelEnhancements	= true;
	private List<SwaggerGen>	pluginSwaggers;

	IgniteGenerator plugins(List<SwaggerGen> pluginSwaggers) {
		this.setPluginSwaggers(pluginSwaggers);
		return this;
	}

	@Override
	public List<File> generate() {

		if (swagger == null || config == null) {
			throw new RuntimeException("missing swagger input or config!");
		}

		configureGeneratorProperties();
		configureSwaggerInfo();

		// add Starter Igmite installs
		if (generateStarterModelEnhancements) {
			enhanceSwagger();
		}

		// resolve inline models
		InlineModelResolver inlineModelResolver = new InlineModelResolver();
		inlineModelResolver.flatten(swagger);

		List<File> files = new ArrayList<File>();

		// models
		List<Object> allModels = new ArrayList<Object>();
		generateModels(files, allModels);

		// apis
		List<Object> allOperations = new ArrayList<Object>();
		generateApis(files, allOperations, allModels);

		// supporting files
		Map<String, Object> bundle = buildSupportFileBundle(allOperations, allModels);
		generateSupportingFiles(files, bundle);
		config.processSwagger(swagger);

		return files;
	}

	@Override
	protected void configureGeneratorProperties() {

		// Starter enhancements
		generateStarterModelEnhancements = Boolean.valueOf(config
				.additionalProperties()
				.getOrDefault("igniteGenerateModelEnhancements", new Boolean(
						true))
				.toString());

		generateStarterCRUDOps = Boolean.valueOf(config.additionalProperties()
				.getOrDefault("igniteGenerateCRUDOps", new Boolean(true))
				.toString());
		// end Starter enhancements

		super.configureGeneratorProperties();
	}

	/**
	 * Add the Starter Ignite required enhancements
	 */
	private void enhanceSwagger() {
		Set<String> keys = this.swagger.getDefinitions().keySet();
		Map<String, Path> priorPaths = this.swagger.getPaths();
		this.swagger.setPaths(new HashMap<String, Path>());
		for (String k : keys) {
			Model m = this.swagger.getDefinitions().get(k);

			// put in the ignite fields
			addIgniteFields(m);

			// optionally add the REST api for crud ops
			if (generateStarterCRUDOps) {
				String path = "/" + k;
				// Path existing =
				// this.swagger.getPaths().get(path.toLowerCase());

				if (!"ApiResponse".equals(k)) { // handle reserved word case(s)
					Path ops = addCrudOps(k, m);
					if (ops != null) {
						this.swagger.getPaths().put(IGNITE_GEN_REST_PATH_PREFIX
								+ path + "/{param}", ops);
					}
					Path opsl = addListOp(k, m);
					if (opsl != null) {
						this.swagger.getPaths().put(IGNITE_GEN_REST_PATH_PREFIX
								+ path + "/list/{param}", opsl);
					}
				}
			}
		}
		// for (String f : priorPaths.keySet()) {
		// Path px = priorPaths.get(f);
		// this.swagger.getPaths().put("logic/" + f, px);
		// }
	}

	/**
	 * @return
	 */
	private PathParameter getSearchPathParameter() {
		PathParameter p = new PathParameter();
		p.type("string");
		p.setDefaultValue("0");
		// p.setPattern("string");
		p.setName("param"); // k + "Example");
		p.setAccess("public");
		p.setDescription("Search example: JSON");
		return p;
	}

	/**
	 * @param m
	 * @return
	 */
	private BodyParameter getBodyPathParameter(String k, Response r) {
		RefModel m = new RefModel();
		m.set$ref("#/definitions/" + k);
		m.setReference(k);
		r.responseSchema(m);
		r.setDescription("Results fetched OK");

		BodyParameter up = new BodyParameter();
		up.setName("body");
		up.setAccess("public");
		up.setDescription("Updated JSON data");
		up.setRequired(true);
		up.setSchema(m);
		return up;
	}

	/**
	 * @return
	 */
	private PathParameter getIdPathParameter() {
		PathParameter p = new PathParameter();
		p.type("integer");
		p.setMinimum(new BigDecimal(0));
		// p.setName(k + "ID");
		p.setName("param");
		p.setAccess("public");
		p.setDescription("Retreive a single result by ID");
		p.setRequired(true);
		return p;
	}

	private void addSecurity(String k, Operation loadOp) {
		List value;
		value = new ArrayList();
		value.add("read: items"); // + k);
		value.add("write: items"); // " + k);
		loadOp.addSecurity("automator_auth", value);
	}

	private Operation createCRUDOp(String opName, String opType, String opDesc, Parameter p) {
		Operation insertOp = new Operation();
		insertOp.setDescription("Starter Ignite Auto Generated " + opName + ":"
				+ opType);
		insertOp.setSummary(opDesc);
		// insertOp.addTag("insert-tag"); insert of alternate tag

		// name causes dupe method gen
		insertOp.addTag(opType);

		insertOp.operationId(opType);
		insertOp.addParameter(p);
		// insertOp.getVendorExtensions().put("x-contentType",
		// "application/json");
		// insertOp.getVendorExtensions().put("x-accepts",
		// "application/json");
		insertOp.getVendorExtensions().put("x-tags", "[{tag=" + opType + "}]");
		addSecurity(opDesc, insertOp);
		return insertOp;
	}

	/**
	 * Adds Starter Ignite required API CRUD REST endpoints.
	 * 
	 * @param m
	 * @return 
	 */
	private Path addCrudOps(String k, Model model) {
		Path ops = new Path();

		// Insert
		Response r = new Response();
		BodyParameter up = getBodyPathParameter(k, r);
		Operation insertOp = createCRUDOp(k, "insert", "Insert a new " + k
				+ " into the system", up);
		insertOp.addConsumes("application/json");
		insertOp.addProduces("application/json");
		insertOp.response(200, r);
		ops.setPost(insertOp);

		// Update
		r = new Response();
		up = getBodyPathParameter(k, r);
		Operation updateOp = createCRUDOp(k, "update", "Update a " + k
				+ " in the system", up);
		updateOp.addConsumes("application/json");
		updateOp.addProduces("text/plain");
		up = getBodyPathParameter(k, r);
		updateOp.addParameter(up);
		updateOp.addConsumes("application/json");
		updateOp.response(200, r);
		ops.setPut(updateOp);

		// Delete
		Operation deleteOp = createCRUDOp(k, "delete", "Delete an " + k
				+ " from the system", getIdPathParameter());
		deleteOp.addConsumes("text/plain");
		deleteOp.addProduces("text/plain");
		updateOp.response(200, r);
		ops.setDelete(deleteOp);

		// Load
		Operation loadOp = createCRUDOp(k, "load", "Load a " + k
				+ " from the system", getIdPathParameter());
		loadOp.addConsumes("text/plain");
		loadOp.addProduces("application/json");
		loadOp.response(200, r);
		ops.setGet(loadOp);
		return ops;
	}

	/**
	 * Adds Starter Ignite required API CRUD REST endpoints.
	 * 
	 * @param m
	 * @param existing 
	 * @return 
	 */
	private Path addListOp(String k, Model model) {
		Path ops = new Path();
		Response r = new Response();

		/*
		 * TODO: Implement List result!
		 * responses:
		 * 200:
		 * description: search results matching criteria
		 * schema:
		 * type: array
		 * items:
		 * $ref: '#/definitions/Data'
		 */
		ArrayModel mx = new ArrayModel();
		ArrayProperty apx = new ArrayProperty();
		// apx.setItems(new Property().description("yo").);

		RefModel m = new RefModel();
		m.set$ref("#/definitions/" + k);
		m.setReference(k);
		r.responseSchema(m); // (Model) apx);

		PathParameter p = getSearchPathParameter();

		// List createa a new list path
		Operation listOp = new Operation();
		listOp.addConsumes("application/xml");
		listOp.addConsumes("application/json");
		listOp.addProduces("application/xml");
		listOp.addProduces("application/json");
		listOp.setDescription("Starter Ignite Auto Generated Listing");
		listOp.setSummary("Listing");
		// listOp.addTag("list-tag"); // causes dupe method gen
		listOp.operationId("list");
		listOp.addParameter(p);
		listOp.addResponse("200", r);

		// security
		// [{automator_auth=[write:Account, read:Account]}]
		List value = new ArrayList();
		value.add("read: items"); // + k);
		listOp.addSecurity("automator_auth", value);
		// vendorExtensions
		// {x-contentType=application/json,
		// x-accepts=application/json, x-tags=[{tag=account}]}
		listOp.getVendorExtensions().put("x-contentType", "application/json");
		listOp.getVendorExtensions().put("x-accepts", "application/json");
		listOp.getVendorExtensions().put("x-tags", "[{tag=" + k + "}]");
		// listOp.addTag(k);
		ops.setGet(listOp);

		return ops;
	}

	/**
	 * Adds Starter Ignite required API properties.
	 * 
	 * API properties are added to the Model objects, and reflected in generated 
	 * Database and Persistence classes.
	 * 
	 * @param m
	 */
	private void addIgniteFields(Model mx) {

		ModelImpl m = (ModelImpl) mx;

		// keyVersion
		Map<PropertyBuilder.PropertyId, Object> args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The version of the SecureField key used to crypt this row (generated)");
		args.put(PropertyBuilder.PropertyId.DEFAULT, "1.0");
		Property value = PropertyBuilder.build("integer", "int64", args);
		m.addProperty("keyVersion", value);

		// keyVersion
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The spec of the SecureField key used to crypt this row (generated)");
		// args.put(PropertyBuilder.PropertyId.MIN_LENGTH, "200");
		args.put(PropertyBuilder.PropertyId.DEFAULT, "dev");
		args.put(PropertyBuilder.PropertyId.EXAMPLE, "{keyOwner:111, keySource:'session | system'}");

		value = PropertyBuilder.build("string", "", args);
		m.addProperty("keyVersion", value);
		// maprops.put("keySpec", value);

		// OwnerId
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The ID of the user that owns this data (generated)");
		value = PropertyBuilder.build("integer", "int64", args);
		m.addProperty("ownerId", value);

		// CreatedDate
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The created date for this record/object (generated)");
		value = PropertyBuilder.build("string", "date-time", args);
		m.addProperty("createdDate", value);

		// ModifiedDate
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The last-modified date for this record/object (generated)");
		value = PropertyBuilder.build("string", "date-time", args);
		m.addProperty("modifiedDate", value);

		// id -- all objects must have id as primary key
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.ALLOW_EMPTY_VALUE, false);
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "Primary Key for Object (generated)");
		value = PropertyBuilder.build("integer", "int64", args);
		m.addProperty("id", value);
	}

	/**
	 * @return the pluginSwaggers
	 */
	public List<SwaggerGen> getPluginSwaggers() {
		return pluginSwaggers;
	}

	/**
	 * @param pluginSwaggers the pluginSwaggers to set
	 */
	public void setPluginSwaggers(List<SwaggerGen> pluginSwaggers) {
		this.pluginSwaggers = pluginSwaggers;
	}

}