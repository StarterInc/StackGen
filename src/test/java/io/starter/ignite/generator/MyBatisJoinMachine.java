package io.starter.ignite.generator;

import org.junit.Test;

public class MyBatisJoinMachine {

	@Test
	public void test() {

		// detect the join condition

		String t1 = "customer";
		String t2 = "order";
		String t3 = "order_detail";

		String f1 = "customer_id";
		String f2 = "order_id";

		String[] tables = { t1, t2, t3 };
		String[] fields = { f1, f2 };

		for (String field : fields) {
			//
			MyBatisJoin j = new MyBatisJoin(field, tables);
			System.out.println(j.getXMLFragment());
			System.out.println(j.getDML());
		}

		// fail("Not yet implemented");
	}

}
