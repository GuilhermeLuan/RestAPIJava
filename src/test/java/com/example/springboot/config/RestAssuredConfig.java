//package com.example.springboot.config;
//
//import io.restassured.RestAssured;
//import io.restassured.specification.RequestSpecification;
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Lazy;
//
//@TestConfiguration
//@Lazy
//public class RestAssuredConfig {
//    @LocalServerPort
//    private int port;
//
//    @BeforeEach
//    void setUrl() {
//        RestAssured.baseURI = "http://localhost:" + port;
//        RestAssured.port = port;
//    }
//
//    @Bean(name = "requestSpecificationRegularUser")
//    public RequestSpecification requestSpecificationRegularUser(){
//        return RestAssured
//                .given()
//                .baseUri(BASE_URI + port)
//                .header("Authorization", "Bearer " + token);
//    }
//}
