package com.epam.gym.controller;

import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.request.TrainerUpdateRequestDto;
import com.epam.gym.dto.response.TrainerResponseDto;
import com.epam.gym.dto.response.TrainerUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTrainerDto;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/trainers")
@AllArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;
    private final UserService userService;

    @GetMapping("/{username}")
    public TrainerResponseDto getProfile(@PathVariable String username) {
        return trainerService.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/{username}")
    public TrainerUpdateResponseDto updateProfile(
            @PathVariable String username,
            @Valid @RequestBody TrainerUpdateRequestDto requestModel
    ) {
        return trainerService.update(username, requestModel).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/trainings/{username}")
    public Set<TrainingResponseForTrainerDto> getTrainingsList(
            @PathVariable String username,
            @RequestParam(value = "periodFrom", required = false) LocalDate periodFrom,
            @RequestParam(value = "periodTo", required = false) LocalDate periodTo,
            @RequestParam(value = "traineeName", required = false) String traineeName) {

        if (trainerService.findByUsername(username).isEmpty()) {
            throw new NotFoundException();
        }

        return trainerService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, traineeName);
    }

    @PatchMapping("/active-state/{username}")
    public void changeProfileActiveState(@PathVariable String username, @RequestBody Boolean isActive) {
        if (!userService.updateActiveState(username, isActive)) {
            throw new NotFoundException();
        }
    }
}