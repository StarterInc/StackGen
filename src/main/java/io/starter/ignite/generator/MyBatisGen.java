package io.starter.ignite.generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

	public MyBatisGen(StackGenConfigurator cfg) {
		super(cfg);
		// TODO Auto-generated constructor stub
	}

	protected static final Logger logger = LoggerFactory.getLogger(MyBatisGen.class);

	List<String> alreadyAdded = new ArrayList<>(); // dedupe

	public Map<String, Object> createMyBatis(Class<?> c, MyBatisGen gen) throws Exception {

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
	public String getMyBatisModelClassName(String apiClassName) {
		final String apibn = MyBatisGen.getBaseJavaName(apiClassName);
		return StringTool.getUpperCaseFirstLetter(config.getSchemaName()) + apibn;
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

	private String replaceConfigVariables(String input) {
		// iterate the config variables
		try {

			// set props from fields
			String[] fx = StackGenConfigurator.getPropertyNames();
			for (String f : fx) {
				String fn = "${" + f + "}";
				if (input.contains(fn)) {
					Object v = config.get(f);
					input = input.replace(fn, v.toString());
				}
			}
			
			// set dynamic props
			String[] mx = StackGenConfigurator.getMethodPropertyNames();
			for (String m : mx) {
				String mn = "${" + StringTool.getLowerCaseFirstLetter(m) + "}";
				if (input.contains(mn)) {
					Object v = config.callGet(m);
					input = input.replace(mn, v.toString());
				}
			}
			
		} catch (Exception x) {
			logger.error("replaceConfigVariables failed: " + x);
		}
		return input;
	}

	/**
	 * generate the MyBatis generator config
	 */
	public void generate() throws Exception {

		final List<String> warnings = new ArrayList<>();
		final boolean overwrite = true;
		final File configFile = new File(config.getMybatisGenConfigOut());

		// we need to change some values in this template
		List<String> cfg = FileUtils.readLines(configFile, "utf-8");
		OutputStream sourceStream = new ByteArrayOutputStream();
		cfg.stream().map(s -> replaceConfigVariables(s)).forEach(s -> {
			try {
				sourceStream.write(s.getBytes());
			} catch (IOException e) {
				//
			}
		});
		InputStream targetStream = IOUtils.toInputStream(sourceStream.toString());

		// InputStream targetStream = new ByteArrayInputStreasourceStream.r);
		final ConfigurationParser cp = new ConfigurationParser(warnings);
		final org.mybatis.generator.config.Configuration cfx = cp.parseConfiguration(targetStream);

		final DefaultShellCallback callback = new DefaultShellCallback(overwrite);

		final MyBatisGenerator myBatisGenerator = new MyBatisGenerator(cfx, callback, warnings);

		final ProgressCallback cb = null;

		((org.mybatis.generator.config.Context) cfx.getContexts().get(0)).getProperties().put("schemaName",
				config.getSchemaName());
		myBatisGenerator.generate(cb);
		for (final String warning : warnings) {
			MyBatisGen.logger.warn("WARNING: MyBatis Generation: " + warning);
		}
	}

	@Override
	public Object createSetter(Field f) {
		return null;
	}

	@Override
	public Object createAccessor(Field f) {
		return null;
	}

	@Override
	public FieldSpec createMember(Field f) {
		return null;
	}

	private Document jdx;
	private Document jdt;

	@Override
	public void generate(String className, List<Object> fieldList, List<MethodSpec> getters, List<MethodSpec> setters)
			throws Exception {

		// create a new JDOM Element
		// generatorConfiguration
		// <table schema="patriot" tableName="user">
		// <generatedKey column="id" sqlStatement="JDBC" />
		// </table>

		String packageName = null;
		final int dotpos = className.lastIndexOf(".");
		packageName = className.substring(0, dotpos);
		// packageName = "gen." + packageName;
		className = className.substring(dotpos + 1);

		MyBatisGen.logger.info("Load MyBatis Generator Config XML template...");
		final File genConfigFile = new File(config.getMybatisGenConfigTemplate());
		jdt = createMyBatisXMLGenConfigNodes(jdt, className, genConfigFile);

		FileUtil.ensurePathExists(new File(config.getMybatisGenConfigOut()));
		DOMEditor.write(jdt, config.getMybatisGenConfigOut());

		MyBatisGen.logger.info("Load MyBatis Persistence Config XML template...");
		final File configFile = new File(config.getMybatisConfigTemplate());
		jdx = createMyBatisXMLConfigNodes(jdx, className, configFile);
		DOMEditor.write(jdx, config.getMybatisConfigOut()); // for runtime
	}

	// mappers>
	// <mapper resource="io/starter/sqlmaps/AclMapper.xml" />
	private Document createMyBatisXMLConfigNodes(Document jdo, String className, File configFile)
			throws JDOMException, IOException {

		MyBatisGen.logger.info("Parse MyBatis Template: " + configFile.getAbsolutePath());

		if (jdo == null) {
			jdo = DOMEditor.parse("mybatis", configFile.getAbsolutePath());
		}

		final Element el = new Element("mapper").setAttribute("resource", convertToMapperSyntax(className));

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

	private String convertToMapperSyntax(String className) {
		return config.getSqlMapsPath() + config.getSchemaName() + className + "Mapper.xml";
	}

	private Document createMyBatisXMLGenConfigNodes(Document jdo, String className, File configFile)
			throws JDOMException, IOException {

		MyBatisGen.logger.info("Parse MyBatis Template: " + configFile.getAbsolutePath());
		Table table = new Table(config);

		if (jdo == null) {
			jdo = DOMEditor.parse("mybatis", configFile.getAbsolutePath());
		}

		// dedupe
		if (!alreadyAdded.contains(className)) {
			alreadyAdded.add(className);
			final Element el = new Element("table").setAttribute("schema", config.getSchemaName()).setAttribute("tableName",
					table.convertToDBSyntax(className));

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

	void createMyBatisFromModelFolder() throws Exception {
		MyBatisGen.logger.info("Iterate Swagger Entities and create Tables...");
		final File[] modelFiles = Gen.getJavaFiles(config.getJavaGenSourceFolder() + "/" + config.getModelPackageDir() , false);

		for (final File mf : modelFiles) {
			final String mna = mf.getName();
			if (mna.indexOf(".") < 0) {
				MyBatisGen.logger.warn("Cannot Generate MyBatis from Model file: " + mna);
			} else {
				String cn = mna.substring(0, mna.indexOf("."));
				cn = config.getIgniteModelPackage() + "." + cn;
				MyBatisGen.logger.info("Loading Class from ModelFile: " + cn);
				final URLClassLoader classLoader = new URLClassLoader(
						new URL[] { new File(config.getJavaGenSourceFolder()).toURI().toURL() });
				final Class<?> loadedClass = classLoader.loadClass(cn);
				createMyBatis(loadedClass, this);
				classLoader.close();
			}
		}
		MyBatisGen.logger.info("Generate...");
		generate();
	}

}
