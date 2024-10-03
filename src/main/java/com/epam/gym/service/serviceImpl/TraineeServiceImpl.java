package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.dto.model.request.TraineeUpdateRequestModel;
import com.epam.gym.dto.model.response.SimpleTrainerResponseModel;
import com.epam.gym.dto.model.response.TraineeResponseModel;
import com.epam.gym.dto.model.response.TraineeUpdateResponseModel;
import com.epam.gym.dto.model.response.TrainingResponseForTraineeModel;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    private final UserService userService;
    private final TraineeRepository traineeRepository;
    private final TrainerService trainerService;

    @Autowired
    public TraineeServiceImpl(UserService userService, TraineeRepository traineeRepository, @Lazy TrainerService trainerService) {
        this.userService = userService;
        this.traineeRepository = traineeRepository;
        this.trainerService = trainerService;
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
    public Optional<TraineeResponseModel> findByUsernameToResponse(String username) {
        var trainee = findByUsername(username);

        if (trainee.isEmpty()) {
            return Optional.empty();
        }

        var t = trainee.get();
        TraineeResponseModel response = new TraineeResponseModel(
                t.getUser().getFirstName(),
                t.getUser().getLastName(),
                t.getDob(),
                t.getAddress(),
                t.getUser().getIsActive(),
                t.getTrainers().stream().map(x -> new SimpleTrainerResponseModel(
                        x.getUser().getUsername(),
                        x.getUser().getFirstName(),
                        x.getUser().getLastName(),
                        x.getSpecialization().getName().name())
                ).toList()
        );

        return Optional.of(response);
    }

    @Override
    public Optional<TraineeUpdateResponseModel> update(String username, TraineeUpdateRequestModel model) {
        Optional<TraineeDto> traineeInDb = findByUsername(username);

        if (traineeInDb.isEmpty()) {
            return Optional.empty();
        }

        var traineeDto = traineeInDb.get();
        traineeDto.getUser().setFirstName(model.firstName());
        traineeDto.getUser().setLastName(model.lastName());
        traineeDto.setDob(model.dob());

        if (model.address() != null) {
            traineeDto.setAddress(model.address());
        }

        traineeDto.getUser().setIsActive(model.isActive());

        boolean success = update(traineeDto);

        if (!success) {
            return Optional.empty();
        }

        return Optional.of(
                new TraineeUpdateResponseModel(
                        traineeDto.getUser().getUsername(),
                        traineeDto.getUser().getFirstName(),
                        traineeDto.getUser().getLastName(),
                        traineeDto.getDob(),
                        traineeDto.getAddress(),
                        traineeDto.getUser().getIsActive(),
                        traineeDto.getTrainers().stream().map(
                                x -> new SimpleTrainerResponseModel(
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
        Optional<TraineeDto> trainee = findByUsername(username);

        if (trainee.isEmpty()) {
            return false;
        }

        return delete(trainee.get().getId());
    }

    @Override
    public Set<TrainerEntity> getTrainers(BigInteger id) {
        Optional<TraineeEntity> traineeEntity = traineeRepository.findById(id);

        if (traineeEntity.isPresent()) {
            traineeEntity.get().getTrainers().size();
            return traineeEntity.get().getTrainers();
        }

        return Set.of();
    }

    @Override
    public Set<TrainerEntity> getTrainers(String username) {
        Optional<TraineeDto> trainee = findByUsername(username);

        if (trainee.isPresent()) {
            trainee.get().getTrainers().size();
            return trainee.get().getTrainers();
        }

        return Set.of();
    }

    @Override
    public Set<TrainerEntity> getUnassignedTrainersByUsername(String username) {
        return traineeRepository.getUnassignedTrainersByUsername(username);
    }

    @Override
    public Set<SimpleTrainerResponseModel> getUnassignedTrainersByUsernameToResponse(String username) {
        Set<TrainerEntity> trainerEntitySet = getUnassignedTrainersByUsername(username);

        return trainerEntitySet.stream().map(
                x -> new SimpleTrainerResponseModel(
                        x.getUser().getUsername(),
                        x.getUser().getFirstName(),
                        x.getUser().getLastName(),
                        x.getSpecialization().getName().name()
                )
        ).collect(Collectors.toSet());
    }

    @Override
    public Set<TrainingEntity> getTrainingsByUsername(String username) {
        var trainee = findByUsername(username);

        if (trainee.isPresent()) {
            trainee.get().getTrainings().size();
            return trainee.get().getTrainings();
        }

        return Set.of();
    }

    @Override
    public Set<TrainingResponseForTraineeModel> getTrainingsByUsernameToResponse(String username, LocalDate periodFrom, LocalDate periodTo, String trainerName, String trainingType) {
        Set<TrainingEntity> trainingEntities = getTrainingsByUsername(username);

        return trainingEntities.stream()
                .filter(t -> (periodFrom == null || !t.getDate().isBefore(periodFrom)) &&
                        (periodTo == null || !t.getDate().isAfter(periodTo)) &&
                        (trainerName == null || t.getTrainer().getUser().getUsername().equalsIgnoreCase(trainerName)) &&
                        (trainingType == null || t.getType().getName().name().equalsIgnoreCase(trainingType)))
                .map(t -> {
                    var trainer = t.getTrainer().getUser();

                    return new TrainingResponseForTraineeModel(
                            t.getName(),
                            t.getDate(),
                            t.getType().getName().name(),
                            t.getDuration(),
                            "%s %s".formatted(trainer.getFirstName(), trainer.getLastName())
                    );
                })
                .collect(Collectors.toSet());
    }

    @Override
    public boolean assignTrainer(BigInteger traineeId, BigInteger trainerId) {
        Optional<TraineeDto> trainee = get(traineeId);
        Optional<TrainerDto> trainer = trainerService.get(trainerId);

        if (trainee.isPresent() && trainer.isPresent()) {
            trainee.get().getTrainers().add(TrainerEntity.fromDto(trainer.get()));
            return update(trainee.get());
        }

        return false;
    }

    @Override
    public boolean unassignTrainer(BigInteger traineeId, BigInteger trainerId) {
        var trainee = get(traineeId);
        var trainer = trainerService.get(trainerId);

        if (trainee.isPresent() && trainer.isPresent()) {
            trainee.get().getTrainers().remove(TrainerEntity.fromDto(trainer.get()));
            return update(trainee.get());
        }

        return false;
    }

    @Override
    public Set<SimpleTrainerResponseModel> updateTrainerListByUsername(String username, List<String> trainerUsernames) {
        Optional<TraineeDto> trainee = findByUsername(username);

        if (trainee.isEmpty()) {
            return Set.of();
        }

        TraineeDto t = trainee.get();
        var trainersFromUsernames = trainerUsernames.stream()
                .map(trainerService::findByUsername)
                .filter(Optional::isPresent)
                .map(optionalTrainer -> TrainerEntity.fromDto(optionalTrainer.get()))
                .collect(Collectors.toSet());

        t.setTrainers(trainersFromUsernames);

        return update(t) ?
                trainersFromUsernames.stream().map(
                        trainer -> new SimpleTrainerResponseModel(
                                trainer.getUser().getUsername(),
                                trainer.getUser().getFirstName(),
                                trainer.getUser().getLastName(),
                                trainer.getSpecialization().getName().name()
                        )
                ).collect(Collectors.toSet())
                : Set.of();
    }

    @Override
    public TraineeDto save(TraineeDto trainee) {
        if (trainee.getUser() != null && !trainee.getUser().isValid()) {
            throw new IllegalArgumentException("Invalid user");
        }

        var traineeEntity = traineeRepository.save(TraineeEntity.fromDto(trainee));
        return traineeEntity.toDto();
    }

    @Override
    public boolean update(TraineeDto trainee) {
        if (trainee.getUser() != null && !trainee.getUser().isValid()) {
            throw new IllegalArgumentException("Invalid user");
        }

        Optional<TraineeEntity> traineeEntityOptional = traineeRepository.findById(trainee.getId());

        if (traineeEntityOptional.isPresent()) {
            TraineeEntity traineeEntity = traineeEntityOptional.get();
            traineeEntity.setAddress(trainee.getAddress());
            traineeEntity.setDob(trainee.getDob());
            traineeEntity.setUser(trainee.getUser());
            traineeEntity.setTrainers(trainee.getTrainers());
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