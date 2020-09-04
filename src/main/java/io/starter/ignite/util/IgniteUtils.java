package io.starter.ignite.util;

public class IgniteUtils {

	public static Object cloneObject(Object delegate) {
		try {
			Object clone = delegate.getClass().newInstance();
			for (java.lang.reflect.Field field : delegate.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				try {
					field.set(clone, field.get(delegate));
				} catch (Exception e) {
					; // ok
				}
			}
			return clone;
		} catch (Exception e) {
			return null;
		}
	}

}
