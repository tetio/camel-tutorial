package com.baeldung.camel;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

public class JsonMessageConverter implements MessageConverter {

	private JsonConverter jsonConverter;
	private Class<?> classOfT;

	public JsonMessageConverter(JsonConverter jsonConverter, Class<?> classOfT) {
		this.jsonConverter = jsonConverter;
		this.classOfT = classOfT;
	}

	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		TextMessage message = session.createTextMessage();
		String json = jsonConverter.toJson(object);
		message.setText(json);
		return message;
	}

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		String json = ((TextMessage) message).getText();
		return jsonConverter.fromJson(json, classOfT);
	}
	
}
