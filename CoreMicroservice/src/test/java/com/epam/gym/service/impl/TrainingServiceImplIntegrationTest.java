package com.epam.gym.service.impl;

import com.epam.gym.Application;
import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.request.TrainingAddRequestDto;
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

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainingServiceImplIntegrationTest {
    @Autowired
    private TrainingServiceImpl trainingService;

    @Test
    void shouldSave() {
        // given
        var model = new TrainingAddRequestDto(
                "johndoe1",
                "vincestewart50",
                "TRAINING NAME",
                LocalDate.of(2025, 1, 1),
                3
        );

        // when & then
        trainingService.save(model);
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