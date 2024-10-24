package com.epam.gym.service;

import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.dto.response.RegistrationResponseDto;

public interface WebAuthService {

    JwtTokenResponseDto authenticate(String username, String password);

    void changePassword(String username, String oldPassword, String newPassword);

    RegistrationResponseDto registerTrainee(TraineeRegistrationDto trainee);

    RegistrationResponseDto registerTrainer(TrainerRegistrationDto trainer);

    void logout(String username);

    JwtTokenResponseDto refreshAccessToken(String refreshToken);
}