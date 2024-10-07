package com.epam.gym;

import com.epam.gym.service.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        log.info("Application started ...");

        GreetingService service = context.getBean(GreetingService.class);
        service.logGreeting();
    }
}