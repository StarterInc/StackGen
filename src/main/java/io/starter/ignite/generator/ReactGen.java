package io.starter.ignite.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import io.starter.ignite.generator.react.AppEntityObject;
import io.starter.ignite.util.FileUtil;
import io.starter.toolkit.StringTool;

/**
 * Uses MustacheJava
 * 
 * https://github.com/spullara/mustache.java
 * 
 * @author John McMahon (@TechnoCharms)
 *
 */
public class ReactGen extends Gen implements Generator {

	protected static final Logger logger = LoggerFactory
			.getLogger(ReactGen.class);

	/**
	 * copy the resulting output to the export folder
	 * 
	 * @param gen
	 * @throws IOException
	 */
	private static void export(ReactGen gen) throws IOException {

		logger.debug("Exporting: " + REACT_TEMPLATE_APP_FOLDER + " to: "
				+ REACT_EXPORT_FOLDER);
		FileUtil.copyFolder(REACT_TEMPLATE_APP_FOLDER, REACT_EXPORT_FOLDER);

		logger.debug("Exporting: " + REACT_APP_OUTPUT_FOLDER + REACT_APP_NAME
				+ " to: " + REACT_EXPORT_FOLDER);
		FileUtil.copyFolder(REACT_APP_OUTPUT_FOLDER
				+ REACT_APP_NAME, REACT_EXPORT_FOLDER);
	}

	/**
	 * Create Entity Objects which are basically schema of Objects with
	 * configuration data for the React Native generation
	 * 
	 * 
	 * @param gen
	 * @throws Exception
	 */
	static void generateEntitiesFromModelFolder(ReactGen gen) throws Exception {
		logger.debug("Iterate MyBatis Entities and create React App Entity Classes...");
		String[] modelFiles = SwaggerGen.getModelFiles();
		for (String mf : modelFiles) {
			String cn = mf.substring(0, mf.indexOf("."));
			// cn = cn + ".class";
			cn = IGNITE_DATAMODEL_PACKAGE + cn;
			logger.debug("Loading Classes from ModelFile: " + cn);
			try {
				createAppEntities(gen, Class.forName(cn));
			} catch (Exception e) {
				logger.error("ReactGen.generateEntitesFromModel failed: "
						+ e.toString());
			}
		}
	}

	private static void createAppEntities(ReactGen gen, Class<?> forName) {

		if (gen == null)
			throw new IllegalStateException(
					"No ReactGen context in createAppEntities");
		if (Configuration.REACT_APP_NAME == null)
			throw new IllegalStateException("No AppName in createAppEntities");
		if (forName == null)
			throw new IllegalStateException(
					"No Class defined in createAppEntities");

		AppEntityObject ap = new AppEntityObject(Configuration.REACT_APP_NAME,
				forName);
		Configuration.REACT_DATA_OBJECTS.add(ap);
	}

	public static void main(String[] args) throws Exception {
		ReactGen.generateReactNative();
	}

	public static void generateReactNative() throws Exception {
		// System.setProperty("user.dir",
		// "/user/projects/StarterIgnite/StarterIgniteServer");
		// System.setProperty("user.dir",
		// "/StarterIgniteServer");

		ReactGen gen = new ReactGen();
		generateEntitiesFromModelFolder(gen);

		File[] templateFiles = Gen.getSourceFilesInFolder(new File(
				REACT_TEMPLATE_FOLDER), FOLDER_SKIP_LIST);

		for (Object o : templateFiles) {
			String fname = o.toString();
			String shortName = fname.substring(fname.lastIndexOf("/") + 1);

			// NEEDED? fname = ReactGen.renamePaths(fname,
			// REACT_APP_NAME);

			// for each object in system, create a REDUX
			// action and reducer from templates
			if (shortName.startsWith("objectName")) {
				for (AppEntityObject aeo : Configuration.REACT_DATA_OBJECTS) {

					// read in template file
					String foutp = StringTool
							.replaceText(fname, "objectName", aeo.objectname);

					// mustache
					generateFromTemplate(aeo, fname, foutp);
				}
			} else {
				// single file mustache
				generateFromTemplate(gen, fname, null);
			}
		}

		// copy the files to the target project folder
		export(gen);

		logger.error("TODO: FIX RUN NPM");
		// run npm to build and run react native app!
		// String[] args1 = { "npm", "install" };
		// RunCommand.runSafe("npm", args1);

	}

	private static HashMap<String, Object> getMappingData() {

		HashMap<String, Object> appVals = new HashMap<String, Object>();
		appVals.put("app_name", "TESTARIO");

		return appVals;
	}

	/**
	 * @param mf
	 * @param appVals
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void copyFileTemplate(Object gen, String fname, String multifile) throws IOException, FileNotFoundException {

		String foutp = StringTool.replaceText(fname, REACT_TEMPLATE_APP_FOLDER
				+ "/", REACT_APP_OUTPUT_FOLDER + REACT_APP_NAME + "/starter/");

		// read in template file
		if (multifile != null) {
			foutp = StringTool.replaceText(multifile, REACT_TEMPLATE_APP_FOLDER
					+ "/", REACT_APP_OUTPUT_FOLDER + REACT_APP_NAME
							+ "/starter/");
		}

		File fout = new File(foutp);
		File finp = new File(fname);

		fout.mkdirs();
		if (fout.isDirectory())
			fout.delete();
		if (!finp.isDirectory()) {
			try {
				Reader fread = new FileReader(finp);

				fout.delete();
				fout.createNewFile();
				Writer fwriter = new FileWriter(fout);

				logger.debug("Copying template file: " + fout);

				fwriter.flush();
			} catch (Exception e) {
				logger.error("Failed to copy file template: " + e);
			}
		}
	}

	/**
	 * @param mf
	 * @param appVals
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void generateFromTemplate(Object gen, String fname, String multifile) throws IOException, FileNotFoundException {

		MustacheFactory mf = new DefaultMustacheFactory();
		String foutp = StringTool
				.replaceText(fname, REACT_TEMPLATE_FOLDER, REACT_APP_OUTPUT_FOLDER
						+ REACT_APP_NAME + "/");

		// read in template file
		if (multifile != null) {
			foutp = StringTool
					.replaceText(multifile, REACT_TEMPLATE_FOLDER, REACT_APP_OUTPUT_FOLDER
							+ REACT_APP_NAME + "/");
		}

		File fout = new File(foutp);
		File finp = new File(fname);

		fout.mkdirs();
		if (fout.isDirectory())
			fout.delete();
		if (!fout.exists()) {
			fout.mkdirs();
			fout.delete();
		}
		if (!finp.isDirectory()) {
			try {
				Reader fread = new FileReader(finp);

				fout.delete();
				fout.createNewFile();
				Writer fwriter = new FileWriter(fout);

				logger.debug("Mustaching template: " + finp
						+ " to output file: " + fout);
				Mustache reactmf = mf.compile(fread, REACT_APP_OUTPUT_FOLDER);

				// if we are dealing with a sub-object
				reactmf.execute(fwriter, gen);

				fwriter.flush();
			} catch (Exception e) {
				logger.error("Failed to generate from template: " + e);
			}
		}
	}

	static void generateReactNativeFromAppFolder() {
		File[] templateFiles = Gen.getSourceFilesInFolder(new File(
				Configuration.REACT_TEMPLATE_APP_FOLDER), Configuration.FOLDER_SKIP_LIST);
		for (Object o : templateFiles) {
			logger.debug("React Native Template file: " + o.toString());
		}
	}

	@Override
	public Object createSetter(Field fld) {
		String fieldName = fld.getName();
		if (fieldName.startsWith("ajc$")) // skip aspects
			return null;

		Class fieldType = fld.getType();
		String fldName = StringTool.proper(fieldName);
		fldName = "get" + fldName;

		String ret = fldName;

		return ret;
	}

	@Override
	public Object createAccessor(Field f) {
		return null;
	}

	@Override
	public Object createMember(Field f) {
		return null;
	}

	@Override
	public void generate(String className, List<FieldSpec> fieldList, List<MethodSpec> getters, List<MethodSpec> setters) throws Exception {}

}
