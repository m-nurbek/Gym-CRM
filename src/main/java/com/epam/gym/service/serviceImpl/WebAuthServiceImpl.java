package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import com.epam.gym.service.WebAuthService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class WebAuthServiceImpl implements WebAuthService {
    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    private final MeterRegistry meterRegistry;
    private final Counter counter;

    public WebAuthServiceImpl(UserService userService, TraineeService traineeService, TrainerService trainerService, MeterRegistry meterRegistry) {
        this.userService = userService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.meterRegistry = meterRegistry;
        counter = Counter.builder("user.login.counter")
                .tag("status", "authenticated")
                .description("Total number of users' logins to the system")
                .register(this.meterRegistry);
    }

    @Override
    public boolean authenticate(String username, String password) {
        counter.increment();
        return userService.isUsernameAndPasswordMatch(username, password);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userService.changePassword(username, oldPassword, newPassword);
    }

    private UserDto registerUser(String firstName, String lastName) {
        return userService.save(firstName, lastName, true);
    }

    @Override
    public String[] registerTrainee(String firstName, String lastName, LocalDate dob, String address) {
        UserDto u = registerUser(firstName, lastName);
        traineeService.save(dob, address, u.id());

        return new String[]{u.username(), u.password()};
    }

    @Override
    public String[] registerTrainer(String firstName, String lastName, TrainingTypeEnum specialization) {
        UserDto u = registerUser(firstName, lastName);
        trainerService.save(specialization, u.id());

        return new String[]{u.username(), u.password()};
    }
}