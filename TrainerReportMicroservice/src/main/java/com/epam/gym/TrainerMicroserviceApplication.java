package com.epam.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class TrainerMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainerMicroserviceApplication.class, args);
    }
}