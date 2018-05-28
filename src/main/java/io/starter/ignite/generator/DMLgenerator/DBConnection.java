package io.starter.ignite.generator.DMLgenerator;

import java.sql.Connection;
import java.sql.SQLException;

import io.starter.ignite.security.dao.ConnectionFactory;

public class DBConnection {

	public static void main(String[] args) throws SQLException {

		Connection conn = ConnectionFactory.getConnection();
		System.out.print("Got Connection: " + conn.isValid(1000));

	}

}
