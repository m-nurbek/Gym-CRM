package com.epam.gym.controller;

import com.epam.gym.dto.request.ChangeLoginDto;
import com.epam.gym.dto.request.RefreshTokenRequestDto;
import com.epam.gym.dto.request.UserCredentialDto;
import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.service.JwtService;
import com.epam.gym.service.WebAuthService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeLogin(@RequestHeader("Authorization") String tokenWithPrefix, @Valid @RequestBody ChangeLoginDto changedLogin) {
        String username = jwtService.extractUsername(jwtService.extractToken(tokenWithPrefix));
        authService.changePassword(username, changedLogin.oldPassword(), changedLogin.newPassword());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String tokenWithPrefix) {
        authService.logout(jwtService.extractUsername(jwtService.extractToken(tokenWithPrefix)));
    }

    @PostMapping("/refresh-token")
    public JwtTokenResponseDto refreshAccessToken(@RequestBody RefreshTokenRequestDto request) {
        return authService.refreshAccessToken(request.refreshToken());
    }
}