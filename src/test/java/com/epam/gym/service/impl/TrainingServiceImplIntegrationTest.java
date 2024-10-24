package com.epam.gym.service.impl;

import com.epam.gym.Application;
import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.request.TrainingAddRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

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

    @Test
    void shouldNotSave1() {
        // given
        var model = new TrainingAddRequestDto(
                "johndoe1",
                "nonExistentTrainer",
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

    @Test
    void shouldNotSave2() {
        // given
        var model = new TrainingAddRequestDto(
                "nonExistentTrainee",
                "vincestewart50",
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

    @Test
    void shouldNotSave3() {
        // given
        var model = new TrainingAddRequestDto(
                "johndoe1",
                "janedoe2", // trainee username
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

    @Test
    void shouldNotSave4() {
        // given
        var model = new TrainingAddRequestDto(
                "umalewis49", // trainer username
                "vincestewart50",
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