package io.starter.ignite.generator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import io.starter.ignite.generator.DMLgenerator.Table;
import io.starter.ignite.util.DOMEditor;

/**
 * responsible for generating MyBatis config
 * 
 * @author John McMahon (@TechnoCharms)
 *
 */
public class MyBatisGen extends Gen implements Generator {

	protected static final Logger	logger			= LoggerFactory
			.getLogger(MyBatisGen.class);

	List<String>					alreadyAdded	= new ArrayList<String>();	// dedupe
																				// list

	public static Map createMyBatis(Class c, MyBatisGen gen) throws Exception {

		// projectDir.mkdirs();
		logger.debug("Generate MyBatis...");

		Map classesToGenerate = gen.processClasses(c, null, gen);

		logger.debug("Write updated XML...");

		return classesToGenerate;

	}

	/**
	 * generate the MyBatis generator config
	 */
	public static void generate() throws Exception {

		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File(MYBATIS_GEN_CONFIG_OUT);

		ConfigurationParser cp = new ConfigurationParser(warnings);
		org.mybatis.generator.config.Configuration config = cp
				.parseConfiguration(configFile);

		DefaultShellCallback callback = new DefaultShellCallback(overwrite);

		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
				callback, warnings);

		ProgressCallback cb = null;

		myBatisGenerator.generate(cb);
		for (String warning : warnings) {
			logger.error("WARNING: MyBatis Generation: " + warning);
		}
	}

	@Override
	public Object createSetter(Field f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createAccessor(Field f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createMember(Field f) {
		return null;
	}

	Document	jdx;
	Document	jdt;

	@Override
	public void generate(String className, List<FieldSpec> fieldList, List<MethodSpec> getters, List<MethodSpec> setters) throws Exception {

		// create a new JDOM Element
		// generatorConfiguration
		// <table schema="patriot" tableName="user">
		// <generatedKey column="id" sqlStatement="JDBC" />
		// </table>

		String packageName = null;
		int dotpos = className.lastIndexOf(".");
		packageName = className.substring(0, dotpos);
		packageName = "gen." + packageName;
		className = className.substring(dotpos + 1);

		logger.debug("Load MyBatis Generator XML template...");
		File genConfigFile = new File(MYBATIS_GEN_CONFIG);
		jdt = createMyBatisXMLGenConfigNodes(jdt, className, genConfigFile);
		DOMEditor.write(jdt, MYBATIS_GEN_CONFIG_OUT);

		logger.debug("Load MyBatis Generator XML template...");
		File configFile = new File(MYBATIS_CONFIG);
		jdx = createMyBatisXMLConfigNodes(jdx, className, configFile);
		DOMEditor.write(jdx, MYBATIS_CONFIG_OUT); // for runtime
	}

	// mappers>
	// <mapper resource="io/starter/sqlmaps/AclMapper.xml" />
	private Document createMyBatisXMLConfigNodes(Document jdo, String className, File configFile) throws JDOMException, IOException {

		logger.debug("Parse MyBatis Template: " + configFile.getAbsolutePath());

		if (jdo == null)
			jdo = DOMEditor.parse("mybatis", configFile.getAbsolutePath());

		Element el = new Element("mapper").setAttribute("resource", MyBatisGen
				.convertToMapperSyntax(className));

		Element rootElement = jdo.getRootElement();
		List<Element> listEmpElement = rootElement.getChildren();

		// loop through to add every sqlf mapping element
		for (Element empElement : listEmpElement) {
			if (empElement.getName().equals("mappers"))
				empElement.addContent(el);

		}
		return jdo;
	}

	private static String convertToMapperSyntax(String className) {
		return SQL_MAPS_PATH + "Ignite" + className + "Mapper.xml";
	}

	private Document createMyBatisXMLGenConfigNodes(Document jdo, String className, File configFile) throws JDOMException, IOException {

		logger.debug("Parse MyBatis Template: " + configFile.getAbsolutePath());

		if (jdo == null)
			jdo = DOMEditor.parse("mybatis", configFile.getAbsolutePath());

		// dedupe
		if (!alreadyAdded.contains(className)) {
			alreadyAdded.add(className);
			Element el = new Element("table").setText("1000")
					.setAttribute("schema", SCHEMA_NAME)
					.setAttribute("tableName", Table
							.convertToDBSyntax(className));

			if (true) { // (setters.size() > 0) {
				Element el2 = new Element("generatedKey")
						.setAttribute("column", "id")
						.setAttribute("sqlStatement", "JDBC");
				el.setContent(el2);
			}

			Element rootElement = jdo.getRootElement();
			List<Element> listEmpElement = rootElement.getChildren();

			// loop through to add every sqlf mapping element
			for (Element empElement : listEmpElement) {
				if (empElement.getName().equals("context"))
					empElement.addContent(el);

				Object pn = empElement.getAttribute("tableName");
				if (pn != null) {
					if (pn.toString().equals("PLACEHOLDER_NODE")) {
						rootElement.removeContent(empElement);
					}
				}
			}

		}
		return jdo;
	}

	@Override
	public String toString() {
		return "MyBatis Generator";
	}

	static String[] getModelFiles() {
		File modelDir = new File(Configuration.MYBATIS_MODEL_CLASSES);
		String[] modelFiles = modelDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.contains("Example"))
					return false;
				if (name.contains(MYBATIS_CLASS_PREFIX))
					return false;
				if (name.contains("Mapper"))
					return false;
				if (name.contains(ADD_GEN_CLASS_NAME))
					return false;
				return name.toLowerCase().endsWith(".java");
			}
		});

		if (modelFiles != null && modelFiles.length < 1) {
			throw new IllegalStateException(
					"JavaGen Failure: no model classfiles found. Check the MYBATIS_MODEL_CLASSES value.");
		}
		return modelFiles;
	}

	static void createMyBatisFromModelFolder() throws Exception {
		logger.debug("Iterate Swagger Entities and create Tables...");
		File[] modelFiles = Gen.getModelJavaFiles();
		MyBatisGen gen = new MyBatisGen();
		for (File mf : modelFiles) {
			String cn = mf.getName().substring(0, mf.getName().indexOf("."));
			cn = Configuration.MODEL_PACKAGE + "." + cn;
			logger.debug("Loading Classes from ModelFile: " + cn);
			// Create a new custom class loader, pointing to the
			// directory that contains the
			// compiled
			// classes, this should point to the top of the package
			// structure!
			URLClassLoader classLoader = new URLClassLoader(new URL[] {
					new File(JAVA_GEN_SRC_FOLDER).toURI().toURL() });

			Class<?> loadedClass = classLoader.loadClass(cn);
			createMyBatis(loadedClass, gen);
			classLoader.close();
		}
		logger.debug("Run Generation Script...");
		generate();
	}

}
