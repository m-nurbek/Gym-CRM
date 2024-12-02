package com.epam.gym.controller;

import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.request.ActiveStateRequestDto;
import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TraineeUpdateRequestDto;
import com.epam.gym.dto.request.UpdateTrainersListRequestDto;
import com.epam.gym.dto.response.RegistrationResponseDto;
import com.epam.gym.dto.response.SimpleTrainerResponseDto;
import com.epam.gym.dto.response.TraineeResponseDto;
import com.epam.gym.dto.response.TraineeUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTraineeDto;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.UserService;
import com.epam.gym.service.WebAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/trainees")
@AllArgsConstructor
@Tag(name = "Trainee controller endpoints")
public class TraineeController {
    private final TraineeService traineeService;
    private final UserService userService;
    private final WebAuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseDto registerTrainee(@Valid @RequestBody TraineeRegistrationDto trainee) {
        return authService.registerTrainee(trainee);
    }

    @Secured("ROLE_TRAINEE")
    @GetMapping("/{username}")
    public TraineeResponseDto getProfile(@PathVariable String username) {
        return traineeService.findByUsername(username);
    }

    @Secured("ROLE_TRAINEE")
    @PutMapping("/{username}")
    public TraineeUpdateResponseDto updateProfile(
            @PathVariable String username,
            @Valid @RequestBody TraineeUpdateRequestDto requestModel) {
        return traineeService.update(username, requestModel).orElseThrow(NotFoundException::new);
    }

    @Secured("ROLE_TRAINEE")
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@PathVariable String username) {
        traineeService.deleteByUsername(username);
    }

    @Secured("ROLE_TRAINEE")
    @GetMapping("/trainers/{username}")
    public Set<SimpleTrainerResponseDto> getNotAssignedActiveTrainers(@PathVariable String username) {
        return traineeService.getUnassignedTrainersByUsernameToResponse(username);
    }

    @Secured("ROLE_TRAINEE")
    @PutMapping("/trainers/{username}")
    public Set<SimpleTrainerResponseDto> updateTrainersList(
            @PathVariable String username,
            @Valid @RequestBody UpdateTrainersListRequestDto requestDto) {
        return traineeService.updateTrainerListByUsername(username, requestDto.trainerUsernames());
    }

    @Secured("ROLE_TRAINEE")
    @GetMapping("/trainings/{username}")
    public Set<TrainingResponseForTraineeDto> getTrainingsList(
            @PathVariable String username,
            @RequestParam(value = "periodFrom", required = false) LocalDate periodFrom,
            @RequestParam(value = "periodTo", required = false) LocalDate periodTo,
            @RequestParam(value = "trainerName", required = false) String trainerName,
            @RequestParam(value = "trainingType", required = false) String trainingType) {
        return traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);
    }

    @Secured("ROLE_TRAINEE")
    @PatchMapping("/active-state/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeProfileActiveState(@PathVariable String username, @Valid @RequestBody ActiveStateRequestDto activeStateRequestDto) {
        userService.updateActiveState(username, activeStateRequestDto.isActive());
    }
}