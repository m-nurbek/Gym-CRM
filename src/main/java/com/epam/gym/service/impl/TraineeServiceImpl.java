package com.epam.gym.service.impl;

import com.epam.gym.dto.request.TraineeUpdateRequestDto;
import com.epam.gym.dto.response.SimpleTrainerResponseDto;
import com.epam.gym.dto.response.TraineeResponseDto;
import com.epam.gym.dto.response.TraineeUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTraineeDto;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.TraineeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    @Override
    public TraineeResponseDto save(LocalDate dob, String address, BigInteger userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);

        if (user == null || user.getTrainer() != null) {
            return null;
        }

        TraineeEntity trainee = traineeRepository.save(new TraineeEntity(
                null,
                dob,
                address,
                user,
                Set.of(),
                Set.of()
        ));

        return new TraineeResponseDto(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDob(),
                trainee.getAddress(),
                trainee.getUser().getIsActive(),
                trainee.getTrainers().stream().map(t -> new SimpleTrainerResponseDto(
                        t.getUser().getUsername(),
                        t.getUser().getFirstName(),
                        t.getUser().getLastName(),
                        t.getSpecialization().getName().name()
                )).toList()
        );
    }

    @Override
    public Optional<TraineeResponseDto> findByUsername(String username) {
        TraineeEntity trainee = traineeRepository.findByUser_Username(username).orElse(null);

        if (trainee == null) {
            return Optional.empty();
        }

        TraineeResponseDto response = new TraineeResponseDto(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDob(),
                trainee.getAddress(),
                trainee.getUser().getIsActive(),
                trainee.getTrainers().stream().map(x -> new SimpleTrainerResponseDto(
                        x.getUser().getUsername(),
                        x.getUser().getFirstName(),
                        x.getUser().getLastName(),
                        x.getSpecialization().getName().name())
                ).toList()
        );

        return Optional.of(response);
    }

    @Override
    public Optional<TraineeUpdateResponseDto> update(String username, TraineeUpdateRequestDto model) {
        TraineeEntity traineeInDb = traineeRepository.findByUser_Username(username).orElse(null);

        if (traineeInDb == null) {
            return Optional.empty();
        }

        traineeInDb.getUser().setFirstName(model.firstName());
        traineeInDb.getUser().setLastName(model.lastName());
        traineeInDb.getUser().setIsActive(model.isActive());

        userRepository.updateProfileById(
                traineeInDb.getUser().getId(),
                model.firstName(),
                model.lastName(),
                model.isActive()
        );

        traineeRepository.update(
                traineeInDb.getId(),
                model.dob(),
                model.address()
        );

        return Optional.of(
                new TraineeUpdateResponseDto(
                        username,
                        model.firstName(),
                        model.lastName(),
                        model.dob(),
                        model.address(),
                        model.isActive(),
                        traineeInDb.getTrainers().stream().map(
                                x -> new SimpleTrainerResponseDto(
                                        x.getUser().getUsername(),
                                        x.getUser().getFirstName(),
                                        x.getUser().getLastName(),
                                        x.getSpecialization().getName().name()
                                )
                        ).toList()
                )
        );
    }

    @Override
    public boolean deleteByUsername(String username) {
        Optional<TraineeEntity> optionalTrainee = traineeRepository.findByUser_Username(username);
        return optionalTrainee.filter(trainee -> delete(trainee.getId())).isPresent();
    }

    private boolean delete(BigInteger id) {
        if (!traineeRepository.existsById(id)) {
            return false;
        }

        traineeRepository.deleteById(id);
        return true;
    }

    @Override
    public Set<SimpleTrainerResponseDto> getUnassignedTrainersByUsernameToResponse(String username) {
        if (!traineeRepository.existsByUser_Username(username)) {
            return Set.of();
        }

        Set<TrainerEntity> trainerEntitySet = trainerRepository.getUnassignedTrainersByTraineeUsername(username);

        return trainerEntitySet.stream().map(
                x -> new SimpleTrainerResponseDto(
                        x.getUser().getUsername(),
                        x.getUser().getFirstName(),
                        x.getUser().getLastName(),
                        x.getSpecialization().getName().name()
                )
        ).collect(Collectors.toSet());
    }

    @Override
    public Set<TrainingResponseForTraineeDto> getTrainingsByUsernameToResponse(
            String username, LocalDate periodFrom, LocalDate periodTo, String trainerName, String trainingType) {
        Set<TrainingEntity> trainingEntities = trainingRepository.findByTraineeUsername(username);

        return trainingEntities.stream()
                .filter(t -> (periodFrom == null || !t.getDate().isBefore(periodFrom)) &&
                        (periodTo == null || !t.getDate().isAfter(periodTo)) &&
                        (trainerName == null || t.getTrainer().getUser().getUsername().equals(trainerName)) &&
                        (trainingType == null || t.getType().getName().name().equalsIgnoreCase(trainingType)))
                .map(t -> {
                    UserEntity trainer = t.getTrainer().getUser();
                    String trainerFullName = "%s %s".formatted(trainer.getFirstName(), trainer.getLastName());

                    return new TrainingResponseForTraineeDto(
                            t.getName(),
                            t.getDate(),
                            t.getType().getName().name(),
                            t.getDuration(),
                            trainerFullName
                    );
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Set<SimpleTrainerResponseDto> updateTrainerListByUsername(String username, List<String> trainerUsernames) {
        TraineeEntity trainee = traineeRepository.findByUser_Username(username).orElse(null);

        if (trainee == null) {
            return Set.of();
        }

        Set<TrainerEntity> trainersFromUsernames = trainerUsernames.stream()
                .map(trainerRepository::findByUser_Username)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        trainee.setTrainers(trainersFromUsernames);
        traineeRepository.save(trainee);

        return trainersFromUsernames.stream().map(
                t -> new SimpleTrainerResponseDto(
                        t.getUser().getUsername(),
                        t.getUser().getFirstName(),
                        t.getUser().getLastName(),
                        t.getSpecialization().getName().name()
                )
        ).collect(Collectors.toSet());
    }
}