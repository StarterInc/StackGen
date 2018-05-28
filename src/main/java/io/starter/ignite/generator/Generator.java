package io.starter.ignite.generator;

import java.lang.reflect.Field;
import java.util.List;

public interface Generator extends Configuration{
	Object createSetter(Field f);

	Object createAccessor(Field f);

	Object createValue(Field f);

	void generate(String className, List fieldList, List getters, List setters)
			throws Exception;
}