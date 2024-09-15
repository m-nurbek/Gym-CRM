package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.facade.Facade;
import com.epam.gym.repository.hibernate.init.DatabaseInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Application {
    public static void main(String[] args) {
        log.info("Application started ...");

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class)) {
            Facade facade = context.getBean(Facade.class);
            facade.run();
        }
    }
}