package com.baeldung.camel;

/**
 * a Mock class to show how some other layer
 * (a persistence layer, for instance)
 * could be used inside a Camel
 * 
 */
public class ExampleServices {

    public static void example(MyBean bodyIn, String secret) {
        bodyIn.setName( "Hello, " + bodyIn.getName() );
        bodyIn.setId(bodyIn.getId()*10);
        bodyIn.setSecret(secret);
    }
}