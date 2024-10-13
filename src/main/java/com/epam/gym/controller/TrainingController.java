package com.epam.gym.controller;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.service.TrainingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trainings")
@AllArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    public void addTraining(@Valid @RequestBody TrainingAddRequestDto model) {
        if (!trainingService.save(model)) {
            throw new BadRequestException();
        }
    }
}