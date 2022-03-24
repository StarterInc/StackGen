package io.starter.ignite.generator.swagger.languages;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import io.swagger.codegen.v3.*;
import io.swagger.codegen.v3.ignore.CodegenIgnoreProcessor;
import io.swagger.codegen.v3.templates.TemplateEngine;
import io.swagger.codegen.v3.utils.ImplementationVersion;
import io.swagger.codegen.v3.utils.URLPathUtil;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultGenerator extends AbstractGenerator implements Generator {
    protected final Logger LOGGER = LoggerFactory.getLogger(DefaultGenerator.class);
    protected CodegenConfig config;
    protected ClientOptInput opts;
    protected OpenAPI openAPI;
    protected CodegenIgnoreProcessor ignoreProcessor;
    protected TemplateEngine templateEngine;
    private Boolean generateApis = true;
    private Boolean generateModels = true;
    private Boolean generateSupportingFiles = true;
    private Boolean generateApiTests = true;
    private Boolean generateApiDocumentation = true;
    private Boolean generateModelTests = true;
    private Boolean generateModelDocumentation = true;
    private Boolean generateSwaggerMetadata = true;
    private Boolean useOas2 = false;
    private String basePath;
    private String basePathWithoutHost;
    private String contextPath;
    private Map<String, String> generatorPropertyDefaults = new HashMap();

    public DefaultGenerator() {
    }

    public Generator opts(ClientOptInput opts) {
        this.opts = opts;
        this.openAPI = opts.getOpenAPI();
        this.config = opts.getConfig();
        this.config.additionalProperties().putAll(opts.getOpts().getProperties());
        String ignoreFileLocation = this.config.getIgnoreFilePathOverride();
        if (ignoreFileLocation != null) {
            File ignoreFile = new File(ignoreFileLocation);
            if (ignoreFile.exists() && ignoreFile.canRead()) {
                this.ignoreProcessor = new CodegenIgnoreProcessor(ignoreFile);
            } else {
                this.LOGGER.warn("Ignore file specified at {} is not valid. This will fall back to an existing ignore file if present in the output directory.", ignoreFileLocation);
            }
        }

        if (this.ignoreProcessor == null) {
            this.ignoreProcessor = new CodegenIgnoreProcessor(this.config.getOutputDir());
        }

        return this;
    }

    public void setGenerateSwaggerMetadata(Boolean generateSwaggerMetadata) {
        this.generateSwaggerMetadata = generateSwaggerMetadata;
    }

    public void setGeneratorPropertyDefault(String key, String value) {
        this.generatorPropertyDefaults.put(key, value);
    }

    private Boolean getGeneratorPropertyDefaultSwitch(String key, Boolean defaultValue) {
        String result = null;
        if (this.generatorPropertyDefaults.containsKey(key)) {
            result = (String)this.generatorPropertyDefaults.get(key);
        }

        return result != null ? Boolean.valueOf(result) : defaultValue;
    }

    private String getScheme() {
        String scheme = URLPathUtil.getScheme(this.openAPI, this.config);
        if (StringUtils.isBlank(scheme)) {
            scheme = "https";
        }

        scheme = this.config.escapeText(scheme);
        return scheme;
    }

    private void configureGeneratorProperties() {
        if (System.getProperty("generateApis") != null) {
            this.generateApis = Boolean.valueOf(System.getProperty("generateApis"));
        } else {
            this.generateApis = System.getProperty("apis") != null ? Boolean.TRUE : this.getGeneratorPropertyDefaultSwitch("apis", (Boolean)null);
        }

        if (System.getProperty("generateModels") != null) {
            this.generateModels = Boolean.valueOf(System.getProperty("generateModels"));
        } else {
            this.generateModels = System.getProperty("models") != null ? Boolean.TRUE : this.getGeneratorPropertyDefaultSwitch("models", (Boolean)null);
        }

        String supportingFilesProperty = System.getProperty("supportingFiles");
        if (supportingFilesProperty != null && supportingFilesProperty.equalsIgnoreCase("false")) {
            this.generateSupportingFiles = false;
        } else {
            this.generateSupportingFiles = supportingFilesProperty != null ? Boolean.TRUE : this.getGeneratorPropertyDefaultSwitch("supportingFiles", (Boolean)null);
        }

        if (this.generateApis == null && this.generateModels == null && this.generateSupportingFiles == null) {
            this.generateApis = this.generateModels = this.generateSupportingFiles = true;
        } else {
            if (this.generateApis == null) {
                this.generateApis = false;
            }

            if (this.generateModels == null) {
                this.generateModels = false;
            }

            if (this.generateSupportingFiles == null) {
                this.generateSupportingFiles = false;
            }
        }

        Boolean generateModelTestsOption = this.getCustomOptionBooleanValue("--model-tests");
        if (generateModelTestsOption == null) {
            generateModelTestsOption = System.getProperty("modelTests") != null ? Boolean.valueOf(System.getProperty("modelTests")) : null;
        }

        Boolean generateModelDocsOption = this.getCustomOptionBooleanValue("--model-docs");
        if (generateModelDocsOption == null) {
            generateModelDocsOption = System.getProperty("modelDocs") != null ? Boolean.valueOf(System.getProperty("modelDocs")) : null;
        }

        Boolean generateAPITestsOption = this.getCustomOptionBooleanValue("--api-tests");
        if (generateAPITestsOption == null) {
            generateAPITestsOption = System.getProperty("apiTests") != null ? Boolean.valueOf(System.getProperty("apiTests")) : null;
        }

        Boolean generateAPIDocsOption = this.getCustomOptionBooleanValue("--api-docs");
        if (generateAPIDocsOption == null) {
            generateAPIDocsOption = System.getProperty("apiDocs") != null ? Boolean.valueOf(System.getProperty("apiDocs")) : null;
        }

        Boolean useOas2Option = this.getCustomOptionBooleanValue("--use-oas2");
        this.generateModelTests = generateModelTestsOption != null ? generateModelTestsOption : this.getGeneratorPropertyDefaultSwitch("modelTests", true);
        this.generateModelDocumentation = generateModelDocsOption != null ? generateModelDocsOption : this.getGeneratorPropertyDefaultSwitch("modelDocs", true);
        this.generateApiTests = generateAPITestsOption != null ? generateAPITestsOption : this.getGeneratorPropertyDefaultSwitch("apiTests", true);
        this.generateApiDocumentation = generateAPIDocsOption != null ? generateAPIDocsOption : this.getGeneratorPropertyDefaultSwitch("apiDocs", true);
        this.useOas2 = useOas2Option != null ? useOas2Option : this.getGeneratorPropertyDefaultSwitch("useOas2", false);
        this.config.additionalProperties().put("generateApiTests", this.generateApiTests);
        this.config.additionalProperties().put("generateModelTests", this.generateModelTests);
        this.config.additionalProperties().put("generateApiDocs", this.generateApiDocumentation);
        this.config.additionalProperties().put("generateModelDocs", this.generateModelDocumentation);
        if (useOas2Option != null && !this.config.additionalProperties().containsKey("useOas2")) {
            this.config.additionalProperties().put("useOas2", this.useOas2);
        }

        if (!this.generateApiTests && !this.generateModelTests) {
            this.config.additionalProperties().put("excludeTests", true);
        }

        if (System.getProperty("debugSwagger") != null) {
            Json.prettyPrint(this.openAPI);
        }

        this.config.processOpts();
        this.config.preprocessOpenAPI(this.openAPI);
        this.config.additionalProperties().put("generatorVersion", ImplementationVersion.read());
        this.config.additionalProperties().put("generatedDate", ZonedDateTime.now().toString());
        this.config.additionalProperties().put("generatedYear", String.valueOf(ZonedDateTime.now().getYear()));
        this.config.additionalProperties().put("generatorClass", this.config.getClass().getName());
        this.config.additionalProperties().put("inputSpec", this.config.getInputSpec());
        if (this.openAPI.getExtensions() != null) {
            this.config.vendorExtensions().putAll(this.openAPI.getExtensions());
        }

        this.templateEngine = this.config.getTemplateEngine();
        URL url = URLPathUtil.getServerURL(this.openAPI, this.config);
        this.contextPath = this.config.escapeText(url == null ? "" : url.getPath());
        this.basePath = this.config.escapeText(URLPathUtil.getHost(this.openAPI));
        this.basePathWithoutHost = this.config.escapeText(this.contextPath);
    }

    void configureSwaggerInfo() {
        Info info = this.openAPI.getInfo();
        if (info != null) {
            if (info.getTitle() != null) {
                this.config.additionalProperties().put("appName", this.config.escapeText(info.getTitle()));
            }

            if (info.getVersion() != null) {
                this.config.additionalProperties().put("appVersion", this.config.escapeText(info.getVersion()));
            } else {
                this.LOGGER.error("Missing required field info version. Default appVersion set to 1.0.0");
                this.config.additionalProperties().put("appVersion", "1.0.0");
            }

            if (StringUtils.isEmpty(info.getDescription())) {
                this.config.additionalProperties().put("appDescription", "No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)");
                this.config.additionalProperties().put("unescapedAppDescription", "No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)");
            } else {
                this.config.additionalProperties().put("appDescription", this.config.escapeText(info.getDescription()));
                this.config.additionalProperties().put("unescapedAppDescription", info.getDescription());
            }

            if (info.getContact() != null) {
                Contact contact = info.getContact();
                if (contact.getEmail() != null) {
                    this.config.additionalProperties().put("infoEmail", this.config.escapeText(contact.getEmail()));
                }

                if (contact.getName() != null) {
                    this.config.additionalProperties().put("infoName", this.config.escapeText(contact.getName()));
                }

                if (contact.getUrl() != null) {
                    this.config.additionalProperties().put("infoUrl", this.config.escapeText(contact.getUrl()));
                }
            }

            if (info.getLicense() != null) {
                License license = info.getLicense();
                if (license.getName() != null) {
                    this.config.additionalProperties().put("licenseInfo", this.config.escapeText(license.getName()));
                }

                if (license.getUrl() != null) {
                    this.config.additionalProperties().put("licenseUrl", this.config.escapeText(license.getUrl()));
                }
            }

            if (info.getVersion() != null) {
                this.config.additionalProperties().put("version", this.config.escapeText(info.getVersion()));
            } else {
                this.LOGGER.error("Missing required field info version. Default version set to 1.0.0");
                this.config.additionalProperties().put("version", "1.0.0");
            }

            if (info.getTermsOfService() != null) {
                this.config.additionalProperties().put("termsOfService", this.config.escapeText(info.getTermsOfService()));
            }

        }
    }

    void generateModelTests(List<File> files, Map<String, Object> models, String modelName) throws IOException {
        Iterator var4 = this.config.modelTestTemplateFiles().keySet().iterator();

        while(var4.hasNext()) {
            String templateName = (String)var4.next();
            String suffix = (String)this.config.modelTestTemplateFiles().get(templateName);
            String filename = this.config.modelTestFileFolder() + File.separator + this.config.toModelTestFilename(modelName) + suffix;
            if ((new File(filename)).exists()) {
                this.LOGGER.info("File exists. Skipped overwriting " + filename);
            } else {
                File written = this.processTemplateToFile(models, templateName, filename);
                if (written != null) {
                    files.add(written);
                }
            }
        }

    }

    void generateModelDocumentation(List<File> files, Map<String, Object> models, String modelName) throws IOException {
        Iterator var4 = this.config.modelDocTemplateFiles().keySet().iterator();

        while(var4.hasNext()) {
            String templateName = (String)var4.next();
            String suffix = (String)this.config.modelDocTemplateFiles().get(templateName);
            String filename = this.config.modelDocFileFolder() + File.separator + this.config.toModelDocFilename(modelName) + suffix;
            if (!this.config.shouldOverwrite(filename)) {
                this.LOGGER.info("Skipped overwriting " + filename);
            } else {
                File written = this.processTemplateToFile(models, templateName, filename);
                if (written != null) {
                    files.add(written);
                }
            }
        }

    }

    void generateModels(List<File> files, List<Object> allModels) {
        if (this.generateModels) {
            Map<String, Schema> schemas = this.openAPI.getComponents().getSchemas();
            if (schemas != null) {
                String modelNames = System.getProperty("models");
                Set<String> modelsToGenerate = null;
                if (modelNames != null && !modelNames.isEmpty()) {
                    modelsToGenerate = new HashSet(Arrays.asList(modelNames.split(",")));
                }

                Set<String> modelKeys = schemas.keySet();
                Iterator var8;
                String name;
                if (modelsToGenerate != null && !modelsToGenerate.isEmpty()) {
                    Set<String> updatedKeys = new HashSet();
                    var8 = ((Set)modelKeys).iterator();

                    while(var8.hasNext()) {
                        name = (String)var8.next();
                        if (modelsToGenerate.contains(name)) {
                            updatedKeys.add(name);
                        }
                    }

                    modelKeys = updatedKeys;
                }

                Map<String, Object> allProcessedModels = new TreeMap(new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        return ObjectUtils.compare(DefaultGenerator.this.config.toModelName(o1), DefaultGenerator.this.config.toModelName(o2));
                    }
                });
                var8 = ((Set)modelKeys).iterator();

                Map models;
                while(var8.hasNext()) {
                    name = (String)var8.next();

                    try {
                        if (!this.config.getIgnoreImportMapping() && this.config.importMapping().containsKey(name)) {
                            this.LOGGER.info("Model " + name + " not imported due to import mapping");
                        } else {
                            Schema schema = (Schema)schemas.get(name);
                            Map<String, Schema> schemaMap = new HashMap();
                            schemaMap.put(name, schema);
                            models = this.processModels(this.config, schemaMap, schemas);
                            models.put("classname", this.config.toModelName(name));
                            models.putAll(this.config.additionalProperties());
                            allProcessedModels.put(name, models);
                            List<Object> modelList = (List)models.get("models");
                            if (modelList != null && modelList.isEmpty()) {
                            }
                        }
                    } catch (Exception var20) {
                        throw new RuntimeException("Could not process model '" + name + "'.Please make sure that your schema is correct!", var20);
                    }
                }

                ISchemaHandler schemaHandler = this.config.getSchemaHandler();
                schemaHandler.readProcessedModels(allProcessedModels);
                List<CodegenModel> composedModels = schemaHandler.getModels();
                Iterator var25;
                if (composedModels != null && !composedModels.isEmpty()) {
                    var25 = composedModels.iterator();

                    while(var25.hasNext()) {
                        CodegenModel composedModel = (CodegenModel)var25.next();
                        models = this.processModel(composedModel, this.config, schemas);
                        models.put("classname", this.config.toModelName(composedModel.name));
                        models.putAll(this.config.additionalProperties());
                        allProcessedModels.put(composedModel.name, models);
                    }
                }

                Map<String, Object> allProcessedModels2 = this.config.postProcessAllModels(allProcessedModels);
                var25 = allProcessedModels2.keySet().iterator();

                while(var25.hasNext()) {
                    String modelName = (String)var25.next();
                    models = (Map)allProcessedModels2.get(modelName);

                    try {
                        if (this.config.getIgnoreImportMapping() || !this.config.importMapping().containsKey(modelName)) {
                            Map<String, Object> modelTemplate = (Map)((List)models.get("models")).get(0);
                            if (this.config.checkAliasModel() && modelTemplate != null && modelTemplate.containsKey("model")) {
                                CodegenModel codegenModel = (CodegenModel)modelTemplate.get("model");
                                Map<String, Object> vendorExtensions = codegenModel.getVendorExtensions();
                                boolean isAlias = false;
                                if (vendorExtensions.get("x-is-alias") != null) {
                                    isAlias = Boolean.parseBoolean(vendorExtensions.get("x-is-alias").toString());
                                }

                                if (isAlias) {
                                    continue;
                                }
                            }

                            allModels.add(modelTemplate);
                            Iterator var29 = this.config.modelTemplateFiles().keySet().iterator();

                            while(var29.hasNext()) {
                                String templateName = (String)var29.next();
                                String suffix = (String)this.config.modelTemplateFiles().get(templateName);
                                String filename = this.config.modelFileFolder() + File.separator + this.config.toModelFilename(modelName) + suffix;
                                if (!this.config.shouldOverwrite(filename)) {
                                    this.LOGGER.info("Skipped overwriting " + filename);
                                } else {
                                    File written = this.processTemplateToFile(models, templateName, filename);
                                    if (written != null) {
                                        files.add(written);
                                    }
                                }
                            }

                            if (this.generateModelTests) {
                                this.generateModelTests(files, models, modelName);
                            }

                            if (this.generateModelDocumentation) {
                                this.generateModelDocumentation(files, models, modelName);
                            }
                        }
                    } catch (Exception var19) {
                        throw new RuntimeException("Could not generate model '" + modelName + "'", var19);
                    }
                }

                if (System.getProperty("debugModels") != null) {
                    this.LOGGER.info("############ Model info ############");
                    Json.prettyPrint(allModels);
                }

            }
        }
    }

    void generateApis(List<File> files, List<Object> allOperations, List<Object> allModels) {
        if (this.generateApis) {
            boolean hasModel = true;
            if (allModels == null || allModels.isEmpty()) {
                hasModel = false;
            }

            Map<String, List<CodegenOperation>> paths = this.processPaths(this.openAPI.getPaths());
            Set<String> apisToGenerate = null;
            String apiNames = System.getProperty("apis");
            if (apiNames != null && !apiNames.isEmpty()) {
                apisToGenerate = new HashSet(Arrays.asList(apiNames.split(",")));
            }

            if (apisToGenerate != null && !apisToGenerate.isEmpty()) {
                Map<String, List<CodegenOperation>> updatedPaths = new TreeMap();
                Iterator var9 = ((Map)paths).keySet().iterator();

                while(var9.hasNext()) {
                    String m = (String)var9.next();
                    if (apisToGenerate.contains(m)) {
                        updatedPaths.put(m, (List<CodegenOperation>) ((Map)paths).get(m));
                    }
                }

                paths = updatedPaths;
            }

            Iterator var18 = ((Map)paths).keySet().iterator();

            label121:
            while(var18.hasNext()) {
                String tag = (String)var18.next();

                try {
                    List<CodegenOperation> ops = (List)((Map)paths).get(tag);
                    Collections.sort(ops, new Comparator<CodegenOperation>() {
                        public int compare(CodegenOperation one, CodegenOperation another) {
                            return ObjectUtils.compare(one.operationId, another.operationId);
                        }
                    });
                    Map<String, Object> operation = this.processOperations(this.config, tag, ops, allModels);
                    this.processSecurityProperties(operation);
                    operation.put("basePath", this.basePath);
                    operation.put("basePathWithoutHost", this.basePathWithoutHost);
                    operation.put("contextPath", this.contextPath);
                    operation.put("baseName", tag);
                    operation.put("modelPackage", this.config.modelPackage());
                    operation.putAll(this.config.additionalProperties());
                    operation.put("classname", this.config.toApiName(tag));
                    operation.put("classVarName", this.config.toApiVarName(tag));
                    operation.put("importPath", this.config.toApiImport(tag));
                    operation.put("classFilename", this.config.toApiFilename(tag));
                    if (!this.config.vendorExtensions().isEmpty()) {
                        operation.put("vendorExtensions", this.config.vendorExtensions());
                    }

                    boolean sortParamsByRequiredFlag = true;
                    if (this.config.additionalProperties().containsKey("sortParamsByRequiredFlag")) {
                        sortParamsByRequiredFlag = Boolean.valueOf(this.config.additionalProperties().get("sortParamsByRequiredFlag").toString());
                    }

                    operation.put("sortParamsByRequiredFlag", sortParamsByRequiredFlag);
                    operation.put("hasModel", hasModel);
                    allOperations.add(new HashMap(operation));

                    for(int i = 0; i < allOperations.size(); ++i) {
                        Map<String, Object> oo = (Map)allOperations.get(i);
                        if (i < allOperations.size() - 1) {
                            oo.put("hasMore", "true");
                        }
                    }

                    Iterator var21 = this.config.apiTemplateFiles().keySet().iterator();

                    while(true) {
                        String filename;
                        File written;
                        String templateName;
                        while(var21.hasNext()) {
                            templateName = (String)var21.next();
                            filename = this.config.apiFilename(templateName, tag);
                            if (!this.config.shouldOverwrite(filename) && (new File(filename)).exists()) {
                                this.LOGGER.info("Skipped overwriting " + filename);
                            } else {
                                written = this.processTemplateToFile(operation, templateName, filename);
                                if (written != null) {
                                    files.add(written);
                                }
                            }
                        }

                        if (this.generateApiTests) {
                            var21 = this.config.apiTestTemplateFiles().keySet().iterator();

                            while(var21.hasNext()) {
                                templateName = (String)var21.next();
                                filename = this.config.apiTestFilename(templateName, tag);
                                if ((new File(filename)).exists()) {
                                    this.LOGGER.info("File exists. Skipped overwriting " + filename);
                                } else {
                                    written = this.processTemplateToFile(operation, templateName, filename);
                                    if (written != null) {
                                        files.add(written);
                                    }
                                }
                            }
                        }

                        if (!this.generateApiDocumentation) {
                            break;
                        }

                        var21 = this.config.apiDocTemplateFiles().keySet().iterator();

                        while(true) {
                            while(true) {
                                if (!var21.hasNext()) {
                                    continue label121;
                                }

                                templateName = (String)var21.next();
                                filename = this.config.apiDocFilename(templateName, tag);
                                if (!this.config.shouldOverwrite(filename) && (new File(filename)).exists()) {
                                    this.LOGGER.info("Skipped overwriting " + filename);
                                } else {
                                    written = this.processTemplateToFile(operation, templateName, filename);
                                    if (written != null) {
                                        files.add(written);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception var17) {
                    throw new RuntimeException("Could not generate api file for '" + tag + "'", var17);
                }
            }

            if (System.getProperty("debugOperations") != null) {
                this.LOGGER.info("############ Operation info ############");
                Json.prettyPrint(allOperations);
            }

        }
    }

    void generateSupportingFiles(List<File> files, Map<String, Object> bundle) {
        if (this.generateSupportingFiles) {
            Set<String> supportingFilesToGenerate = null;
            String supportingFiles = System.getProperty("supportingFiles");
            boolean generateAll = false;
            if (supportingFiles != null && supportingFiles.equalsIgnoreCase("true")) {
                generateAll = true;
            } else if (supportingFiles != null && !supportingFiles.isEmpty()) {
                supportingFilesToGenerate = new HashSet(Arrays.asList(supportingFiles.split(",")));
            }

            Iterator var6 = this.config.supportingFiles().iterator();

            String outputFilename;
            while(var6.hasNext()) {
                SupportingFile support = (SupportingFile)var6.next();

                try {
                    String outputFolder = this.config.outputFolder();
                    if (StringUtils.isNotEmpty(support.folder)) {
                        outputFolder = outputFolder + File.separator + support.folder;
                    }

                    File of = new File(outputFolder);
                    if (!of.isDirectory()) {
                        of.mkdirs();
                    }

                    outputFilename = outputFolder + File.separator + support.destinationFilename.replace('/', File.separatorChar);
                    if (!this.config.shouldOverwrite(outputFilename)) {
                        this.LOGGER.info("Skipped overwriting " + outputFilename);
                    } else {
                        String templateFile;
                        if (support instanceof GlobalSupportingFile) {
                            templateFile = this.config.getCommonTemplateDir() + File.separator + support.templateFile;
                        } else {
                            templateFile = this.getFullTemplateFile(this.config, support.templateFile);
                        }

                        boolean shouldGenerate = true;
                        if (!generateAll && supportingFilesToGenerate != null && !supportingFilesToGenerate.isEmpty()) {
                            shouldGenerate = supportingFilesToGenerate.contains(support.destinationFilename);
                        }

                        if (shouldGenerate) {
                            if (this.ignoreProcessor.allowsFile(new File(outputFilename))) {
                                if (templateFile.endsWith("mustache")) {
                                    String rendered = this.templateEngine.getRendered(templateFile, bundle);
                                    this.writeToFile(outputFilename, rendered);
                                    files.add(new File(outputFilename));
                                } else {
                                    Object in = null;

                                    try {
                                        in = new FileInputStream(templateFile);
                                    } catch (Exception var18) {
                                    }

                                    if (in == null) {
                                        in = this.getClass().getClassLoader().getResourceAsStream(this.getCPResourcePath(templateFile));
                                    }

                                    File outputFile = new File(outputFilename);
                                    OutputStream out = new FileOutputStream(outputFile, false);
                                    if (in != null) {
                                        this.LOGGER.info("writing file " + outputFile);
                                        IOUtils.copy((InputStream)in, out);
                                        out.close();
                                    } else {
                                        this.LOGGER.warn("can't open " + templateFile + " for input");
                                    }

                                    files.add(outputFile);
                                }
                            } else {
                                this.LOGGER.info("Skipped generation of " + outputFilename + " due to rule in .swagger-codegen-ignore");
                            }
                        }
                    }
                } catch (Exception var19) {
                    throw new RuntimeException("Could not generate supporting file '" + support + "'", var19);
                }
            }

            String swaggerCodegenIgnore = ".swagger-codegen-ignore";
            String ignoreFileNameTarget = this.config.outputFolder() + File.separator + ".swagger-codegen-ignore";
            File ignoreFile = new File(ignoreFileNameTarget);
            String swaggerVersionMetadata;
            if (this.generateSwaggerMetadata && !ignoreFile.exists()) {
                swaggerVersionMetadata = File.separator + this.config.getCommonTemplateDir() + File.separator + ".swagger-codegen-ignore";
                outputFilename = this.readResourceContents(swaggerVersionMetadata);

                try {
                    this.writeToFile(ignoreFileNameTarget, outputFilename);
                } catch (IOException var17) {
                    throw new RuntimeException("Could not generate supporting file '.swagger-codegen-ignore'", var17);
                }

                files.add(ignoreFile);
            }

            if (this.generateSwaggerMetadata) {
                swaggerVersionMetadata = this.config.outputFolder() + File.separator + ".swagger-codegen" + File.separator + "VERSION";
                File swaggerVersionMetadataFile = new File(swaggerVersionMetadata);

                try {
                    this.writeToFile(swaggerVersionMetadata, ImplementationVersion.read());
                    files.add(swaggerVersionMetadataFile);
                } catch (IOException var16) {
                    throw new RuntimeException("Could not generate supporting file '" + swaggerVersionMetadata + "'", var16);
                }
            }

        }
    }

    Map<String, Object> buildSupportFileBundle(List<Object> allOperations, List<Object> allModels) {
        Map<String, Object> bundle = new HashMap();
        bundle.putAll(this.config.additionalProperties());
        bundle.put("apiPackage", this.config.apiPackage());
        Map<String, Object> apis = new HashMap();
        apis.put("apis", allOperations);
        URL url = URLPathUtil.getServerURL(this.openAPI, this.config);
        if (url != null) {
            bundle.put("host", url.getHost());
        }

        bundle.put("openAPI", this.openAPI);
        bundle.put("basePath", this.basePath);
        bundle.put("basePathWithoutHost", this.basePathWithoutHost);
        bundle.put("scheme", URLPathUtil.getScheme(this.openAPI, this.config));
        bundle.put("contextPath", this.contextPath);
        bundle.put("apiInfo", apis);
        bundle.put("models", allModels);
        boolean hasModel = true;
        if (allModels == null || allModels.isEmpty()) {
            hasModel = false;
        }

        bundle.put("hasModel", hasModel);
        bundle.put("apiFolder", this.config.apiPackage().replace('.', File.separatorChar));
        bundle.put("modelPackage", this.config.modelPackage());
        this.processSecurityProperties(bundle);
        if (this.openAPI.getExternalDocs() != null) {
            bundle.put("externalDocs", this.openAPI.getExternalDocs());
        }

        for(int i = 0; i < allModels.size() - 1; ++i) {
            HashMap<String, CodegenModel> cm = (HashMap)allModels.get(i);
            CodegenModel m = (CodegenModel)cm.get("model");
            m.getVendorExtensions().put("x-has-more-models", Boolean.TRUE);
        }

        this.config.postProcessSupportingFileData(bundle);
        if (System.getProperty("debugSupportingFiles") != null) {
            this.LOGGER.info("############ Supporting file info ############");
            Json.prettyPrint(bundle);
        }

        return bundle;
    }

    public List<File> generate() {
        if (this.openAPI == null) {
            throw new RuntimeException("missing OpenAPI input!");
        } else if (this.config == null) {
            throw new RuntimeException("missing configuration input!");
        } else {
            this.configureGeneratorProperties();
            this.configureSwaggerInfo();
            List<File> files = new ArrayList();
            List<Object> allModels = new ArrayList();
            this.generateModels(files, allModels);
            List<Object> allOperations = new ArrayList();
            this.generateApis(files, allOperations, allModels);
            Map<String, Object> bundle = this.buildSupportFileBundle(allOperations, allModels);
            this.generateSupportingFiles(files, bundle);
            this.config.processOpenAPI(this.openAPI);
            return files;
        }
    }

    private File processTemplateToFile(Map<String, Object> templateData, String templateName, String outputFilename) throws IOException {
        String adjustedOutputFilename = outputFilename.replaceAll("//", "/").replace('/', File.separatorChar);
        if (this.ignoreProcessor.allowsFile(new File(adjustedOutputFilename))) {
            String templateFile = this.getFullTemplateFile(this.config, templateName);
            String rendered = this.templateEngine.getRendered(templateFile, templateData);
            this.writeToFile(adjustedOutputFilename, rendered);
            return new File(adjustedOutputFilename);
        } else {
            this.LOGGER.info("Skipped generation of " + adjustedOutputFilename + " due to rule in .swagger-codegen-ignore");
            return null;
        }
    }

    private static void processMimeTypes(List<String> mimeTypeList, Map<String, Object> operation, String source) {
        if (mimeTypeList != null && !mimeTypeList.isEmpty()) {
            List<Map<String, String>> c = new ArrayList();
            int count = 0;

            HashMap mediaType;
            for(Iterator var5 = mimeTypeList.iterator(); var5.hasNext(); c.add(mediaType)) {
                String key = (String)var5.next();
                mediaType = new HashMap();
                mediaType.put("mediaType", key);
                ++count;
                if (count < mimeTypeList.size()) {
                    mediaType.put("hasMore", "true");
                } else {
                    mediaType.put("hasMore", (Object)null);
                }
            }

            operation.put(source, c);
            String flagFieldName = "has" + source.substring(0, 1).toUpperCase() + source.substring(1);
            operation.put(flagFieldName, true);
        }
    }

    public Map<String, List<CodegenOperation>> processPaths(Paths paths) {
        Map<String, List<CodegenOperation>> ops = new TreeMap();
        Iterator var3 = paths.keySet().iterator();

        while(var3.hasNext()) {
            String resourcePath = (String)var3.next();
            PathItem path = (PathItem)paths.get(resourcePath);
            this.processOperation(resourcePath, "get", path.getGet(), ops, path);
            this.processOperation(resourcePath, "head", path.getHead(), ops, path);
            this.processOperation(resourcePath, "put", path.getPut(), ops, path);
            this.processOperation(resourcePath, "post", path.getPost(), ops, path);
            this.processOperation(resourcePath, "delete", path.getDelete(), ops, path);
            this.processOperation(resourcePath, "patch", path.getPatch(), ops, path);
            this.processOperation(resourcePath, "options", path.getOptions(), ops, path);
        }

        return ops;
    }

    private void processOperation(String resourcePath, String httpMethod, Operation operation, Map<String, List<CodegenOperation>> operations, PathItem path) {
        if (operation != null) {
            if (System.getProperty("debugOperations") != null) {
                this.LOGGER.info("processOperation: resourcePath= " + resourcePath + "\t;" + httpMethod + " " + operation + "\n");
            }

            List<Tag> tags = new ArrayList();
            List<String> tagNames = operation.getTags();
            List<Tag> swaggerTags = this.openAPI.getTags();
            if (tagNames != null) {
                Iterator var9;
                String tagName;
                if (swaggerTags == null) {
                    var9 = tagNames.iterator();

                    while(var9.hasNext()) {
                        tagName = (String)var9.next();
                        tags.add((new Tag()).name(tagName));
                    }
                } else {
                    var9 = tagNames.iterator();

                    while(var9.hasNext()) {
                        tagName = (String)var9.next();
                        boolean foundTag = false;
                        Iterator var12 = swaggerTags.iterator();

                        while(var12.hasNext()) {
                            Tag tag = (Tag)var12.next();
                            if (tag.getName().equals(tagName)) {
                                tags.add(tag);
                                foundTag = true;
                                break;
                            }
                        }

                        if (!foundTag) {
                            tags.add((new Tag()).name(tagName));
                        }
                    }
                }
            }

            if (tags.isEmpty()) {
                tags.add((new Tag()).name("default"));
            }

            Set<String> operationParameters = new HashSet();
            Iterator var20;
            Parameter parameter;
            if (operation.getParameters() != null) {
                var20 = operation.getParameters().iterator();

                while(var20.hasNext()) {
                    parameter = (Parameter)var20.next();
                    operationParameters.add(generateParameterId(parameter));
                }
            }

            if (path.getParameters() != null) {
                var20 = path.getParameters().iterator();

                while(var20.hasNext()) {
                    parameter = (Parameter)var20.next();
                    if (!operationParameters.contains(generateParameterId(parameter)) && operation.getParameters() != null) {
                        operation.getParameters().add(parameter);
                    }
                }
            }

            Map<String, Schema> schemas = this.openAPI.getComponents() != null ? this.openAPI.getComponents().getSchemas() : null;
            Map<String, SecurityScheme> securitySchemes = this.openAPI.getComponents() != null ? this.openAPI.getComponents().getSecuritySchemes() : null;
            List<SecurityRequirement> globalSecurities = this.openAPI.getSecurity();
            Iterator var25 = tags.iterator();

            while(var25.hasNext()) {
                Tag tag = (Tag)var25.next();

                try {
                    CodegenOperation codegenOperation = this.config.fromOperation(resourcePath, httpMethod, operation, schemas, this.openAPI);
                    codegenOperation.tags = new ArrayList(tags);
                    this.config.addOperationToGroup(this.config.sanitizeTag(tag.getName()), resourcePath, operation, codegenOperation, operations);
                    List<SecurityRequirement> securities = operation.getSecurity();
                    if (securities == null || !securities.isEmpty()) {
                        Map<String, SecurityScheme> authMethods = this.getAuthMethods(securities, securitySchemes);
                        if (authMethods == null || authMethods.isEmpty()) {
                            authMethods = this.getAuthMethods(globalSecurities, securitySchemes);
                        }

                        if (authMethods != null && !authMethods.isEmpty()) {
                            codegenOperation.authMethods = this.config.fromSecurity(authMethods);
                            codegenOperation.getVendorExtensions().put("x-has-auth-methods", Boolean.TRUE);
                        }
                    }
                } catch (Exception var18) {
                    String msg = "Could not process operation: " + tag + "\n  Operation: " + operation.getOperationId() + "\n  Resource: " + httpMethod + " " + resourcePath + "\n  Exception: " + var18.getMessage();
                    throw new RuntimeException(msg, var18);
                }
            }

        }
    }

    static String generateParameterId(Parameter parameter) {
        return parameter.getName() + ":" + parameter.getIn();
    }

    private Map<String, Object> processOperations(CodegenConfig config, String tag, List<CodegenOperation> ops, List<Object> allModels) {
        Map<String, Object> operations = new HashMap();
        Map<String, Object> objs = new HashMap();
        objs.put("classname", config.toApiName(tag));
        objs.put("pathPrefix", config.toApiVarName(tag));
        Set<String> opIds = new HashSet();
        int counter = 0;

        String opId;
        for(Iterator var9 = ops.iterator(); var9.hasNext(); opIds.add(opId)) {
            CodegenOperation op = (CodegenOperation)var9.next();
            opId = op.nickname;
            if (opIds.contains(opId)) {
                ++counter;
                op.nickname = op.nickname + "_" + counter;
            }
        }

        objs.put("operation", ops);
        operations.put("operations", objs);
        operations.put("package", config.apiPackage());
        Set<String> allImports = new TreeSet();
        Iterator var16 = ops.iterator();

        while(var16.hasNext()) {
            CodegenOperation op = (CodegenOperation)var16.next();
            allImports.addAll(op.imports);
        }

        List<Map<String, String>> imports = new ArrayList();
        Iterator var19 = allImports.iterator();

        while(var19.hasNext()) {
            String nextImport = (String)var19.next();
            Map<String, String> im = new LinkedHashMap();
            String mapping = (String)config.importMapping().get(nextImport);
            if (mapping == null) {
                mapping = config.toModelImport(nextImport);
            }

            if (mapping != null) {
                im.put("import", mapping);
                imports.add(im);
            }
        }

        operations.put("imports", imports);
        if (imports.size() > 0) {
            operations.put("hasImport", true);
        }

        config.postProcessOperations(operations);
        config.postProcessOperationsWithModels(operations, allModels);
        if (objs.size() > 0) {
            List<CodegenOperation> os = (List)objs.get("operation");
            if (os != null && os.size() > 0) {
                CodegenOperation op = (CodegenOperation)os.get(os.size() - 1);
                op.getVendorExtensions().put("x-has-more", Boolean.FALSE);
            }
        }

        return operations;
    }

    private Map<String, Object> processModels(CodegenConfig config, Map<String, Schema> definitions, Map<String, Schema> allDefinitions) {
        Map<String, Object> objs = new HashMap();
        objs.put("package", config.modelPackage());
        List<Object> models = new ArrayList();
        Set<String> allImports = new LinkedHashSet();
        Iterator var7 = definitions.keySet().iterator();

        HashMap item;
        while(var7.hasNext()) {
            String key = (String)var7.next();
            Schema schema = (Schema)definitions.get(key);
            CodegenModel cm = config.fromModel(key, schema, allDefinitions);
            item = new HashMap();
            item.put("model", cm);
            item.put("schema", schema);
            item.put("importPath", config.toModelImport(cm.classname));
            models.add(item);
            allImports.addAll(cm.imports);
        }

        objs.put("models", models);
        Set<String> importSet = new TreeSet();
        Iterator var13 = allImports.iterator();

        String mapping;
        while(var13.hasNext()) {
            String nextImport = (String)var13.next();
            mapping = (String)config.importMapping().get(nextImport);
            if (mapping == null) {
                mapping = config.toModelImport(nextImport);
            }

            if (mapping != null && !config.defaultIncludes().contains(mapping)) {
                importSet.add(mapping);
            }

            mapping = (String)config.instantiationTypes().get(nextImport);
            if (mapping != null && !config.defaultIncludes().contains(mapping)) {
                importSet.add(mapping);
            }
        }

        List<Map<String, String>> imports = new ArrayList();
        Iterator var16 = importSet.iterator();

        while(var16.hasNext()) {
            mapping = (String)var16.next();
            item = new HashMap();
            item.put("import", mapping);
            imports.add(item);
        }

        objs.put("imports", imports);
        config.postProcessModels(objs);
        return objs;
    }

    private Map<String, Object> processModel(CodegenModel codegenModel, CodegenConfig config, Map<String, Schema> allDefinitions) {
        Map<String, Object> objs = new HashMap();
        objs.put("package", config.modelPackage());
        List<Object> models = new ArrayList();
        objs.put("x-is-composed-model", codegenModel.isComposedModel);
        Map<String, Object> modelObject = new HashMap();
        modelObject.put("model", codegenModel);
        modelObject.put("importPath", config.toModelImport(codegenModel.classname));
        Set<String> allImports = new LinkedHashSet();
        allImports.addAll(codegenModel.imports);
        models.add(modelObject);
        objs.put("models", models);
        Set<String> importSet = new TreeSet();
        Iterator var9 = allImports.iterator();

        String mapping;
        while(var9.hasNext()) {
            String nextImport = (String)var9.next();
            mapping = (String)config.importMapping().get(nextImport);
            if (mapping == null) {
                mapping = config.toModelImport(nextImport);
            }

            if (mapping != null && !config.defaultIncludes().contains(mapping)) {
                importSet.add(mapping);
            }

            mapping = (String)config.instantiationTypes().get(nextImport);
            if (mapping != null && !config.defaultIncludes().contains(mapping)) {
                importSet.add(mapping);
            }
        }

        List<Map<String, String>> imports = new ArrayList();
        Iterator var13 = importSet.iterator();

        while(var13.hasNext()) {
            mapping = (String)var13.next();
            Map<String, String> item = new HashMap();
            item.put("import", mapping);
            imports.add(item);
        }

        objs.put("imports", imports);
        config.postProcessModels(objs);
        return objs;
    }

    private Map<String, SecurityScheme> getAuthMethods(List<SecurityRequirement> securities, Map<String, SecurityScheme> securitySchemes) {
        if (securities != null && securitySchemes != null && !securitySchemes.isEmpty()) {
            Map<String, SecurityScheme> authMethods = new HashMap();
            Iterator var4 = securities.iterator();

            while(var4.hasNext()) {
                SecurityRequirement requirement = (SecurityRequirement)var4.next();
                Iterator var6 = requirement.keySet().iterator();

                while(var6.hasNext()) {
                    String key = (String)var6.next();
                    SecurityScheme securityScheme = (SecurityScheme)securitySchemes.get(key);
                    if (securityScheme != null) {
                        authMethods.put(key, securityScheme);
                    }
                }
            }

            return authMethods;
        } else {
            return null;
        }
    }

    private Boolean getCustomOptionBooleanValue(String option) {
        List<CodegenArgument> languageArguments = this.config.getLanguageArguments();
        if (languageArguments != null && !languageArguments.isEmpty()) {
            Optional<CodegenArgument> optionalCodegenArgument = languageArguments.stream().filter((argument) -> {
                return option.equalsIgnoreCase(argument.getOption());
            }).findFirst();
            return !optionalCodegenArgument.isPresent() ? null : Boolean.valueOf(((CodegenArgument)optionalCodegenArgument.get()).getValue());
        } else {
            return null;
        }
    }

    protected void processSecurityProperties(Map<String, Object> bundle) {
        Map<String, SecurityScheme> securitySchemeMap = this.openAPI.getComponents() != null ? this.openAPI.getComponents().getSecuritySchemes() : null;
        List<CodegenSecurity> authMethods = this.config.fromSecurity(securitySchemeMap);
        if (authMethods != null && !authMethods.isEmpty()) {
            bundle.put("authMethods", authMethods);
            bundle.put("hasAuthMethods", true);
        }

    }
}
