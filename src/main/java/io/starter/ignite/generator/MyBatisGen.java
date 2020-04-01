package io.starter.ignite.generator;

import java.io.File;
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
import io.starter.ignite.util.FileUtil;
import io.starter.toolkit.StringTool;

/**
 * responsible for generating MyBatis config
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class MyBatisGen extends Gen implements Generator {

	protected static final Logger logger = LoggerFactory.getLogger(MyBatisGen.class);

	List<String> alreadyAdded = new ArrayList<>(); // dedupe

	public static Map<String, Object> createMyBatis(Class<?> c, MyBatisGen gen) throws Exception {

		MyBatisGen.logger.info("Generate MyBatis...");

		final Map<String, Object> classesToGenerate = gen.processClasses(c, null, gen);

		MyBatisGen.logger.info("Write updated XML...");

		return classesToGenerate;

	}

	/**
	 * feed it an api class and it will attempt to sanitize and map to MyBatis
	 * artifact name
	 *
	 * @param apiClassName
	 * @return
	 */
	public static String getMyBatisModelClassName(String apiClassName) {
		final String apibn = MyBatisGen.getBaseJavaName(apiClassName);
		return StringTool.getUpperCaseFirstLetter(Configuration.schemaName) + apibn;
	}

	/**
	 * strips the package if any
	 *
	 * @param n
	 * @return
	 */
	static String getBaseJavaName(String n) {
		if (n.length() <= 0) {
			return n;
		}

		if (n.contains(".")) {
			n = n.substring(n.lastIndexOf(".") + 1);
		}
		return n;
	}

	/**
	 * generate the MyBatis generator config
	 */
	public static void generate() throws Exception {

		final List<String> warnings = new ArrayList<>();
		final boolean overwrite = true;
		final File configFile = new File(Configuration.MYBATIS_GEN_CONFIG_OUT);

		final ConfigurationParser cp = new ConfigurationParser(warnings);
		final org.mybatis.generator.config.Configuration config = cp.parseConfiguration(configFile);

		final DefaultShellCallback callback = new DefaultShellCallback(overwrite);

		final MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);

		final ProgressCallback cb = null;

		myBatisGenerator.generate(cb);
		for (final String warning : warnings) {
			MyBatisGen.logger.warn("WARNING: MyBatis Generation: " + warning);
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
	public FieldSpec createMember(Field f) {
		return null;
	}

	private Document jdx;
	private Document jdt;

	@Override
	public void generate(String className, List<FieldSpec> fieldList, List<MethodSpec> getters,
			List<MethodSpec> setters) throws Exception {

		// create a new JDOM Element
		// generatorConfiguration
		// <table schema="patriot" tableName="user">
		// <generatedKey column="id" sqlStatement="JDBC" />
		// </table>

		String packageName = null;
		final int dotpos = className.lastIndexOf(".");
		packageName = className.substring(0, dotpos);
		packageName = "gen." + packageName;
		className = className.substring(dotpos + 1);

		MyBatisGen.logger.info("Load MyBatis Generator XML template...");
		final File genConfigFile = new File(Configuration.MYBATIS_GEN_CONFIG_TEMPLATE);
		jdt = createMyBatisXMLGenConfigNodes(jdt, className, genConfigFile);

		FileUtil.ensurePathExists(new File(Configuration.MYBATIS_GEN_CONFIG_OUT));
		DOMEditor.write(jdt, Configuration.MYBATIS_GEN_CONFIG_OUT);

		MyBatisGen.logger.info("Load MyBatis Generator XML template...");
		final File configFile = new File(Configuration.MYBATIS_CONFIG_TEMPLATE);
		jdx = createMyBatisXMLConfigNodes(jdx, className, configFile);
		DOMEditor.write(jdx, Configuration.MYBATIS_CONFIG_OUT); // for runtime
	}

	// mappers>
	// <mapper resource="io/starter/sqlmaps/AclMapper.xml" />
	private Document createMyBatisXMLConfigNodes(Document jdo, String className, File configFile)
			throws JDOMException, IOException {

		MyBatisGen.logger.info("Parse MyBatis Template: " + configFile.getAbsolutePath());

		if (jdo == null) {
			jdo = DOMEditor.parse("mybatis", configFile.getAbsolutePath());
		}

		final Element el = new Element("mapper").setAttribute("resource", MyBatisGen.convertToMapperSyntax(className));

		final Element rootElement = jdo.getRootElement();
		final List<Element> listEmpElement = rootElement.getChildren();

		// loop through to add every sqlf mapping element
		// for (Element empElement : listEmpElement) {
		// if (empElement.getName().equals("mappers"))
		// empElement.addContent(el);
		//
		// }
		return jdo;
	}

	private static String convertToMapperSyntax(String className) {
		return Configuration.SQL_MAPS_PATH + Configuration.schemaName + className + "Mapper.xml";
	}

	private Document createMyBatisXMLGenConfigNodes(Document jdo, String className, File configFile)
			throws JDOMException, IOException {

		MyBatisGen.logger.info("Parse MyBatis Template: " + configFile.getAbsolutePath());

		if (jdo == null) {
			jdo = DOMEditor.parse("mybatis", configFile.getAbsolutePath());
		}

		// dedupe
		if (!alreadyAdded.contains(className)) {
			alreadyAdded.add(className);
			final Element el = new Element("table").setAttribute("schema", Configuration.schemaName)
					.setAttribute("tableName", Table.convertToDBSyntax(className));

			final Element el2 = new Element("generatedKey").setAttribute("column", "id").setAttribute("sqlStatement",
					"JDBC");
			el.addContent(el2);

			final Element rootElement = jdo.getRootElement();
			final List<Element> listEmpElement = rootElement.getChildren();

			// loop through to add every sqlf mapping element
			for (final Element empElement : listEmpElement) {
				if (empElement.getName().equals("context")) {
					empElement.addContent(el);
				}

				final Object pn = empElement.getAttribute("tableName");
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

	static void createMyBatisFromModelFolder() throws Exception {
		MyBatisGen.logger.info("Iterate Swagger Entities and create Tables...");
		final File[] modelFiles = Gen
				.getJavaFiles(Configuration.JAVA_GEN_SRC_FOLDER + "/" + Configuration.MODEL_PACKAGE_DIR, false);
		final MyBatisGen gen = new MyBatisGen();
		for (final File mf : modelFiles) {
			final String mna = mf.getName();
			if (mna.indexOf(".") < 0) {
				MyBatisGen.logger.warn("Cannot Generate MyBatis from Model file: " + mna);
			} else {
				String cn = mna.substring(0, mna.indexOf("."));
				cn = Configuration.IGNITE_MODEL_PACKAGE + "." + cn;
				MyBatisGen.logger.info("Loading Class from ModelFile: " + cn);
				final URLClassLoader classLoader = new URLClassLoader(
						new URL[] { new File(Configuration.JAVA_GEN_SRC_FOLDER).toURI().toURL() });
				final Class<?> loadedClass = classLoader.loadClass(cn);
				MyBatisGen.createMyBatis(loadedClass, gen);
				classLoader.close();
			}
		}
		MyBatisGen.logger.info("Generate...");
		MyBatisGen.generate();
	}

}
