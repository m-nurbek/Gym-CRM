package com.epam.gym.cucumber.glue;

import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.request.UserCredentialDto;
import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.dto.response.RegistrationResponseDto;
import com.epam.gym.entity.TrainingTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Nurbek on 06.01.2025
 */
@Slf4j
@RequiredArgsConstructor
public class BaseSteps {
    protected final RestTemplate restTemplate;

    protected String username;
    protected String firstName;
    protected String lastName;
    protected String password;
    protected String accessToken;
    protected String refreshToken;
    protected HttpStatusCode statusCode;

    @LocalServerPort
    protected String port;
    protected static final String url = "http://localhost";

    protected void registerTrainee(String firstName, String lastName, String password) {
        try {
            var trainee = new TraineeRegistrationDto(
                    firstName,
                    lastName,
                    LocalDate.of(2000, 1, 1),
                    "address",
                    password
            );

            var response = restTemplate.postForEntity("%s:%s/api/v1/trainees".formatted(url, port), trainee, RegistrationResponseDto.class);
            statusCode = response.getStatusCode();
            username = Objects.requireNonNull(response.getBody()).username();
        } catch (HttpStatusCodeException ex) {
            statusCode = ex.getStatusCode();
            log.error("Error during registration: {}", ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("Unexpected error during registration: {}", ex.getMessage());
        }
    }

    protected void registerTrainer(String firstName, String lastName, String password) {
        try {
            var trainer = new TrainerRegistrationDto(
                    firstName,
                    lastName,
                    TrainingTypeEnum.AEROBICS,
                    password
            );

            var response = restTemplate.postForEntity("%s:%s/api/v1/trainers".formatted(url, port), trainer, RegistrationResponseDto.class);
            statusCode = response.getStatusCode();
            username = Objects.requireNonNull(response.getBody()).username();
        } catch (HttpStatusCodeException ex) {
            statusCode = ex.getStatusCode();
            log.error("Error during registration: {}", ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("Unexpected error during registration: {}", ex.getMessage());
        }
    }

    protected void login(String username, String password) {
        try {
            var credentials = new UserCredentialDto(username, password);
            var response = restTemplate.postForEntity("%s:%s/api/v1/auth/login".formatted(url, port), credentials, JwtTokenResponseDto.class);
            statusCode = response.getStatusCode();
            accessToken = Objects.requireNonNull(response.getBody()).accessToken();
            refreshToken = response.getBody().refreshToken();
        } catch (HttpStatusCodeException ex) {
            statusCode = ex.getStatusCode();
            log.error("Error during login: {}", ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("Unexpected error during login: {}", ex.getMessage());
        }
    }

}