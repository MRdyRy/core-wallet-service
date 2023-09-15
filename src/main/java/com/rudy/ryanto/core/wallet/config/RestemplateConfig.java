package com.rudy.ryanto.core.wallet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestemplateConfig {
    @Value(value = "${rest.client.connect.timeout:60}")
    private long connectionTimeout;

    @Value(value = "${rest.client.read.timeout:60}")
    private long readTimeOut;

    @Value(value = "${swagger.enable:true}")
    private boolean enableSwagger;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = restTemplateBuilder
                .setReadTimeout(Duration.ofSeconds(readTimeOut))
                .setConnectTimeout(Duration.ofSeconds(connectionTimeout))
                .build();
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}
