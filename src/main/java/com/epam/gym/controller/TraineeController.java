package com.epam.gym.controller;

import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.model.request.TraineeUpdateRequestModel;
import com.epam.gym.dto.model.response.SimpleTrainerResponseModel;
import com.epam.gym.dto.model.response.TraineeResponseModel;
import com.epam.gym.dto.model.response.TraineeUpdateResponseModel;
import com.epam.gym.dto.model.response.TrainingResponseForTraineeModel;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/trainees")
@AllArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;
    private final UserService userService;

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeResponseModel getProfile(@PathVariable String username) throws Exception {
        return traineeService.findByUsernameToResponse(username).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeUpdateResponseModel updateProfile(
            @PathVariable String username,
            @Valid @RequestBody TraineeUpdateRequestModel requestModel
    ) throws NotFoundException {
        return traineeService.update(username, requestModel).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@PathVariable String username) throws NotFoundException {
        if (!traineeService.deleteByUsername(username)) {
            throw new NotFoundException();
        }
    }

    @GetMapping("/trainers/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<SimpleTrainerResponseModel> getNotAssignedActiveTrainers(@PathVariable String username) throws NotFoundException {
        if (traineeService.findByUsername(username).isEmpty()) {
            throw new NotFoundException();
        }

        return traineeService.getUnassignedTrainersByUsernameToResponse(username);
    }

    @PutMapping("/update-trainers/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<SimpleTrainerResponseModel> updateTrainersList(
            @PathVariable String username,
            @Valid @RequestBody List<String> trainerUsernameList
    ) throws NotFoundException {
        if (traineeService.findByUsername(username).isEmpty()) {
            throw new NotFoundException();
        }

        return traineeService.updateTrainerListByUsername(username, trainerUsernameList);
    }

    @GetMapping("/trainings/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<TrainingResponseForTraineeModel> getTrainingsList(
            @PathVariable String username,
            @RequestParam(value = "periodFrom", required = false) LocalDate periodFrom,
            @RequestParam(value = "periodTo", required = false) LocalDate periodTo,
            @RequestParam(value = "trainerName", required = false) String trainerName,
            @RequestParam(value = "trainingType", required = false) String trainingType) throws NotFoundException {

        if (traineeService.findByUsername(username).isEmpty()) {
            throw new NotFoundException();
        }

        return traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);
    }

    @PatchMapping("/active-state/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void changeProfileActiveState(@PathVariable String username, @RequestBody Boolean isActive) throws NotFoundException {
        if (!userService.updateActiveState(username, isActive)) {
            throw new NotFoundException();
        }
    }
}