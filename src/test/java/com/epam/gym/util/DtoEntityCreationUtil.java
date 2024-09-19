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
import java.util.Set;

@UtilityClass
public class DtoEntityCreationUtil {
    public static UserDto getNewUserDtoInstance(long index) {
        return new UserDto(
                BigInteger.valueOf(index),
                "Name%d".formatted(index), "Surname%d".formatted(index),
                "username%d".formatted(index), "password%d".formatted(index),
                false, null, null);
    }

    public static UserEntity getNewUserEntityInstance(long index) {
        return new UserEntity(
                BigInteger.valueOf(index),
                "Name%d".formatted(index), "Surname%d".formatted(index),
                "username%d".formatted(index), "password%d".formatted(index),
                false, null, null);
    }

    public static TraineeDto getNewTraineeDtoInstance(long index, UserEntity user, Set<TrainingEntity> trainings, Set<TrainerEntity> trainers) {
        return new TraineeDto(
                BigInteger.valueOf(index),
                LocalDate.of(2003, 1, (int) index % 30),
                "Some Address%d".formatted(index),
                user,
                trainings,
                trainers
        );
    }

    public static TraineeEntity getNewTraineeEntityInstance(long index, UserEntity user, Set<TrainingEntity> trainings, Set<TrainerEntity> trainers) {
        return new TraineeEntity(
                BigInteger.valueOf(index),
                LocalDate.of(2003, 1, (int) index % 30),
                "Some Address%d".formatted(index),
                user,
                trainings,
                trainers
        );
    }

    public static TrainerDto getNewTrainerDtoInstance(long index, TrainingTypeEntity type, UserEntity user, Set<TrainingEntity> trainings, Set<TraineeEntity> trainees) {
        return new TrainerDto(
                BigInteger.valueOf(index),
                type,
                user,
                trainings,
                trainees
        );
    }

    public static TrainerEntity getNewTrainerEntityInstance(long index, TrainingTypeEntity type, UserEntity user, Set<TrainingEntity> trainings, Set<TraineeEntity> trainees) {
        return new TrainerEntity(
                BigInteger.valueOf(index),
                type,
                user,
                trainings,
                trainees
        );
    }

    public static TrainingTypeDto getNewTrainingTypeDtoInstance(long index) {
        return new TrainingTypeDto(
                BigInteger.valueOf(index),
                TrainingTypeEnum.BODYBUILDING,
                Set.of(),
                Set.of()
        );
    }

    public static TrainingTypeEntity getNewTrainingTypeEntityInstance(long index) {
        return new TrainingTypeEntity(
                BigInteger.valueOf(index),
                TrainingTypeEnum.BODYBUILDING,
                Set.of(),
                Set.of()
        );
    }

    public static TrainingDto getNewTrainingDtoInstance(long index, TraineeEntity trainee, TrainerEntity trainer, TrainingTypeEntity type) {
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

    public static TrainingEntity getNewTrainingEntityInstance(long index, TraineeEntity trainee, TrainerEntity trainer, TrainingTypeEntity type) {
        return new TrainingEntity(
                BigInteger.valueOf(index),
                "SOME TRAINING%d".formatted(index),
                LocalDate.of(2024, 10, (int) index % 30),
                3,
                trainee,
                trainer,
                type
        );
    }
}