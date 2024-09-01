package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.dto.UserDto;
import com.epam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigInteger;

@Slf4j
public class Application {
    public static void main(String[] args) {
        log.info("Application started ...");

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class)) {
                UserService u = context.getBean(UserService.class);
    //        TraineeService t = context.getBean(TraineeService.class);

            UserDto user = UserDto.builder().firstName("Daniyar").lastName("Kakimbekov").username("daniyar").password("password1!").build();
    //        TraineeDto trainee = TraineeDto.builder().dob(Date.valueOf("2001-02-02")).address("Kabanbay batyr avenue").user(user).build();
    //        t.add(trainee);
            u.add(user);

    //        t.get(BigInteger.ONE).ifPresent(r -> System.out.println(TraineeDto.Parser.toJson(r)));

            System.out.println("All users:");
            u.getAll().forEach(r -> System.out.println(UserDto.Parser.toJson(r)));
        }

    }
}