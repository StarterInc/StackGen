package io.starter.ignite.generator;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import io.starter.stackgentest.model.User;

public class JavaGenTest {

	@Test
	@Ignore(value="serializing weird")
	public void getApiModelPropertyAnnotationFromField()
			throws NoSuchMethodException, SecurityException, NoSuchFieldException {

		final Class<? extends User> uc = io.starter.stackgentest.model.User.class;

		final Object o = Gen.getApiModelPropertyAnnotation(uc.getField("governmentId"));
		Assert.assertEquals(o.toString(),
				"@io.swagger.annotations.ApiModelProperty(dataField=\"\", secureField=false, access=\"\", notes=\"\", hidden=false, maxValue=1.7976931348623157E308, dataType=\"\", minLength=10, readOnly=false, accessMode=AUTO, required=false, example=1112233334, reference=\"\", extensions=[@io.swagger.annotations.Extension(name=\"\", properties=[@io.swagger.annotations.ExtensionProperty(name=\"\", value=\"\")])], minValue=4.9E-324, allowableValues=\"\", allowEmptyValue=false, name=\"\", position=0, value=\"a 10 digit government ID (encrypted)\", maxLength=10)");
	}

	@Test
	public void stringReplaceMethodText() {

		final String className = "User";
		Assert.assertEquals("int rows = \n" + "	getSelectByMapper() \n" + "	.deleteByPrimaryKey((long)getId()); \n"
				+ "	return rows", JavaGen.deleteMethodText(className));

		Assert.assertEquals("	getSelectByMapper() \n" + "	.insert(this.UserDelegate ); \n" + "	return getId()",
				JavaGen.insertMethodText(className));

		Assert.assertEquals("	// similar to old updateByExampleSelective method\n"
				+ "		int rows =  getSelectByMapper().update(c ->\n"
				+ "		io.starter.stackgen.model.dao.StackgenUserMapper.updateSelectiveColumns(this.UserDelegate , c)\n"
				+ "		.where(io.starter.stackgen.model.dao.StackgenUserDynamicSqlSupport.id,  isEqualTo(getId())));\n"
				+ "		return rows", JavaGen.updateMethodText(className));

		Assert.assertEquals(
				"this.UserDelegate = getSelectByMapper().selectByPrimaryKey(getId()).get();\n" + "		return this",
				JavaGen.loadMethodText(className));

	}
}