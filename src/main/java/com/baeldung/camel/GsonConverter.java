package com.baeldung.camel;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
@Primary
public class GsonConverter implements JsonConverter {

	private Gson gson;
	
	public GsonConverter(Gson gson) {
		this.gson = gson;
	}
	
	@Override
	public <T> String toJson(T objectOfT) {
		return gson.toJson(objectOfT);
	}

	@Override
	public <T> T fromJson(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}

}
