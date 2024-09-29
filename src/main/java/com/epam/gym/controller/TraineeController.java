package com.epam.gym.controller;

import com.epam.gym.dto.model.request.TraineeUpdateRequestModel;
import com.epam.gym.dto.model.response.SimpleTrainerResponseModel;
import com.epam.gym.dto.model.response.TraineeResponseModel;
import com.epam.gym.dto.model.response.TraineeUpdateResponseModel;
import com.epam.gym.dto.model.response.TrainingResponseModel;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/trainee")
@AllArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;
    private final UserService userService;

    // GET input: username!
    // response: firstname, lastname, dob, address, isActive, trainerList[username, firstName, lastName, specialization]
    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseModel> getProfile(@PathVariable String username) {
        var response = traineeService.findByUsernameToResponse(username);

        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }

    // PUT input: username!, firstname!, lastname!, dob, address, isActive!
    // response: username, firstName, lastName, dob, address, isActive, trainerList[username, firstName, lastName, specialization]
    @PutMapping("/{username}")
    public ResponseEntity<TraineeUpdateResponseModel> updateProfile(
            @PathVariable String username,
            @RequestBody TraineeUpdateRequestModel requestModel
    ) {
        var response = traineeService.update(username, requestModel);

        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }

    // DELETE input: username!
    // response: 200 OK
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteProfile(@PathVariable String username) {
        boolean success = traineeService.deleteByUsername(username);

        if (success) {
            return new ResponseEntity<>("Successfully deleted trainee profile", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed to delete trainee profile", HttpStatus.NOT_FOUND);
    }

    // GET input: username!
    // response: trainerList[username, firstName, lastName, specialization]
    @GetMapping("/trainers/{username}")
    public ResponseEntity<Set<SimpleTrainerResponseModel>> getNotAssignedActiveTrainers(@PathVariable String username) {
        if (traineeService.findByUsername(username).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var unassignedTrainersList = traineeService.getUnassignedTrainersByUsernameToResponse(username);
        return new ResponseEntity<>(unassignedTrainersList, HttpStatus.OK);
    }

    // PUT input: username!, trainerList[username!]!
    // response: trainerList[username, firstName, lastName, specialization]
    @PutMapping("/update-trainers/{username}")
    public ResponseEntity<Set<SimpleTrainerResponseModel>> updateTrainersList(
            @PathVariable String username,
            @RequestBody List<String> trainerUsernameList
    ) {
        if (traineeService.findByUsername(username).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var response = traineeService.updateTrainerListByUsername(username, trainerUsernameList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET input: username!, periodFrom, periodTo, traineeOrTrainerName, trainingType
    // response: training -> name, date, type, duration, traineeOrTrainerName
    // TODO: add period filter
    @GetMapping("/trainings/{username}")
    public ResponseEntity<Set<TrainingResponseModel>> getTrainingsList(@PathVariable String username) {
        if (traineeService.findByUsername(username).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var response = traineeService.getTrainingsByUsernameToResponse(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PATCH input: username!, isActive!
    // response: 200 OK
    @PatchMapping("/active-state/{username}")
    public ResponseEntity<String> changeProfileActiveState(@PathVariable String username, @RequestBody Boolean isActive) {
        boolean success = userService.updateActiveState(username, isActive);

        return success ? new ResponseEntity<>("Successfully updated the active state of the trainee profile", HttpStatus.OK)
                : new ResponseEntity<>("Failed to update the active state of the trainee profile", HttpStatus.NOT_FOUND);
    }

}