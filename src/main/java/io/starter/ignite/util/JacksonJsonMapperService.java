package io.starter.ignite.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
final class JacksonJsonMapperService implements JsonMapperService {

	private final ObjectMapper mapper;

	JacksonJsonMapperService(final ObjectMapper mapper) {
		super();
		this.mapper = checkNotNull(mapper);
	}

	@Override
	public String toJson(final Object instance) throws IOException {
		return mapper.writeValueAsString(instance);
	}

	@Override
	public <T> T fromJson(final String json, final Class<T> clazz) throws IOException {
		return mapper.readValue(json, clazz);
	}
}