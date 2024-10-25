package com.epam.gym.service.impl;

import com.epam.gym.Application;
import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.response.RegistrationResponseDto;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class WebAuthServiceImplIntegrationTest {
    @Autowired
    private WebAuthServiceImpl webAuthService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    @Test
    void shouldAuthenticate() {
        // given
        String username = "johndoe1";
        String password = "password1";

        // when & then
        assertDoesNotThrow(() -> webAuthService.authenticate(username, password));
    }

    @ParameterizedTest
    @MethodSource("provideUsernameAndPassword")
    void shouldNotAuthenticate(String username, String password) {
        // when & then
        assertThrows(BadCredentialsException.class, () -> webAuthService.authenticate(username, password));
    }

    private static Stream<Arguments> provideUsernameAndPassword() {
        return Stream.of(
                Arguments.of("non-existent", "password1"),
                Arguments.of("johndoe1", "wrongPassword")
        );
    }

    @Test
    void shouldChangePassword() {
        // given
        String username = "johndoe1";
        String oldPassword = "password1";
        String newPassword = "NEW_PASSWORD";

        // when & then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertDoesNotThrow(() -> webAuthService.changePassword(username, oldPassword, newPassword))
        );
    }

    @Test
    void shouldNotChangePassword1() {
        // given
        String username = "non-existent";
        String oldPassword = "password1";
        String newPassword = "NEW_PASSWORD";

        // when & then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertThrows(NotFoundException.class, () -> webAuthService.changePassword(username, oldPassword, newPassword))
        );
    }

    @Test
    void shouldNotChangePassword2() {
        // given
        String username = "johndoe1";
        String oldPassword = "wrongPassword";
        String newPassword = "NEW_PASSWORD";

        // when & then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertThrows(BadRequestException.class, () -> webAuthService.changePassword(username, oldPassword, newPassword))
        );
    }

    @Test
    void registerTrainee() {
        // given
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        LocalDate dob = LocalDate.of(2001, 1, 1);
        String address = "ADDRESS";
        var traineeRegistrationDto = new TraineeRegistrationDto(firstName, lastName, dob, address, "password");

        // when
        RegistrationResponseDto response = webAuthService.registerTrainee(traineeRegistrationDto);

        var user = userRepository.findByUsername(response.username()).orElse(null);
        var trainee = traineeRepository.findByUser_Username(response.username()).orElse(null);

        // then
        assertAll(
                "Assertions for 'registerTrainee()' method",
                () -> assertThat(response.username()).isEqualTo(firstName + "." + lastName),
                () -> assertThat(user).isNotNull(),
                () -> assertThat(trainee).isNotNull(),
                () -> assertThat(user.getUsername()).isEqualTo(response.username()),
                () -> assertThat(user.getIsActive()).isTrue(),
                () -> assertThat(user.getFirstName()).isEqualTo(firstName),
                () -> assertThat(user.getLastName()).isEqualTo(lastName),
                () -> assertThat(trainee.getDob()).isEqualTo(dob),
                () -> assertThat(trainee.getAddress()).isEqualTo(address),
                () -> assertThat(user.getTrainee().getId()).isEqualTo(trainee.getId()),
                () -> assertThat(trainee.getUser().getId()).isEqualTo(user.getId())
        );
    }

    @Test
    void registerTrainer() {
        // given
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        var type = TrainingTypeEnum.AEROBICS;
        var trainerRegistrationDto = new TrainerRegistrationDto(firstName, lastName, type, "password");

        // when
        RegistrationResponseDto response = webAuthService.registerTrainer(trainerRegistrationDto);

        var user = userRepository.findByUsername(response.username()).orElse(null);
        var trainer = trainerRepository.findByUser_Username(response.username()).orElse(null);

        // then
        assertAll(
                "Assertions for 'registerTrainer()' method",
                () -> assertThat(response.username()).isEqualTo(firstName + "." + lastName),
                () -> assertThat(user).isNotNull(),
                () -> assertThat(trainer).isNotNull(),
                () -> assertThat(user.getUsername()).isEqualTo(response.username()),
                () -> assertThat(user.getIsActive()).isTrue(),
                () -> assertThat(user.getFirstName()).isEqualTo(firstName),
                () -> assertThat(user.getLastName()).isEqualTo(lastName),
                () -> assertThat(trainer.getSpecialization().getName()).isEqualTo(type),
                () -> assertThat(user.getTrainer().getId()).isEqualTo(trainer.getId()),
                () -> assertThat(trainer.getUser().getId()).isEqualTo(user.getId())
        );
    }
}