package com.epam.gym.service;

import com.epam.gym.entity.TrainingTypeEnum;

import java.time.LocalDate;

public interface WebAuthService {
    boolean authenticate(String username, String password);

    boolean changePassword(String username, String oldPassword, String newPassword);

    String[] register(String firstName, String lastName);

    String[] registerTrainee(String firstName, String lastName, LocalDate dob, String address);

    String[] registerTrainer(String firstName, String lastName, TrainingTypeEnum specialization);
}