package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.dto.model.request.TrainerUpdateRequestModel;
import com.epam.gym.dto.model.response.SimpleTraineeResponseModel;
import com.epam.gym.dto.model.response.TrainerResponseModel;
import com.epam.gym.dto.model.response.TrainerUpdateResponseModel;
import com.epam.gym.dto.model.response.TrainingResponseForTrainerModel;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {
    private final UserService userService;
    private final TrainerRepository trainerRepository;
    private final TraineeService traineeService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainerServiceImpl(
            UserService userService,
            TrainerRepository trainerRepository,
            @Lazy TraineeService traineeService,
            TrainingTypeService trainingTypeService) {
        this.userService = userService;
        this.trainerRepository = trainerRepository;
        this.traineeService = traineeService;
        this.trainingTypeService = trainingTypeService;
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
    public Optional<TrainerResponseModel> findByUsernameToResponse(String username) {
        Optional<TrainerDto> trainerDto = findByUsername(username);

        if (trainerDto.isEmpty()) {
            return Optional.empty();
        }

        return trainerDto.map(t -> new TrainerResponseModel(
                t.getUser().getFirstName(),
                t.getUser().getLastName(),
                t.getSpecialization().getName().name(),
                t.getUser().getIsActive(),
                t.getTrainees().stream().map(
                        trainee -> new SimpleTraineeResponseModel(
                                trainee.getUser().getUsername(),
                                trainee.getUser().getFirstName(),
                                trainee.getUser().getLastName())
                ).toList()));
    }

    @Override
    public Optional<TrainerUpdateResponseModel> update(String username, TrainerUpdateRequestModel model) {
        Optional<TrainerDto> trainerDto = findByUsername(username);

        if (trainerDto.isEmpty()) {
            return Optional.empty();
        }

        var trainer = trainerDto.get();
        trainer.getUser().setFirstName(model.firstName());
        trainer.getUser().setLastName(model.lastName());

        if (model.specialization() != null) {
            Optional<TrainingTypeEntity> typeEntity = trainingTypeService.getTrainingTypeName(model.specialization()).map(TrainingTypeEntity::fromDto);
            typeEntity.ifPresent(trainer::setSpecialization);
        }

        trainer.getUser().setIsActive(model.isActive());

        boolean success = update(trainer);

        if (!success) {
            return Optional.empty();
        }

        return Optional.of(
                new TrainerUpdateResponseModel(
                        trainer.getUser().getUsername(),
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        trainer.getSpecialization().getName().name(),
                        trainer.getUser().getIsActive(),
                        trainer.getTrainees().stream()
                                .map(t -> new SimpleTraineeResponseModel(
                                        t.getUser().getUsername(),
                                        t.getUser().getFirstName(),
                                        t.getUser().getLastName()
                                ))
                                .toList()
                )
        );
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
    public Set<TrainingResponseForTrainerModel> getTrainingsByUsernameToResponse(String username, LocalDate periodFrom, LocalDate periodTo, String traineeName) {
        Set<TrainingEntity> trainingEntities = getTrainingsByUsername(username);

        return trainingEntities.stream()
                .filter(t -> (periodFrom == null || !t.getDate().isBefore(periodFrom)) &&
                        (periodTo == null || !t.getDate().isAfter(periodTo)) &&
                        (traineeName == null || t.getTrainee().getUser().getUsername().equalsIgnoreCase(traineeName)))
                .map(t -> {
                    var trainee = t.getTrainee().getUser();

                    return new TrainingResponseForTrainerModel(
                            t.getName(),
                            t.getDate(),
                            t.getType().getName().name(),
                            t.getDuration(),
                            "%s %s".formatted(trainee.getFirstName(), trainee.getLastName())
                    );
                })
                .collect(Collectors.toSet());
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
        if (trainer.getUser() != null && !trainer.getUser().isValid()) {
            throw new IllegalArgumentException("Invalid user");
        }

        var trainerEntity = trainerRepository.save(TrainerEntity.fromDto(trainer));
        return trainerEntity.toDto();
    }

    @Override
    public boolean update(TrainerDto trainer) {
        if (trainer.getUser() != null && !trainer.getUser().isValid()) {
            throw new IllegalArgumentException("Invalid user");
        }

        Optional<TrainerEntity> trainerEntityOptional = trainerRepository.findById(trainer.getId());

        if (trainerEntityOptional.isPresent()) {
            TrainerEntity trainerEntity = trainerEntityOptional.get();
            trainerEntity.setUser(trainer.getUser());
            trainerEntity.setTrainings(trainer.getTrainings());
            trainerEntity.setTrainees(trainer.getTrainees());
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