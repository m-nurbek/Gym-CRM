package com.epam.gym.cucumber;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Nurbek on 05.01.2025
 */
@TestConfiguration
public class CucumberTestConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}