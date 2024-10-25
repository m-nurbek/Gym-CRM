package com.epam.gym.service.impl;

import com.epam.gym.controller.exception.ConflictException;
import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.request.TrainerUpdateRequestDto;
import com.epam.gym.dto.response.SimpleTraineeResponseDto;
import com.epam.gym.dto.response.TrainerResponseDto;
import com.epam.gym.dto.response.TrainerUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTrainerDto;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.repository.TrainingTypeRepository;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.TrainerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public TrainerResponseDto save(TrainingTypeEnum specialization, BigInteger userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        validateUserRoleAssignment(user);

        TrainerEntity trainerEntity = trainerRepository.save(new TrainerEntity(
                null,
                trainingTypeRepository.findByName(specialization).orElse(null),
                user,
                Set.of(),
                Set.of()
        ));

        return new TrainerResponseDto(
                trainerEntity.getUser().getFirstName(),
                trainerEntity.getUser().getLastName(),
                trainerEntity.getSpecialization().getName().name(),
                trainerEntity.getUser().getIsActive(),
                trainerEntity.getTrainees().stream().map(x -> new SimpleTraineeResponseDto(
                        x.getUser().getUsername(),
                        x.getUser().getFirstName(),
                        x.getUser().getLastName()
                )).toList()
        );
    }

    private void validateUserRoleAssignment(UserEntity user) {
        if (user == null) {
            throw new ConflictException("User cannot be null");
        } else if (user.getTrainee() != null || user.getTrainer() != null) {
            throw new ConflictException("User is already assigned to a specific trainee or trainer entity");
        }
    }

    @Override
    public TrainerResponseDto findByUsername(String username) {
        TrainerEntity trainer = trainerRepository.findByUser_Username(username).orElse(null);

        if (trainer == null) {
            throw new NotFoundException("Trainer with this username doesn't exist");
        }

        return new TrainerResponseDto(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getSpecialization().getName().name(),
                trainer.getUser().getIsActive(),
                trainer.getTrainees().stream().map(
                        trainee -> new SimpleTraineeResponseDto(
                                trainee.getUser().getUsername(),
                                trainee.getUser().getFirstName(),
                                trainee.getUser().getLastName())
                ).toList());
    }

    @Override
    public TrainerUpdateResponseDto update(String username, TrainerUpdateRequestDto model) {
        TrainerEntity trainer = trainerRepository.findByUser_Username(username).orElse(null);

        if (trainer == null) {
            throw new NotFoundException("Trainer with this username doesn't exist");
        }

        userRepository.updateProfileById(
                trainer.getUser().getId(),
                model.firstName(),
                model.lastName(),
                model.isActive()
        );

        if (model.specialization() != null) {
            Optional<TrainingTypeEntity> type = trainingTypeRepository.findByName(model.specialization());

            type.ifPresent(trainingTypeEntity -> trainerRepository.updateSpecialization(
                    trainer.getId(),
                    trainingTypeEntity
            ));
        }

        return new TrainerUpdateResponseDto(
                username,
                model.firstName(),
                model.lastName(),
                model.specialization() != null ? model.specialization().name() : null,
                model.isActive(),
                trainer.getTrainees().stream()
                        .map(x -> new SimpleTraineeResponseDto(
                                x.getUser().getUsername(),
                                x.getUser().getFirstName(),
                                x.getUser().getLastName()
                        ))
                        .toList()
        );
    }

    @Override
    public Set<TrainingResponseForTrainerDto> getTrainingsByUsernameToResponse(
            String username, LocalDate periodFrom, LocalDate periodTo, String traineeName) {
        if (!trainerRepository.existsByUser_Username(username)) {
            throw new NotFoundException("Trainer with this username doesn't exist");
        }

        Set<TrainingEntity> trainingEntities = trainingRepository.findByTrainerUsername(username);

        return trainingEntities.stream()
                .filter(t -> (periodFrom == null || !t.getDate().isBefore(periodFrom)) &&
                        (periodTo == null || !t.getDate().isAfter(periodTo)) &&
                        (traineeName == null || t.getTrainee().getUser().getUsername().equals(traineeName)))
                .map(t -> {
                    var trainee = t.getTrainee().getUser();
                    String traineeFullName = "%s %s".formatted(trainee.getFirstName(), trainee.getLastName());

                    return new TrainingResponseForTrainerDto(
                            t.getName(),
                            t.getDate(),
                            t.getType().getName().name(),
                            t.getDuration(),
                            traineeFullName
                    );
                })
                .collect(Collectors.toSet());
    }
}