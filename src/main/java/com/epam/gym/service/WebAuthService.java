package com.epam.gym.service;

import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.response.JwtTokenResponseDto;

public interface WebAuthService {

    JwtTokenResponseDto authenticate(String username, String password);

    boolean changePassword(String username, String oldPassword, String newPassword);

    String[] registerTrainee(TraineeRegistrationDto trainee);

    String[] registerTrainer(TrainerRegistrationDto trainer);

    void logout(String username);

    JwtTokenResponseDto refreshAccessToken(String refreshToken);
}