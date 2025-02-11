//package com.example.springboot.config;
//
//import org.springframework.boot.devtools.restart.RestartScope;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Profile;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.utility.DockerImageName;
//
//@TestConfiguration(proxyBeanMethods = false)
//@Profile("itest")
//public class TestcontainersConfiguration {
//    @Bean
//    @RestartScope
//    PostgreSQLContainer<?> mysqlContainer() {
//        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:14-alpine"));
//    }
//}