package io.starter.ignite.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.DMLgenerator.Table;
import io.swagger.models.Model;

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

	public MyBatisJoin(String field, Model src, Model ref) {
		this.source = src;
		this.target = ref;
		this.propName = field;
		this.refTypeName = ref.getTitle();
		this.myTable = DBGen.decamelize(src.getTitle());
		this.joinTable = DBGen.decamelize(ref.getTitle());

		this.selectName = "get" + this.refTypeName + "sFor" + src.getTitle();
		myColumn = field;

		tableName = Table.convertToDBSyntax(src.getTitle()) + joinTable
				+ Configuration.TUPLE_TABLE_SUFFIX;
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
				+ Configuration.API_MODEL_PACKAGE + "." + refTypeName
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
	public String getQueryXML() {
		return "<select id=\"" + selectName
				+ "\" parameterType=\"java.lang.Integer\" resultMap=\""
				+ Configuration.API_MODEL_PACKAGE + "." + refTypeName + "\">\n"
				+ "			SELECT R.*\n" + "			FROM " + this.myTable
				+ " R, " + this.joinTable + " U, " + tableName + " I\n"
				+ "			WHERE user_id\n"
				+ "			= #{id,jdbcType=INTEGER}\n" + "			AND I."
				+ this.myTable + "_id = U.id\n" + "			AND I."
				+ this.joinTable + "_id = R.id\n" + "		</select>";
	}

	/**
	 * this DML has a SQL statement that creates child objects
	 *
	 * TODO: enable lazy loading
	 *
	 * @return
	 */
	public String getDML() {
		String dml = Table.myMap.get("IDX_TEMPLATE");

		dml = dml.replace("${MY_TABLE}", myTable);
		dml = dml.replace("${REF_TABLE}", joinTable);
		dml = dml.replace("${IDX_TABLE}", tableName);

		logger.info("Created DML for MyBatisJoin : " + this.myTable + ":"
				+ this.joinTable);

		return dml;

	}

}
