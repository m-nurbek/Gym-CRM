package com.epam.gym.service.serviceImpl;

import com.epam.gym.controller.exception.ConflictException;
import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.dto.model.request.TrainerUpdateRequestModel;
import com.epam.gym.dto.model.response.SimpleTraineeResponseModel;
import com.epam.gym.dto.model.response.TrainerResponseModel;
import com.epam.gym.dto.model.response.TrainerUpdateResponseModel;
import com.epam.gym.dto.model.response.TrainingResponseForTrainerModel;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.TrainingTypeEntity;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserService userService;
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
    public Optional<TrainerDto> findById(BigInteger id) {
        return trainerRepository.findById(id).map(TrainerEntity::toDto);
    }

    @Override
    public Optional<TrainerDto> findByUsername(String username) {
        Optional<UserDto> userDto = userService.findByUsername(username);

        if (userDto.isPresent()) {
            Optional<TrainerEntity> trainer = trainerRepository.findById(userDto.get().getTrainer().getId());

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

        TrainerDto trainer = trainerDto.get();
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
    public Set<TrainingEntity> getTrainingsByUsername(String username) {
        Optional<TrainerDto> trainer = findByUsername(username);

        if (trainer.isPresent()) {
            trainer.get().getTrainings().size(); // TODO: review the need for this here
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
    public TrainerDto save(TrainerDto trainer) {
        if (trainer.getUser() != null && !trainer.getUser().isValid()) {
            throw new ConflictException("Invalid user");
        }

        return trainerRepository.save(TrainerEntity.fromDto(trainer)).toDto();
    }

    @Override
    public boolean update(TrainerDto trainer) {
        if (trainer.getUser() != null && !trainer.getUser().isValid()) {
            throw new ConflictException("Invalid user");
        }

        if (!trainerRepository.existsById(trainer.getId())) {
            return false;
        }

        // TODO: review the need for userService here
        userService.updateProfile(UserDto.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .build());
        trainerRepository.update(trainer.getId(), trainer.getUser(), trainer.getSpecialization());
        return true;
    }
}