package com.baeldung.camel;

import javax.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;


@Configuration
public class AppConfig {
	@Autowired
    JsonConverter jsonConverter;
    
    @Bean("jmsTransactionManager")
    public JmsTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }

	@Bean("myBeanJsonMessageConverter")
	public JsonMessageConverter myBeanJsonMessageConverter() {
		return new JsonMessageConverter(jsonConverter, MyBean.class);
	}
}
