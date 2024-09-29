package com.epam.gym.controller;

import com.epam.gym.dto.model.request.TrainerUpdateRequestModel;
import com.epam.gym.dto.model.response.TrainerResponseModel;
import com.epam.gym.dto.model.response.TrainerUpdateResponseModel;
import com.epam.gym.dto.model.response.TrainingResponseModel;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/v1/trainer")
@AllArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;
    private final UserService userService;

    // GET input: username!
    // response: firstName, lastName, specialization, isActive, traineesList[username, firstName, lastName]
    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponseModel> getProfile(@PathVariable String username) {
        var response = trainerService.findByUsernameToResponse(username);

        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }

    // PUT input: username!, firstName!, lastName!, specialization(read only), isActive!
    // response: username, firstName, lastName, specialization, isActive, traineesList[username, firstName, lastName]
    // TODO: review this (specialization - read only)
    @PutMapping("/{username}")
    public ResponseEntity<TrainerUpdateResponseModel> updateProfile(
            @PathVariable String username,
            @RequestBody TrainerUpdateRequestModel requestModel
    ) {
        var response = trainerService.update(username, requestModel);

        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }

    // GET input: username!, periodFrom, periodTo, traineeName
    // response: training -> name, date, type, duration, traineeName
    // TODO: add period
    @GetMapping("/trainings/{username}")
    public ResponseEntity<Set<TrainingResponseModel>> getTrainingsList(@PathVariable String username) {
        if (trainerService.findByUsername(username).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var response = trainerService.getTrainingsByUsernameToResponse(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PATCH input: username!, isActive!
    // response: 200 OK
    @PatchMapping("/active-state/{username}")
    public ResponseEntity<String> changeProfileActiveState(@PathVariable String username, @RequestBody Boolean isActive) {
        boolean success = userService.updateActiveState(username, isActive);

        return success ? new ResponseEntity<>("Successfully updated the active state of the trainer profile", HttpStatus.OK)
                : new ResponseEntity<>("Failed to update the active state of the trainer profile", HttpStatus.NOT_FOUND);
    }
}