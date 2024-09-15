package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final UserService userService;
    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeServiceImpl(UserService userService, TraineeRepository traineeRepository) {
        this.userService = userService;
        this.traineeRepository = traineeRepository;
    }

    @Override
    public Optional<TraineeDto> findByUsername(String username) {
        Optional<UserDto> userDto = userService.findByUsername(username);

        if (userDto.isPresent()) {
            var trainee = traineeRepository.findById(userDto.get().getId());

            if (trainee.isPresent()) {
                TraineeEntity traineeEntity = trainee.get();
                return Optional.of(traineeEntity.toDto());
            }
        }

        return Optional.empty();
    }
}