package io.starter.ignite.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import io.starter.ignite.security.securefield.SecureFieldAccessor;
import io.starter.toolkit.StringTool;
import io.swagger.annotations.ApiModelProperty;

/**
 * Generating Java code
 *
 * @author john
 *
 */
public class JavaGen extends Gen implements Generator {

	protected static final Logger logger = LoggerFactory
			.getLogger(JavaGen.class);

	public static Map<?, ?> createClasses(Class<?> c) throws Exception {
		final JavaGen gen = new JavaGen();
		final Map<?, ?> classesToGenerate = gen.processClasses(c, null, gen);
		return classesToGenerate;
	}

	@Override
	public Object createMember(Field fld) {

		// UNUSED
		/*
		 * String fieldName = fld.getName();
		 * Class<?> fieldType = fld.getType();
		 *
		 * FieldSpec field = FieldSpec
		 * .builder(fieldType, fieldName, Modifier.PRIVATE)
		 * .addJavadoc("Starter Ignite Generated Field: "
		 * + DATE_FORMAT.format(new Date()))
		 * .build();
		 *
		 * return field;
		 */
		return null;
	}

	@Override
	public Object createAccessor(Field fld) {

		boolean isSecure = false;
		boolean isDataField = true;

		try {
			ApiModelProperty apimp = Gen.getApiModelPropertyAnnotation(fld);
			isSecure = apimp.secureField();
			// isSecure = apimp.secureField();
			isDataField = true; // TODO: implement an extension point
		} catch (NoSuchMethodException | SecurityException e1) {
			// no worries
		}

		final String fieldName = fld.getName();
		if (fieldName.startsWith("ajc$")) {
			return null;
		}

		final String className = fld.getDeclaringClass().getName();
		String memberName = className.substring(className.lastIndexOf(".") + 1);
		memberName += "Bean";

		final Class<?> fieldType = fld.getType();
		String fldName = StringTool.proper(fieldName);
		fldName = "get" + fldName;
		try {
			final MethodSpec ret = MethodSpec.methodBuilder(fldName)
					.addJavadoc("Starter Ignite Generated Method: "
							+ DATE_FORMAT.format(new Date()) + "/r/n" + "@see "
							+ fld.getDeclaringClass().getSuperclass().getName()
							+ "/r/n" + "@return the value of " + fieldName)
					.addModifiers(Modifier.PUBLIC).returns(fieldType)
					.addStatement("return " + memberName + "." + fieldName)
					.build();

			if (isSecure)
				ret.annotations.add(AnnotationSpec
						.builder(SecureFieldAccessor.class).build());
			return ret;
		} catch (final Exception e) {
			logger.error("COULD NOT GENERATE FIELD: " + memberName + " "
					+ e.toString());
		}
		return null;
	}

	@Override
	public Object createSetter(Field fld) {

		final String fieldName = fld.getName();

		final String className = fld.getDeclaringClass().getName();
		String memberName = className.substring(className.lastIndexOf(".") + 1);
		memberName += "Bean";

		if (fieldName.startsWith("ajc$")) {
			return null;
		}
		final Class<?> fieldType = fld.getType();
		final String fldName = StringTool.proper(fieldName);
		final String fldNameSet = "set" + fldName;

		try {
			final MethodSpec ret = MethodSpec.methodBuilder(fldNameSet)
					.addJavadoc("Starter Ignite Generated Method: "
							+ DATE_FORMAT.format(new Date()))
					// .addModifiers(Modifier.PUBLIC).addAnnotation(AnnotationSpec.builder(DataField.class).build())
					.addParameter(fieldType, fieldName + "Val")
					.addStatement(memberName + "." + fieldName + " = "
							+ fieldName + "Val")
					.build();

			return ret;
		} catch (final Exception e) {
			logger.error("ERROR CREATING SETTER for: " + fieldName + " "
					+ e.toString());
		}
		return null;
	}

	@Override
	public synchronized void generate(String className, List<FieldSpec> fieldList, List<MethodSpec> getters, List<MethodSpec> setters) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String packageName = null;
		final int dotpos = className.lastIndexOf(".");
		packageName = className.substring(0, dotpos);

		// packageName = ADD_GEN_PACKAGE_NAME + packageName;
		String memberName = className.substring(className.lastIndexOf(".") + 1);
		memberName += "Bean";

		memberName.replace(Configuration.MYBATIS_CLASS_PREFIX, "");

		// instantiate the delegate class

		final URLClassLoader classLoader = new URLClassLoader(
				new URL[] { new File(JAVA_GEN_SRC_FOLDER).toURI().toURL() });

		final Class<?> cx = classLoader.loadClass(className);

		final FieldSpec member = FieldSpec.builder(cx, memberName)
				.addModifiers(Modifier.PRIVATE).build();

		className = className.substring(dotpos + 1);

		className += ADD_GEN_CLASS_NAME;

		// create the Java Class
		final TypeSpec type = TypeSpec.classBuilder(className)
				.addModifiers(Modifier.PUBLIC).addField(member)
				// .superclass(null)
				.addJavadoc("Starter Ignite Generated Class: "
						+ DATE_FORMAT.format(new Date()))
				.addFields(fieldList).addMethods(setters).addMethods(getters)
				.build();

		JavaFile.builder(packageName, type).build().writeTo(JAVA_GEN_SRC);

	}

	@Override
	public String toString() {
		return "Java Generator";
	}

	static void generateClassesFromModelFolder() throws Exception {
		logger.debug("Iterate MyBatis Entities and create Wrapper Classes...");

		final String[] modelFiles = MyBatisGen.getModelFiles();
		for (final String mf : modelFiles) {
			String cn = mf.substring(0, mf.indexOf("."));
			// cn = cn + ".class";
			cn = Configuration.GEN_MODEL_PACKAGE + "." + cn;
			logger.debug("Creating Classes from ModelFile: " + cn);

			try {

				// Create a new custom class loader, pointing to the
				// directory that contains the
				// compiled
				// classes, this should point to the top of the package
				// structure!
				final URLClassLoader classLoader = new URLClassLoader(
						new URL[] { new File(JAVA_GEN_SRC_FOLDER).toURI()
								.toURL() });

				final Class<?> loadedClass = classLoader.loadClass(cn);

				createClasses(loadedClass);
				classLoader.close();
			} catch (final ClassNotFoundException e) {
				logger.error("JavaGen.generateClassesFromModelFolder failed: "
						+ cn + ": " + e.toString());
			}
		}
	}

	/**
	 * compile all the files in the generated folder(s)
	 *
	 * thanks
	 * to:https://stackoverflow.com/questions/21544446/how-do-you-dynamically-compile-and-load-external-java-classes
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	static void compile(String packageDir) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		// test
		final String sourcepath = JAVA_GEN_SRC_FOLDER + packageDir;

		logger.debug("JavaGen Compiling: " + sourcepath);
		System.getProperty("java.class.path");

		// RunCommand.runSafe(cmdarray[0], cmdarray);

		// prepare compiler
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final StandardJavaFileManager fileManager = compiler
				.getStandardFileManager(diagnostics, null, null);

		final List<String> optionList = new ArrayList<>();
		optionList.add("-classpath");
		optionList.add(System.getProperty("java.class.path")
				+ ";gen/src/main/java");

		// File[] fx = new File(sourcepath).listFiles();
		final File[] fx = getSourceFilesInFolder(new File(
				sourcepath), Configuration.SKIP_LIST);

		final Iterable<? extends JavaFileObject> compilationUnit = fileManager
				.getJavaFileObjectsFromFiles(Arrays.asList(fx));

		final JavaCompiler.CompilationTask compilerTask = compiler
				.getTask(null, fileManager, diagnostics, optionList, null, compilationUnit);

		// Compilation Requirements
		logger.debug("Compiling: " + sourcepath);
		if (compilerTask.call()) {
			logger.debug("Compilation Complete.");

			// classes, this should point to the top of the package
			// structure!
			final URLClassLoader classLoader = new URLClassLoader(new URL[] {
					new File(JAVA_GEN_SRC_FOLDER).toURI().toURL() });

			for (final File f : fx) {
				// Load the class from the classloader by name....

				String loadClassName = f.getName()
						.substring(0, f.getName().length() - 5); // strip "java"
				loadClassName = packageDir.replace('/', '.') + loadClassName;
				loadClassName = loadClassName.substring(1); // strip leading dot
				try {
					final Class<?> loadedClass = classLoader
							.loadClass(loadClassName);
					// Create a new instance...
					final Object obj = loadedClass.newInstance();
					logger.debug("Successfully compiled: " + obj.toString());
					// Santity check
					/*
					 * if (obj instanceof DoStuff) { // Cast to the DoStuff
					 * interface DoStuff
					 * stuffToDo = (DoStuff) obj; // Run it baby
					 * stuffToDo.doStuff(); }
					 */
				} catch (final Throwable t) {
					logger.debug("Could not verify: " + f.toString() + " "
							+ t.toString());
				}
			}
			classLoader.close();
		} else {
			for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics
					.getDiagnostics()) {
				try {
					System.out.format("Error on line %d in %s%n", diagnostic
							.getLineNumber(), diagnostic.getSource().toUri());
				} catch (final Exception x) {
					logger.warn("Problem Generating Diagnostic for Object:"
							+ x);
				}
			}
		}
		fileManager.close();

	}
}
