package io.starter.ignite.generator;

import java.lang.reflect.Field;
import java.util.List;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

public interface Generator {
	
	StackGenConfigurator getConfig();
	
	Object createSetter(Field f);

	Object createAccessor(Field f);

	Object createMember(Field f);

	void generate(String className, List<Object> fieldList, List<MethodSpec> getters, List<MethodSpec> setters) throws Exception;
}