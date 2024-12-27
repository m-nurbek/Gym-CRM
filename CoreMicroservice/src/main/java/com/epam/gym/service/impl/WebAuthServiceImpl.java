package com.epam.gym.service.impl;

import com.epam.gym.dto.UserDto;
import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.dto.response.RegistrationResponseDto;
import com.epam.gym.service.BruteForceProtectorService;
import com.epam.gym.service.JwtService;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import com.epam.gym.service.WebAuthService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WebAuthServiceImpl implements WebAuthService {
    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BruteForceProtectorService bruteForceProtectorService;
    private final HttpServletRequest request;

    private final MeterRegistry meterRegistry;
    private Counter counter;

    @PostConstruct
    private void setup() {
        counter = Counter.builder("user.login.counter")
                .tag("status", "authenticated")
                .description("Total number of users' logins to the system")
                .register(this.meterRegistry);
    }

    @Override
    public JwtTokenResponseDto authenticate(String username, String password) {
        counter.increment();

        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        authenticationManager.authenticate(authentication);

        String clientIp = bruteForceProtectorService.getClientIP(request);
        bruteForceProtectorService.loginSucceeded(clientIp);

        return jwtService.generateAccessAndRefreshTokens(username);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        userService.changePassword(username, oldPassword, newPassword);
    }

    private UserDto registerUser(String firstName, String lastName, String password) {
        return userService.save(firstName, lastName, password, true);
    }

    @Override
    public RegistrationResponseDto registerTrainee(TraineeRegistrationDto trainee) {
        UserDto u = registerUser(trainee.firstName(), trainee.lastName(), trainee.password());
        traineeService.save(trainee.dob(), trainee.address(), u.id());
        return new RegistrationResponseDto(u.username());
    }

    @Override
    public RegistrationResponseDto registerTrainer(TrainerRegistrationDto trainer) {
        UserDto u = registerUser(trainer.firstName(), trainer.lastName(), trainer.password());
        trainerService.save(trainer.specialization(), u.id());
        return new RegistrationResponseDto(u.username());
    }

    @Override
    public void logout(String username) {
        jwtService.deleteRefreshToken(username);
    }

    @Override
    public JwtTokenResponseDto refreshAccessToken(String refreshToken) {
        String accessToken = jwtService.refreshAccessToken(refreshToken);

        return new JwtTokenResponseDto(
                accessToken,
                refreshToken
        );
    }
}