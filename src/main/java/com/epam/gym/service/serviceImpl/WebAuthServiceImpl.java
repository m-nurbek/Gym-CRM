package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.UserDto;
import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import com.epam.gym.service.WebAuthService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WebAuthServiceImpl implements WebAuthService {
    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthenticationManager authenticationManager;

    private final MeterRegistry meterRegistry;
    private final Counter counter;

    public WebAuthServiceImpl(UserService userService, TraineeService traineeService, TrainerService trainerService, AuthenticationManager authenticationManager, MeterRegistry meterRegistry) {
        this.userService = userService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.authenticationManager = authenticationManager;
        this.meterRegistry = meterRegistry;
        counter = Counter.builder("user.login.counter")
                .tag("status", "authenticated")
                .description("Total number of users' logins to the system")
                .register(this.meterRegistry);
    }

    @Override
    public void authenticate(String username, String password) {
        counter.increment();

        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        authenticationManager.authenticate(authentication);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userService.changePassword(username, oldPassword, newPassword);
    }

    private UserDto registerUser(String firstName, String lastName, String password) {
        return userService.save(firstName, lastName, password, true);
    }

    @Override
    public String[] registerTrainee(TraineeRegistrationDto trainee) {
        UserDto u = registerUser(trainee.firstName(), trainee.lastName(), trainee.password());
        traineeService.save(trainee.dob(), trainee.address(), u.id());

        return new String[]{u.username(), u.password()};
    }

    @Override
    public String[] registerTrainer(TrainerRegistrationDto trainer) {
        UserDto u = registerUser(trainer.firstName(), trainer.lastName(), trainer.password());
        trainerService.save(trainer.specialization(), u.id());

        return new String[]{u.username(), u.password()};
    }
}