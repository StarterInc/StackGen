package io.starter.ignite.generator.swagger;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import io.starter.ignite.generator.annotations.StackgenModelProperty;
import io.swagger.models.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;

import io.starter.ignite.generator.StackGenConfigurator;
import io.starter.ignite.generator.DBGen;
import io.starter.ignite.generator.IgniteException;
import io.starter.ignite.generator.MyBatisJoin;
import io.starter.ignite.generator.SwaggerGen;

import io.swagger.codegen.v3.ClientOptInput;
import io.swagger.codegen.v3.CodegenConfig;
import io.swagger.codegen.v3.DefaultGenerator;
import io.swagger.codegen.v3.Generator;
import io.swagger.codegen.v3.ignore.CodegenIgnoreProcessor;

import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.properties.StringProperty;

/**
 * Enhance the swagger Code Generation with Starter StackGen features
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class IgniteGenerator extends DefaultGenerator {

	Logger logger = LoggerFactory.getLogger(IgniteGenerator.class);

	// Starter Enhancements
	private Boolean				generateStarterCRUDOps				= true;
	private Boolean				generateStarterModelEnhancements	= true;
	private List<SwaggerGen>	pluginSwaggers;

	StackGenConfigurator cfg = null; 
	public IgniteGenerator(StackGenConfigurator c) {
		cfg = c;
	}

	IgniteGenerator plugins(List<SwaggerGen> pluginSwaggers) {
		this.setPluginSwaggers(pluginSwaggers);
		return this;
	}

	/**
	 * add any additional custom configs
	 * 
	 */
	public void preprocessSwagger() {
		config.additionalProperties().put("serverPort", cfg.defaultPort);
	}

	@Override
	/**
	 * run the code generator and return the generated source code files
	 */
	public List<File> generate() {

		if (swagger == null || config == null) {
			throw new RuntimeException("missing swagger input or config!");
		}

		configureGeneratorProperties();
		configureSwaggerInfo();

		// TODO: add the model link builder here
		StackModelRelationGenerator relationGenerator = new StackModelRelationGenerator();
		List<MyBatisJoin> joins = relationGenerator.generate(swagger, cfg);

		// add Starter StackGen code
		if (generateStarterModelEnhancements) {
			enhanceSwagger();
		}

		// some static settings
		preprocessSwagger();


		// resolve inline models
		InlineModelResolver inlineModelResolver = new InlineModelResolver();
		inlineModelResolver.flatten(swagger);

		// the generated source code files are returned
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
				.getOrDefault("igniteGenerateModelEnhancements", true)
				.toString());

		generateStarterCRUDOps = Boolean.valueOf(config.additionalProperties()
				.getOrDefault("igniteGenerateCRUDOps", true)
				.toString());
		// end Starter enhancements

		super.configureGeneratorProperties();
	}

	/**
	 * Add the Starter StackGen required enhancements
	 */
	@VisibleForTesting
	public void enhanceSwagger() {
		
		// this.swagger.setBasePath("v1");
		Set<String> keys;
		try {
			keys = this.swagger.getDefinitions().keySet();
		}catch(NullPointerException e) {
			throw new IgniteException("Schema: " + this.swagger.toString() + " malformed definitions section");
		}

		fixReservedWords(this.swagger.getDefinitions());

		Map<String, Path> priorPaths = this.swagger.getPaths();
		this.swagger.setPaths(new HashMap<String, Path>());
		for (String k : keys) {
			Model m = this.swagger.getDefinitions().get(k);

			// put in the ignite fields
			addIgniteFields(m);

			if (generateStarterCRUDOps) { // optionally add the REST apis
				// TODO: see if using the dynamic version is useful
				// String path = this.swagger.getBasePath() + "/" + k;
				String path = k;

				Path existing = this.swagger.getPaths().get(path.toLowerCase());

				// IGNITE_GEN_REST_PATH_PREFIX
				if (StackGenConfigurator.checkReservedWord(k)) { // handle reserved
					
					Path ops = addCrudOps(k, m);
					if (ops != null) {
						this.swagger.getPaths().put( path + "/{id}", ops);
					}
					
					Path opsp = addPost(k, m);
					if(opsp != null) {
						this.swagger.getPaths().put( path, opsp);
					}
					
					Path opsl = addListOp(k, m);
					if (opsl != null) {
						this.swagger.getPaths()
								.put( path + "/list/{searchparam}", opsl);
					}
				}
			}
		}
		if (priorPaths != null) {
			logger.warn("Found existing endpoint paths in Schema. NOT Generating endpoints for: ");
			 for (String f : priorPaths.keySet()) {
				// TODO: fix handling of existing paths
				 // Path px = priorPaths.get(f);
				 // this.swagger.getPaths().put(f, px);

				 logger.warn("Path: " + f);
			 }
		}
	}

	static List<String> reservedList = new ArrayList() {
		{
			add("key");
			add("from");
			add("index");
		}
	};

	private void fixReservedWords(Map<String, Model> definitions) {
		// iterate models and rename reserved words
		for(Model model : definitions.values()){
			if(model.getProperties() != null) {

				// modifying map so avoid concurrent exception
				Collection<Property> vx = new ArrayList<Property>();
				vx.addAll(model.getProperties().values());

				for (Property f : vx) {
					if (reservedList.contains(f.getName())) {
						model.getProperties().remove(f.getName());
						f.setName("_" + f.getName());
						model.getProperties().put(f.getName(), f);
					}
				}
			}
		}

	}

	/**
	 * @return
	 */
	private PathParameter getSearchPathParameter() {
		PathParameter p = new PathParameter();
		p.type("string");
		p.setDefaultValue("0");
		// p.setPattern("string");
		
		p.setName("searchparam"); // k + "Example");
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
		up.setName("id");
		up.setAccess("public");
		up.setDescription("JSON payload (updated or new data)");
		up.setRequired(true);
		up.setSchema(m);
		return up;
	}

	/**
	 * @return
	 */
	private PathParameter getIdPathParameter(String typeDesc) {
		PathParameter p = new PathParameter();
		p.type("integer");
		// p.setMinimum(new BigDecimal(0));

		// p.setName(k + "ID");
		p.setName("id");
		p.setAccess("public");
		p.setDescription(typeDesc + " a single result by ID");
		p.setRequired(true);

		return p;
	}

	private void addSecurity(String k, Operation loadOp) {
		List<String> value;
		value = new ArrayList<String>();
		value.add("read: " + k);
		value.add("write: " + k);
		loadOp.addSecurity("automator_auth", value);
	}

	private Operation createCRUDOp(String opName, String opType, String opDesc, Parameter p) {
		Operation anOp = new Operation();
		anOp.setDescription("StackGen Generated API " + opName + ":"
				+ opType);
		anOp.setSummary(opDesc);
		anOp.setExternalDocs(new ExternalDocs("Documentation for " + opName, "https:/docs.stackgen.io/api/" + opName ));
		
		// 'name' causes dupe method generation
		 anOp.addTag(opType + "-" + opName);

		anOp.operationId(opType);
		anOp.addParameter(p);
		anOp.getVendorExtensions().put("x-tags", "[{tag=" + opType + "}]");
		addSecurity(opDesc, anOp);
		return anOp;
	}

	
	private Path addPost(String k, Model model) {
		Path ops = new Path();

		// POST
		Response r = new Response();
		BodyParameter up = getBodyPathParameter(k, r);
		Operation insertOp = createCRUDOp(k, "insert", "Insert a new " + k
				+ " into the system", up);
		insertOp.addConsumes("application/json");
		insertOp.addProduces("application/json");
		StringProperty p = new StringProperty("application/json");
		r.addHeader("Content-Type", p);
		StringProperty s = new StringProperty("text/plain");
		r.addHeader("Content-Type", s);
		insertOp.response(200, r);
		ops.setPost(insertOp);
		return ops;
	}
	
	/**
	 * Adds Starter StackGen required API CRUD REST endpoints.
	 * 
	 * @param m
	 * @return 
	 */
	private Path addCrudOps(String k, Model model) {
		Path ops = new Path();
		Response r = new Response();
		BodyParameter up = getBodyPathParameter(k, r);

		// PUT
		Operation updateOp = createCRUDOp(k, "update", "Update an existing " + k , getIdPathParameter("Update"));
		up = getBodyPathParameter(k, r);
		updateOp.addParameter(up);
		updateOp.response(200, r);
		ops.setPut(updateOp);

		// DELETE
		Operation deleteOp = createCRUDOp(k, "delete", "Delete an existing " + k
				+ " from the system", getIdPathParameter("Delete"));
		updateOp.response(200, r);
		ops.setDelete(deleteOp);

		// GET
		Operation loadOp = createCRUDOp(k, "load", "Load an existing " + k
				+ " from the system", getIdPathParameter("Load"));
		ComposedModel mxt = new ComposedModel();
		// mxt.setReference(k);
		mxt.setReference("#/definitions/" + k);
		r.setResponseSchema(mxt);
		loadOp.response(200, r);
		ops.setGet(loadOp);
		return ops;
	}

	/**
	 * Adds Starter StackGen required API CRUD REST endpoints.
	 * 
	 * @param m
	 * @param existing 
	 * @return 
	 */
	private Path addListOp(String k, Model model) {
		Path ops = new Path();

		// List createa a new list path
		Operation listOp = new Operation();
		listOp.setDescription("Starter StackGen Auto Generated Listing");
		listOp.setSummary("Retreive a list of " + k + "s from the service");
		// listOp.addTag("list-tag"); // causes dupe method gen
		listOp.operationId("list");

		Response r = new Response();

		ArrayModel m = new ArrayModel();
		ObjectProperty objectProp = new ObjectProperty();
		m.setItems(objectProp);
		m.setReference("#/definitions/" + k);
		r.responseSchema(m);

		listOp.addResponse("200", r);

		// causes attempt to transform lists into strings
		// ArrayModel mx = new ArrayModel();
		// mx.setReference(k);
		// StringProperty p = new StringProperty("#/definitions/" + k);
		// mx.setItems(p);
		// r.setResponseSchema(mx);

		PathParameter px = getSearchPathParameter();
		listOp.addParameter(px);

		// security
		// [{automator_auth=[write:Account, read:Account]}]
		List<String> value = new ArrayList<String>();
		value.add("read: " + k); // + k);
		value.add("write: " + k); // + k);
		listOp.addSecurity("automator_auth", value);

		// vendorExtensions
		// {x-contentType=application/json, x-accepts=application/json, x-tags=[{tag=account}]}
		
		listOp.getVendorExtensions().put("x-contentType", "application/json");
		listOp.getVendorExtensions().put("x-accepts", "application/json");
		listOp.getVendorExtensions().put("x-tags", "[{tag=" + k + "}]");
		// listOp.addTag(k);
		ops.setGet(listOp);

		return ops;
	}

	/**
	 * Adds Starter StackGen required API properties.
	 * 
	 * API properties are added to the Model objects, and reflected in generated 
	 * Database and Persistence classes.
	 * 
	 * @param m
	 */
	private void addIgniteFields(Model m) {
		logger.info("Adding StackGen custom props for: " + m.getTitle());
		// AbstractModel m = (AbstractModel) mx;
		Map<PropertyBuilder.PropertyId, Object> args = new HashMap<PropertyBuilder.PropertyId, Object>();
		// id -- all objects must have id as primary key
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.READ_ONLY, true);
		args.put(PropertyBuilder.PropertyId.TITLE, "Id");
		args.put(PropertyBuilder.PropertyId.ALLOW_EMPTY_VALUE, false);
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "Primary Key for Object (generated column)");
		Property value = PropertyBuilder.build("integer", "int64", args);
		value.setPosition(1);
		value.setName("id");
		value.setAccess(StackgenModelProperty.AccessMode.READ_ONLY.name());
		m.getProperties().put("id", value);
		
		// keyVersion
		args.put(PropertyBuilder.PropertyId.TITLE, "Securefield Key Version");
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The version of the SecureField key used to crypt this row (generated column)");
		args.put(PropertyBuilder.PropertyId.DEFAULT, "1.0");
		value = PropertyBuilder.build("integer", "int64", args);
		m.getProperties().put("keyVersion", value);

		// keySpec
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.TITLE, "Securefield Key Spec");
		args.put(PropertyBuilder.PropertyId.READ_ONLY, true);
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The spec of the SecureField key used to crypt this row (generated column)");
		// args.put(PropertyBuilder.PropertyId.MIN_LENGTH, "200");
		args.put(PropertyBuilder.PropertyId.DEFAULT, "dev");
		args.put(PropertyBuilder.PropertyId.EXAMPLE, "keySource:system");
		value = PropertyBuilder.build("string", "", args);
		m.getProperties().put("keySpec", value);

		// OwnerId
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.TITLE, "StackGen owner id");
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The ID of the user that owns this data (generated column)");
		value = PropertyBuilder.build("integer", "int64", args);
		m.getProperties().put("ownerId", value);

		// CreatedDate
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.TITLE, "Created Date");
		args.put(PropertyBuilder.PropertyId.READ_ONLY, true);
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The created date for this record/object (generated column)");
		value = PropertyBuilder.build("string", "date-time", args);
		m.getProperties().put("createdDate", value);

		// ModifiedDate
		args = new HashMap<PropertyBuilder.PropertyId, Object>();
		args.put(PropertyBuilder.PropertyId.TITLE, "Modified Date");
		args.put(PropertyBuilder.PropertyId.DESCRIPTION, "The last-modified date for this record/object (generated column)");
		value = PropertyBuilder.build("string", "date-time", args);
		m.getProperties().put("modifiedDate", value);

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

	public Swagger getSwagger() {
		return swagger;
	}
	
	@Override
	public Generator opts(ClientOptInput opts) {
		this.opts = opts;
		this.swagger = opts.getSwagger();
		this.config = opts.getConfig();
		this.config.additionalProperties()
				.putAll(opts.getOpts().getProperties());

		String ignoreFileLocation = this.config.getIgnoreFilePathOverride();
		if (ignoreFileLocation != null) {
			final File ignoreFile = new File(ignoreFileLocation);
			if (ignoreFile.exists() && ignoreFile.canRead()) {
				this.ignoreProcessor = new CodegenIgnoreProcessor(ignoreFile);
			} else {
				logger.warn("Ignore file specified at {} is not valid. This will fall back to an existing ignore file if present in the output directory.", ignoreFileLocation);
			}
		}

		if (this.ignoreProcessor == null) {
			this.ignoreProcessor = new CodegenIgnoreProcessor(
					this.config.getOutputDir());
		}

		return this;
	}
    /**
     * Get the template file path with template dir prepended, and use the
     * library template if exists.
     *
     * @param config Codegen config
     * @param templateFile Template file
     * @return String Full template file path
     */
	@Override
    public String getFullTemplateFile(CodegenConfig config, String templateFile) {

        //check the supplied template library folder for the file
        final String library = cfg.getLibrary();
        final String templateDir = cfg.getTemplateDir();
        if (StringUtils.isNotEmpty(library) && StringUtils.isNotEmpty(templateDir)) {
            //look for the file in the library subfolder of the supplied template
			final String libTemplateFile = templateDir + "/libraries/" + library + "/" + templateFile;  // buildLibraryFilePath(config.templateDir(), library, templateFile);
            if (new File(libTemplateFile).exists()) {
                return libTemplateFile;
            }
        }
		//check the supplied template main folder for the file
		final String template = templateDir + File.separator + templateFile;
		if (new File(template).exists()) {
			return template;
		}
        // fallback to default handling
		return super.getFullTemplateFile(config, templateFile);
    }
}