package io.starter.ignite.util;

public interface SystemConstants {

	public static final int		IGNITE_MAJOR_VERSION		= 1;
	public static final int		IGNITE_MINOR_VERSION		= 1;

	public static String		rootFolder					= (System
			.getProperty("rootFolder") != null
					? System.getProperty("rootFolder")
					: System.getProperty("user.dir"));

	public static String		dbName						= (System
			.getProperty("dbName") != null ? System.getProperty("dbName")
					: "NONE");
	public static String		dbUrl						= (System
			.getProperty("dbUrl") != null ? System.getProperty("dbUrl")
					: "NONE");
	public static String		dbUser						= (System
			.getProperty("dbUser") != null ? System.getProperty("dbUser")
					: "NONE");
	public static String		dbPassword					= (System
			.getProperty("dbPassword") != null
					? System.getProperty("dbPassword")
					: "NONE");
	public static String		S3_STARTER_MEDIA_BUCKET		= (System
			.getProperty("S3_MEDIA_BUCKET") != null
					? System.getProperty("S3_STARTER_MEDIA_BUCKET")
					: null);
	public static String		S3_STARTER_SYSTEM_BUCKET	= (System
			.getProperty("S3_STARTER_SYSTEM_BUCKET") != null
					? System.getProperty("S3_STARTER_SYSTEM_BUCKET")
					: null);
	public static String		S3_STARTER_SERVICE			= (System
			.getProperty("S3_STARTER_SERVICE") != null
					? System.getProperty("S3_STARTER_SERVICE")
					: null);
	public static String		awsAccessKey				= (System
			.getProperty("awsAccessKey") != null
					? System.getProperty("awsAccessKey")
					: null);
	public static String		awsSecretKey				= (System
			.getProperty("awsSecretKey") != null
					? System.getProperty("awsSecretKey")
					: null);

	public static String		JNDI_DB_LOOKUP_STRING		= "jndi/ignite";

	// mixc encryption
	public static String		SECURE_KEY_PROPERTY			= "starterIgniteSecureKey";
	public static String		SECRET_KEY					= System
			.getProperty(SECURE_KEY_PROPERTY);

	public static String		CIPHER_NAME					= "AES/CBC/PKCS5PADDING";
	public static String		S3_STARTER_MEDIA_FOLDER		= null;
	public static Integer		ANON_USERID					= null;
	public final static int		KEY_SIZE					= 256;
	public static final String	KEYGEN_INSTANCE_NAME		= "AES";

	public static final String	SESSION_VAR_USER			= null;
	public static final String	SESSION_VAR_SQLSESSION		= null;

}
