package com.rudy.ryanto.core.wallet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
        //for specific handler
//        restTemplate.setErrorHandler(new ApiErrorRestErrorHandler<ApiError>(ApiError.class));
        //for log request and response
//        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return restTemplate;
    }

//    class ApiErrorRestErrorHandler<T> extends DefaultResponseErrorHandler {
//
//        private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//        private final HttpMessageConverterExtractor<T> jacksonMessageConverter;
//
//        public ApiErrorRestErrorHandler(Class<T> responseType) {
//
//            this.messageConverters.add(new MappingJackson2HttpMessageConverter());
//            this.jacksonMessageConverter = new HttpMessageConverterExtractor<>(responseType, this.messageConverters);
//            ;
//        }
//    }
}
