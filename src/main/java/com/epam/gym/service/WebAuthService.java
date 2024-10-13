package com.epam.gym.service;

import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;

public interface WebAuthService {

    void authenticate(String username, String password);

    boolean changePassword(String username, String oldPassword, String newPassword);

    String[] registerTrainee(TraineeRegistrationDto trainee);

    String[] registerTrainer(TrainerRegistrationDto trainer);
}