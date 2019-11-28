package io.starter.ignite.security.securefield;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.regex.Pattern;

import io.starter.ignite.generator.Configuration;

/* ##LICENSE## */

import io.starter.ignite.security.crypto.SecureEncrypter;

@Aspect
public class SecureFieldAspect implements Configuration {

	protected static final Logger logger = LoggerFactory.getLogger(SecureFieldAspect.class);

	private final static boolean SKIP_IBATIS_CALLER = true;
	private final static String FIELD_GET = "get(@io.starter.ignite.security.securefield.SecureField * *)";
	private final static String FIELD_SET = "set(@io.starter.ignite.security.securefield.SecureField * *)";
	private final static Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

	@Bean
	private PasswordEncoder encoder(int strength) {
		if(strength < 0 || strength > 31) {
			strength = 15;
		}
		return new BCryptPasswordEncoder(strength);
	}
	
	@Around(FIELD_GET)
	public Object getSecureField(ProceedingJoinPoint pjp) throws Throwable {

		if (DISABLE_SECURE_FIELD_ASPECT) {
			logger.trace("SKIPPING SECURE FIELD GETTER");
			return pjp.proceed();
		} else {
			logger.trace("INVOKING SECURE FIELD GETTER");
		}

		if (rejectIbatisCall()) {
			return pjp.proceed(pjp.getArgs());
		}

		logger.trace("Get Secure Field for: " + pjp.toLongString());
		final Object targetObject = pjp.getTarget();
		final String secureFieldName = pjp.getSignature().getName();

		final Field secureField = targetObject.getClass().getDeclaredField(secureFieldName);
		secureField.setAccessible(true);
		final Object encryptedObject = secureField.get(targetObject);
		secureField.setAccessible(false);

		final SecureField sf = secureField.getAnnotation(SecureField.class);

		if (sf == null) {
			logger.warn("Null SecureField Annotation on Field: " + secureField);
			return pjp.proceed(pjp.getArgs());
		}
		if (sf.enabled()) {
			logger.trace("FOUND SECUREFIELD ANNOTATION: " + sf.toString());
		} else {
			logger.trace("FOUND DISABLED SECUREFIELD ANNOTATION: " + sf.toString());
		}

		// password handling
		// returns the plaintext crypted value which can then be decrypted
		// by authorization code. also prevents returning decrypted
		// cleartext passwords via apis
		if (sf.type() == SecureField.Type.HASHED) {
			return String.valueOf(encryptedObject);
		}

		if (secureField.getType().equals(String.class)) {
			return SecureEncrypter.decrypt(String.valueOf(encryptedObject));
		} else {
			logger.warn("SecureFieldAspect only currently supports decrypting Text values: " + pjp);
			return pjp.proceed();
		}
	}

	/**
	 * @throws Throwable
	 */
	private boolean rejectIbatisCall() throws Throwable {
		 StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		// if iBatis is calling, do not decrypt
		// TODO: support other persistence implementations
		for ( StackTraceElement o : ste ) {
			final String cnm = o.getClassName();
			if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
				logger.warn("REJECT IBATIS CALL" + cnm);
				return true;
			}
		}
		return false;
	}

	@Around(FIELD_SET)
	public Object setSecureField(ProceedingJoinPoint pjp) throws Throwable {

		if (DISABLE_SECURE_FIELD_ASPECT) {
			logger.trace("SKIPPING SECURE FIELD SETTER");
			return pjp.proceed();
		}

		if (rejectIbatisCall()) {
			return pjp.proceed(pjp.getArgs());
		}
		final Object targetObject = pjp.getTarget();
		final String secureFieldName = pjp.getSignature().getName();
		final Field secureField = targetObject.getClass().getDeclaredField(secureFieldName);
		final boolean access = secureField.isAccessible();

		logger.trace("Set Secure Field for: " + pjp.toLongString());
		final String clearTextValue = String.valueOf(pjp.getArgs()[0]);
		final SecureField sf = secureField.getAnnotation(SecureField.class);

		if (sf == null) {
			logger.warn("Null SecureField Annotation on Field being advised by SecureFieldAspect: " + secureField);
			return pjp.proceed();
		}
		
		if (sf.enabled()) {
			logger.trace("PROCESSING SECUREFIELD ANNOTATION: " + sf.toString());
		
			String currentStringValue = (secureField.get(targetObject)!=null? secureField.get(targetObject).toString() : null);
			String encryptedValue = null;
			// password handling
			if (sf.type() == SecureField.Type.HASHED) {
				if (BCRYPT_PATTERN.matcher(clearTextValue).matches()) {
					// clearTextValue is an encoded bcrypt value -- do not double encrypt.
					if (!clearTextValue.equals(currentStringValue)) {
						logger.error("WRITE FAILURE: value for: " + secureField.getName() + " not changed on: "
								+ targetObject.getClass().getName() + ". Are you setting a BCrypt value on a "
								+ SecureField.Type.SYMMETRIC
								+ " SecureField. Cannot overwrite BCrypt value with probably BCrypt value.");
					}
				} else {
					// handle one-way secure hash encryption
					int strength = sf.strength();
					encryptedValue = encoder(strength).encode(clearTextValue);
				}
			} else {
				// use non-hashed SYMMETRIC encryption
				encryptedValue = SecureEncrypter.encrypt(clearTextValue);
			}

			if (secureField.getType().equals(String.class)) {
				secureField.setAccessible(true);
				secureField.set(targetObject, encryptedValue);
				secureField.setAccessible(access);
			} else {
				logger.warn("SecureFieldAspect only currently supports encrypting Text values: " + pjp);
				return pjp.proceed();
			}
		
		
		} else {
			logger.warn("FOUND DISABLED SECUREFIELD ANNOTATION: " + sf.toString());
		}
		
		
		return null;
	}


}