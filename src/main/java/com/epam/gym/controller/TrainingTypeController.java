package com.epam.gym.controller;

import com.epam.gym.dto.response.TrainingTypeResponseDto;
import com.epam.gym.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/training-types")
@AllArgsConstructor
@Tag(name = "Training Type controller endpoints")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping
    public Set<TrainingTypeResponseDto> getTrainingTypes() {
        return trainingTypeService.getAllToResponse();
    }
}