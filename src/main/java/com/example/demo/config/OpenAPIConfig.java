package com.example.demo.config;


import com.google.api.client.util.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenAPIConfig {

    @Value("${openai.api.url")
    private String apiURL;

    @Bean
    public RestClient restClient(){
        return RestClient.builder()
                .baseUrl(apiURL)
                .build();
    }
}
