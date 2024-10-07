package com.epam.gym;

import com.epam.gym.service.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
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
    public Object stgWarningLog() {
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
        ApplicationContext context = SpringApplication.run(Application.class, args);
        log.info("Application started ...");

        GreetingService service = context.getBean(GreetingService.class);
        service.logGreeting();
    }
}