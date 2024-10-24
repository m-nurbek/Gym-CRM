package com.epam.gym;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Slf4j
@SpringBootApplication
public class Application {

    @Bean
    @Profile("prod")
    public Object productionWarningLog() {
        log.info("====================== RUNNING THE APPLICATION IN PRODUCTION MODE! ======================");
        return null;
    }

    @Bean
    @Profile("stg")
    public Object stagWarningLog() {
        log.info("====================== RUNNING THE APPLICATION IN STG MODE! ======================");
        return null;
    }

    @Bean
    @Profile("local")
    public Object localWarningLog() {
        log.info("====================== RUNNING THE APPLICATION IN LOCAL MODE! ======================");
        return null;
    }

    @Bean
    @Profile("dev")
    public Object devWarningLog() {
        log.info("====================== RUNNING THE APPLICATION IN DEVELOPMENT MODE! ======================");
        return null;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}