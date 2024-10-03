package com.epam.gym.service;

import com.epam.gym.entity.TrainingTypeEnum;

import java.time.LocalDate;

public interface AuthService {
    boolean authenticate(String username, String password);

    boolean changePassword(String username, String oldPassword, String newPassword);

    boolean isAuthenticated(String username);

    boolean logout(String username);

    void logoutOfAllAccounts();

    String[] register(String firstName, String lastName);

    String[] registerTrainee(String firstName, String lastName, LocalDate dob, String address);

    String[] registerTrainer(String firstName, String lastName, TrainingTypeEnum specialization);

    String getUsernameOfAuthenticatedAccount();
}