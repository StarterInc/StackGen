/**
 * 
 */
package io.starter.ignite.generator;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extentech.toolkit.StringTool;

/**
 * @author john
 *
 */
public class Gen {

	// TODO: potentially cache decrypted values
	private static final boolean WRITE_LOCAL_FIELDS = false;

	/**
	 * iterate over the Class heirarchy and build a list of public classes and
	 * methods
	 * 
	 * @param ob
	 * @throws Exception
	 */
	public Map<String, Object> processClasses(Class<?> ob, Map<String, Object> results, Generator impl)
			throws Exception {
		String className = ob.getName();
		int dotpos = className.lastIndexOf(".");
		String packageName = className;
		if (dotpos > -1)
			packageName = className.substring(0, dotpos);

		if (packageName.toUpperCase().startsWith("[LIO")) // skip Java
			return null;
		if (packageName.toUpperCase().startsWith("[LJAVA")) // skip Java
			return null;
		if (packageName.toUpperCase().startsWith("JAVA"))
			return null;
		if (className.toUpperCase().startsWith("[LJAVA")) // skip Java
			return null;

		className = className.substring(dotpos + 1);
		className = StringTool.replaceChars(";", className, "");

		String packageDir = Configuration.JAVA_GEN_SRC_FOLDER + "/" + StringTool.replaceChars(".", packageName, "/");

		java.io.File pkg = new java.io.File(packageDir);
		pkg.mkdirs();

		if (results == null)
			results = new HashMap();
		if (results.get(className) != null) // skip dupes
			return null;

		io.starter.ignite.util.Logger.log("Crawling Class Heirarchy for Root Class: " + packageName + "." + className);

		results.put(className, ob);

		java.lang.reflect.Field[] fields = ob.getDeclaredFields();
		List<Object> fieldList = new ArrayList<Object>();
		List<Object> getters = new ArrayList<Object>();
		List<Object> setters = new ArrayList<Object>();

		// recursively crawl the member objects
		for (Field f : fields) {
			Class<?> retval = f.getType();
			if (!retval.isPrimitive() && (!retval.getName().equals(className))) {
				if (!retval.getName().startsWith("L[java.") && !retval.getName().startsWith("ajc$")
						&& !retval.getName().startsWith("[C"))
					processClasses(retval, results, impl);
			}

			// Uses the appropriate adapter:
			if (!f.getName().startsWith("ajc$")) { // skip aspects
				io.starter.ignite.util.Logger
						.log(this.toString() + " generating Field : " + f.getName() + " Type: " + f.getType());

				Object fldObj = impl.createValue(f);
				if (fldObj != null && WRITE_LOCAL_FIELDS)
					fieldList.add(fldObj);

				Object fldAccess = impl.createAccessor(f);
				if (fldAccess != null)
					getters.add(fldAccess);

				Object setter = impl.createSetter(f);
				if (setter != null)
					setters.add(setter);
			}
		}

		//
		impl.generate(packageName + "." + className, fieldList, getters, setters);

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

}
