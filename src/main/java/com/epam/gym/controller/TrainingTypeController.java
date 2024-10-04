package com.epam.gym.controller;

import com.epam.gym.dto.model.response.TrainingTypeResponseModel;
import com.epam.gym.service.TrainingTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/training-types")
@AllArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<TrainingTypeResponseModel> getTrainingTypes() {
        return trainingTypeService.getAllToResponse();
    }
}