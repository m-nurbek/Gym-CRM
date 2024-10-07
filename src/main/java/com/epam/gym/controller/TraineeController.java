package com.epam.gym.controller;

import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.request.TraineeUpdateRequestDto;
import com.epam.gym.dto.response.SimpleTrainerResponseDto;
import com.epam.gym.dto.response.TraineeResponseDto;
import com.epam.gym.dto.response.TraineeUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTraineeDto;
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
@RequestMapping("/api/v1/trainees")
@AllArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;
    private final UserService userService;

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeResponseDto getProfile(@PathVariable String username) {
        return traineeService.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeUpdateResponseDto updateProfile(
            @PathVariable String username,
            @Valid @RequestBody TraineeUpdateRequestDto requestModel
    ) {
        return traineeService.update(username, requestModel).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@PathVariable String username) {
        if (!traineeService.deleteByUsername(username)) {
            throw new NotFoundException();
        }
    }

    @GetMapping("/trainers/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<SimpleTrainerResponseDto> getNotAssignedActiveTrainers(@PathVariable String username) {
        if (traineeService.findByUsername(username).isEmpty()) {
            throw new NotFoundException();
        }

        return traineeService.getUnassignedTrainersByUsernameToResponse(username);
    }

    @PutMapping("/update-trainers/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<SimpleTrainerResponseDto> updateTrainersList(
            @PathVariable String username,
            @Valid @RequestBody List<String> trainerUsernameList
    ) {
        if (traineeService.findByUsername(username).isEmpty()) {
            throw new NotFoundException();
        }

        return traineeService.updateTrainerListByUsername(username, trainerUsernameList);
    }

    @GetMapping("/trainings/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Set<TrainingResponseForTraineeDto> getTrainingsList(
            @PathVariable String username,
            @RequestParam(value = "periodFrom", required = false) LocalDate periodFrom,
            @RequestParam(value = "periodTo", required = false) LocalDate periodTo,
            @RequestParam(value = "trainerName", required = false) String trainerName,
            @RequestParam(value = "trainingType", required = false) String trainingType) {

        if (traineeService.findByUsername(username).isEmpty()) {
            throw new NotFoundException();
        }

        return traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);
    }

    @PatchMapping("/active-state/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void changeProfileActiveState(@PathVariable String username, @RequestBody Boolean isActive) {
        if (!userService.updateActiveState(username, isActive)) {
            throw new NotFoundException();
        }
    }
}