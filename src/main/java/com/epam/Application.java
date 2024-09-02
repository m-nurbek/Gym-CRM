package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.facade.Facade;
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