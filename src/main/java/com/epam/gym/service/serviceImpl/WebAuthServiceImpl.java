package com.epam.gym.service.serviceImpl;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.service.UserService;
import com.epam.gym.service.WebAuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component(value = "WebAuthService")
@AllArgsConstructor
public class WebAuthServiceImpl implements WebAuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    @Override
    public boolean authenticate(String username, String password) {
        return userService.isUsernameAndPasswordMatch(username, password);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userService.changePassword(username, oldPassword, newPassword);
    }

    @Override
    public String[] register(String firstName, String lastName) {
        UserDto u = registerUser(firstName, lastName);
        return new String[]{u.getUsername(), u.getPassword()};
    }

    private UserDto registerUser(String firstName, String lastName) {
        return userService.save(UserDto.builder().firstName(firstName).lastName(lastName).isActive(true).build());
    }

    @Override
    public String[] registerTrainee(String firstName, String lastName, LocalDate dob, String address) {
        UserDto u = registerUser(firstName, lastName);
        UserEntity userEntity = userRepository.findById(u.getId()).orElseThrow(BadRequestException::new);
        traineeService.save(TraineeDto.builder().address(address).dob(dob).user(userEntity).build());

        return new String[]{u.getUsername(), u.getPassword()};
    }

    @Override
    public String[] registerTrainer(String firstName, String lastName, TrainingTypeEnum specialization) {
        Optional<TrainingTypeDto> type = trainingTypeService.getTrainingTypeName(specialization);

        if (type.isEmpty()) {
            throw new BadRequestException();
        }

        UserDto u = registerUser(firstName, lastName);
        UserEntity userEntity = userRepository.findById(u.getId()).orElseThrow(BadRequestException::new);
        trainerService.save(TrainerDto.builder().specialization(TrainingTypeEntity.fromDto(type.get())).user(userEntity).build());

        return new String[]{u.getUsername(), u.getPassword()};
    }
}