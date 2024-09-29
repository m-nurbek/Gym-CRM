package com.epam.gym.service.serviceImpl;

import com.epam.gym.aop.Loggable;
import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.AuthService;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService typeService;

    /*
     * This map is used to store the authentication status of the user.
     * The key is the username and the value is the authentication status.
     * will contain only one entry at a time.
     */
    private final ConcurrentMap<String, Boolean> map = new ConcurrentHashMap<>(1);

    @Loggable
    @Override
    public boolean authenticate(String username, String password) {
        if (isAuthenticated(username)) {
            return true;
        }

        boolean status = userRepository.isUsernameAndPasswordMatch(username, password);

        if (status) {
            logoutOfAllAccounts();
            map.put(username, true);
        }

        return status;
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userService.changePassword(username, oldPassword, newPassword);
    }

    @Loggable
    @Override
    public boolean isAuthenticated(String username) {
        return map.getOrDefault(username, false);
    }

    @Loggable
    @Override
    public boolean logout(String username) {
        return map.remove(username);
    }

    @Override
    public void logoutOfAllAccounts() {
        map.clear();
    }

    @Override
    public String[] register(String firstName, String lastName) {
        var user = userService.save(
                new UserDto(null, firstName, lastName, "username", "password", true, null, null)
        );

        return new String[]{user.getUsername(), user.getPassword()};
    }

    @Override
    public String[] registerTrainee(String firstName, String lastName, LocalDate dob, String address) {
        var user = userService.save(
                new UserDto(null, firstName, lastName, "username", "password", true, null, null)
        );

        var trainee = traineeService.save(TraineeDto.builder()
                .dob(dob)
                .address(address)
                .user(UserEntity.fromDto(user)).build());

        if (trainee == null) {
            throw new IllegalArgumentException("Trainee registration failed");
        }

        return new String[]{user.getUsername(), user.getPassword()};
    }

    @Override
    public String[] registerTrainer(String firstName, String lastName, TrainingTypeEnum specialization) {
        var user = userService.save(
                new UserDto(null, firstName, lastName, "username", "password", true, null, null)
        );

        var type = typeService.getTrainingTypeName(specialization);

        if (type.isEmpty()) {
            throw new IllegalArgumentException("Invalid training type");
        }

        var trainer = trainerService.save(TrainerDto.builder().specialization(
                TrainingTypeEntity.fromDto(type.get())
        ).user(UserEntity.fromDto(user)).build());

        if (trainer == null) {
            throw new IllegalArgumentException("Trainer registration failed");
        }

        return new String[]{user.getUsername(), user.getPassword()};
    }

    @Override
    public String getUsernameOfAuthenticatedAccount() {
        return map.keySet().stream().findFirst().orElse(null);
    }
}