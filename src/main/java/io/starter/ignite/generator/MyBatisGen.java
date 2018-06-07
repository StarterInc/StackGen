package io.starter.ignite.generator;

import io.starter.ignite.util.DOMEditor;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * responsible for generating MyBatis config
 * 
 * @author john
 *
 */
public class MyBatisGen extends Gen implements Generator {

	Document jdo = null;

	public static Map createMyBatis(Class c, MyBatisGen gen) throws Exception {

		// projectDir.mkdirs();
		io.starter.ignite.util.Logger.log("Generate MyBatis...");

		Map classesToGenerate = gen.processClasses(c, null, gen);

		io.starter.ignite.util.Logger.log("Write updated XML...");

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
		org.mybatis.generator.config.Configuration config = cp.parseConfiguration(configFile);

		DefaultShellCallback callback = new DefaultShellCallback(overwrite);

		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);

		ProgressCallback cb = null;

		myBatisGenerator.generate(cb);
		for (String warning : warnings) {
			System.err.println("WARNING: MyBatis Generation: " + warning);
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
	public Object createValue(Field f) {
		return null;
	}

	@Override
	public void generate(String className, List fieldList, List getters, List setters) throws Exception {
		// create a new JDOM Element
		// generatorConfiguration
		// <table schema="patriot" tableName="user">
		// <generatedKey column="id" sqlStatement="JDBC" />
		// </table>

		String packageName = null;
		int dotpos = className.lastIndexOf(".");
		packageName = className.substring(0, dotpos);
		packageName = "gen." + packageName;
		className = className.substring(dotpos);

		io.starter.ignite.util.Logger.log("Load XML template...");
		File configFile = new File(MYBATIS_GEN_CONFIG);

		if (jdo == null) {
			System.err.println("Reading MyBatis Generator Config Template: " + MYBATIS_GEN_CONFIG);
			jdo = DOMEditor.parse("mybatis", configFile.getAbsolutePath());
		}
		Element el = new Element("table").setText("1000").setAttribute("schema", SCHEMA_NAME).setAttribute("tableName",
				className.toUpperCase());
		if (true) { // (setters.size() > 0) {
			Element el2 = new Element("generatedKey").setAttribute("column", "id").setAttribute("sqlStatement", "JDBC");
			el.setContent(el2);
		}

		final Namespace ns = Namespace.getNamespace("mybatis");

		Element rootElement = jdo.getRootElement();
		List<Element> listEmpElement = rootElement.getChildren();

		// loop through to edit every Employee element
		for (Element empElement : listEmpElement) {

			List<Element> listEmpElementChildren = empElement.getChildren();

			// change the name to BLOCK letters
			String name = empElement.getChildText("table", ns);
			if (empElement.getName().equals("context"))
				empElement.addContent(el);

		}
		DOMEditor.write(jdo, MYBATIS_GEN_CONFIG_OUT);
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
				if (name.contains(JavaGen.ADD_GEN_CLASS_NAME))
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
		io.starter.ignite.util.Logger.log("Iterate Swagger Entities and create Tables...");
		File[] modelFiles = Gen.getFiles();
		MyBatisGen gen = new MyBatisGen();
		for (File mf : modelFiles) {
			String cn = mf.getName().substring(0, mf.getName().indexOf("."));
			cn = Configuration.MODEL_PACKAGE + "." + cn;
			System.err.println("Creating Classes from ModelFile: " + cn);
			// Create a new custom class loader, pointing to the directory that contains the
			// compiled
			// classes, this should point to the top of the package structure!
			URLClassLoader classLoader = new URLClassLoader(new URL[] { new File(JAVA_GEN_SRC_FOLDER).toURI().toURL() });

			Class<?> loadedClass = classLoader.loadClass(cn);
			createMyBatis(loadedClass, gen);
		}
		io.starter.ignite.util.Logger.log("Run Generation Script...");
		generate();
	}

}
