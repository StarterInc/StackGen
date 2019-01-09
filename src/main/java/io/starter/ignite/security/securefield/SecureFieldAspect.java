package io.starter.ignite.security.securefield;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.Configuration;

/* ##LICENSE## */

import io.starter.ignite.security.crypto.SecureEncrypter;

@Aspect
public class SecureFieldAspect implements Configuration {

	protected static final Logger	logger				= LoggerFactory
			.getLogger(SecureFieldAspect.class);

	private final static boolean	SKIP_IBATIS_CALLER	= true;
	private final static String		FIELD_GET			= "get(@io.starter.ignite.security.securefield.SecureField * *)";
	private final static String		FIELD_SET			= "set(@io.starter.ignite.security.securefield.SecureField * *)";

	@Around(FIELD_GET)
	public Object getSecureField(ProceedingJoinPoint pjp) throws Throwable {

		if (DISABLE_SECURE_FIELD_ASPECT) {
			logger.info("SKIPPING SECURE FIELD GETTER");
			return pjp.proceed();
		}

		String cnm = Thread.currentThread().getStackTrace()[8].getClassName();

		// logger.error("Calling: " + cnm);
		// if iBatis is calling, do not decrypt
		if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
			return pjp.proceed(pjp.getArgs());
		}

		logger.info("Get Secure Field for: " + pjp.toLongString());
		Object targetObject = pjp.getTarget();
		String secureFieldName = pjp.getSignature().getName();
		Field secureField = targetObject.getClass()
				.getDeclaredField(secureFieldName);
		secureField.setAccessible(true);
		Object encryptedObject = secureField.get(targetObject);
		secureField.setAccessible(false);

		if (secureField.getType().equals(String.class)) {
			return SecureEncrypter.decrypt(String.valueOf(encryptedObject));
		} else {
			logger.info("SecureFieldAspect only currently supports decrypting Text values: "
					+ pjp);
			return pjp.proceed();
		}
	}

	@Around(FIELD_SET)
	public Object setSecureField(ProceedingJoinPoint pjp) throws Throwable {

		if (DISABLE_SECURE_FIELD_ASPECT) {
			logger.info("SKIPPING SECURE FIELD SETTER");
			return pjp.proceed();
		}
		String cnm = Thread.currentThread().getStackTrace()[8].getClassName();

		// logger.error("Calling: " + cnm);
		// if iBatis is calling, do not encrypt
		if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
			return pjp.proceed(pjp.getArgs());
		}
		logger.info("Set Secure Field for: " + pjp.toLongString());
		String clearTextValue = String.valueOf(pjp.getArgs()[0]);
		String encryptedValue = SecureEncrypter.encrypt(clearTextValue);
		Object targetObject = pjp.getTarget();
		String secureFieldName = pjp.getSignature().getName();
		Field secureField = targetObject.getClass()
				.getDeclaredField(secureFieldName);
		boolean access = secureField.isAccessible();

		if (secureField.getType().equals(String.class)) {
			secureField.setAccessible(true);
			secureField.set(targetObject, encryptedValue);
			secureField.setAccessible(access);
		} else {
			logger.info("SecureFieldAspect only currently supports encrypting Text values: "
					+ pjp);
			return pjp.proceed();
		}
		return null;
	}
}