package com.epam.gym.util;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.TrainingDto;
import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.entity.UserEntity;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class DtoEntityCreationUtil {
    public static UserDto getNewUserDtoInstance(long index) {
        return new UserDto(
                BigInteger.valueOf(index),
                "Name%d".formatted(index), "Surname%d".formatted(index),
                "username%d".formatted(index), "password%d".formatted(index),
                false);
    }

    public static UserEntity getNewUserEntityInstance(long index) {
        return new UserEntity(
                BigInteger.valueOf(index),
                "Name%d".formatted(index), "Surname%d".formatted(index),
                "username%d".formatted(index), "password%d".formatted(index),
                false);
    }

    public static TraineeDto getNewTraineeDtoInstance(long index, UserDto userDto, List<TrainingDto> trainingDtos, List<TrainerDto> trainerDtos) {
        return new TraineeDto(
                BigInteger.valueOf(index),
                LocalDate.of(2003, 1, (int) index % 30),
                "Some Address%d".formatted(index),
                userDto,
                trainingDtos,
                trainerDtos
        );
    }

    public static TraineeEntity getNewTraineeEntityInstance(long index, long userId, UserEntity userEntity, List<TrainingEntity> trainingEntities, List<TrainerEntity> trainerEntities) {
        return new TraineeEntity(
                BigInteger.valueOf(index),
                LocalDate.of(2003, 1, (int) index % 30),
                "Some Address%d".formatted(index),
                BigInteger.valueOf(userId),
                userEntity,
                trainingEntities,
                trainerEntities
        );
    }

    public static TrainerDto getNewTrainerDtoInstance(long index, TrainingTypeDto type, UserDto user, List<TrainingDto> trainingDtos, List<TraineeDto> traineeDtos) {
        return new TrainerDto(
                BigInteger.valueOf(index),
                type,
                user,
                trainingDtos,
                traineeDtos
        );
    }

    public static TrainerEntity getNewTrainerEntityInstance(long index, long specializationId, long userId, TrainingTypeEntity type, UserEntity user, List<TrainingEntity> trainingEntities, List<TraineeEntity> traineeEntities) {
        return new TrainerEntity(
                BigInteger.valueOf(index),
                BigInteger.valueOf(specializationId),
                BigInteger.valueOf(userId),
                type,
                user,
                trainingEntities,
                traineeEntities
        );
    }

    public static TrainingTypeDto getNewTrainingTypeDtoInstance(long index, List<TrainerDto> trainers, List<TrainingDto> trainings) {
        return new TrainingTypeDto(
                BigInteger.valueOf(index),
                TrainingTypeEnum.BODYBUILDING,
                trainers,
                trainings
        );
    }

    public static TrainingTypeEntity getNewTrainingTypeEntityInstance(long index, List<TrainerEntity> trainerEntities, List<TrainingEntity> trainingEntities) {
        return new TrainingTypeEntity(
                BigInteger.valueOf(index),
                TrainingTypeEnum.BODYBUILDING,
                trainerEntities,
                trainingEntities
        );
    }

    public static TrainingDto getNewTrainingDtoInstance(long index, TraineeDto trainee, TrainerDto trainer, TrainingTypeDto type) {
        return new TrainingDto(
                BigInteger.valueOf(index),
                trainee,
                trainer,
                "SOME TRAINING%d".formatted(index),
                type,
                LocalDate.of(2024, 10, (int) index % 30),
                3
        );
    }

    public static TrainingEntity getNewTrainingEntityInstance(long index, long traineeId, long trainerId, long typeId, TraineeEntity trainee, TrainerEntity trainer, TrainingTypeEntity type) {
        return new TrainingEntity(
                BigInteger.valueOf(index),
                BigInteger.valueOf(traineeId),
                BigInteger.valueOf(trainerId),
                "SOME TRAINING%d".formatted(index),
                BigInteger.valueOf(typeId),
                LocalDate.of(2024, 10, (int) index % 30),
                3,
                trainee,
                trainer,
                type
        );
    }
}