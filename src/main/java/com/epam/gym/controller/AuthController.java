package com.epam.gym.controller;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.controller.exception.UnauthorizedException;
import com.epam.gym.dto.model.request.ChangeLoginModel;
import com.epam.gym.dto.model.request.TraineeRegistrationModel;
import com.epam.gym.dto.model.request.TrainerRegistrationModel;
import com.epam.gym.dto.model.request.UserCredentialModel;
import com.epam.gym.service.serviceImpl.WebAuthServiceImpl;
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
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final WebAuthServiceImpl authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Valid @RequestBody UserCredentialModel credential) throws UnauthorizedException {
        if (!authService.authenticate(credential.username(), credential.password())) {
            throw new UnauthorizedException();
        }
    }

    @PostMapping("/register/trainee")
    @ResponseStatus(HttpStatus.OK)
    public String registerTrainee(@Valid @RequestBody TraineeRegistrationModel trainee) {
        String[] usernameAndPassword = authService.registerTrainee(trainee.firstName(), trainee.lastName(), trainee.dob(), trainee.address());

        return """
                        Registration successful.
                        YOUR USERNAME: %s
                        YOUR PASSWORD: %s
                """.formatted(usernameAndPassword[0], usernameAndPassword[1]);
    }

    @PostMapping("/register/trainer")
    @ResponseStatus(HttpStatus.OK)
    public String registerTrainer(@Valid @RequestBody TrainerRegistrationModel trainer) {
        String[] usernameAndPassword = authService.registerTrainer(trainer.firstName(), trainer.lastName(), trainer.specialization());

        return """
                        Registration successful.
                        YOUR USERNAME: %s
                        YOUR PASSWORD: %s
                """.formatted(usernameAndPassword[0], usernameAndPassword[1]);
    }

    @PostMapping("/change-password/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void changeLogin(@PathVariable String username, @Valid @RequestBody ChangeLoginModel changedLogin) throws BadRequestException {
        if (!authService.changePassword(username, changedLogin.oldPassword(), changedLogin.newPassword())) {
            throw new BadRequestException();
        }
    }
}