package com.epam.gym.service.impl;

import com.epam.gym.Application;
import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.dto.response.SimpleTraineeResponseDto;
import com.epam.gym.dto.response.SimpleTrainerResponseDto;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainingServiceImplIntegrationTest {
    @Autowired
    private TrainingServiceImpl trainingService;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;

    @Test
    void shouldSave() {
        // given
        String traineeUsername = "johndoe1";
        String trainerUsername = "vincestewart50";
        var model = new TrainingAddRequestDto(
                traineeUsername,
                trainerUsername,
                "TRAINING NAME",
                LocalDate.of(2025, 1, 1),
                3
        );

        // when
        trainingService.save(model);
        var trainerList = traineeService.findByUsername(traineeUsername).trainerList().stream().map(SimpleTrainerResponseDto::username).toList();
        var traineeList = trainerService.findByUsername(trainerUsername).traineeList().stream().map(SimpleTraineeResponseDto::username).toList();

        // then
        assertAll(
                "Training creation check",
                () -> assertTrue(traineeList.contains(traineeUsername)),
                () -> assertTrue(trainerList.contains(trainerUsername))
        );
    }

    private static Stream<Arguments> provideTraineeAndTrainerUsernames() {
        return Stream.of(
                Arguments.of("johndoe1", "nonExistentTrainer"),
                Arguments.of("nonExistentTrainee", "vincestewart50"),
                Arguments.of("johndoe1", "janedoe2"),
                Arguments.of("umalewis49", "vincestewart50")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTraineeAndTrainerUsernames")
    void shouldNotSave(String traineeUsername, String trainerUsername) {
        // given
        var model = new TrainingAddRequestDto(
                traineeUsername,
                trainerUsername,
                "TRAINING NAME",
                LocalDate.of(2025, 1, 1),
                3
        );

        // when & then
        assertAll(
                "Assertions for 'save()' method",
                () -> assertThrows(BadRequestException.class, () -> trainingService.save(model))
        );
    }
}