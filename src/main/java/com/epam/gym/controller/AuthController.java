package com.epam.gym.controller;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.request.ChangeLoginDto;
import com.epam.gym.dto.request.RefreshTokenRequestDto;
import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.request.UserCredentialDto;
import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.service.JwtService;
import com.epam.gym.service.WebAuthService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "Authentication endpoints")
public class AuthController {
    private final WebAuthService authService;
    private final JwtService jwtService;

    @Timed(value = "request.login.api", description = "Login API Response Time", histogram = true, percentiles = {0.5, 0.7, 0.9, 0.99})
    @PostMapping("/login")
    public JwtTokenResponseDto login(@Valid @RequestBody UserCredentialDto credential) {
        return authService.authenticate(credential.username(), credential.password());
    }

    @PostMapping("/register/trainee")
    public String registerTrainee(@Valid @RequestBody TraineeRegistrationDto trainee) {
        String[] usernameAndPassword = authService.registerTrainee(trainee);

        return """
                        Registration successful.
                        YOUR USERNAME: %s
                        YOUR PASSWORD: %s
                """.formatted(usernameAndPassword[0], usernameAndPassword[1]);
    }

    @PostMapping("/register/trainer")
    public String registerTrainer(@Valid @RequestBody TrainerRegistrationDto trainer) {
        String[] usernameAndPassword = authService.registerTrainer(trainer);

        return """
                        Registration successful.
                        YOUR USERNAME: %s
                        YOUR PASSWORD: %s
                """.formatted(usernameAndPassword[0], usernameAndPassword[1]);
    }

    @PostMapping("/change-password/{username}")
    public void changeLogin(@PathVariable String username, @Valid @RequestBody ChangeLoginDto changedLogin) {
        if (!authService.changePassword(username, changedLogin.oldPassword(), changedLogin.newPassword())) {
            throw new BadRequestException();
        }
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String tokenWithPrefix) {
        String jwtToken = tokenWithPrefix.trim().split("\\s+")[1]; // remove prefix
        authService.logout(jwtService.extractUsername(jwtToken));
    }

    @PostMapping("/refresh-token")
    public JwtTokenResponseDto refreshAccessToken(@RequestBody RefreshTokenRequestDto request) {
        return authService.refreshAccessToken(request.refreshToken());
    }
}