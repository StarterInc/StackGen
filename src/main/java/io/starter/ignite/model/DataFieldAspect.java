package io.starter.ignite.model;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.starter.ignite.generator.Configuration;

@Aspect
public class DataFieldAspect implements Configuration {

	protected static final Logger	logger				= LoggerFactory
			.getLogger(DataFieldAspect.class);

	private final static boolean	SKIP_IBATIS_CALLER	= true;

	// someday may be useful...
	// private final static String METHOD_GET =
	// "execution(@io.starter.ignite.model.DataField * *(..))";
	// private final static String METHOD_SET =
	// "execution(@io.starter.ignite.model.DataField * *(..))";

	private final static String		FIELD_GET			= "get(@io.starter.ignite.model.DataField * *)";
	private final static String		FIELD_SET			= "set(@io.starter.ignite.model.DataField * *)";

	@Around(FIELD_GET)
	public Object getDataField(ProceedingJoinPoint pjp) throws Throwable {

		if (DISABLE_DATA_FIELD_ASPECT) {
			logger.debug("SKIPPING DATA FIELD GETTER");
			return pjp.proceed();
		}

		String cnm = Thread.currentThread().getStackTrace()[8].getClassName();

		// logger.error("Calling: " + cnm);
		// if iBatis is calling, do not read
		if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
			return pjp.proceed(pjp.getArgs());
		}

		logger.debug("Get Data Field for: " + pjp.toLongString());
		Object targetObject = pjp.getTarget();
		String dataFieldName = pjp.getSignature().getName();
		Field dataField = targetObject.getClass()
				.getDeclaredField(dataFieldName);
		dataField.setAccessible(true);
		Object persistedObject = dataField.get(targetObject);
		dataField.setAccessible(false);
		return DataPersister.read(String.valueOf(persistedObject));
	}

	@Around(FIELD_SET)
	public Object setDataField(ProceedingJoinPoint pjp) throws Throwable {

		if (DISABLE_DATA_FIELD_ASPECT) {
			logger.debug("SKIPPING DATA FIELD SETTER" + pjp.toString());
			return pjp.proceed();
		}

		String cnm = Thread.currentThread().getStackTrace()[8].getClassName();

		// logger.error("Calling: " + cnm);
		// if iBatis is calling, do not persist
		if (cnm.toLowerCase().contains("ibatis") && SKIP_IBATIS_CALLER) {
			return pjp.proceed(pjp.getArgs());
		}
		logger.debug("Set Data Field for: " + pjp.toLongString());
		String clearTextValue = String.valueOf(pjp.getArgs()[0]);

		// TODO: init with a pre-existing value...
		// String persistedValue =
		// DataPersister.persist(clearTextValue);

		Object targetObject = pjp.getTarget();
		String dataFieldName = pjp.getSignature().getName();
		Field dataField = targetObject.getClass()
				.getDeclaredField(dataFieldName);
		dataField.setAccessible(true);

		// dataField.set(targetObject, persistedValue);

		dataField.setAccessible(false);
		return null;
	}
}