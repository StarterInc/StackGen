package io.starter.ignite.generator;

import io.swagger.models.ModelImpl;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.DMLgenerator.Table;
import io.swagger.models.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * handle the generation of the "glue" between 2 tables using joins and optionally a tuple or "IDX" table to 
 * handle many-to-many relationships
 *
 *
<pre>

	<mapper namespace="io.starter.ignite.model.dao.StarterRoleMapper" >

	 	<resultMap id="roleObject" type="io.starter.model.Role">
			<id column="id" jdbcType="INTEGER" property="id" />
			...
		</resultMap>

		<select id="getRolesForUser" parameterType="java.lang.Integer" resultMap="roleObject">
			SELECT R.*
			FROM role R, user U, user_role_idx I
			WHERE user_id
			= #{id,jdbcType=INTEGER}
			AND I.user_id = U.id
			AND I.role_id = R.id
		</select>
</pre>
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class MyBatisJoin {

	protected static final Logger	logger		= LoggerFactory
			.getLogger(MyBatisJoin.class);

	StackGenConfigurator config;
	Model							source		= null;
	Model							target		= null;

	String							joinTable	= "undefined";
	String							joinColumn	= "undefined";
	String							myTable		= "undefined";
	String							myColumn	= "undefined";
	String							tableName	= "undefined";
	private String					refTypeName	= "undefined";
	private String					selectName	= "undefined";

	private String					propName	= "undefined";
	String TUPLE_TABLE_SUFFIX = "_idx";
	public void getMyBatisDBJoin(String field, Model src, Model ref, StackGenConfigurator config) {
		Table table = new Table(config);
		this.source = src;
		this.target = ref;
		this.config = config;
		this.propName = DBGen.decamelize(field);
		if(propName.startsWith("_")){
			propName = propName.substring(1);
		}

		this.refTypeName = ref.getTitle();
		if(refTypeName.startsWith("_")){
			refTypeName = refTypeName.substring(1);
		}

		this.myTable = DBGen.decamelize(src.getTitle());
		this.joinTable = DBGen.decamelize(ref.getTitle());

		this.selectName = "get" + this.refTypeName + "sFor" + src.getTitle();
		myColumn = field;
		tableName = table.convertToDBSyntax(src.getTitle()) + joinTable
				+ TUPLE_TABLE_SUFFIX;
	}

	public MyBatisJoin(Swagger swagger, String field, Model src, Model ref, StackGenConfigurator config) {
		Table table = new Table(config);
		this.source = src;
		this.target = ref;
		this.config = config;
		this.propName = field;

		this.refTypeName = ref.getTitle();

		this.myTable = src.getTitle();
		this.joinTable = ref.getTitle();



		this.selectName = "get" + this.refTypeName + "sFor" + src.getTitle();
		myColumn = field;
		tableName = src.getTitle() + joinTable
				+ "Idx";

		swagger.addDefinition(tableName, getModel(table) );
	}


	/**
	 * this XML must be inserted in the Source table Mapping section.
	 *
	 * we are defining a field in the mapped output object that is derived from a subquery select
	 *
	 * TODO: enable lazy loading
	 *
	 * @return the XML to insert into the source mapping
	 */
	public String getXML() {
		return "<collection column=\"id\" javaType=\"ArrayList\"" + " ofType=\""
				+ config.getApiModelPackage() + "." + refTypeName
				+ "\" property=\"" + propName + "\" select=\"" + selectName
				+ "\" />";
	}

	/**
	 * this XML has a SQL statement that creates child objects
	 *
	 * TODO: enable lazy loading
	 *
	 * @return
	 */
	public String getQueryXML(StackGenConfigurator config) {
		return "<select id=\"" + selectName
				+ "\" parameterType=\"java.lang.Integer\" resultMap=\""
				+ config.getApiModelPackage() + "." + refTypeName + "\">\n"
				+ "			SELECT R.*\n" + "			FROM " + this.myTable
				+ " R, " + this.joinTable + " U, " + tableName + " I\n"
				+ "			WHERE user_id\n"
				+ "			= #{id,jdbcType=INTEGER}\n" + "			AND I."
				+ this.myTable + "_id = U.id\n" + "			AND I."
				+ this.joinTable + "_id = R.id\n" + "		</select>";
	}

	/**
	 * Model has the ids for the linked tables
	 *
	 * @return
	 */
	public Model getModel(Table table) {
		Model retMod = new ModelImpl();
		String mt = myTable;
		String jt = joinTable;

		retMod.setTitle(myTable + joinTable + "Idx");

		retMod.setDescription("Index table for table relationship (generated)");
		// retMod.setExample("");
		// retMod.setReference("");

		Map<String, Property> props = new HashMap<String, Property>();

		Map<PropertyBuilder.PropertyId, Object> sargs = new HashMap();

		Property idField = PropertyBuilder.build(
				"integer",
				"int64",
				sargs
		);
		idField.setName("id");
		idField.setAllowEmptyValue(false);
		idField.setRequired(true);

		// relying on auto-generated id downstream
		// props.put("id", idField);

		Property sourceTable = PropertyBuilder.build(
				"integer",
				"int64",
				sargs
		);
		sourceTable.setName(mt);
		sourceTable.setDescription("The id in the " + myTable + " table which owns the " + joinTable + " entry.");
		sourceTable.setAllowEmptyValue(false);
		sourceTable.setRequired(true);
		props.put(mt, sourceTable);

		Property joinTable = PropertyBuilder.build(
				"integer",
				"int64",
				sargs
		);
		joinTable.setName(jt);
		joinTable.setDescription("The id in the " + joinTable + " table which is related to the " + myTable + " entry.");
		joinTable.setAllowEmptyValue(false);
		joinTable.setRequired(true);
		props.put(jt, joinTable);

		logger.info("Created Model for Swagger Join : " + sourceTable + ":"
				+ joinTable);

		retMod.setProperties(props);

		return retMod;

	}

	/**
	 * this DML has a SQL statement that creates child objects
	 *
	 * @return
	 */
	public String getDML() {
		String dml = Table.myMap.get("IDX_TEMPLATE");
		String mt = myTable.substring(1);
		String jt = joinTable.substring(1);
		dml = dml.replace("${MY_TABLE}", mt);
		dml = dml.replace("${REF_TABLE}", jt);
		dml = dml.replace("${IDX_TABLE}", tableName);

		logger.info("Created DML for MyBatisJoin : " + this.myTable + ":"
				+ this.joinTable);

		return dml;

	}

}
