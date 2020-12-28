package com.baeldung.camel;

import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestRoutes extends RouteBuilder {

    @Value("${server.port}")
    String serverPort;

    @Value("${baeldung.api.path}")
    String contextPath;

    @Override
    public void configure() {


        // from("timer:hello?period={{timer.period}}").routeId("hello")
        // .transform().method("myBean", "saySomething")
        // .log(LoggingLevel.INFO, "Inside route 'hello'")
        // .toD("jms:queue:TEST_TRADELENS")
        // .end();

        // http://localhost:8080/camel/api-doc
        restConfiguration().contextPath(contextPath) //
                .port(serverPort).enableCORS(true).apiContextPath("/api-doc").apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1").apiProperty("cors", "true") // cross-site
                .apiContextRouteId("doc-api").component("servlet").bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");
        /**
         * The Rest DSL supports automatic binding json/xml contents to/from POJOs using
         * Camels Data Format. By default the binding mode is off, meaning there is no
         * automatic binding happening for incoming and outgoing messages. You may want
         * to use binding if you develop POJOs that maps to your REST services request
         * and response types.
         */
        rest("/api/").description("Hello REST Service").id("hello-route").get("/hello")
                .produces(MediaType.APPLICATION_JSON).consumes(MediaType.APPLICATION_JSON)
                // .get("/hello/{place}")
                .bindingMode(RestBindingMode.auto).type(MyBean.class).enableCORS(true)
                // .outType(OutBean.class)
                .to("direct:helloService");
        ;

        rest("/api/").description("Teste REST Service").id("api-route").post("/bean")
                .produces(MediaType.APPLICATION_JSON).consumes(MediaType.APPLICATION_JSON)
                // .get("/hello/{place}")
                .bindingMode(RestBindingMode.auto).type(MyBean.class).enableCORS(true)
                // .outType(OutBean.class)
                .to("direct:remoteService");

        from("direct:remoteService").routeId("direct-route").tracing().log(">>> ${body.id}").log(">>> ${body.name}")
                .setExchangePattern(ExchangePattern.InOnly)
                // .transform().simple("blue ${in.body.name}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        MyBean bodyIn = (MyBean) exchange.getIn().getBody();

                        ExampleServices.example(bodyIn);

                        exchange.getIn().setBody(bodyIn);
                    }
                })
                // .toD("jms:queue:TEST_TRADELENS?exchangePattern=InOnly&messageConverter=#myBeanJsonMessageConverter")
                .toD("jms:queue:TEST_TRADELENS?messageConverter=#myBeanJsonMessageConverter")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));

        from("direct:helloService").routeId("hello-direct-route").tracing().log(">>> Hello").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("<h1>Hello!<h1>");
            }
        })
                // .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
                .toD("jms:queue:TEST_TRADELENS?exchangePattern=InOnly");

        rest().post("/message").route().routeId("PUBLISH_EVENT_RECEPTION")
                .log(LoggingLevel.INFO, "Inside PUBLISH_EVENT_RECEPTION").filter(simple("${body} contains 'foo'"))
                .to("log:foo").end().toD("jms:queue:TEST_TRADELENS")
                // + "?messageConverter=#messageJsonMessageConverter"
                // + "&headerFilterStrategy=#removeAllHeaderFilterStrategy")
                .endRest();
    }
}