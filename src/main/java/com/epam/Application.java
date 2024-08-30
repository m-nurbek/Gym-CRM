package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        UserService service = context.getBean(UserService.class);

    }
}