package io.starter.ignite.generator.swagger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.StackGenConfigurator;
import io.starter.ignite.generator.MyBatisJoin;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

/**
 * 
 *  * StackGen Model support for ONE-TO-MANY and MANY-TO-MANY relationships 
 * can be expressed as a many-to-many with a unique constraint on the "ONE" field
 * 
 * 
 * 2 stages: 
 * - create "fake" swagger objects for IDX tables, let MyBatis and DML go forth
 * - do 2nd pass on MyBatis XML output and add the proper many-to-many hydration mappings
 * 
 *  - when we encounter a FK_ID field in the Swagger ,we need to implement a many-to-many IDX table
 *  - when we encounter a one-to many "xxxx_id" relationship -- ie: owner_id
 * 
 * <pre>	
 * <resultMap id="UserObject" type="io.starter.model.User">
		<id column="id" jdbcType="INTEGER" property="id" />
	...
		<collection column="id" javaType="ArrayList" ofType="io.starter.model.Location" property="locations" select="getLocationsForUser" />
	</resultMap>
	
	<!-- use existing defined Location by reference -->
	<select id="getLocationsForUser" parameterType="java.lang.Integer" 
		resultMap="io.starter.stackgen.model.dao.StackgenLocation">
		select
		* from location
		where owner_id =
		#{id,jdbcType=INTEGER}
	</select>
	
	</pre>
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public class StackModelRelationGenerator {
	protected final Logger logger = LoggerFactory
			.getLogger(StackModelRelationGenerator.class);

	public List<MyBatisJoin> generate(Swagger swagger, StackGenConfigurator config) {
		logger.warn("Generating Stack Model Relationships for : "
				+ swagger.getInfo().getDescription());

		List<MyBatisJoin> refs = new ArrayList<MyBatisJoin>();

		// for each model, iterate the props
		// if it is an ARRAY value,
		Map<String, Model> models = swagger.getDefinitions();
		fixTitles(models);
		List<Model> safeIterate = new ArrayList<Model>();
		safeIterate.addAll(models.values());
		for (Model mdx : safeIterate) {
			Map<String, Property> props = mdx.getProperties();

			fixNames(props);

			/*
			 * ref String "#/definitions/CalendarEvent" (id=241) String
			 * simpleRef String "CalendarEvent" (id=242) String 242
			 * type RefType RefType (id=243) RefType 243 4
			 * name String "events" (id=194) String 194 13440
			 * required boolean false boolean
			 * type String "ref" (id=196) String 196 13440
			 */

			for (Property prop : props.values()) {
				// then we need an IDX table
				String field = prop.getName();
				String[] tables = { prop.getName() };
				String pt = prop.getType();
				// it is a one-many foreign key ie: OWNER_ID
				if (pt.equals("integer")
						&& prop.getName().toLowerCase().endsWith("_id")) {

					logger.info("PROP: " + pt);
					// TODO: handle the one-to-many ID issue
					// MyBatisJoin j = new MyBatisJoin(field, tables);
				} else if (pt.equals("array")) {
					logger.info("PROP: " + pt);
					createIdx(swagger, config, refs, models, mdx, (ArrayProperty) prop, field);
				} else if (pt.equals("ref")) {
					createRef(swagger, config, refs, models, mdx, (RefProperty) prop, field);
				}
			}
		}
		logger.info("Done Generating Stack Model Relationships.");
		return refs;

	}

	private void createRef(Swagger swagger, StackGenConfigurator config, List<MyBatisJoin> refs, Map<String, Model> models, Model mdx, RefProperty rp, String field) {

	}

	private void createIdx(Swagger swagger, StackGenConfigurator config, List<MyBatisJoin> refs, Map<String, Model> models, Model mdx, ArrayProperty arr, String field) {
		if(arr.getItems() instanceof  RefProperty){
			RefProperty rp = (RefProperty)arr.getItems();
			String sr = rp.getSimpleRef();
			String sr1 = rp.get$ref();
			if (sr1 != null)
				sr1 = sr1.substring(sr1.lastIndexOf("/") + 1, sr1
						.length());

			Model rpm = models.get(sr);
			// create IDX table for array types
			// like shared multiple resources GROUPS or ROLES
			if (rpm != null) {
				if (rpm.getTitle() == null) {
					rpm.setTitle(sr1);
				}
				MyBatisJoin j = new MyBatisJoin(swagger, field, mdx, rpm, config);
				// creates the *Ref XML to inject into the Main Object
				// Mapping
				refs.add(j);
			}
		}else if(arr.getItems() instanceof StringProperty){

		}

	}

	private void fixTitles(Map<String, Model> models) {
		for (String key : models.keySet()) {
			Model p = models.get(key);
			p.setTitle(key);
		}
	}

	private void fixNames(Map<String, Property> props) {
		for (String key : props.keySet()) {
			Property p = props.get(key);
			p.setName(key);
		}
	}

	/*
	 * 
	 * String t1 = "customer";
	 * String t2 = "order";
	 * String t3 = "order_detail";
	 * 
	 * String f1 = "customer_id";
	 * String f2 = "order_id";
	 * 
	 * String[] tables = { t1, t2, t3 };
	 * String[] fields = { f1, f2 };
	 * 
	 * for (String field : fields) {
	 * MyBatisJoin j = new MyBatisJoin(field, tables);
	 * System.out.println(j.getXMLFragment());
	 * System.out.println(j.getDML());
	 * }
	 */

}
