package com.example.springboot.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import static com.example.springboot.config.Constants.BASE_URI;

@TestConfiguration
@Lazy
public class RestAssuredConfig {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUrl() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.port = port;
    }

    @Bean(name = "requestSpecificationAdminUser")
    public RequestSpecification requestSpecificationRegularUser() {
        return RestAssured
                .given()
                .baseUri(BASE_URI + port);
    }
}
