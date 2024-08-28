package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.entity.UserEntity;
import com.epam.service.UserService;
import com.epam.service.serviceImpl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        UserService service = context.getBean(UserServiceImpl.class);

        service.addUser(new UserEntity(1L, "Nurbek", "Malikov", "m-nurbek", "password", true));
        service.addUser(new UserEntity(2L, "Dariya", "Dalabayeva", "dariyusha", "password", true));
        service.addUser(new UserEntity(3L, "Daniyar", "Kakimbekov", "daniyar", "password", true));
        service.addUser(new UserEntity(4L, "Kostya", "Tsoy", "konstantin", "password", true));

        System.out.println(service.getUsers());
        System.out.println("===========");
        System.out.println(service.getUser(3L));
        System.out.println("===========");
        service.deleteUser(3L);
        System.out.println(service.getUsers());

    }
}