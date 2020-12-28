package com.baeldung.camel;

import javax.jms.ConnectionFactory;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;


@Configuration
public class AppConfig {
	@Autowired
    JsonConverter jsonConverter;
    
    @Value("${server.port}")
    String serverPort;

    @Value("${baeldung.api.path}")
    String contextPath;
    
    @Bean("jmsTransactionManager")
    public JmsTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }

	@Bean("myBeanJsonMessageConverter")
	public JsonMessageConverter myBeanJsonMessageConverter() {
		return new JsonMessageConverter(jsonConverter, MyBean.class);
    }
    
    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new CamelHttpTransportServlet(), contextPath + "/*");
        servlet.setName("CamelServlet");
        return servlet;
    }
}
