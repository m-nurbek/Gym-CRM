package com.epam.gym.service.impl;

import com.epam.gym.Application;
import com.epam.gym.controller.exception.ConflictException;
import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.UserDto;
import com.epam.gym.dto.request.TrainerUpdateRequestDto;
import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.dto.response.TrainerResponseDto;
import com.epam.gym.dto.response.TrainerUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTrainerDto;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainerServiceImplIntegrationTest {
    @Autowired
    private TrainerServiceImpl trainerService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSave() {
        // given
        var specialization = TrainingTypeEnum.AEROBICS;
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        boolean isActive = false;
        String password = "password";

        // when
        UserDto user = userService.save(firstName, lastName, password, isActive);
        TrainerResponseDto trainer = trainerService.save(specialization, user.id());

        UserEntity userEntity = userRepository.findById(user.id()).orElse(null);

        // then
        assertAll(
                "Assertions for 'save()' method",
                () -> assertThat(trainer).isNotNull(),
                () -> assertThat(trainer.firstName()).isEqualTo(firstName),
                () -> assertThat(trainer.lastName()).isEqualTo(lastName),
                () -> assertThat(trainer.isActive()).isEqualTo(isActive),
                () -> assertThat(trainer.specialization()).isEqualTo(specialization.name()),

                () -> assertThat(userEntity).isNotNull(),
                () -> assertThat(userEntity.getFirstName()).isEqualTo(firstName),
                () -> assertThat(userEntity.getLastName()).isEqualTo(lastName),
                () -> assertThat(userEntity.getTrainee()).isNull(),
                () -> assertThat(userEntity.getTrainer()).isNotNull()
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 25, 26, 50, 51})
    void shouldNotSave(Long id) {
        // given
        var specialization = TrainingTypeEnum.AEROBICS;
        BigInteger userId = BigInteger.valueOf(id);

        // when & then
        assertAll(
                "Assertions for 'save()' method",
                () -> assertThrows(ConflictException.class, () -> trainerService.save(specialization, userId))
        );
    }

    @Test
    void shouldFindByUsername() {
        // given
        String username = "vincestewart50";

        // when
        TrainerResponseDto trainer = trainerService.findByUsername(username);

        // then
        assertAll(
                "Assertions for 'findByUsername()' method",
                () -> assertThat(trainer).isNotNull()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"johndoe1", "non-existent"})
    void shouldNotFindUsername(String username) {
        // when & then
        assertAll(
                "Assertions for 'findByUsername()' method",
                () -> assertThrows(NotFoundException.class, () -> trainerService.findByUsername(username))
        );
    }

    @Test
    void shouldUpdate() {
        // given
        String username = "vincestewart50";
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        var type = TrainingTypeEnum.BOXING;
        boolean isActive = false;

        TrainerUpdateRequestDto model = new TrainerUpdateRequestDto(
                firstName,
                lastName,
                type,
                isActive
        );

        // when
        TrainerUpdateResponseDto trainer = trainerService.update(username, model);

        UserEntity user = userRepository.findByUsername(username).orElse(null);
        TrainerResponseDto trainerResponseDto = trainerService.findByUsername(username);

        // then
        assertAll(
                "Assertions for 'update()' method",
                () -> assertThat(trainer).isNotNull(),
                () -> assertThat(trainer.firstName()).isEqualTo(firstName),
                () -> assertThat(trainer.lastName()).isEqualTo(lastName),
                () -> assertThat(trainer.username()).isEqualTo(username),
                () -> assertThat(trainer.isActive()).isEqualTo(isActive),
                () -> assertThat(trainer.specialization()).isEqualTo(type.name()),

                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getFirstName()).isEqualTo(firstName),
                () -> assertThat(user.getLastName()).isEqualTo(lastName),
                () -> assertThat(user.getIsActive()).isEqualTo(isActive),
                () -> assertThat(user.getTrainer()).isNotNull(),
                () -> assertThat(user.getTrainee()).isNull(),

                () -> assertThat(trainerResponseDto).isNotNull(),
                () -> assertThat(trainerResponseDto.specialization()).isEqualTo(type.name())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"non-existent", "johndoe1"})
    void shouldNotUpdate(String username) {
        // given
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        var type = TrainingTypeEnum.BOXING;
        boolean isActive = false;

        TrainerUpdateRequestDto model = new TrainerUpdateRequestDto(
                firstName,
                lastName,
                type,
                isActive
        );

        // when & then
        assertAll(
                "Assertions for 'update()' method",
                () -> assertThrows(NotFoundException.class, () -> trainerService.update(username, model))
        );
    }

    @Test
    void shouldGetTrainingsByUsernameToResponse1() {
        // given
        String username = "vincestewart50";
        LocalDate periodFrom = null;
        LocalDate periodTo = null;
        String traineeName = null;

        // when
        Set<TrainingResponseForTrainerDto> trainings = trainerService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, traineeName);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isNotNull(),
                () -> assertThat(trainings.isEmpty()).isFalse(),
                () -> assertThat(trainings.size()).isEqualTo(1)
        );
    }

    private static Stream<Arguments> provideArgumentsForGetTrainingsByUsername() {
        return Stream.of(
                Arguments.of("vincestewart50", null, null, null, 4),
                Arguments.of("vincestewart50", LocalDate.of(2027, 1, 1), LocalDate.of(2030, 1, 1), null, 1),
                Arguments.of("vincestewart50", null, null, "janedoe2", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForGetTrainingsByUsername")
    void shouldGetTrainingsByUsernameToResponse2(String trainerUsername, LocalDate periodFrom, LocalDate periodTo, String traineeName, int resultSize) {
        // given
        TrainingAddRequestDto model1 = new TrainingAddRequestDto(
                "johndoe1",
                trainerUsername,
                "TRAINING 1",
                LocalDate.of(2025, 1, 1),
                3
        );

        TrainingAddRequestDto model2 = new TrainingAddRequestDto(
                "janedoe2",
                trainerUsername,
                "TRAINING 2",
                LocalDate.of(2030, 1, 1),
                3
        );

        TrainingAddRequestDto model3 = new TrainingAddRequestDto(
                "alicesmith3",
                trainerUsername,
                "TRAINING 2",
                LocalDate.of(2035, 1, 1),
                3
        );

        // when
        trainingService.save(model1);
        trainingService.save(model2);
        trainingService.save(model3);
        Set<TrainingResponseForTrainerDto> trainer = trainerService.getTrainingsByUsernameToResponse(trainerUsername, periodFrom, periodTo, traineeName);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainer).isNotNull(),
                () -> assertThat(trainer.isEmpty()).isFalse(),
                () -> assertThat(trainer.size()).isEqualTo(resultSize)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"johndoe1", "non-existent"})
    void shouldNotGetTrainingsByUsernameToResponse(String username) {
        // given
        LocalDate periodFrom = null;
        LocalDate periodTo = null;
        String traineeName = null;

        // when & then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThrows(NotFoundException.class, () -> trainerService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, traineeName))
        );
    }
}