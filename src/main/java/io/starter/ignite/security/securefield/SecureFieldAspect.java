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
		} else {
			logger.info("INVOKING SECURE FIELD GETTER");
		}

		if (rejectIbatisCall()) {
			return pjp.proceed(pjp.getArgs());
		}

		logger.info("Get Secure Field for: " + pjp.toLongString());
		final Object targetObject = pjp.getTarget();
		final String secureFieldName = pjp.getSignature().getName();

		final Field secureField = targetObject.getClass()
				.getDeclaredField(secureFieldName);
		secureField.setAccessible(true);
		final Object encryptedObject = secureField.get(targetObject);
		secureField.setAccessible(false);

		final SecureField sf = secureField.getAnnotation(SecureField.class);

		if (sf == null) {
			logger.info("Null SecureField Annotation on Field: " + secureField);
			return pjp.proceed(pjp.getArgs());
		}
		if (sf.enabled()) {
			logger.info("FOUND SECUREFIELD ANNOTATION: " + sf.toString());
		} else {
			logger.info("FOUND DISABLED SECUREFIELD ANNOTATION: "
					+ sf.toString());
		}
		if (secureField.getType().equals(String.class)) {
			return SecureEncrypter.decrypt(String.valueOf(encryptedObject));
		} else {
			logger.info("SecureFieldAspect only currently supports decrypting Text values: "
					+ pjp);
			return pjp.proceed();
		}
	}

	/**
	 * @throws Throwable
	 */
	private boolean rejectIbatisCall() throws Throwable {
		// check if iBatis is calling, do not decrypt
		// TODO: support other persistence implementations
		for (StackTraceElement o : Thread.currentThread().getStackTrace()) {
			final String cnm = o.getClassName();
			if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
				System.err.println("REJECT IBATIS CALL" + cnm);
				return true;
			}
		}
		return false;
	}

	@Around(FIELD_SET)
	public Object setSecureField(ProceedingJoinPoint pjp) throws Throwable {

		if (DISABLE_SECURE_FIELD_ASPECT) {
			logger.info("SKIPPING SECURE FIELD SETTER");
			return pjp.proceed();
		}

		if (rejectIbatisCall()) {
			return pjp.proceed(pjp.getArgs());
		}

		logger.info("Set Secure Field for: " + pjp.toLongString());
		final String clearTextValue = String.valueOf(pjp.getArgs()[0]);
		final String encryptedValue = SecureEncrypter.encrypt(clearTextValue);
		final Object targetObject = pjp.getTarget();
		final String secureFieldName = pjp.getSignature().getName();
		final Field secureField = targetObject.getClass()
				.getDeclaredField(secureFieldName);
		final boolean access = secureField.isAccessible();

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