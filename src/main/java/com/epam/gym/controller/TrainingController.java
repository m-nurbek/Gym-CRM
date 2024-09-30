package com.epam.gym.controller;

import com.epam.gym.aop.Authenticated;
import com.epam.gym.dto.model.request.TrainingAddRequestModel;
import com.epam.gym.service.TrainingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/training")
@AllArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    // POST input: traineeUsername, trainerUsername, TrainingName, TrainingDate, TrainingDuration
    // response: 200 OK
    @Authenticated
    @PostMapping
    public ResponseEntity<String> addTraining(@Valid @RequestBody TrainingAddRequestModel model) {
        boolean success = trainingService.save(model);

        if (!success) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}