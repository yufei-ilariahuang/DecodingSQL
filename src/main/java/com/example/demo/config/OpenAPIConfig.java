package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenAPIConfig {

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.model}")
    private String apiModel;

    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(apiURL)
                .build();
    }

    // Optional: Getter methods for the configuration if needed
    public String getApiURL() {
        return apiURL;
    }

    public String getApiModel() {
        return apiModel;
    }

    public String getApiKey() {
        return apiKey;
    }
}