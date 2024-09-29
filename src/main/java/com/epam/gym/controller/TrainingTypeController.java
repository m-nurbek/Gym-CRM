package com.epam.gym.controller;

import com.epam.gym.aop.Authenticated;
import com.epam.gym.dto.model.response.TrainingTypeResponseModel;
import com.epam.gym.service.TrainingTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/v1/training-type")
@AllArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    // GET input: -
    // response: training types [type, typeId]
    @Authenticated
    @GetMapping
    public ResponseEntity<Set<TrainingTypeResponseModel>> getTrainingTypes() {
        var response = trainingTypeService.getAllToResponse();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}