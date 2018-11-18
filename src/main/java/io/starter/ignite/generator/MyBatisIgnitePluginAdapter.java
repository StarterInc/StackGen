/**
 * 
 */
package io.starter.ignite.generator;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author john
 *
 */
public class MyBatisIgnitePluginAdapter extends PluginAdapter {

	protected static final Logger	logger				= LoggerFactory
			.getLogger(MyBatisIgnitePluginAdapter.class);

	public static final String		ANNOTATION_CLASS	= "annotationClass";
	public static final String		ANNOTATION_STRING	= "annotationString";
	private String					annotationClass;
	private String					annotationString;

	public MyBatisIgnitePluginAdapter() {
		logger.error("Instantiating MyBatisIgnitePluginAdapter...");
	}

	@Override
	public boolean validate(List<String> warnings) {
		annotationClass = properties.getProperty(ANNOTATION_CLASS);
		annotationString = properties.getProperty(ANNOTATION_STRING);
		return true;
	}

	@Override
	public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {

		if (Configuration.DEBUG)
			logger.error("MyBatisIgnitePluginAdapter Generating: " + field
					+ " class:" + field.getType().getShortName());

		field.setVisibility(JavaVisibility.PROTECTED);
		if (annotationClass != null) {
			topLevelClass.addImportedType(new FullyQualifiedJavaType(
					annotationClass));
		}
		if (field.getType().getFullyQualifiedName()
				.equals("java.lang.String")) {
			field.addAnnotation(annotationString);
		} else if (field.getType().equals("java.util.Date")) {
			logger.warn("MyBatisIgnitePluginAdapter SecureField TODO: handle dates: "
					+ field);
		}

		return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
	}
}
