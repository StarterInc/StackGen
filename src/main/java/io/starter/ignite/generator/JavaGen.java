package io.starter.ignite.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import io.starter.ignite.generator.DMLgenerator.Table;
import io.starter.toolkit.StringTool;

/**
 * Generating Java code
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class JavaGen extends Gen implements Generator {

	public JavaGen(StackGenConfigurator cfg) {
		super(cfg);
	}

	protected static final Logger logger = LoggerFactory.getLogger(JavaGen.class);
	public String LINE_FEED = "\r\n";
	private Table table = new Table(config);

	/**
	 * Generate stack classes from another class
	 * 
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> createClasses(Class<?> c) throws Exception {
		final JavaGen gen = new JavaGen(config);
		final Map<?, ?> classesToGenerate = gen.processClasses(c, null, gen);
		return classesToGenerate;
	}

	@Override
	public Object createMember(Field fld) {
		return null;
	}

	@Override
	public Object createAccessor(Field fld) {

		final String fieldName = fld.getName();
		if (fieldName.startsWith("ajc$") || fieldName.equals("delegate") || fieldName.equals("serialVersionUID")) {
			return null;
		}

		// validate the column names for round trip compatibility
		validateColumn(fieldName);

		final String className = fld.getDeclaringClass().getName();
		String memberName = className.substring(className.lastIndexOf(".") + 1);
		memberName += "Delegate";

		final Class<?> fieldType = fld.getType();
		final String fldName = StringTool.getGetMethodNameFromVar(fieldName);

		try {
			final MethodSpec ret = MethodSpec.methodBuilder(fldName)
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date())
							+ config.LINE_FEED + config.LINE_FEED + "@see " + fld.getDeclaringClass().getName()
							+ config.LINE_FEED + config.LINE_FEED + "@return the value of: " + fieldName)
					.addModifiers(Modifier.PUBLIC).returns(fieldType)
					.addStatement("return " + memberName + "." + fieldName).build();
			return ret;
		} catch (final Exception e) {
			JavaGen.logger.error("COULD NOT GENERATE FIELD: " + memberName + " " + e.toString());
		}
		return null;
	}

	/**
	 * check validity of the column name (it will survive round trip codegen
	 * camel/decamel)
	 *
	 * @param fieldName
	 * @throws IgniteException
	 */
	private void validateColumn(final String fieldName) throws IgniteException {
		if (fieldName.equals(fieldName.toLowerCase()) || fieldName.equals(fieldName.toUpperCase())) { // case
																										// insensitive
			return;
		}
		final String checkName = table.convertToDBSyntax(fieldName);
		final String nameRoundTrip = table.convertToJavaSyntax(checkName);
		final String syntaxCheck = table.convertToDBSyntax(nameRoundTrip);

		if (!checkName.equals(syntaxCheck)) {
			throw new IgniteException(
					"COLUMN NAME IS UNSUPPORTED: " + checkName + ":" + nameRoundTrip + ":" + syntaxCheck);
		}
	}

	@Override
	public Object createSetter(Field fld) {

		final String fieldName = fld.getName();

		final String className = fld.getDeclaringClass().getName();
		String memberName = className.substring(className.lastIndexOf(".") + 1);
		memberName += "Delegate";

		if (fieldName.startsWith("ajc$") || fieldName.equals("delegate") || fieldName.equals("serialVersionUID")) {
			return null;
		}
		final Class<?> fieldType = fld.getType();
		final String fldNameSet = StringTool.getSetMethodNameFromVar(fieldName);

		try {
			final MethodSpec ret = MethodSpec.methodBuilder(fldNameSet)
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					// .addModifiers(Modifier.PUBLIC).addAnnotation(AnnotationSpec.builder(DataField.class).build())
					.addModifiers(Modifier.PUBLIC).addParameter(fieldType, fieldName + "Val")
					.addStatement(memberName + "." + fieldName + " = " + fieldName + "Val").build();

			return ret;
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR CREATING SETTER for: " + fieldName + " " + e.toString());
		}
		return null;
	}

	public FieldSpec createLoggerField(String className) throws ClassNotFoundException {

		final Class<?> cx = Class.forName("org.slf4j.Logger");
		return FieldSpec
				.builder(cx, "log").addModifiers(Modifier.PRIVATE).initializer("org.slf4j.LoggerFactory\n"
						+ "			.getLogger(" + className + config.ADD_GEN_CLASS_NAME + ".class)")
				.addAnnotation(getJSONIgnoreSpec()).build();
	}

	// ## MYBATIS INSERT/UPDATE/DELETE/LIST

	public FieldSpec createSQLSessionFactoryField() throws ClassNotFoundException {

		final Class<?> cx = Class.forName("org.apache.ibatis.session.SqlSessionFactory");
		return FieldSpec.builder(cx, "sqlSessionFactory").addModifiers(Modifier.PRIVATE)
				.addAnnotation(getJSONIgnoreSpec()).build();
	}

	public FieldSpec createMybatisSessionField() throws ClassNotFoundException {
		final Class<?> cx = Class.forName("org.apache.ibatis.session.SqlSession");
		return FieldSpec.builder(cx, "mybatisSession").addModifiers(Modifier.PRIVATE).addAnnotation(getJSONIgnoreSpec())
				.build();
	}

	/**
	 * create get object mapper method
	 *
	 * @param className
	 * @return
	 */
	public MethodSpec createGetObjectMapper(String className) {
		final String methodText = "objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY);"
				+ "\n" + "return objectMapper";
		try {

			final ClassName cx = ClassName.get("com.fasterxml.jackson.databind", "ObjectMapper");
			return MethodSpec.methodBuilder("getObjectMapper")
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText).returns(cx).build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating getObjectMapper method for: " + className + " " + e.toString());
		}
		return null;
	}

	/**
	 *
	 * create getAcceptHeader method
	 *
	 * @param className
	 * @return
	 */
	public MethodSpec createGetAcceptHeader(String className) {
		final String methodText = "return httpServletRequest.getHeader(\"accept\")";
		try {

			final ClassName cx = ClassName.get("java.lang", "String");
			return MethodSpec.methodBuilder("getAcceptHeader")
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText).addAnnotation(getJSONIgnoreSpec())
					.returns(cx).build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating getAcceptHeader method for: " + className + " " + e.toString());
		}
		return null;
	}

	/**
	 * create getHttpServletRequest mapper method
	 *
	 * @param className
	 * @return
	 */
	public MethodSpec createGetHttpServletRequest(String className) {
		final String methodText = "return httpServletRequest";

		try {

			final ClassName cx = ClassName.get("javax.servlet.http", "HttpServletRequest");
			return MethodSpec.methodBuilder("getHttpServletRequest")
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText).returns(cx).build();
		} catch (final Exception e) {
			JavaGen.logger
					.error("ERROR creating createGetHttpServletRequest method for: " + className + " " + e.toString());
		}
		return null;
	}

	/**
	 * create setDelegate method
	 */
	public MethodSpec createSetDelegate(String className) {

		JavaGen.getBaseJavaName(className);

		final String methodText = setDelegateText(className); // bname + "Delegate = (" + className + ")bx";
		try {
			return MethodSpec.methodBuilder("setDelegate")
					.addJavadoc("Starter StackGen 'JavaGen' Generated Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText).addParameter(Object.class, "bx").build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating setDelegate method for: " + className + " " + e.toString());
		}
		return null;
	}

	/**
	 * create getselectByMapper method
	 *
	 */
	public MethodSpec createGetSelectByMapper(String className) {
		final String methodText = getMapperText(className);
		try {
			return MethodSpec.methodBuilder("getSelectByMapper")
					.addJavadoc(config.GENERATED_TEXT_BLOCK + "  Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText)
					.returns(ClassName.get(config.getModelDaoPackage(),
							MyBatisGen.getMyBatisModelClassName(className, config) + "Mapper"))
					.build();
		} catch (final Exception e) {
			JavaGen.logger
					.error("ERROR creating createGetSelectByMapper method for: " + className + " " + e.toString());
		}
		return null;
	}
	/**
	 * create getDelegate method
	 *
	 */
	public MethodSpec createGetDelegate(String className) {
		final String l = JavaGen.getBaseJavaName(className);
		final String methodText = "return this." + l + "Delegate";
		try {
			return MethodSpec.methodBuilder("getDelegate")
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText)
					.returns(ClassName.get("java.lang", "Object")).build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating getDelegate method for: " + className + " " + e.toString());
		}
		return null;
	}

	/**
	 * create MyBatis list method
	 *
	 * @param className
	 * @return
	 */
	public MethodSpec createList(String className) {
		final String methodText = listMethodText(className);
		try {

			final ClassName cx = ClassName.get("java.util", "List");
			return MethodSpec.methodBuilder("list").addAnnotation(getPostFilterSpec())
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					// .addParameter(String.class, "searchParam")
					.addModifiers(Modifier.PUBLIC).addStatement(methodText).returns(cx).build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating list method for: " + className + " " + e.toString());
		}
		return null;
	}

	/**
	 * create MyBatis insert method
	 *
	 * @param className
	 * @return
	 */
	public MethodSpec createInsert(String className) {
		final String methodText = insertMethodText(className);
		try {
			return MethodSpec.methodBuilder("insert").addAnnotation(getPreAuthorizeSpec(className))
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText).returns(TypeName.LONG).build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating insert method for: " + className + " " + e.toString());
		}
		return null;
	}

	/**
	 * create MyBatis load method final modelDaoPackage.StarterUser ret = session
	 * .selectOne("modelDaoPackage.StarterUserMapper.selectByPrimaryKey", getId());
	 *
	 * @param className
	 * @return
	 */
	public MethodSpec createLoad(String className) {
		final String methodText = loadMethodText(className);
		try {
			final ClassName cx = ClassName.get(config.getIgniteModelPackage(), getJavaServiceName(className));
			return MethodSpec.methodBuilder("load").addAnnotation(getPostFilterSpec())
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText).returns(cx).build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating load method for: " + className + " " + e.toString());
		}
		return null;
	}

	/**
	 * create MyBatis delete method
	 *
	 * @param className
	 * @return
	 */
	public MethodSpec createDelete(String className) {
		final String methodText = deleteMethodText(className);
		try {
			return MethodSpec.methodBuilder("delete")
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					.addAnnotation(getPreAuthorizeSpec(className)).addModifiers(Modifier.PUBLIC)
					.addStatement(methodText).returns(TypeName.INT).build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating delete method for: " + className + " " + e.toString());
		}
		return null;
	}

	/**
	 * create MyBatis update method
	 *
	 * @param className
	 * @return
	 */
	public MethodSpec createUpdate(String className) {
		final String methodText = updateMethodText(className);
		try {
			return MethodSpec.methodBuilder("update").addAnnotation(getPreAuthorizeSpec(className))
					.addJavadoc(config.GENERATED_TEXT_BLOCK + " Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText).returns(TypeName.INT).build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating update method for: " + className + " " + e.toString());
		}
		return null;
	}

	// NEW MYBATIS 3.5 methods

	private String setDelegateText(String className) {
		final String l = JavaGen.getBaseJavaName(className);
		final String m = getMyBatisSQLMapsName(className);
		return String.format("	if(bx instanceof %1$s) {\n" + "		  final %2$s sgx = new %2$s();\n"
				+ "		  try{\n" + "org.springframework.util.ReflectionUtils.shallowCopyFieldState(bx, sgx);\n"
				+ "} catch (Exception e){\n" + "	throw new RuntimeException(\"Data Update Failure: \" + e);\n"
				+ "}\n" + "		  %1$sDelegate = sgx;\n" + "	} else {\n" + "		  %1$sDelegate = (%2$s)bx;\n"
				+ "	}\n", l, m);
	}

	public String getMapperText(String className) {
		final String m = getMyBatisSQLMapsName(className);

		String x = "if (sqlSessionFactory == null) {\n" + 
				"			sqlSessionFactory = io.starter.ignite.security.dao.MyBatisConnectionFactory.getSqlSessionFactory();\n" + 
				"			if (!sqlSessionFactory.getConfiguration()\n" + 
				"					.hasMapper(%1$sMapper.class)) {\n" + 
				"				sqlSessionFactory.getConfiguration()\n" + 
				"						.addMapper(%1$sMapper.class);\n" + 
				"			}\n" + 
				"		}\n" + 
				"		try{\n"
				+ "			if (connection == null || connection.isClosed()) {\n" + 
				"				connection = io.starter.ignite.security.dao.ConnectionFactory.getConnection();\n" + 
				"			}\n" + 
				"			mybatisSession = sqlSessionFactory.openSession(connection);\n" + "\n" + 
				"			return mybatisSession.getMapper(%1$sMapper.class);\n" + 
				"		} catch (java.sql.SQLException e) {\n" + 
				"			e.printStackTrace();\n" + 
				"		}\n" + 
				"		return null";
		
		return String.format(x, m);
		}

	public String loadMethodText(String className) {
		final String l = JavaGen.getBaseJavaName(className);
		final String m = getMyBatisSQLMapsName(className);

		return String.format(
				"this.%1$sDelegate = getSelectByMapper().selectByPrimaryKey(getId()).get();\n" 
				+ "if(connection !=null)try{connection.close();}catch(java.sql.SQLException e) {;}\n" 
				+ "		return this", l, m);
	}

	public String insertMethodText(String className) {
		final String l = JavaGen.getBaseJavaName(className);
		final String m = getMyBatisSQLMapsName(className);

		return String.format(
				"	getSelectByMapper() \n" + "	.insertSelective(this.%1$sDelegate ); \n" 
						+ "if(connection !=null)try{connection.close();}catch(java.sql.SQLException e) {;}\n" 
						+ "	return getId()", l, m);
	}

	public String deleteMethodText(String className) {
		final String l = JavaGen.getBaseJavaName(className);
		final String m = getMyBatisSQLMapsName(className);

		return String.format("int rows = \n" + "	getSelectByMapper() \n" + "	.deleteByPrimaryKey((long)getId()); \n"
				+ "if(connection !=null)try{connection.close();}catch(java.sql.SQLException e) {;}\n" 
				+ "	return rows", l, m);
	}

	public String updateMethodText(String className) {
		final String l = JavaGen.getBaseJavaName(className);
		final String m = getMyBatisSQLMapsName(className);

		return String.format("	// similar to old updateByExampleSelective method\n"
				+ "		int rows =  getSelectByMapper().update(c ->\n"
				+ "		%2$sMapper.updateSelectiveColumns(this.%1$sDelegate , c)\n"
				+ "		.where(%2$sDynamicSqlSupport.id,  isEqualTo(getId())));\n"
				+ "		if(connection !=null)try{connection.close();}catch(java.sql.SQLException e) {;}\n" 
				+ "		return rows", l, m);
	}

	public String listMethodText(String className) {
		final String l = JavaGen.getBaseJavaName(className);
		final String m = getMyBatisSQLMapsName(className);

		return String.format(
				"	final java.util.List<%2$s> rows =\n" + "		getSelectByMapper()\n" + "		.select(\n"
						+ "			c -> c\n" + "		);\n" + "	\n" + "	// wrap in our AccountService class\n"
						+ "	java.util.List ret = new java.util.ArrayList();\n" + "	for (%2$s u : rows) {\n"
						+ "		%1$sService ux = new %1$sService();\n" + "		ux.setDelegate(u);\n"
						+ "		u = null; // mark for gc\n" + "		ret.add(ux);\n" + "	}\n" 
						+ "if(connection !=null)try{connection.close();}catch(java.sql.SQLException e) {;}\n" 
						+ "\n" + "	return ret",
				l, m);

	}

	// TODO: implement search capability
	public String searchByMethodText(String className) {
		final String l = JavaGen.getBaseJavaName(className);
		final String m = getMyBatisSQLMapsName(className);

		return String.format("%1$sService us = new %1$sService();\n" + "		Optional<%2$s> ux = \n"
				+ "				us.getSelectByMapper() // we expose the full power of the MyBatis Dynamic SQL mapper\n"
				+ "				.selectOne(c -> c.where(%2$sDynamicSqlSupport." + l
				+ ", // use %2$sDynamicSqlSupport for all the col names\n"
				+ "		 isEqualTo(loginRequest.getUsername())));\n" + "		us.setDelegate(ux.get().delegate);\n"
				+ "		\n" + "		SpringUser tx = new SpringUser(us)", l, m);

	}

	AnnotationSpec preAuthorize = null;

	private AnnotationSpec getPreAuthorizeSpec(String obn) {
		if (preAuthorize == null) {
			preAuthorize = AnnotationSpec.builder(org.springframework.security.access.prepost.PreAuthorize.class)
					.addMember("value", "\"hasPermission(#" + obn + ", 'WRITE')\"").build();
		}
		return preAuthorize;
	}

	AnnotationSpec postFilter = null;

	private AnnotationSpec getPostFilterSpec() {
		if (postFilter == null) {
			postFilter = AnnotationSpec.builder(org.springframework.security.access.prepost.PostFilter.class)
					.addMember("value", "\"hasPermission(filterObject, 'READ')\"").build();
		}
		return postFilter;
	}

	AnnotationSpec autoWired = null;

	private AnnotationSpec getAutoWiredSpec() {
		if (autoWired == null) {
			autoWired = AnnotationSpec.builder(org.springframework.beans.factory.annotation.Autowired.class).build();
		}
		return autoWired;
	}

	AnnotationSpec JSONIgnored;

	private AnnotationSpec getJSONIgnoreSpec() {
		if (JSONIgnored == null) {
			JSONIgnored = AnnotationSpec.builder(com.fasterxml.jackson.annotation.JsonIgnore.class).build();
		}
		return JSONIgnored;
	}

	private MethodSpec createToJSON(String className) {
		final String mapperName = JavaGen.getBaseJavaName(className);
		final String methodText = "return " + mapperName + "Delegate.toJSON()";
		try {

			final ClassName cx = ClassName.get("java.lang", "String");
			return MethodSpec.methodBuilder("toJSON")
					.addJavadoc("Starter StackGen 'JavaGen' Generated Method: " + config.DATE_FORMAT.format(new Date()))
					.addModifiers(Modifier.PUBLIC).addStatement(methodText).returns(cx).build();
		} catch (final Exception e) {
			JavaGen.logger.error("ERROR creating toJSON method for: " + className + " " + e.toString());
		}
		return null;
	}

	private String getMyBatisSQLMapsName(String className) {

		return config.getModelDaoPackage() + "." + MyBatisGen.getMyBatisModelClassName(className, config);

	}

	private String getMyBatisName(final String className) {
		return config.getModelDaoPackage() + "." + MyBatisGen.getMyBatisModelClassName(className, config);
	}
	// ## END MYBATIS INSERT/UPDATE/DELETE/LIST

	@Override
	public synchronized void generate(String className, List<Object> fieldList, List<MethodSpec> getters,
			List<MethodSpec> setters)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		if (className.contains("ModelApi")) {
			JavaGen.logger.warn("Encountered non-data class... skipping: " + className);
			return;
		}

		// TODO: cleanup
		final int dotpos = className.lastIndexOf(".");
		String memberName = className.substring(dotpos + 1);
		final String memberType = memberName;
		memberName += "Delegate";

		// add the spring mvc fields
		final FieldSpec connection = createConnectionField();
		final FieldSpec objectMapper = createObjectMapperField();
		final FieldSpec httpServletRequest = createHttpServletRequestField();

		// include a static import

		// add the builtins
		final FieldSpec logr = createLoggerField(className);

		// add the MyBatis persistence methods
		final FieldSpec ssf = createSQLSessionFactoryField();
		final FieldSpec mbsx = createMybatisSessionField();

		// add the MyBatis Search Mapper Accessor
		final FieldSpec mbe = createMyBatisSearchMapperField(className);

		final List<MethodSpec> methodList = new ArrayList<>();
		if (ssf != null) {
			fieldList.add(logr);
			fieldList.add(ssf);
			fieldList.add(mbsx);
			fieldList.add(connection);
			fieldList.add(objectMapper);
			fieldList.add(httpServletRequest);
			fieldList.add(mbe);

			methodList.add(createGetSelectByMapper(className));
			methodList.add(createGetHttpServletRequest(className));
			methodList.add(createGetObjectMapper(className));
			methodList.add(createGetAcceptHeader(className));
			methodList.add(createSetDelegate(className));
			methodList.add(createGetDelegate(className));
			methodList.add(createList(className));
			methodList.add(createLoad(className));
			methodList.add(createInsert(className));
			methodList.add(createUpdate(className));
			methodList.add(createDelete(className));
			methodList.add(createToJSON(className));

			JavaGen.logger.info("Created " + methodList.size() + " generated methods");
		}
		// classes, this should point to the top of the package
		final URLClassLoader classLoader = new URLClassLoader(
				new URL[] { new File(config.getJavaGenSourceFolder()).toURI().toURL(),
						new File(config.getJavaGenResourcesFolder()).toURI().toURL() });

		String delegateInterfaceType = memberType;
		delegateInterfaceType = delegateInterfaceType.substring(delegateInterfaceType.lastIndexOf(".") + 1);
		delegateInterfaceType += config.SPRING_DELEGATE;
		final ClassName delegateInterfaceClass = ClassName.get(config.getApiPackage(), delegateInterfaceType);
		try {
			delegateInterfaceType = config.getApiPackage() + "." + delegateInterfaceType;
			classLoader.loadClass(delegateInterfaceType);
		} catch (final Exception x) {
			// delegateInterfaceClass = null;
			classLoader.close();
			throw new IgniteException("FATAL: Could not load the delegate class: " + delegateInterfaceType);
		}

		// instantiate the delegate class
		final Class<?> cxt = classLoader.loadClass(getMyBatisName(className));

		final FieldSpec member = FieldSpec.builder(cxt, memberName).addModifiers(Modifier.PRIVATE)
				.addAnnotation(JSONIgnored).initializer("new " + cxt.getCanonicalName() + "()").build();

		className = className.substring(dotpos + 1);
		className += config.ADD_GEN_CLASS_NAME;

		AnnotationSpec delegateAnnotation = AnnotationSpec.builder(org.springframework.stereotype.Service.class)
				.addMember("value", "$S", getJavaVariableName(className)).build();

		// handle edge case bug where reserved name collides with
		// our naming convention based code here
		if (className.startsWith("Model")) {
			JavaGen.logger
					.warn("Fixing name of delegate for 'Model' edge case. May break mappings that start with 'Model' ");
			delegateAnnotation = AnnotationSpec.builder(org.springframework.stereotype.Service.class)
					.addMember("value", "$S", getJavaVariableName(className.replace("Model", ""))).build();
		}

		// bean constructor
		final MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build();
		methodList.add(constructor);

		List<FieldSpec> fl = new ArrayList<FieldSpec>();
		for (Object f : fieldList) {
			fl.add((FieldSpec) f);
		}

		// create the Java Class
		final com.squareup.javapoet.TypeSpec.Builder builder = TypeSpec.classBuilder(className)
				.addModifiers(Modifier.PUBLIC).addField(member)
				// .superclass(null)
				.addJavadoc("StackGen 'JavaGen' Generated Class: " + config.LINE_FEED
						+ config.DATE_FORMAT.format(new Date()))
				.addFields(fl).addMethods(setters).addMethods(getters).addMethods(methodList);

		// finally associate the service class with the delegate
		if (delegateInterfaceClass != null) {
			builder.addSuperinterface(delegateInterfaceClass);
			builder.addAnnotation(delegateAnnotation);
		}

		// add static import(s)
		final ClassName mybatisWhereCondition = ClassName.get("org.mybatis.dynamic.sql", "SqlBuilder");

		JavaFile.builder(config.getIgniteModelPackage(), builder.build()).addStaticImport(mybatisWhereCondition, "*")
				.build().writeTo(config.JAVA_GEN_SRC);
		classLoader.close();
	}

	private FieldSpec createMyBatisSearchMapperField(String className) throws ClassNotFoundException {
		Class<?> cx;
		try {
			final String lnx = getMyBatisName(className);
			cx = loadClass(null, lnx + "Mapper");
		} catch (MalformedURLException | InstantiationException | IllegalAccessException e) {
			throw new IgniteException("FAILED TO LINK MyBatis Model");
		}
		return FieldSpec.builder(cx, "selectByMapper").addAnnotation(getJSONIgnoreSpec()).addModifiers(Modifier.PRIVATE)
				.build();
	}

	private FieldSpec createConnectionField() throws ClassNotFoundException {
		final Class<?> cx = Class.forName("java.sql.Connection");
		return FieldSpec.builder(cx, "connection").addAnnotation(getAutoWiredSpec()).addAnnotation(getJSONIgnoreSpec())
				.build();
	}

	private FieldSpec createObjectMapperField() throws ClassNotFoundException {
		final Class<?> cx = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
		return FieldSpec.builder(cx, "objectMapper").addAnnotation(getAutoWiredSpec())
				.addAnnotation(getJSONIgnoreSpec()).build();
	}

	private FieldSpec createHttpServletRequestField() throws ClassNotFoundException {
		final AnnotationSpec ano = getAutoWiredSpec();
		final Class<?> cx = Class.forName("javax.servlet.http.HttpServletRequest");
		return FieldSpec.builder(cx, "httpServletRequest").addAnnotation(ano).addAnnotation(getJSONIgnoreSpec())
				.build();
	}

	String getJavaVariableName(String n) {
		if (n.length() < 2) {
			return n;
		}
		n = n.replace(config.ADD_GEN_CLASS_NAME, "");
		final String firstChar = n.substring(0, 1).toLowerCase();
		return firstChar + n.substring(1) + config.SPRING_DELEGATE;
	}

	String getJavaServiceName(String n) {
		if (n.contains(".")) {
			n = n.substring(n.lastIndexOf(".") + 1);
		}
		return n + config.ADD_GEN_CLASS_NAME;
	}

	String getMyBatisJavaName(String n) {
		return MyBatisGen.getMyBatisModelClassName(n, config);
	}

	static String getBaseJavaName(String n) {
		return MyBatisGen.getBaseJavaName(n);
	}

	@Override
	public String toString() {
		return "Java Generator";
	}

	void generateClassesFromModelFolder() throws Exception {
		JavaGen.logger.info("Iterate MyBatis Entities and create Wrapper Classes...");

		final String[] modelFiles = getModelFileNames();
		// this should point to the top of the package structure!
		final URLClassLoader classLoader = new URLClassLoader(
				new URL[] { new File(config.getJavaGenSourceFolder()).toURI().toURL() });

		for (final String mf : modelFiles) {
			String cn = mf.substring(0, mf.indexOf("."));
			// cn = cn + ".class";
			cn = config.getModelPackage() + "." + cn;
			JavaGen.logger.info("Creating Classes from ModelFile: " + cn);

			// try {
			final Class<?> loadedClass = classLoader.loadClass(cn);
			createClasses(loadedClass);
			// } catch (final ClassNotFoundException e) {
			// logger.error("cd : " + cn + ": " + e.toString());
			// }
		}
		classLoader.close();
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
	void compile(String packageDir) throws IOException, ClassNotFoundException, InstantiationException, IgniteException,
			IllegalAccessException {
		// test
		final String sourcepath = config.getJavaGenSourceFolder() + "/" + packageDir;

		JavaGen.logger.info("JavaGen Compiling: " + sourcepath);

		// prepare compiler
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

		final Properties p = System.getProperties();
		p.getProperty("java.class.path");

		final List<String> optionList = new ArrayList<>();
		optionList.add("-classpath");

		Properties px = System.getProperties();

		logger.trace("COMPILING GENERATED FILES USING CP: " + System.getProperty("java.class.path"));

		optionList.add(System.getProperty("java.class.path") + System.getProperty("path.separator")
				+ config.getJavaGenSourceFolder() + System.getProperty("path.separator") + config.getSourceMainJava());

		final File[] fx = Gen.getJavaFiles(sourcepath, true);

		final Iterable<? extends JavaFileObject> compilationUnit = fileManager
				.getJavaFileObjectsFromFiles(Arrays.asList(fx));

		final JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnostics, optionList,
				null, compilationUnit);

		// Compilation Requirements
		JavaGen.logger.info("Compiling: " + sourcepath);

		URL[] paths = new URL[] { new File(config.getJavaGenSourceFolder()).toURI().toURL(),
				new File(config.getJavaGenResourcesFolder()).toURI().toURL(),
				new File(config.getModelPackageDir()).toURI().toURL(),
				new File(config.getModelDaoPackageDir()).toURI().toURL() };

		if (compilerTask.call()) {
			JavaGen.logger.info("Compilation Complete.");

			// load the newly compiled classes this should point to the
			// top of the package structure
			final URLClassLoader classLoader = new URLClassLoader(paths);

			for (final File f : fx) {
				// Load the class from the classloader by name....

				String loadClassName = f.getName().replace(".java", "");
				loadClassName = packageDir.replace('/', '.') + loadClassName;
				if (loadClassName.indexOf(".") == 0) {
					loadClassName = loadClassName.substring(1); // strip leading
				}
				// dot
				loadClass(classLoader, loadClassName);
			}
			classLoader.close();
		} else {
			for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
				String dsrc = "";
				try {
					dsrc = diagnostic.getSource().toUri().toString();
				} catch (final NullPointerException x) {
					dsrc = diagnostic.toString(); // ignore
				}
				if (dsrc != null
						&& (dsrc.contains("Swagger2SpringBoot.java") || dsrc.contains("CAL10NAnnotationProcessor"))) {
					// EXPECTED!
				} else {
					try {
						dsrc = String.format("Error on line %d in %s%n: %m", diagnostic.getLineNumber(), dsrc,
								diagnostic);
					} catch (final Exception e) {

					}
					logger.error("Compiling " + packageDir + " FAILED.  " + diagnostic.toString());
				}
			}
		}
		fileManager.close();
	}

	/**
	 * @param classLoader
	 * @param loadClassName
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	private Class<?> loadClass(URLClassLoader classLoader, String loadClassName)
			throws MalformedURLException, InstantiationException, IllegalAccessException {

		if (classLoader == null) {
			classLoader = new URLClassLoader(new URL[] { new File(config.getJavaGenSourceFolder()).toURI().toURL(),
					new File(config.getJavaGenResourcesFolder()).toURI().toURL() });
		}

		try {
			final Class<?> loadedClass = classLoader.loadClass(loadClassName);
			// Create a new instance...
			if (!loadedClass.isInterface()) {
				loadedClass.newInstance();
				JavaGen.logger.trace("Successfully compiled class: " + loadClassName); // obj.toString());
			} else {
				JavaGen.logger.info("Successfully compiled interface: " + loadClassName);
			}
			return loadedClass;
		} catch (final java.lang.NoClassDefFoundError nfe) {
			logger.error("Problem with class names: " + nfe.toString() + " : " + loadClassName);
		} catch (final InstantiationException nm) {
			// normal for no-default constructors
		} catch (final ClassNotFoundException e) {
			// normal
		}
		return null;
	}
}
