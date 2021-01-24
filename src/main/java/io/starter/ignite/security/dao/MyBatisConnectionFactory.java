package io.starter.ignite.security.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

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
	private static SqlSessionFactory sqlSessionFactory;
	private static SqlSessionFactory wp_SqlSessionFactory;

	protected static final Logger logger = LoggerFactory.getLogger(MyBatisConnectionFactory.class);
	private static final String MYBATIS_CONFIG_FILE = "MyBatisConfig.xml";

	public static String DATABASE_CHOICE = System.getProperty("profile") != null ? System.getProperty("profile")
			: "production";

	static {
		try {
			MyBatisConnectionFactory.initSqlSessionFactory();
		} catch (final FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (final IOException iOException) {
			iOException.printStackTrace();
		}
	}

	private static void initSqlSessionFactory() throws IOException {
		MyBatisConnectionFactory.logger
				.info("MyBatisConnectionFactory loading: " + MyBatisConnectionFactory.DATABASE_CHOICE);

		final String resource = MyBatisConnectionFactory.MYBATIS_CONFIG_FILE;
		InputStream inputStream = Resources.getResourceAsStream(resource);

		// merge props from ENV
		Properties props = System.getProperties();
		Map env = System.getenv();
		for(Object name : env.keySet()){
			props.put(name, env.get(name));
		}

		if (MyBatisConnectionFactory.sqlSessionFactory == null) {
			MyBatisConnectionFactory.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,
					MyBatisConnectionFactory.DATABASE_CHOICE, props);
		}
		inputStream.close();
		inputStream = Resources.getResourceAsStream(resource);
		if (MyBatisConnectionFactory.wp_SqlSessionFactory == null) {
			MyBatisConnectionFactory.wp_SqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,
					"wordpress", props);
		}
		inputStream.close();
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return MyBatisConnectionFactory.sqlSessionFactory;
	}

	public static SqlSessionFactory getWpSqlSessionFactory() {
		return MyBatisConnectionFactory.wp_SqlSessionFactory;
	}
}
