package com.epam.service;

import com.epam.aop.Loggable;
import com.epam.dto.TraineeDto;
import com.epam.entity.TraineeEntity;
import com.epam.repository.TraineeRepository;
import com.epam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeService implements CrudService<TraineeDto, BigInteger> {
    private final TraineeRepository traineeRepository;
    private final UserRepository userRepository;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, UserRepository userRepository) {
        this.traineeRepository = traineeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Loggable
    public TraineeDto add(TraineeDto traineeDto) {
        TraineeEntity traineeEntity = TraineeEntity.fromDto(traineeDto);
        TraineeEntity t = traineeRepository.save(traineeEntity);

        return t.toDto(userRepository);
    }

    @Override
    @Loggable
    public TraineeDto update(TraineeDto traineeDto) {
        if (traineeDto == null || traineeDto.getId() == null) {
            return null;
        }

        TraineeEntity traineeEntity = TraineeEntity.fromDto(traineeDto);
        traineeRepository.update(traineeEntity.getId(), traineeEntity);

        return get(traineeEntity.getId()).orElse(null);
    }

    @Override
    @Loggable
    public void delete(BigInteger id) {
        traineeRepository.deleteById(id);
    }

    @Override
    @Loggable
    public Optional<TraineeDto> get(BigInteger id) {
        var trainee = traineeRepository.findById(id);
        return trainee.map(traineeEntity -> traineeEntity.toDto(userRepository));
    }

    @Override
    @Loggable
    public List<TraineeDto> getAll() {
        List<TraineeEntity> traineeEntities = new ArrayList<>();
        traineeRepository.findAll().forEach(traineeEntities::add);

        return traineeEntities.stream()
                .map(traineeEntity -> traineeEntity.toDto(userRepository))
                .toList();
    }
}