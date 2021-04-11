package io.starter.ignite.util;

public interface SystemConstants {

	public static String getValueOrDefault(String v, String defaultVal) {
		String ret = System.getProperty(v); //
		if(ret != null) {
			return ret;
		}
		ret = System.getenv(v);
		if(ret != null) {
			return ret;
		}
		return defaultVal;
	}

	public static String getValue(String v) {
		String ret = System.getProperty(v); //
		if(ret != null) {
			return ret;
		}
		return  System.getenv(v);
	}
	
	int IGNITE_MAJOR_VERSION = 0;
	float IGNITE_MINOR_VERSION = 9.31f;

	String rootFolder = getValue("rootFolder") != null ? getValue("rootFolder")
			: getValue("user.dir");
	String schemaName = getValue("schemaName") != null ? getValue("schemaName")
			: "stackgen";
	String dbDriver = getValue("dbDriver") != null ? getValue("dbDriver")
			: "com.mysql.cj.jdbc.Driver";
	String dbName = getValue("dbName") != null ? getValue("dbName") : "NONE";
	String dbUrl = getValue("dbUrl") != null ? getValue("dbUrl") : "NONE";
	String dbUser = getValue("dbUser") != null ? getValue("dbUser") : "NONE";
	String dbPassword = getValue("dbPassword") != null ? getValue("dbPassword") : "NONE";
	String S3_STARTER_MEDIA_BUCKET = getValue("S3_MEDIA_BUCKET") != null
			? getValue("S3_STARTER_MEDIA_BUCKET")
			: null;
	String S3_STARTER_SYSTEM_BUCKET = getValue("S3_STARTER_SYSTEM_BUCKET") != null
			? getValue("S3_STARTER_SYSTEM_BUCKET")
			: null;
	String S3_STARTER_SERVICE = getValue("S3_STARTER_SERVICE") != null
			? getValue("S3_STARTER_SERVICE")
			: null;
	String awsAccessKey = getValue("awsAccessKey") != null ? getValue("awsAccessKey") : null;
	String awsSecretKey = getValue("awsSecretKey") != null ? getValue("awsSecretKey") : null;

	String JNDI_DB_LOOKUP_STRING = "jndi/ignite";

	// mixc encryption
	String SECURE_KEY_PROPERTY = "starterIgniteSecureKey";
	String SECRET_KEY = getValue(SECURE_KEY_PROPERTY);

	String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
	String S3_STARTER_MEDIA_FOLDER = null;
	Integer ANON_USERID = null;
	int KEY_SIZE = 256;
	String KEYGEN_INSTANCE_NAME = "AES";

	String SESSION_VAR_USER = null;
	String SESSION_VAR_SQLSESSION = null;

}
