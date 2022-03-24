package io.starter.ignite.generator;

import io.swagger.models.Swagger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import io.swagger.models.Model;

/**
 * StackGen Model support for ONE-TO-MANY and MANY-TO-MANY relationships can be
 * expressed as a many-to-many with a unique constraint on the "ONE" field
 *
 * - when we encounter a FK_ID field in the Swagger ,we need to implement a
 * many-to-many IDX table - when we encounter a list of FK_ID objects
 *
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */

public class MyBatisJoinTest {

	Model m1, m2;

	StackGenConfigurator config = new StackGenConfigurator();
	
	@Before
	public void setup() {

		config.setSchemaName("stackgen");
		config.setArtifactId("stackgen");
		
		// create mock
		m1 = Mockito.mock(Model.class);

		// define return value for method getTitle()
		Mockito.when(m1.getTitle()).thenReturn("Order");

		// again for 2nd object
		m2 = Mockito.mock(Model.class);
		Mockito.when(m2.getTitle()).thenReturn("OrderDetail");
	}

	@Test
	@Ignore
	public void dmlOutput() {

		// handle the join condition
		final String field = "orderdetails";
		final MyBatisJoin j = new MyBatisJoin( field, m1, m2, config);

		// lock this down
		Assert.assertEquals("should never change", "CREATE TABLE `stackgen$_order_order_detail_idx` (\n" +
						"  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Auto generated Incrementing PK - do not update',\n" +
						"  `order_id` int(11) DEFAULT -1,\n" +
						"  `order_detail_id` int(11) DEFAULT -1,\n" +
						"  PRIMARY KEY (`id`),\n" +
						"  UNIQUE KEY `StackGenUQIDX` (`order_id`,`order_detail_id`))",
				j.getDML());
	}

	@Test
	@Ignore
	public void queryXMLOutput() {

		// handle the join condition
		final String field = "orderdetails";
		final MyBatisJoin j = new MyBatisJoin( field, m1, m2, config);

		// lock this down
		Assert.assertEquals("should never change",
				"<select id=\"getOrderDetailsForOrder\" parameterType=\"java.lang.Integer\" resultMap=\"io.starter.stackgen.model.OrderDetail\">\n" +
						"\t\t\tSELECT R.*\n" +
						"\t\t\tFROM _order R, _order_detail U, stackgen$_order_order_detail_idx I\n" +
						"\t\t\tWHERE user_id\n" +
						"\t\t\t= #{id,jdbcType=INTEGER}\n" +
						"\t\t\tAND I._order_id = U.id\n" +
						"\t\t\tAND I._order_detail_id = R.id\n" +
						"\t\t</select>",
				j.getQueryXML(config));
	}

	@Test
	@Ignore
	public void xmlOutput() {

		// handle the join condition
		final String field = "orderdetails";
		final MyBatisJoin j = new MyBatisJoin( field, m1, m2, config);

		// lock this down
		Assert.assertEquals("should never change",
				"<collection column=\"id\" javaType=\"ArrayList\" ofType=\"io.starter.stackgen.model.OrderDetail\" property=\"orderdetails\" select=\"getOrderDetailsForOrder\" />",
				j.getXML());
	}

}