package io.starter.ignite.util;

import java.io.IOException;

public interface JsonMapperService {

	String toJson(Object instance) throws IOException;

	<T> T fromJson(String json, Class<T> clazz) throws IOException;
}
