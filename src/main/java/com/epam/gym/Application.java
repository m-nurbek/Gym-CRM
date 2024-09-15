package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.service.serviceImpl.TrainerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Application {
    public static void main(String[] args) {
        log.info("Application started ...");

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class)) {
//            Facade facade = context.getBean(Facade.class);
//            facade.run();

            TrainerServiceImpl service = context.getBean(TrainerServiceImpl.class);

            var trainerDto = service.findByUsername("xandercarter26");

            System.out.println("APPLICATION ==================");
            trainerDto.ifPresent(System.out::println);
        }
    }
}