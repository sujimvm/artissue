package com.artissue.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Value("${toss.api.secret-key}")
    private String secretKey;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList((ClientHttpRequestInterceptor) (request, body, execution) -> {
            request.getHeaders().setBasicAuth(secretKey, ""); // Basic Auth 설정
            return execution.execute(request, body);
        }));
        return restTemplate;
    }

}
