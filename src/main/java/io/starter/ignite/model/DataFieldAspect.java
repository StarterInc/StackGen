package io.starter.ignite.model;

/* ##LICENSE## */

import io.starter.ignite.util.Logger;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class DataFieldAspect {

	private final static boolean SKIP_IBATIS_CALLER = true;
	private final static String METHOD_GET = "execution(@io.starter.ignite.model.DataField * *(..))";
	private final static String METHOD_SET = "execution(@io.starter.ignite.model.DataField * *(..))";

	@Around(METHOD_GET)
	public Object getDataField(ProceedingJoinPoint pjp) throws Throwable {
		String cnm = Thread.currentThread().getStackTrace()[8].getClassName();

		// io.starter.ignite.util.Logger.error("Calling: " + cnm);
		// if iBatis is calling, do not read
		if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
			return pjp.proceed(pjp.getArgs());
		}

		Logger.debug("Get Data Field for: " + pjp.toLongString());
		Object targetObject = pjp.getTarget();
		String dataFieldName = pjp.getSignature().getName();
		Field dataField = targetObject.getClass().getDeclaredField(dataFieldName);
		dataField.setAccessible(true);
		Object persistedObject = dataField.get(targetObject);
		dataField.setAccessible(false);
		return DataPersister.read(String.valueOf(persistedObject));
	}

	@Around(METHOD_SET)
	public Object setDataField(ProceedingJoinPoint pjp) throws Throwable {
		String cnm = Thread.currentThread().getStackTrace()[8].getClassName();

		// io.starter.ignite.util.Logger.error("Calling: " + cnm);
		// if iBatis is calling, do not persist
		if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
			return pjp.proceed(pjp.getArgs());
		}
		Logger.debug("Set Data Field for: " + pjp.toLongString());
		String clearTextValue = String.valueOf(pjp.getArgs()[0]);
		String persistedValue = DataPersister.persist(clearTextValue);
		Object targetObject = pjp.getTarget();
		String dataFieldName = pjp.getSignature().getName();
		Field dataField = targetObject.getClass().getDeclaredField(dataFieldName);
		dataField.setAccessible(true);
		dataField.set(targetObject, persistedValue);
		dataField.setAccessible(false);
		return null;
	}
}