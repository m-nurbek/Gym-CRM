package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    @Override
    public TraineeDto save(TraineeDto trainee) {
        var traineeEntity = traineeRepository.save(TraineeEntity.fromDto(trainee));
        return traineeEntity.toDto();
    }

    @Override
    public boolean update(TraineeDto trainee) {
        Optional<TraineeEntity> traineeEntityOptional = traineeRepository.findById(trainee.getId());

        if (traineeEntityOptional.isPresent()) {
            TraineeEntity traineeEntity = traineeEntityOptional.get();
            traineeEntity.setAddress(trainee.getAddress());
            traineeEntity.setDob(trainee.getDob());
            traineeEntity.setUser(trainee.getUser());
            traineeEntity.setTrainings(trainee.getTrainings());

            return traineeRepository.update(traineeEntity);
        }

        return false;
    }

    @Override
    public boolean delete(BigInteger id) {
        return traineeRepository.deleteById(id);
    }

    @Override
    public Optional<TraineeDto> get(BigInteger bigInteger) {
        var trainee = traineeRepository.findById(bigInteger);
        return trainee.map(TraineeEntity::toDto);
    }

    @Override
    public List<TraineeDto> getAll() {
        List<TraineeEntity> traineeEntities = new ArrayList<>();
        traineeRepository.findAll().forEach(traineeEntities::add);

        return traineeEntities.stream().map(TraineeEntity::toDto).toList();
    }

    @Override
    public long count() {
        return traineeRepository.count();
    }
}