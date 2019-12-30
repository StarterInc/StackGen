/**
 *
 */
package io.starter.ignite.generator;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.toolkit.StringTool;

/**
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms mcmahon
 *
 */
public class MyBatisIgnitePluginAdapter extends PluginAdapter implements Configuration {

	protected static final Logger logger = LoggerFactory.getLogger(MyBatisIgnitePluginAdapter.class);
	private final boolean useDelegate = false;

	public MyBatisIgnitePluginAdapter() {
		MyBatisIgnitePluginAdapter.logger.error("Instantiating MyBatisIgnitePluginAdapter...");
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

		// we are replacing the super type but still need the original import
		topLevelClass.addImportedType(topLevelClass.getType());

		final String scn = MyBatisIgnitePluginAdapter.getSuperClassName(topLevelClass);
		final FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(scn);
		topLevelClass.addImportedType(superClass);
		topLevelClass.setSuperClass(superClass);

		final FullyQualifiedJavaType dtx = new FullyQualifiedJavaType("java.util.Date");
		topLevelClass.addImportedType(dtx);

		final FullyQualifiedJavaType dtd = new FullyQualifiedJavaType("java.math.BigDecimal");
		topLevelClass.addImportedType(dtd);

		final FullyQualifiedJavaType dti = new FullyQualifiedJavaType("java.time.Instant");
		topLevelClass.addImportedType(dti);

		final FullyQualifiedJavaType dto = new FullyQualifiedJavaType("java.time.OffsetDateTime");
		topLevelClass.addImportedType(dto);

		final FullyQualifiedJavaType dtz = new FullyQualifiedJavaType("java.time.ZoneOffset");
		topLevelClass.addImportedType(dtz);

		// add custom methods
		if (useDelegate) {

			final Field f = new Field("delegate", superClass);
			f.setVisibility(JavaVisibility.PUBLIC);
			f.setInitializationString(" new " + scn + "()");
			topLevelClass.addField(f);

			final Method tojson = new Method("toJSON");
			tojson.addAnnotation("@Override");
			tojson.addBodyLine("return delegate.toJSON();");

			final FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.String");
			final JavaVisibility visibility = JavaVisibility.PUBLIC;
			tojson.setVisibility(visibility);
			tojson.setReturnType(returnType);
			topLevelClass.addMethod(tojson);

			final Method getdelegate = new Method("getDelegate");
			getdelegate.addAnnotation("@Override");
			getdelegate.addBodyLine("return delegate;");
			getdelegate.setVisibility(visibility);

			final FullyQualifiedJavaType ret = new FullyQualifiedJavaType("io.starter.ignite.model.DataModelObject");
			topLevelClass.addImportedType(ret);

			getdelegate.setReturnType(ret);
			topLevelClass.addMethod(getdelegate);

		}
		return true;
	}

	/**
	 * get the superclass name
	 *
	 * @param topLevelClass
	 * @return
	 */
	public static String getSuperClassName(TopLevelClass topLevelClass) {

		String cn = topLevelClass.getType().getFullyQualifiedName();
		final String ccsn = "dao." + StringTool.getUpperCaseFirstLetter(Configuration.schemaName);
		if (!cn.contains(ccsn)) {
			throw new RuntimeException("Could Not Strip DAO Package Name from the MyBatis DAO delegate field name");
		}
		cn = cn.replace(ccsn, "");

		if (cn.contains("..")) {
			throw new IllegalStateException("Could not get getSuperClassName due to package collision: " + cn
					+ ". Change value of schemaName: " + Configuration.schemaName);
		}

		MyBatisIgnitePluginAdapter.logger.info("SuperClass Name MYBATIS member: " + cn);
		return cn;
	}

	@Override
	public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable,
			Plugin.ModelClassType modelClassType) {

		final List<String> ln = method.getBodyLines();
		for (String l : ln) {
			method.getBodyLines().remove(l);
			if (useDelegate) {
				l = l.replace("return ", "return delegate.");
			}
			method.addBodyLine(l);
		}
		return super.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable,
				modelClassType);
	}

	@Override
	public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable,
			Plugin.ModelClassType modelClassType) {

		final List<String> ln = method.getBodyLines();

		final List<String> it = ln.subList(0, ln.size());
		for (String l : it) {
			method.getBodyLines().remove(l);
			if (useDelegate) {
				l = l.replace("this.", "delegate.");
			}
			method.addBodyLine(l);
		}
		return super.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable,
				modelClassType);
	}

	@Override
	public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, ModelClassType modelClassType) {

		MyBatisIgnitePluginAdapter.logger.info("MyBatisIgnitePluginAdapter Generating: " + field + " name:"
				+ field.getName() + Configuration.LINE_FEED + " class:" + field.getType().getShortName());

		field.setVisibility(JavaVisibility.PROTECTED);

		return false;
	}

	@Override
	public boolean validate(List<String> warnings) {
		MyBatisIgnitePluginAdapter.logger.info("MyBatis Warnings: ");
		for (final String w : warnings) {
			MyBatisIgnitePluginAdapter.logger.info(w);
		}
		MyBatisIgnitePluginAdapter.logger.info("End MyBatis Warnings");
		return true;
	}
}
