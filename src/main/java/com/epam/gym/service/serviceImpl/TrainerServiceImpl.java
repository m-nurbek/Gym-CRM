package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {
    private final UserService userService;
    private final TrainerRepository trainerRepository;
    private final TraineeService traineeService;

    @Autowired
    public TrainerServiceImpl(UserService userService, TrainerRepository trainerRepository, @Lazy TraineeService traineeService) {
        this.userService = userService;
        this.trainerRepository = trainerRepository;
        this.traineeService = traineeService;
    }

    @Override
    public Optional<TrainerDto> findByUsername(String username) {
        Optional<UserDto> userDto = userService.findByUsername(username);

        if (userDto.isPresent()) {
            var trainer = trainerRepository.findById(userDto.get().getId());

            if (trainer.isPresent()) {
                TrainerEntity trainerEntity = trainer.get();
                return Optional.of(trainerEntity.toDto());
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> getUserProfile(BigInteger id) {
        Optional<TrainerEntity> trainer = trainerRepository.findById(id);

        return trainer.map(TrainerEntity::getUser);
    }

    @Override
    public Set<TrainingEntity> getTrainingsByUsername(String username) {
        var trainer = findByUsername(username);

        if (trainer.isPresent()) {
            trainer.get().getTrainings().size();
            return trainer.get().getTrainings();
        }

        return Set.of();
    }

    @Override
    public boolean assignTrainee(BigInteger trainerId, BigInteger traineeId) {
        var trainer = get(trainerId);
        var trainee = traineeService.get(traineeId);

        if (trainer.isPresent() && trainee.isPresent()) {
            TrainerDto trainerDto = trainer.get();
            trainerDto.getTrainees().add(TraineeEntity.fromDto(trainee.get()));
            return update(trainerDto);
        }

        return false;
    }

    @Override
    public boolean unassignTrainee(BigInteger trainerId, BigInteger traineeId) {
        var trainer = get(trainerId);
        var trainee = traineeService.get(traineeId);

        if (trainer.isPresent() && trainee.isPresent()) {
            TrainerDto trainerDto = trainer.get();
            trainerDto.getTrainees().remove(TraineeEntity.fromDto(trainee.get()));
            return update(trainerDto);
        }

        return false;
    }

    @Override
    public TrainerDto save(TrainerDto trainer) {
        var trainerEntity = trainerRepository.save(TrainerEntity.fromDto(trainer));
        return trainerEntity.toDto();
    }

    @Override
    public boolean update(TrainerDto trainer) {
        Optional<TrainerEntity> trainerEntityOptional = trainerRepository.findById(trainer.getId());

        if (trainerEntityOptional.isPresent()) {
            TrainerEntity trainerEntity = trainerEntityOptional.get();
            trainerEntity.setUser(trainer.getUser());
            trainerEntity.setTrainings(trainer.getTrainings());
            trainerEntity.setSpecialization(trainer.getSpecialization());

            return trainerRepository.update(trainerEntity);
        }

        return false;
    }

    @Override
    public boolean delete(BigInteger id) {
        return trainerRepository.deleteById(id);
    }

    @Override
    public Optional<TrainerDto> get(BigInteger id) {
        var trainer = trainerRepository.findById(id);
        return trainer.map(TrainerEntity::toDto);
    }

    @Override
    public List<TrainerDto> getAll() {
        List<TrainerEntity> trainerEntities = new ArrayList<>();
        trainerRepository.findAll().forEach(trainerEntities::add);

        return trainerEntities.stream().map(TrainerEntity::toDto).toList();
    }

    @Override
    public long count() {
        return trainerRepository.count();
    }
}