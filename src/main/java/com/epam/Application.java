package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.dto.UserDto;
import com.epam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigInteger;

@Slf4j
public class Application {
    public static void main(String[] args) {
        log.info("Hello");
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        UserService u = context.getBean(UserService.class);

        UserDto user = UserDto.builder().firstName("Nurbek").lastName("Malikov").username("m-nurbek").password("password1!").build();
        u.add(user);

        u.get(BigInteger.ONE).ifPresent(r -> System.out.println(UserDto.Parser.toJson(r)));
    }
}