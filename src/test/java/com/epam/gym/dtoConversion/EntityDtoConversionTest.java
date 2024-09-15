package com.epam.gym.dtoConversion;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.entity.UserEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.epam.gym.util.DtoEntityCreationUtil.getNewTraineeDtoInstance;
import static com.epam.gym.util.DtoEntityCreationUtil.getNewTraineeEntityInstance;
import static com.epam.gym.util.DtoEntityCreationUtil.getNewUserDtoInstance;
import static com.epam.gym.util.DtoEntityCreationUtil.getNewUserEntityInstance;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EntityDtoConversionTest {
    @ParameterizedTest
    @MethodSource("userArgumentsProvider")
    void shouldConvertUser(UserDto userDto, UserEntity userEntity) {
        // given
        // when
        var resultToDto = userEntity.toDto();
        var resultFromDto = UserEntity.fromDto(userDto);

        // then
        assertAll(
                "Conversion from entity to dto",
                () -> assertNotNull(resultToDto, "Dto must not be null"),
                () -> assertEquals(userEntity.getId(), resultToDto.getId()),
                () -> assertEquals(userEntity.getFirstName(), resultToDto.getFirstName()),
                () -> assertEquals(userEntity.getLastName(), resultToDto.getLastName()),
                () -> assertEquals(userEntity.getUsername(), resultToDto.getUsername()),
                () -> assertEquals(userEntity.getPassword(), resultToDto.getPassword())
        );

        assertAll(
                "Conversion from dto to entity",
                () -> assertNotNull(resultFromDto, "Entity must not be null"),
                () -> assertEquals(userDto.getId(), resultFromDto.getId()),
                () -> assertEquals(userDto.getFirstName(), resultFromDto.getFirstName()),
                () -> assertEquals(userDto.getLastName(), resultFromDto.getLastName()),
                () -> assertEquals(userDto.getUsername(), resultFromDto.getUsername()),
                () -> assertEquals(userDto.getPassword(), resultFromDto.getPassword())
        );
    }

    @ParameterizedTest
    @MethodSource("traineeArgumentsProvider")
    void shouldConvertTrainee(TraineeDto traineeDto, TraineeEntity traineeEntity) {
        // when
        var resultToDto = traineeEntity.toDto();
        var resultFromDto = TraineeEntity.fromDto(traineeDto);

        // then
        assertAll(
                "Conversion from entity to dto",
                () -> assertNotNull(resultToDto, "Dto must not be null"),
                () -> assertEquals(traineeEntity.getId(), resultToDto.getId()),
                () -> assertEquals(traineeEntity.getDob(), resultToDto.getDob()),
                () -> assertEquals(traineeEntity.getTrainers().stream().map(TrainerEntity::toDto).toList(), resultToDto.getTrainers()),
                () -> assertEquals(traineeEntity.getTrainings().stream().map(TrainingEntity::toDto).toList(), resultToDto.getTrainings())
        );

        assertAll(
                "Conversion from dto to entity",
                () -> assertNotNull(resultFromDto, "Entity must not be null"),
                () -> assertEquals(traineeDto.getId(), resultFromDto.getId()),
                () -> assertEquals(traineeDto.getDob(), resultFromDto.getDob()),
                () -> assertEquals(traineeDto.getTrainers().stream().map(TrainerEntity::fromDto).toList(), resultFromDto.getTrainers()),
                () -> assertEquals(traineeDto.getTrainings().stream().map(TrainingEntity::fromDto).toList(), resultFromDto.getTrainings())
        );
    }

    @ParameterizedTest
    @MethodSource("traineeArgumentsProviderEdgeCase1")
    void shouldThrowExceptionWhileConvertingTrainee(TraineeDto traineeDto, TraineeEntity traineeEntity) {
        assertAll(
                "Should throw and error because user arg in trainee object is null",
                () -> assertThrows(IllegalArgumentException.class, traineeEntity::toDto),
                () -> assertThrows(IllegalArgumentException.class, () -> TraineeEntity.fromDto(traineeDto))
        );
    }

    @ParameterizedTest
    @MethodSource("traineeArgumentsProviderEdgeCase2")
    void shouldHandleNullCaseWhileConvertingTrainee(TraineeDto traineeDto, TraineeEntity traineeEntity) {
        assertAll(
                "Should not throw error on any other null fields",
                () -> assertDoesNotThrow(() -> traineeEntity.toDto()),
                () -> assertDoesNotThrow(() -> TraineeEntity.fromDto(traineeDto))
        );
    }

    private static Stream<Arguments> userArgumentsProvider() {
        return Stream.of(generateUserEntityAndDtoArgs(5));
    }

    private static Stream<Arguments> traineeArgumentsProvider() {
        return Stream.of(generateTraineeEntityAndDtoArgs(5));
    }

    private static Stream<Arguments> traineeArgumentsProviderEdgeCase1() {
        return Stream.of(generateEdgeCaseTraineeArgs1());
    }

    private static Stream<Arguments> traineeArgumentsProviderEdgeCase2() {
        return Stream.of(generateEdgeCaseTraineeArgs2());
    }

    /**
     * Method to generate arguments for the parametrized test
     *
     * @param number number of arguments to generate
     * @return array of Arguments with {@link UserDto} and {@link UserEntity}
     */
    private static Arguments[] generateUserEntityAndDtoArgs(int number) {
        List<Arguments> args = new ArrayList<>();

        for (int i = 1; i <= number; i++) {
            args.add(Arguments.of(
                    getNewUserDtoInstance(i),
                    getNewUserEntityInstance(i)
            ));
        }

        return args.toArray(new Arguments[0]);
    }

    /**
     * Method to generate arguments for the parametrized test
     *
     * @param number number of arguments to generate
     * @return array of Arguments with {@link TraineeDto} and {@link TraineeEntity}
     */
    private static Arguments[] generateTraineeEntityAndDtoArgs(int number) {
        List<Arguments> args = new ArrayList<>();

        for (int i = 1; i <= number; i++) {
            var userDto = getNewUserDtoInstance(i);
            var userEntity = getNewUserEntityInstance(i);

            args.add(Arguments.of(
                    getNewTraineeDtoInstance(i, userDto, List.of(), List.of()),
                    getNewTraineeEntityInstance(i, i, userEntity, List.of(), List.of())
            ));
        }

        return args.toArray(new Arguments[0]);
    }

    /**
     * Generate trainee where user argument is null
     *
     * @return array of Arguments with {@link TraineeDto} and {@link TraineeEntity}
     */
    private static Arguments[] generateEdgeCaseTraineeArgs1() {
        List<Arguments> args = new ArrayList<>();

        // null for user
        args.add(Arguments.of(
                getNewTraineeDtoInstance(1, null, List.of(), List.of()),
                getNewTraineeEntityInstance(1, 0, null, List.of(), List.of())
        ));

        return args.toArray(new Arguments[0]);
    }

    /**
     * Generate trainee where trainee list and trainer list are null
     *
     * @return array of Arguments with {@link TraineeDto} and {@link TraineeEntity}
     */
    private static Arguments[] generateEdgeCaseTraineeArgs2() {
        List<Arguments> args = new ArrayList<>();

        var userDto = getNewUserDtoInstance(1);
        var userEntity = getNewUserEntityInstance(1);

        // null for trainee list and trainer list
        args.add(Arguments.of(
                getNewTraineeDtoInstance(1, userDto, null, null),
                getNewTraineeEntityInstance(1, 1, userEntity, null, null)
        ));

        args.add(Arguments.of(
                getNewTraineeDtoInstance(1, userDto, List.of(), null),
                getNewTraineeEntityInstance(1, 1, userEntity, List.of(), null)
        ));

        args.add(Arguments.of(
                getNewTraineeDtoInstance(1, userDto, null, List.of()),
                getNewTraineeEntityInstance(1, 1, userEntity, null, List.of())
        ));

        return args.toArray(new Arguments[0]);
    }
}