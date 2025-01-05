package com.epam.gym.cucumber.glue;

import com.epam.gym.dto.request.ChangeLoginDto;
import com.epam.gym.dto.request.RefreshTokenRequestDto;
import com.epam.gym.dto.request.TraineeUpdateRequestDto;
import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.dto.response.TraineeResponseDto;
import com.epam.gym.dto.response.TraineeUpdateResponseDto;
import com.epam.gym.repository.RefreshTokenRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
public class CucumberSteps extends BaseSteps {
    private final RefreshTokenRepository tokenRepository;

    public CucumberSteps(RestTemplate restTemplate, RefreshTokenRepository tokenRepository) {
        super(restTemplate);
        this.tokenRepository = tokenRepository;
    }

    @Given("firstname: {string}, lastname: {string} and password: {string}")
    public void givenCredentials(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    @When("trainee registers")
    public void whenRegisteringTrainee() {
        registerTrainee(firstName, lastName, password);
    }

    @When("trainer registers")
    public void whenRegisteringTrainer() {
        registerTrainer(firstName, lastName, password);
    }

    @When("login with username: {string} and password: {string}")
    public void whenLogins(String username, String password) {
        login(username, password);
    }

    @When("changing password to {string}")
    public void whenChangePassword(String newPassword) {
        var requestBody = new ChangeLoginDto(password, newPassword);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<ChangeLoginDto> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        var response = restTemplate.postForEntity("%s:%s/api/v1/auth/change-password".formatted(url, port), requestEntity, Void.class);
        statusCode = response.getStatusCode();
    }

    @When("logout")
    public void whenLogout() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);
        var response = restTemplate.postForEntity("%s:%s/api/v1/auth/logout".formatted(url, port), requestEntity, Void.class);
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

    @When("trying to get trainee profile")
    public void whenGetProfile() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setBearerAuth(accessToken);
            HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

            var response = restTemplate.exchange(
                    "%s:%s/api/v1/trainees/%s".formatted(url, port, username),
                    HttpMethod.GET,
                    requestEntity,
                    TraineeResponseDto.class
            );

            statusCode = response.getStatusCode();
            firstName = Objects.requireNonNull(response.getBody()).firstName();
            lastName = response.getBody().lastName();
        } catch (HttpStatusCodeException ex) {
            statusCode = ex.getStatusCode();
            log.error("Error when trying to get trainee profile: {}", ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("Unexpected error when trying to get trainee profile: {}", ex.getMessage());
        }
    }

    @When("trying to update trainee profile with new lastname: {string}")
    public void whenUpdateProfile(String newLastName) {
        try {
            var requestBody = new TraineeUpdateRequestDto(
                    firstName,
                    newLastName,
                    LocalDate.of(2000, 1, 1),
                    "address",
                    true
            );

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setBearerAuth(accessToken);
            HttpEntity<TraineeUpdateRequestDto> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

            var response = restTemplate.exchange(
                    "%s:%s/api/v1/trainees/%s".formatted(url, port, username),
                    HttpMethod.PUT,
                    httpEntity,
                    TraineeUpdateResponseDto.class
            );

            statusCode = response.getStatusCode();
            firstName = Objects.requireNonNull(response.getBody()).firstName();
            lastName = response.getBody().lastName();
        } catch (HttpStatusCodeException ex) {
            statusCode = ex.getStatusCode();
            log.error("Error when trying to update trainee profile: {}", ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("Unexpected error when trying to update trainee profile: {}", ex.getMessage());
        }
    }

    @When("trying to delete profile")
    public void whenDeleteProfile() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setBearerAuth(accessToken);
            HttpEntity<TraineeUpdateRequestDto> httpEntity = new HttpEntity<>(httpHeaders);

            var response = restTemplate.exchange(
                    "%s:%s/api/v1/trainees/%s".formatted(url, port, username),
                    HttpMethod.DELETE,
                    httpEntity,
                    Void.class
            );

            statusCode = response.getStatusCode();
        } catch (HttpStatusCodeException ex) {
            statusCode = ex.getStatusCode();
            log.error("Error when trying to delete trainee profile: {}", ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("Unexpected error when trying to delete trainee profile: {}", ex.getMessage());
        }
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

    @Then("the firstName is {string} and lastName is {string}")
    public void thenFirstAndLastNamesAre(String firstName, String lastName) {
        assertThat(this.firstName).isNotNull();
        assertThat(this.lastName).isNotNull();
        assertThat(this.firstName).isEqualTo(firstName);
        assertThat(this.lastName).isEqualTo(lastName);
    }

    @Then("the username should start with {string}")
    public void thenUsernameIs(String username) {
        assertThat(this.username).isNotNull();
        assertThat(this.username).startsWith(username);
    }

    @Then("the password is {string}")
    public void thenPasswordIs(String password) {
        assertThat(this.password).isNotNull();
        assertThat(this.password).isEqualTo(password);
    }
}