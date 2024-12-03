package com.epam.gym.controller;

import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nurbek on 03.12.2024
 */
@RestController
@RequestMapping("/api/v1/trainers")
public record TrainerController(
        TrainerService trainerService
) {

    @PostMapping("/workload")
    public void handleTrainerWorkload(@Valid @RequestBody TrainerWorkloadRequest request) {
        trainerService.processWorkload(request);
    }
}