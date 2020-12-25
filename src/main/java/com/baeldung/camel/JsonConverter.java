package com.baeldung.camel;

public interface JsonConverter {

	<T> String toJson(T objectOfT) ;
	<T> T fromJson(String json, Class<T> classOfT);
	
}