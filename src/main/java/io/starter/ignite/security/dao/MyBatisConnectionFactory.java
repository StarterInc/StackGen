package io.starter.ignite.security.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This differs from the ConnectionFactory in that it is a MyBatis session
 * factory, for use when we are connecting via mybatis.
 * 
 * Is there a reason to not connect with mybatis ever?
 * 
 * @author nick
 * 
 */
public class MyBatisConnectionFactory {
	private static SqlSessionFactory	sqlSessionFactory;
	private static SqlSessionFactory	wp_SqlSessionFactory;

	protected static final Logger		logger			= LoggerFactory
			.getLogger(MyBatisConnectionFactory.class);

	public static String				DATABASE_CHOICE	= "production";

	static {
		if (System.getProperty("PARAM1") != null && System.getProperty("PARAM1")
				.equalsIgnoreCase("production")) {
			// use default production DB
		} else { // backup/staging
			DATABASE_CHOICE = "staging";
		}

		try {
			logger.debug("MyBatisConnectionFactory loading: "
					+ DATABASE_CHOICE);

			String resource = "Configuration.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			if (sqlSessionFactory == null) {
				sqlSessionFactory = new SqlSessionFactoryBuilder()
						.build(inputStream, DATABASE_CHOICE, System
								.getProperties());
			}
			inputStream.close();
			inputStream = Resources.getResourceAsStream(resource);
			if (wp_SqlSessionFactory == null) {
				wp_SqlSessionFactory = new SqlSessionFactoryBuilder()
						.build(inputStream, "wordpress", System
								.getProperties());
			}
			inputStream.close();

		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public static SqlSessionFactory getWpSqlSessionFactory() {
		return wp_SqlSessionFactory;
	}
}
