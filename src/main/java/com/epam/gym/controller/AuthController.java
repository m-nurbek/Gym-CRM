package com.epam.gym.controller;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.request.ChangeLoginDto;
import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.request.UserCredentialDto;
import com.epam.gym.service.WebAuthService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final WebAuthService authService;

    @Timed(value = "request.login.api", description = "Login API Response Time", histogram = true, percentiles = {0.5, 0.7, 0.9, 0.99})
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Valid @RequestBody UserCredentialDto credential) {
        authService.authenticate(credential.username(), credential.password());
    }

    @PostMapping("/register/trainee")
    @ResponseStatus(HttpStatus.OK)
    public String registerTrainee(@Valid @RequestBody TraineeRegistrationDto trainee) {
        String[] usernameAndPassword = authService.registerTrainee(trainee);

        return """
                        Registration successful.
                        YOUR USERNAME: %s
                        YOUR PASSWORD: %s
                """.formatted(usernameAndPassword[0], usernameAndPassword[1]);
    }

    @PostMapping("/register/trainer")
    @ResponseStatus(HttpStatus.OK)
    public String registerTrainer(@Valid @RequestBody TrainerRegistrationDto trainer) {
        String[] usernameAndPassword = authService.registerTrainer(trainer);

        return """
                        Registration successful.
                        YOUR USERNAME: %s
                        YOUR PASSWORD: %s
                """.formatted(usernameAndPassword[0], usernameAndPassword[1]);
    }

    @PostMapping("/change-password/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void changeLogin(@PathVariable String username, @Valid @RequestBody ChangeLoginDto changedLogin) {
        if (!authService.changePassword(username, changedLogin.oldPassword(), changedLogin.newPassword())) {
            throw new BadRequestException();
        }
    }
}