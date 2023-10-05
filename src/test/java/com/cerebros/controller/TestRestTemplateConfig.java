package com.cerebros.controller;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TestRestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Create and return a mock RestTemplate using Mockito
        return Mockito.mock(RestTemplate.class);
    }
}
