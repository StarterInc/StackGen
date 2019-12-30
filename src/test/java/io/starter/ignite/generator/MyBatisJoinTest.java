package io.starter.ignite.generator;

import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
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

	@Before
	public void setup() {

		// create mock
		m1 = Mockito.mock(Model.class);

		// define return value for method getTitle()
		Mockito.when(m1.getTitle()).thenReturn("Order");

		// again for 2nd object
		m2 = Mockito.mock(Model.class);
		Mockito.when(m2.getTitle()).thenReturn("OrderDetail");
	}

	@Test
	public void dmlOutput() {

		// handle the join condition
		final String field = "orderdetails";
		final MyBatisJoin j = new MyBatisJoin(field, m1, m2);

		// lock this down
		Assert.assertEquals("should never change", "CREATE TABLE `stackgen$_order_order_detail_idx` (\n"
				+ "  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Auto generated Incrementing PK - do not update',\n"
				+ "  `_order_id` int(11) DEFAULT -1,\n" + "  `_order_detail_id` int(11) DEFAULT -1,\n"
				+ "  PRIMARY KEY (`id`),\n" + "  UNIQUE KEY `StackGenUQIDX` (`_order_id`,`_order_detail_id`))",
				j.getDML());
	}

	@Test
	public void queryXMLOutput() {

		// handle the join condition
		final String field = "orderdetails";
		final MyBatisJoin j = new MyBatisJoin(field, m1, m2);

		// lock this down
		Assert.assertEquals("should never change",
				"<select id=\"getOrderDetailsForOrder\" parameterType=\"java.lang.Integer\" resultMap=\"io.starter.stackgen.model.OrderDetail\">\n"
						+ "			SELECT R.*\n"
						+ "			FROM _order R, _order_detail U, stackgen$_order_order_detail_idx I\n"
						+ "			WHERE user_id\n" + "			= #{id,jdbcType=INTEGER}\n"
						+ "			AND I._order_id = U.id\n" + "			AND I._order_detail_id = R.id\n"
						+ "		</select>",
				j.getQueryXML());
	}

	@Test
	public void xmlOutput() {

		// handle the join condition
		final String field = "orderdetails";
		final MyBatisJoin j = new MyBatisJoin(field, m1, m2);

		// lock this down
		Assert.assertEquals("should never change",
				"<collection column=\"id\" javaType=\"ArrayList\" ofType=\"io.starter.stackgen.model.OrderDetail\" property=\"orderdetails\" select=\"getOrderDetailsForOrder\" />",
				j.getXML());
	}

}