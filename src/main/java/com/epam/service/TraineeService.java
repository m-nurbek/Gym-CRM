package com.epam.service;

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
    public TraineeService(TraineeRepository traineeRepository, UserRepository userRepository1) {
        this.traineeRepository = traineeRepository;
        this.userRepository = userRepository1;
    }

    @Override
    public void add(TraineeDto traineeDto) {
        TraineeEntity traineeEntity = TraineeEntity.fromDto(traineeDto);
        traineeRepository.save(traineeEntity);
    }

    @Override
    public void update(TraineeDto obj) {
        TraineeEntity traineeEntity = TraineeEntity.fromDto(obj);
        traineeRepository.update(traineeEntity.getId(), traineeEntity);
    }

    @Override
    public void delete(BigInteger bigInteger) {
        traineeRepository.deleteById(bigInteger);
    }

    @Override
    public Optional<TraineeDto> get(BigInteger bigInteger) {
        return traineeRepository.findById(bigInteger).map(traineeEntity -> traineeEntity.toDto(userRepository));
    }

    @Override
    public List<TraineeDto> getAll() {
        List<TraineeEntity> traineeEntities = new ArrayList<>();
        traineeRepository.findAll().forEach(traineeEntities::add);

        return traineeEntities.stream()
                .map(traineeEntity -> traineeEntity.toDto(userRepository))
                .toList();
    }
}