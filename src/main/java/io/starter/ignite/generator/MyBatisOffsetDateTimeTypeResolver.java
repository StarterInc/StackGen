package io.starter.ignite.generator;

import java.sql.Types;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import io.starter.toolkit.StringTool;

public class MyBatisOffsetDateTimeTypeResolver extends org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl
		implements JavaTypeResolver, Configuration {

	@Override
	protected FullyQualifiedJavaType overrideDefaultType(IntrospectedColumn column,
			FullyQualifiedJavaType defaultType) {

		FullyQualifiedJavaType answer = super.overrideDefaultType(column, defaultType);

		switch (column.getJdbcType()) {
		case Types.VARCHAR:
			answer = calculateEnumStringType(column, defaultType);
			break;
		default:
			break;
		}

		return answer;
	}

	protected FullyQualifiedJavaType calculateEnumStringType(IntrospectedColumn column,
			FullyQualifiedJavaType defaultType) {
		FullyQualifiedJavaType answer;
		final String enumCheck = column.getRemarks();
		if (enumCheck.contains(Configuration.MYBATIS_COL_ENUM_FLAG)) {
			String secn = column.getActualColumnName();
			secn = DBGen.camelize(secn);
			secn = StringTool.getUpperCaseFirstLetter(secn);
			secn = secn + "Enum";

			final String tn = StringTool.getTextBetweenDelims(enumCheck, ":", ":");

			secn = tn + "." + secn;

			answer = new FullyQualifiedJavaType(secn);
		} else {
			answer = defaultType;
		}
		return answer;
	}

	@Override
	protected FullyQualifiedJavaType calculateTimestampType(IntrospectedColumn column,
			FullyQualifiedJavaType defaultType) {
		FullyQualifiedJavaType answer;

		if (useJSR310Types) {
			answer = new FullyQualifiedJavaType("java.time.OffsetDateTime");
		} else {
			answer = defaultType;
		}

		return answer;
	}

}