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

/**
 * @author john
 *
 */
public class MyBatisIgnitePluginAdapter extends PluginAdapter {

	public static final String ANNOTATION_CLASS = "annotationClass";
	public static final String ANNOTATION_STRING = "annotationString";
	private String annotationClass;
	private String annotationString;

	public MyBatisIgnitePluginAdapter() {
		System.err.println("Instantiating MyBatisIgnitePluginAdapter...");
	}

	@Override
	public boolean validate(List<String> warnings) {
		annotationClass = properties.getProperty(ANNOTATION_CLASS);
		annotationString = properties.getProperty(ANNOTATION_STRING);
		return true;
	}

	@Override
	public boolean modelFieldGenerated(Field field,
			TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, ModelClassType modelClassType) {

		if (Configuration.DEBUG)
			System.err.println("MyBatisIgnitePluginAdapter Generating: "
					+ field + " class:"
					+ field.getType().getFullyQualifiedName());

		field.setVisibility(JavaVisibility.PROTECTED);
		topLevelClass.addImportedType(new FullyQualifiedJavaType(
				annotationClass));
		if (field.getType().getFullyQualifiedName().equals("java.lang.String")) {
			field.addAnnotation(annotationString);
		} else if (field.getType().equals("java.util.Date")) {
			System.err
					.println("MyBatisIgnitePluginAdapter TODO: handle dates: "
							+ field);
		}

		return super.modelFieldGenerated(field, topLevelClass,
				introspectedColumn, introspectedTable, modelClassType);
	}
}
