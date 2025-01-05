package com.epam.gym.cucumber.glue;

import com.epam.gym.dto.request.ChangeLoginDto;
import com.epam.gym.dto.request.RefreshTokenRequestDto;
import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.request.UserCredentialDto;
import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.dto.response.RegistrationResponseDto;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.repository.RefreshTokenRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nurbek on 05.01.2025
 */
@Slf4j
@RequiredArgsConstructor
public class AuthSteps {
    private final RestTemplate restTemplate;
    private final RefreshTokenRepository tokenRepository;

    private String entityFirstName;
    private String entityLastName;
    private String entityUsername;
    private String entityPassword;

    private String accessToken;
    private String refreshToken;

    @LocalServerPort
    private String port;
    private static final String url = "http://localhost";

    private HttpStatusCode statusCode;

    @Given("firstname: {string}, lastname: {string} and password: {string}")
    public void givenTraineeCredentials(String firstName, String lastName, String password) {
        entityFirstName = firstName;
        entityLastName = lastName;
        entityPassword = password;
    }

    @When("trainee registers")
    public void whenRegisteringTrainee() {
        var trainee = new TraineeRegistrationDto(
                entityFirstName,
                entityLastName,
                LocalDate.of(2000, 1, 1),
                "address",
                entityPassword
        );

        var response = restTemplate.postForEntity("%s:%s/api/v1/trainees".formatted(url, port), trainee, RegistrationResponseDto.class);
        statusCode = response.getStatusCode();
        entityUsername = Objects.requireNonNull(response.getBody()).username();
    }

    @When("trainer registers")
    public void whenRegisteringTrainer() {
        var trainer = new TrainerRegistrationDto(
                entityFirstName,
                entityLastName,
                TrainingTypeEnum.AEROBICS,
                entityPassword
        );

        var response = restTemplate.postForEntity("%s:%s/api/v1/trainers".formatted(url, port), trainer, RegistrationResponseDto.class);
        statusCode = response.getStatusCode();
        entityUsername = Objects.requireNonNull(response.getBody()).username();
    }

    @When("login with username: {string} and password: {string}")
    public void whenLogins(String username, String password) {
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

    @When("changing password to {string}")
    public void whenChangePassword(String newPassword) {
        var requestBody = new ChangeLoginDto(entityPassword, newPassword);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<ChangeLoginDto> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        var response = restTemplate.postForEntity("%s:%s/api/v1/auth/change-password".formatted(url, port), requestEntity, Object.class);
        statusCode = response.getStatusCode();
    }

    @When("logout")
    public void whenLogout() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);
        var response = restTemplate.postForEntity("%s:%s/api/v1/auth/logout".formatted(url, port), requestEntity, Object.class);
        statusCode = response.getStatusCode();
    }

    @When("trying to refresh token")
    public void whenRefreshingToken() {
        var request = new RefreshTokenRequestDto(refreshToken);
        var response = restTemplate.postForEntity("%s:%s/api/v1/auth/refresh-token".formatted(url, port), request, JwtTokenResponseDto.class);
        statusCode = response.getStatusCode();
        accessToken = Objects.requireNonNull(response.getBody()).accessToken();
        refreshToken = response.getBody().refreshToken();
    }

    @Then("access token and refresh token are valid")
    public void thenJwtTokensAreValid() {
        assertThat(accessToken).isNotNull();
        assertThat(refreshToken).isNotNull();
        assertThat(tokenRepository.findByToken(refreshToken).isPresent()).isTrue();
    }

    @Then("the status code is {int}")
    public void thenStatusCodeIs(int status) {
        assertThat(statusCode.value()).isEqualTo(status);
    }

    @Then("the username should start with {string}")
    public void thenUsernameIs(String username) {
        assertThat(entityUsername).startsWith(username);
    }

    @Then("the password is {string}")
    public void thenPasswordIs(String password) {
        assertThat(entityPassword).isEqualTo(password);
    }
}