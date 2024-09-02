package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.facade.Facade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigInteger;

@Slf4j
public class Application {
    public static void main(String[] args) {
        log.info("Application started ...");

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class)) {
//            UserService u = context.getBean(UserService.class);
//            UserDto user = UserDto.builder().firstName("Daniyar").lastName("Kakimbekov").username("daniyar").password("password1!").build();
//            u.add(user);
//            System.out.println("All users:");
//            u.getAll().forEach(r -> System.out.println(UserDto.Parser.toJson(r)));

//            Facade facade = context.getBean(Facade.class);
//            facade.run();
        }

    }
}