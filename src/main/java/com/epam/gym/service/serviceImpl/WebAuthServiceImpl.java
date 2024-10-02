package com.epam.gym.service.serviceImpl;

import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.service.AuthService;
import com.epam.gym.service.UserService;
import com.epam.gym.service.WebAuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component(value = "WebAuthService")
@AllArgsConstructor
public class WebAuthServiceImpl implements WebAuthService {
    private final UserService userService;
    private final AuthService authService;

    @Override
    public boolean authenticate(String username, String password) {
        return userService.isUsernameAndPasswordMatch(username, password);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return authService.changePassword(username, oldPassword, newPassword);
    }

    @Override
    public String[] register(String firstName, String lastName) {
        return authService.register(firstName, lastName);
    }

    @Override
    public String[] registerTrainee(String firstName, String lastName, LocalDate dob, String address) {
        return authService.registerTrainee(firstName, lastName, dob, address);
    }

    @Override
    public String[] registerTrainer(String firstName, String lastName, TrainingTypeEnum specialization) {
        return authService.registerTrainer(firstName, lastName, specialization);
    }
}