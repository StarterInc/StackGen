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
 * @author John McMahon (@TechnoCharms) mcmahon
 *
 */
public class MyBatisIgnitePluginAdapter extends PluginAdapter
		implements Configuration {

	protected static final Logger logger = LoggerFactory
			.getLogger(MyBatisIgnitePluginAdapter.class);

	public MyBatisIgnitePluginAdapter() {
		logger.error("Instantiating MyBatisIgnitePluginAdapter...");
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String scn = getSuperClassName(topLevelClass);
		Field f = new Field();
		f.setName("delegate");
		f.setVisibility(JavaVisibility.PUBLIC);
		f.setInitializationString(" new " + scn + "()");
		f.setType(new TopLevelClass(scn).getType());
		topLevelClass.addField(f);
		topLevelClass.addImportedType(scn);

		FullyQualifiedJavaType dtx = new FullyQualifiedJavaType(
				"java.util.Date");
		topLevelClass.addImportedType(dtx);

		FullyQualifiedJavaType dtd = new FullyQualifiedJavaType(
				"java.math.BigDecimal");
		topLevelClass.addImportedType(dtd);

		FullyQualifiedJavaType dti = new FullyQualifiedJavaType(
				"java.time.Instant");
		topLevelClass.addImportedType(dti);

		FullyQualifiedJavaType dto = new FullyQualifiedJavaType(
				"java.time.OffsetDateTime");
		topLevelClass.addImportedType(dto);

		FullyQualifiedJavaType dtz = new FullyQualifiedJavaType(
				"java.time.ZoneOffset");
		topLevelClass.addImportedType(dtz);

		Method tojson = new Method();
		tojson.addAnnotation("@Override");
		tojson.addBodyLine("return delegate.toJSON();");
		tojson.setName("toJSON");
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(
				"java.lang.String");
		JavaVisibility visibility = JavaVisibility.PUBLIC;
		tojson.setVisibility(visibility);
		tojson.setReturnType(returnType);
		topLevelClass.addMethod(tojson);

		return true;
	}

	/**
	 * get the superclass name
	 * 
	 * @param topLevelClass
	 * @return
	 */
	private String getSuperClassName(TopLevelClass topLevelClass) {

		String cn = topLevelClass.getType().getFullyQualifiedName();

		// handle stripping out the Schema name in the delegate
		// class
		if (cn.toLowerCase().contains(schemaName)) {
			// the schema is always lowercase, so adjust it for
			// Camelcase
			String ccsn = StringTool.proper(schemaName);
			cn = cn.replace(ccsn, "");

			// replace package with actual delegate model package
			cn = cn.replace(MODEL_DAO_PACKAGE, MODEL_PACKAGE);

			if (cn.contains("..")) {
				throw new IllegalStateException(
						"Could not get getSuperClassName due to package collision: "
								+ cn + ". Change value of schemaName: "
								+ schemaName);
			}
		}

		logger.info("SuperClass Name MYBATIS member: " + cn);
		return cn;
	}

	@Override
	public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType) {
		String secn = introspectedColumn.getRemarks();

		List<String> ln = method.getBodyLines();
		for (String l : ln) {
			method.getBodyLines().remove(l);
			String cnm = introspectedColumn.getJdbcTypeName().toUpperCase();
			l = l.replace("return ", "return delegate.");
			if (cnm.contains("DATE") || cnm.contains("TIMESTAMP")) {
				l = l.replace(";", ".toInstant().getNano() / 1000000);");
				l = l.replace("return delegate.", "return new Date(delegate.");
			} else if (secn.contains(MYBATIS_COL_ENUM_FLAG)) {
				l = l.replace(";", ".getValue();");
			}
			method.addBodyLine(l);
		}
		return super.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
	}

	@Override
	public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType) {

		List<String> ln = method.getBodyLines();

		String cnm = introspectedColumn.getJdbcTypeName().toUpperCase();
		String secn = introspectedColumn.getRemarks();

		List<String> it = ln.subList(0, ln.size());
		for (String l : it) {
			method.getBodyLines().remove(l);
			l = l.replace("this.", "delegate.");
			String membername = l.substring(0, l.indexOf("="));
			String varname = membername.substring(membername.indexOf(".") + 1);
			if (cnm.contains("DATE") || cnm.contains("TIMESTAMP")) {

				method.addBodyLine("Instant instant = " + varname
						+ ".toInstant();");
				method.addBodyLine("String TIMEZONE_OFFSET = \""
						+ TIMEZONE_OFFSET + "\";");
				method.addBodyLine("ZoneOffset offset = ZoneOffset.of(TIMEZONE_OFFSET);");

				String bl = "OffsetDateTime odt = OffsetDateTime.ofInstant(instant, offset);";
				method.addBodyLine(bl);
				String stx = membername + " = odt;";
				method.addBodyLine(stx);

			} else if (secn.contains(MYBATIS_COL_ENUM_FLAG)) {
				method.addBodyLine(getEnumHandling(varname, introspectedTable
						.getAliasedFullyQualifiedTableNameAtRuntime()));
			} else {
				method.addBodyLine(l);
			}

		}

		return super.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
	}

	@Override
	public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {

		logger.info("MyBatisIgnitePluginAdapter Generating: " + field + " name:"
				+ field.getName() + LINE_FEED + " class:"
				+ field.getType().getShortName());

		field.setVisibility(JavaVisibility.PROTECTED);
		if (ANNOTATAION_CLASS != null) {
			topLevelClass.addImportedType(new FullyQualifiedJavaType(
					ANNOTATAION_CLASS));
		}
		if (field.getType().getFullyQualifiedName()
				.equals("java.lang.String")) {
			field.addAnnotation("@" + ANNOTATAION_CLASS);
		} else if (field.getType().equals("java.util.Date")) {
			logger.warn("MyBatisIgnitePluginAdapter SecureField TODO: handle dates: "
					+ field);
		}

		return false;
	}

	private String getEnumHandling(String enumName, String tableName) {
		tableName = tableName.substring(TABLE_NAME_PREFIX.length());
		tableName = DBGen.camelize(tableName);
		tableName = StringTool.getUpperCaseFirstLetter(tableName.trim());

		enumName = enumName.trim();

		String setEnumValMethod = StringTool.getSetMethodNameFromVar(enumName);

		String fullEnumName = tableName + "."
				+ StringTool.getUpperCaseFirstLetter(enumName.trim());;
		String enumcode = "delegate." + setEnumValMethod.trim() + "("
				+ fullEnumName + "Enum.fromValue(" + enumName
				+ " == null ? null : " + enumName + ".trim()));";
		return enumcode;
	}

	@Override
	public boolean validate(List<String> warnings) {
		logger.info("MyBatis Warnings: ");
		for (String w : warnings) {
			logger.info(w);
		}
		logger.info("End MyBatis Warnings");
		return true;
	}
}
