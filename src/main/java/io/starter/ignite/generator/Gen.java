/**
 * 
 */
package io.starter.ignite.generator;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.javapoet.MethodSpec;

import io.starter.toolkit.StringTool;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author John McMahon (@TechnoCharms)
 *
 */
public class Gen {

	protected static final Logger logger = LoggerFactory.getLogger(Gen.class);

	/**
	 * iterate over the Class heirarchy and build a list of public classes and
	 * methods
	 * 
	 * @param ob
	 * @throws Exception
	 */
	public synchronized Map<String, Object> processClasses(Class<?> ob, Map<String, Object> results, Generator impl) throws Exception {
		String className = ob.getName();
		int dotpos = className.lastIndexOf(".");
		String packageName = className;
		if (dotpos > -1)
			packageName = className.substring(0, dotpos);

		if (!packageName.toUpperCase().startsWith("IO")) // skip Non Generated
			return null;

		if (className.toUpperCase().contains("ENUM")) // skip Java ENUMS
			return null;

		className = className.substring(dotpos + 1);
		className = StringTool.replaceChars(";", className, "");

		String packageDir = Configuration.JAVA_GEN_SRC_FOLDER + "/"
				+ StringTool.replaceChars(".", packageName, "/");

		java.io.File pkg = new java.io.File(packageDir);
		pkg.mkdirs();

		if (results == null)
			results = new HashMap<String, Object>();
		if (results.get(className) != null) // skip dupes
			return null;

		logger.debug("Crawling Class Heirarchy for Root Class: " + packageName
				+ "." + className);

		results.put(className, ob);

		java.lang.reflect.Field[] fields = ob.getDeclaredFields();
		List fieldList = new ArrayList();
		List getters = new ArrayList();
		List setters = new ArrayList();

		// recursively crawl the member objects
		for (Field f : fields) {
			Class<?> retval = f.getType();
			if (!retval.isPrimitive()
					&& (!retval.getName().equals(className))) {
				if (!retval.getName().startsWith("L[java.")
						&& !retval.getName().startsWith("ajc$")
						&& !retval.getName().startsWith("[C"))
					processClasses(retval, results, impl);
			}

			// Uses the appropriate adapter:
			if (!f.getName().startsWith("ajc$")) { // skip aspects
				logger.debug(this.toString() + " generating Field : "
						+ f.getName() + " Type: " + f.getType());

				Object fldObj = impl.createMember(f);
				if (fldObj != null && impl instanceof DBGen)
					fieldList.add(fldObj);

				MethodSpec fldAccess = (MethodSpec) impl.createAccessor(f);
				if (fldAccess != null)
					getters.add(fldAccess);

				MethodSpec setter = (MethodSpec) impl.createSetter(f);
				if (setter != null)
					setters.add(setter);
			}
		}

		//
		impl.generate(packageName + "."
				+ className, fieldList, getters, setters);

		return results;
	}

	/**
	 * @return
	 */
	static File[] getModelJavaFiles() {
		File modelDir = new File(Configuration.API_MODEL_CLASSES);
		File[] modelFiles = modelDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.contains(Configuration.MYBATIS_CLASS_PREFIX))
					return false;
				if (name.contains(Configuration.ADD_GEN_CLASS_NAME))
					return false;

				return name.toLowerCase().endsWith(".java");
			}
		});
		return modelFiles;
	}

	/**
	 * @return
	 */
	static File[] getModelClassFiles() {
		File modelDir = new File(Configuration.API_MODEL_CLASSES);
		File[] modelFiles = modelDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".java");
			}
		});
		return modelFiles;
	}

	static File[] getSourceFilesInFolder(File f, List<String> skipList) {
		File[] modelFiles = f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (skipList.contains(name))
					return false;
				if (new File(dir.getPath() + "/" + name).isDirectory())
					return true;
				if (name.toLowerCase().endsWith(".java"))
					return true;
				if (name.toLowerCase().endsWith(".xml"))
					return true;
				if (name.toLowerCase().endsWith(".js"))
					return true;
				if (name.toLowerCase().endsWith(".json"))
					return true;
				if (name.toLowerCase().endsWith(".properties"))
					return true;
				if (name.toLowerCase().endsWith(".info"))
					return true;
				if (name.toLowerCase().endsWith(".md"))
					return true;
				if (name.toLowerCase().endsWith(".txt"))
					return true;
				if (name.toLowerCase().endsWith(".sh"))
					return true;

				return false;
			}
		});
		List<File> folderFiles = new ArrayList<File>();
		for (File fx : modelFiles) {
			if (fx.isDirectory()) {
				File[] subdirFiles = getSourceFilesInFolder(fx, Configuration.FOLDER_SKIP_LIST);
				folderFiles.addAll(Arrays.asList(subdirFiles));
			} else {
				folderFiles.add(fx);
			}
		}
		Object[] ob = folderFiles.toArray();
		File[] fret = new File[ob.length];
		int i = 0;
		for (Object ft : ob)
			fret[i++] = (File) ft;
		return fret;
	}

	public static Object[] getAllFields(Class<?> type) {
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields.toArray();
	}

	public static ApiModelProperty getApiModelPropertyAnnotation(Field f) throws NoSuchMethodException, SecurityException {
		String methodName = "get" + StringTool.proper(f.getName());
		Method getter = f.getDeclaringClass().getMethod(methodName);
		// get the annotation
		ApiModelProperty anno = getter
				.getDeclaredAnnotation(ApiModelProperty.class);
		return anno;
	}

}
