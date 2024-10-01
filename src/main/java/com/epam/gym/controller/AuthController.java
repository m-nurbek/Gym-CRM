package com.epam.gym.controller;

import com.epam.gym.aop.Authenticated;
import com.epam.gym.dto.model.request.ChangeLoginModel;
import com.epam.gym.dto.model.request.TraineeRegistrationModel;
import com.epam.gym.dto.model.request.TrainerRegistrationModel;
import com.epam.gym.dto.model.request.UserCredentialModel;
import com.epam.gym.dto.model.request.UserRegistrationModel;
import com.epam.gym.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserCredentialModel credential) {
        if (authService.authenticate(credential.username(), credential.password())) {
            return ResponseEntity.ok("Login successful");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
    }

    @Authenticated
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        String username = authService.getUsernameOfAuthenticatedAccount();

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
        }

        if (authService.logout(username)) {
            return ResponseEntity.ok("Logout successful");
        }

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Logout failed");
    }

    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationModel user) {
        String[] usernameAndPassword = authService.register(user.firstName(), user.lastName());

        return new ResponseEntity<>("""
                        Registration successful.
                        YOUR USERNAME: %s
                        YOUR PASSWORD: %s
                """.formatted(usernameAndPassword[0], usernameAndPassword[1]),
                HttpStatus.OK
        );
    }

    @PostMapping("/register/trainee")
    public ResponseEntity<String> registerTrainee(@Valid @RequestBody TraineeRegistrationModel trainee) {
        String[] usernameAndPassword = authService.registerTrainee(trainee.firstName(), trainee.lastName(), trainee.dob(), trainee.address());

        return new ResponseEntity<>("""
                        Registration successful.
                        YOUR USERNAME: %s
                        YOUR PASSWORD: %s
                """.formatted(usernameAndPassword[0], usernameAndPassword[1]),
                HttpStatus.OK
        );
    }

    @PostMapping("/register/trainer")
    public ResponseEntity<String> registerTrainer(@Valid @RequestBody TrainerRegistrationModel trainer) {
        String[] usernameAndPassword = authService.registerTrainer(trainer.firstName(), trainer.lastName(), trainer.specialization());

        return new ResponseEntity<>("""
                        Registration successful.
                        YOUR USERNAME: %s
                        YOUR PASSWORD: %s
                """.formatted(usernameAndPassword[0], usernameAndPassword[1]),
                HttpStatus.OK
        );
    }

    @Authenticated
    @PostMapping("/login/change-password/{username}")
    public ResponseEntity<String> changeLogin(@PathVariable String username, @Valid @RequestBody ChangeLoginModel changedLogin) {
        boolean success = authService.changePassword(username, changedLogin.oldPassword(), changedLogin.newPassword());

        if (success) {
            return new ResponseEntity<>("Changed password successfully!", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed to change password", HttpStatus.BAD_REQUEST);
    }
}