package com.epam.gym.controller;

import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.service.TrainingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trainings")
@AllArgsConstructor
@Tag(name = "Training controller endpoints")
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTraining(@Valid @RequestBody TrainingAddRequestDto model) {
        trainingService.save(model);
    }
}