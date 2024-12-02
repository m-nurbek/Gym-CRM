package com.epam.gym.controller;

import com.epam.gym.dto.request.ActiveStateRequestDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.request.TrainerUpdateRequestDto;
import com.epam.gym.dto.response.RegistrationResponseDto;
import com.epam.gym.dto.response.TrainerResponseDto;
import com.epam.gym.dto.response.TrainerUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTrainerDto;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import com.epam.gym.service.WebAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping("/api/v1/trainers")
@AllArgsConstructor
@Tag(name = "Trainer controller endpoints")
public class TrainerController {
    private final TrainerService trainerService;
    private final UserService userService;
    private final WebAuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseDto registerTrainer(@Valid @RequestBody TrainerRegistrationDto trainer) {
        return authService.registerTrainer(trainer);
    }

    @Secured("ROLE_TRAINER")
    @GetMapping("/{username}")
    public TrainerResponseDto getProfile(@PathVariable String username) {
        return trainerService.findByUsername(username);
    }

    @Secured("ROLE_TRAINER")
    @PutMapping("/{username}")
    public TrainerUpdateResponseDto updateProfile(
            @PathVariable String username,
            @Valid @RequestBody TrainerUpdateRequestDto requestModel) {
        return trainerService.update(username, requestModel);
    }

    @Secured("ROLE_TRAINER")
    @GetMapping("/trainings/{username}")
    public Set<TrainingResponseForTrainerDto> getTrainingsList(
            @PathVariable String username,
            @RequestParam(value = "periodFrom", required = false) LocalDate periodFrom,
            @RequestParam(value = "periodTo", required = false) LocalDate periodTo,
            @RequestParam(value = "traineeName", required = false) String traineeName) {
        return trainerService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, traineeName);
    }

    @Secured("ROLE_TRAINER")
    @PatchMapping("/active-state/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeProfileActiveState(@PathVariable String username, @Valid @RequestBody ActiveStateRequestDto activeStateRequestDto) {
        userService.updateActiveState(username, activeStateRequestDto.isActive());
    }
}