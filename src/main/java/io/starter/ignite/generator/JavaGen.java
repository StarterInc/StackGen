package io.starter.ignite.generator;

import io.starter.ignite.model.DataField;
import io.starter.ignite.util.RunCommand;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import com.extentech.toolkit.StringTool;
import com.squareup.javapoet.*;

/**
 * Generating Java code
 * 
 * @author john
 *
 */
public class JavaGen extends Gen implements Generator {

	static String className = null;
	static String memberName = null;
	static String memberType = null;

	public static Map createClasses(Class c) throws Exception {
		className = c.getName();
		String cname = className.substring(className.lastIndexOf("."));

		// define the delegate member variable (from the MyBatis-generated
		// model)
		memberName = className.substring(className.lastIndexOf(".") + 1);
		// memberName = StringTool.proper(memberName);
		memberType = GEN_MODEL_PACKAGE + memberName;

		memberName += "Bean";

		JavaGen gen = new JavaGen();
		Map classesToGenerate = gen.processClasses(c, null, gen);
		return classesToGenerate;
	}

	@Override
	public Object createValue(Field fld) {
		String fieldName = fld.getName();
		Class fieldType = fld.getType();

		FieldSpec field = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE)
				.addJavadoc("Starter Ignite Generated Field: " + DATE_FORMAT.format(new Date())).build();

		return field;
	}

	@Override
	public Object createAccessor(Field fld) {

		String fieldName = fld.getName();
		if (fieldName.startsWith("ajc$")) // skip aspects
			return null;

		Class fieldType = fld.getType();
		String fldName = StringTool.proper(fieldName);
		fldName = "get" + fldName;

		MethodSpec ret = MethodSpec.methodBuilder(fldName)
				.addJavadoc("Starter Ignite Generated Method: " + DATE_FORMAT.format(new Date()))
				.addModifiers(Modifier.PUBLIC).returns(fieldType)
				.addAnnotation(AnnotationSpec.builder(DataField.class).build())
				.addStatement("return " + memberName + "." + fieldName).build();

		return ret;
	}

	@Override
	public Object createSetter(Field fld) {

		String fieldName = fld.getName();
		if (fieldName.startsWith("ajc$")) // skip aspects
			return null;
		Class fieldType = fld.getType();
		String fldName = StringTool.proper(fieldName);
		String fldNameSet = "set" + fldName;

		MethodSpec ret = MethodSpec.methodBuilder(fldNameSet)
				.addJavadoc("Starter Ignite Generated Method: " + DATE_FORMAT.format(new Date()))
				.addModifiers(Modifier.PUBLIC).addAnnotation(AnnotationSpec.builder(DataField.class).build())
				.addParameter(fieldType, fieldName + "Val")
				.addStatement(memberName + "." + fieldName + " = " + fieldName + "Val").build();

		return ret;
	}

	@Override
	public void generate(String className, List fieldList, List getters, List setters)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String packageName = null;
		int dotpos = className.lastIndexOf(".");
		packageName = className.substring(0, dotpos);

		// packageName = ADD_GEN_PACKAGE_NAME + packageName;

		className = className.substring(dotpos + 1);

		className += ADD_GEN_CLASS_NAME;

		// instantiate the delegate class
		String memberPkg = memberType.substring(0, memberType.lastIndexOf("."));

		Class cx = Class.forName(memberType);
		FieldSpec member = FieldSpec.builder(cx, memberName).addModifiers(Modifier.PRIVATE).build();

		// create the Java Class
		TypeSpec type = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).addField(member)
				// .superclass(null)
				.addJavadoc("Starter Ignite Generated Class: " + DATE_FORMAT.format(new Date())).addFields(fieldList)
				.addMethods(setters).addMethods(getters).build();

		JavaFile.builder(packageName, type).build().writeTo(JAVA_GEN_SRC);

	}

	@Override
	public String toString() {
		return "Java Generator";
	}

	static void generateClassesFromModelFolder() throws Exception {
		io.starter.ignite.util.Logger.log("Iterate MyBatis Entities and create Wrapper Classes...");

		String[] modelFiles = MyBatisGen.getModelFiles();
		for (String mf : modelFiles) {
			String cn = mf.substring(0, mf.indexOf("."));
			// cn = cn + ".class";
			cn = "io.starter.ignite.model." + cn;
			io.starter.ignite.util.Logger.log("Creating Classes from ModelFile: " + cn);

			try {

				createClasses(Class.forName(cn));
			} catch (ClassNotFoundException e) {
				System.err.println("FAILURE Creating Classes from ModelFile: " + cn);
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
	static void compile(String packageDir)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		// test
		String sourcepath = JAVA_GEN_SRC_FOLDER + packageDir;

		io.starter.ignite.util.Logger.log("JavaGen Compiling: " + sourcepath);
		String[] cmdarray = new String[] { "javac", sourcepath + "User.java", "-sourcepath", sourcepath, "-classpath",
				System.getProperty("java.class.path") };

		// RunCommand.runSafe(cmdarray[0], cmdarray);

		// prepare compiler
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

		// TODO: add dependencies
		List<String> optionList = new ArrayList<String>();
		optionList.add("-classpath");
		optionList.add(System.getProperty("java.class.path") + ";gen/src/main/java");

		// File[] fx = new File(sourcepath).listFiles();
		File[] fx = getFilesInFolder(new File(sourcepath), Configuration.SKIP_LIST);

		Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(fx));

		JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnostics, optionList, null,
				compilationUnit);

		// Compilation Requirements
		io.starter.ignite.util.Logger.log("Compiling: " + sourcepath);
		if (compilerTask.call()) {
			io.starter.ignite.util.Logger.log("Compilation Complete.");

			// classes, this should point to the top of the package structure!
			URLClassLoader classLoader = new URLClassLoader(
					new URL[] { new File(JAVA_GEN_SRC_FOLDER).toURI().toURL() });

			for (File f : fx) {
				// Load the class from the classloader by name....

				String loadClassName = f.getName().substring(0, f.getName().length() - 5); // strip "java"
				loadClassName = packageDir.replace('/', '.') + loadClassName;
				loadClassName = loadClassName.substring(1); // strip leading dot
				try {
					Class<?> loadedClass = classLoader.loadClass(loadClassName);
					// Create a new instance...
					Object obj = loadedClass.newInstance();
					io.starter.ignite.util.Logger.log("Successfully compiled: " + obj.toString());
					// Santity check
					/*
					 * if (obj instanceof DoStuff) { // Cast to the DoStuff interface DoStuff
					 * stuffToDo = (DoStuff) obj; // Run it baby stuffToDo.doStuff(); }
					 */
				} catch (Exception e) {
					io.starter.ignite.util.Logger.log("Could not verify: " + f.toString());
				}
			}
			classLoader.close();
		} else {
			for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
				System.out.format("Error on line %d in %s%n", diagnostic.getLineNumber(),
						diagnostic.getSource().toUri());
			}
		}
		fileManager.close();

	}
}
