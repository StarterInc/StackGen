package io.starter.ignite.generator;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.starter.stackgentest.model.User;

public class JavaGenTest {
	
	StackGenConfigurator config = new StackGenConfigurator();
	
	@Before
	public void setup() {

		config.setSchemaName("stackgen");
		config.setArtifactId("stackgen");
	}
	
	@Test
	@Ignore(value="serializing weird")
	public void getApiModelPropertyAnnotationFromField()
			throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		final Class<? extends User> uc = io.starter.stackgentest.model.User.class;

		final Object o = Gen.getApiModelPropertyAnnotation(uc.getField("governmentId"));
		Assert.assertEquals(o.toString(),
				"@io.starter.ignite.generator.annotations.StackgenModelProperty(dataField=\"\", secureField=false, access=\"\", notes=\"\", hidden=false, maxValue=1.7976931348623157E308, dataType=\"\", minLength=10, readOnly=false, accessMode=AUTO, required=false, example=1112233334, reference=\"\", extensions=[@io.swagger.annotations.Extension(name=\"\", properties=[@io.swagger.annotations.ExtensionProperty(name=\"\", value=\"\")])], minValue=4.9E-324, allowableValues=\"\", allowEmptyValue=false, name=\"\", position=0, value=\"a 10 digit government ID (encrypted)\", maxLength=10)");
	}

	@Test
	public void stringReplaceMethodText() {

		
		JavaGen jg = new JavaGen(config);
		
		final String className = "User";
		Assert.assertEquals(
				"int rows = \n" + 
				"	getSelectByMapper() \n" + 
				"	.deleteByPrimaryKey((long)getId()); \n" + 
				"if(connection !=null)try{connection.close();}catch(java.sql.SQLException e) {;}\n" + 
				"	return rows", jg.deleteMethodText(className));

		Assert.assertEquals("	getSelectByMapper() \n" + "	.insertSelective(this.UserDelegate ); \n" + "if(connection !=null)try{connection.close();}catch(java.sql.SQLException e) {;}\n" + 
				"	return getId()",
				jg.insertMethodText(className));

		Assert.assertEquals("	// similar to old updateByExampleSelective method\n"
				+ "		int rows =  getSelectByMapper().update(c ->\n"
				+ "		io.starter.stackgen.model.dao.StackgenUserMapper.updateSelectiveColumns(this.UserDelegate , c)\n"
				+ "		.where(io.starter.stackgen.model.dao.StackgenUserDynamicSqlSupport.id,  isEqualTo(getId())));\n"
				+ "		if(connection !=null)try{connection.close();}catch(java.sql.SQLException e) {;}\n" 
				+ "		return rows", jg.updateMethodText(className));

		Assert.assertEquals(
				" java.util.Optional<?> ret = getSelectByMapper().selectByPrimaryKey(getId());\n" + 
				"	  if(!ret.isPresent()) {\n" + 
				"		  return null;\n" + 
				"	  }\n" + 
				"this.UserDelegate = (StackgenUser) ret.get();\n" + 
				"if(connection !=null)\n" + 
				"try{\n" + 
				"connection.close();\n"
				+ "}catch(java.sql.SQLException e) {;}\n" 
				+ "		return this",
				jg.loadMethodText(className));

	}
}