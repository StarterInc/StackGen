package io.starter.ignite.security.securefield;

/* ##LICENSE## */

import io.starter.ignite.security.crypto.SecureEncrypter;
import io.starter.ignite.util.Logger;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class SecureFieldAspect {

	private final static boolean SKIP_IBATIS_CALLER = true;
	private final static String FIELD_GET = "get(@io.starter.ignite.security.securefield.SecureField * *)";
	private final static String FIELD_SET = "set(@io.starter.ignite.security.securefield.SecureField * *)";

	@Around(FIELD_GET)
	public Object getSecureField(ProceedingJoinPoint pjp) throws Throwable {
		String cnm = Thread.currentThread().getStackTrace()[8].getClassName();

		// System.err.println("Calling: " + cnm);
		// if iBatis is calling, do not decrypt
		if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
			return pjp.proceed(pjp.getArgs());
		}

		Logger.debug("Get Secure Field for: " + pjp.toLongString());
		Object targetObject = pjp.getTarget();
		String secureFieldName = pjp.getSignature().getName();
		Field secureField = targetObject.getClass().getDeclaredField(
				secureFieldName);
		secureField.setAccessible(true);
		Object encryptedObject = secureField.get(targetObject);
		secureField.setAccessible(false);
		return SecureEncrypter.decrypt(String.valueOf(encryptedObject));
	}

	@Around(FIELD_SET)
	public Object setSecureField(ProceedingJoinPoint pjp) throws Throwable {
		String cnm = Thread.currentThread().getStackTrace()[8].getClassName();

		// System.err.println("Calling: " + cnm);
		// if iBatis is calling, do not encrypt
		if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
			return pjp.proceed(pjp.getArgs());
		}
		Logger.debug("Set Secure Field for: " + pjp.toLongString());
		String clearTextValue = String.valueOf(pjp.getArgs()[0]);
		String encryptedValue = SecureEncrypter.encrypt(clearTextValue);
		Object targetObject = pjp.getTarget();
		String secureFieldName = pjp.getSignature().getName();
		Field secureField = targetObject.getClass().getDeclaredField(
				secureFieldName);
		boolean access = secureField.isAccessible();
		secureField.setAccessible(true);
		secureField.set(targetObject, encryptedValue);
		secureField.setAccessible(access);
		return null;
	}
}