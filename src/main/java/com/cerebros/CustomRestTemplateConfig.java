package com.cerebros;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import java.util.Collections;

@Configuration
public class CustomRestTemplateConfig {

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(20))
                .messageConverters(converter)
                .setReadTimeout(Duration.ofSeconds(20));
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Use the custom RestTemplateBuilder with your desired settings
        return builder.build();
    }
}
