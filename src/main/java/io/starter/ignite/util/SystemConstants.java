package io.starter.ignite.util;

public interface SystemConstants {

	int IGNITE_MAJOR_VERSION = 1;
	int IGNITE_MINOR_VERSION = 1;

	String rootFolder = System.getProperty("rootFolder") != null ? System.getProperty("rootFolder")
			: System.getProperty("user.dir");

	String dbDriver = System.getProperty("dbDriver") != null ? System.getProperty("dbDriver")
			: "com.mysql.cj.jdbc.Driver";
	String dbName = System.getProperty("dbName") != null ? System.getProperty("dbName") : "NONE";
	String dbUrl = System.getProperty("dbUrl") != null ? System.getProperty("dbUrl") : "NONE";
	String dbUser = System.getProperty("dbUser") != null ? System.getProperty("dbUser") : "NONE";
	String dbPassword = System.getProperty("dbPassword") != null ? System.getProperty("dbPassword") : "NONE";
	String S3_STARTER_MEDIA_BUCKET = System.getProperty("S3_MEDIA_BUCKET") != null
			? System.getProperty("S3_STARTER_MEDIA_BUCKET")
			: null;
	String S3_STARTER_SYSTEM_BUCKET = System.getProperty("S3_STARTER_SYSTEM_BUCKET") != null
			? System.getProperty("S3_STARTER_SYSTEM_BUCKET")
			: null;
	String S3_STARTER_SERVICE = System.getProperty("S3_STARTER_SERVICE") != null
			? System.getProperty("S3_STARTER_SERVICE")
			: null;
	String awsAccessKey = System.getProperty("awsAccessKey") != null ? System.getProperty("awsAccessKey") : null;
	String awsSecretKey = System.getProperty("awsSecretKey") != null ? System.getProperty("awsSecretKey") : null;

	String JNDI_DB_LOOKUP_STRING = "jndi/ignite";

	// mixc encryption
	String SECURE_KEY_PROPERTY = "starterIgniteSecureKey";
	String SECRET_KEY = System.getProperty(SECURE_KEY_PROPERTY);

	String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
	String S3_STARTER_MEDIA_FOLDER = null;
	Integer ANON_USERID = null;
	int KEY_SIZE = 256;
	String KEYGEN_INSTANCE_NAME = "AES";

	String SESSION_VAR_USER = null;
	String SESSION_VAR_SQLSESSION = null;

}
