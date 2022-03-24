package io.starter.ignite.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class IgniteUtils {

	public static Object getAnnotatedValue(Field field, String vName, Class<?> cx) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Annotation ano = 
				getAnnotationForField(field, cx);
		if(ano == null) {
			return null;
		}
		return ano.annotationType().getMethod(vName).invoke(ano);
	}
	
	public static Annotation getAnnotationForField(Field f, Class<?> lookingFor) {
		Annotation[] decl = f.getDeclaredAnnotations();
		for(Annotation c : decl) {
			Class<?> tn = c.annotationType();
			if(tn.toString().equals(lookingFor.toString())) {
				return c;
			}
		}
		return null;
	}
	
	public static Object cloneObject(Object delegate) {
		try {
			Object clone = delegate.getClass().newInstance();
			for (java.lang.reflect.Field field : delegate.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				try {
					if(!field.getName().equals("delegate") && field.get(delegate) != null) {
						field.set(clone, field.get(delegate));
					}
				} catch (Exception e) {
                    // ok
				}
			}
			return delegate;
		} catch (Exception e) {
			return null;
		}
	}

}
