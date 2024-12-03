package com.epam.gym.integration;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nurbek on 03.12.2024
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return requestTemplate -> {
            // get the current transaction ID of the request and put it in the header
            String transactionId = MDC.get("transactionId");
            if (transactionId != null) {
                requestTemplate.header("transactionId", transactionId);
            }
        };
    }

}