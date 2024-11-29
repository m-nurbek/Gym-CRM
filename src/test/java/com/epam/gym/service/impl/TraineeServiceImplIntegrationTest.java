package com.epam.gym.service.impl;

import com.epam.gym.Application;
import com.epam.gym.controller.exception.ConflictException;
import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.UserDto;
import com.epam.gym.dto.request.TraineeUpdateRequestDto;
import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.dto.response.SimpleTrainerResponseDto;
import com.epam.gym.dto.response.TraineeResponseDto;
import com.epam.gym.dto.response.TraineeUpdateResponseDto;
import com.epam.gym.dto.response.TrainingResponseForTraineeDto;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.TrainingService;
import com.epam.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class TraineeServiceImplIntegrationTest {
    @Autowired
    private TraineeServiceImpl traineeService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSave() {
        // given
        LocalDate dob = LocalDate.of(2000, 1, 1);
        String address = "ADDRESS";
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        boolean isActive = false;
        String password = "password";

        // when
        UserDto user = userService.save(firstName, lastName, password, isActive);
        TraineeResponseDto trainee = traineeService.save(dob, address, user.id());
        UserEntity userEntity = userRepository.findById(user.id()).orElse(null);

        // then
        assertAll(
                "Assertions for 'save()' method",
                () -> assertThat(trainee).isNotNull(),
                () -> assertThat(trainee.firstName()).isEqualTo(firstName),
                () -> assertThat(trainee.lastName()).isEqualTo(lastName),
                () -> assertThat(trainee.isActive()).isEqualTo(isActive),
                () -> assertThat(trainee.dob()).isEqualTo(dob),
                () -> assertThat(trainee.address()).isEqualTo(address),

                () -> assertThat(userEntity).isNotNull(),
                () -> assertThat(userEntity.getFirstName()).isEqualTo(firstName),
                () -> assertThat(userEntity.getLastName()).isEqualTo(lastName),
                () -> assertThat(userEntity.getTrainee()).isNotNull(),
                () -> assertThat(userEntity.getTrainer()).isNull()
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 25, 26, 50, 51})
    void shouldNotSave(Long id) {
        // given
        LocalDate dob = LocalDate.of(2000, 1, 1);
        String address = "ADDRESS";
        BigInteger userId = BigInteger.valueOf(id);

        // when & then
        assertAll(
                "Assertions for 'save()' method",
                () -> assertThrows(ConflictException.class, () -> traineeService.save(dob, address, userId))
        );
    }

    @Test
    void shouldFindByUsername() {
        // given
        String username = "johndoe1";

        // when
        TraineeResponseDto trainee = traineeService.findByUsername(username);

        // then
        assertAll(
                "Assertions for 'findByUsername()' method",
                () -> assertThat(trainee).isNotNull(),
                () -> assertThat(trainee.firstName()).isEqualTo("John"),
                () -> assertThat(trainee.lastName()).isEqualTo("Doe")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"non-existent", "vincestewart50"})
    void shouldNotFindByUsername(String username) {
        // when & then
        assertAll(
                "Assertions for 'findByUsername()' method",
                () -> assertThrows(NotFoundException.class, () -> traineeService.findByUsername(username))
        );
    }

    @Test
    void shouldUpdate() {
        // given
        String username = "johndoe1";
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        LocalDate dob = LocalDate.of(2000, 1, 1);
        String address = "ADDRESS";
        boolean isActive = false;

        TraineeUpdateRequestDto model = new TraineeUpdateRequestDto(
                firstName,
                lastName,
                dob,
                address,
                isActive
        );

        // when
        TraineeUpdateResponseDto trainee = traineeService.update(username, model).orElse(null);

        UserEntity user = userRepository.findByUsername(username).orElse(null);
        TraineeResponseDto traineeResponseDto = traineeService.findByUsername(username);

        // then
        assertAll(
                "Assertions for 'update()' method",
                () -> assertThat(trainee).isNotNull(),
                () -> assertThat(trainee.firstName()).isEqualTo(firstName),
                () -> assertThat(trainee.lastName()).isEqualTo(lastName),
                () -> assertThat(trainee.username()).isEqualTo(username),
                () -> assertThat(trainee.isActive()).isEqualTo(isActive),
                () -> assertThat(trainee.address()).isEqualTo(address),
                () -> assertThat(trainee.dob()).isEqualTo(dob),

                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getFirstName()).isEqualTo(firstName),
                () -> assertThat(user.getLastName()).isEqualTo(lastName),
                () -> assertThat(user.getIsActive()).isEqualTo(isActive),
                () -> assertThat(user.getTrainer()).isNull(),
                () -> assertThat(user.getTrainee()).isNotNull(),

                () -> assertThat(traineeResponseDto).isNotNull(),
                () -> assertThat(traineeResponseDto.firstName()).isEqualTo(firstName),
                () -> assertThat(traineeResponseDto.lastName()).isEqualTo(lastName)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"vincestewart50", "non-existent"})
    void shouldNotUpdate(String username) {
        // given
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        LocalDate dob = LocalDate.of(2000, 1, 1);
        String address = "ADDRESS";
        boolean isActive = false;

        TraineeUpdateRequestDto model = new TraineeUpdateRequestDto(
                firstName,
                lastName,
                dob,
                address,
                isActive
        );

        // when
        TraineeUpdateResponseDto trainee = traineeService.update(username, model).orElse(null);

        // then
        assertAll(
                "Assertions for 'update()' method",
                () -> assertThat(trainee).isNull()
        );
    }

    @Test
    void shouldDeleteByUsername() {
        // given
        String username = "johndoe1";

        // when & then
        traineeService.deleteByUsername(username);

        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThrows(NotFoundException.class, () -> traineeService.findByUsername(username))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"non-existent", "vincestewart50"})
    void shouldNotDeleteByUsername(String username) {
        // when & then
        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThrows(NotFoundException.class, () -> traineeService.deleteByUsername(username))
        );
    }

    @Test
    void shouldGetUnassignedTrainersByUsernameToResponse1() {
        // given
        String username = "johndoe1";

        // when
        Set<SimpleTrainerResponseDto> unassignedTrainers = traineeService.getUnassignedTrainersByUsernameToResponse(username);

        // then
        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThat(unassignedTrainers).isNotEmpty(),
                () -> assertThat(unassignedTrainers.size()).isEqualTo(24)
        );
    }

    @Test
    void shouldGetUnassignedTrainersByUsernameToResponse2() {
        // given
        String username = "johndoe1";

        // when
        traineeService.updateTrainerListByUsername(username, List.of(
                "janedoe2", // trainee username (should not be added)
                "vincestewart50",
                "umalewis49",
                "tomward48",
                "saracooper47"
        ));
        Set<SimpleTrainerResponseDto> unassignedTrainers = traineeService.getUnassignedTrainersByUsernameToResponse(username);

        // then
        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThat(unassignedTrainers).isNotEmpty(),
                () -> assertThat(unassignedTrainers.size()).isEqualTo(21)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"non-existent", "vincestewart50"})
    void shouldNotGetUnassignedTrainersByUsernameToResponse(String username) {
        // when & then
        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThrows(NotFoundException.class, () -> traineeService.getUnassignedTrainersByUsernameToResponse(username))
        );
    }

    @Test
    void shouldGetTrainingsByUsernameToResponse1() {
        // given
        String username = "johndoe1";
        LocalDate periodFrom = null;
        LocalDate periodTo = null;
        String trainerName = null;
        String trainingType = null;

        // when
        Set<TrainingResponseForTraineeDto> trainings = traineeService
                .getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isNotNull(),
                () -> assertThat(trainings.isEmpty()).isFalse(),
                () -> assertThat(trainings.size()).isEqualTo(1)
        );
    }

    private static Stream<Arguments> provideArgumentsForGetTrainingsByUsername() {
        return Stream.of(
                Arguments.of("johndoe1", null, null, null, null, 4),
                Arguments.of("johndoe1", LocalDate.of(2027, 1, 1), LocalDate.of(2030, 1, 1), null, null, 1),
                Arguments.of("johndoe1", null, null, "tomward48", null, 1),
                Arguments.of("johndoe1", null, null, null, "SWIMMING", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForGetTrainingsByUsername")
    void shouldGetTrainingsByUsernameToResponse2(String traineeUsername, LocalDate periodFrom, LocalDate periodTo, String trainerName, String trainingType, int resultSize) {
        // given
        TrainingAddRequestDto model1 = new TrainingAddRequestDto(
                traineeUsername,
                "vincestewart50",
                "TRAINING 1",
                LocalDate.of(2025, 1, 1),
                3
        );

        TrainingAddRequestDto model2 = new TrainingAddRequestDto(
                traineeUsername,
                "umalewis49",
                "TRAINING 2",
                LocalDate.of(2030, 1, 1),
                3
        );

        TrainingAddRequestDto model3 = new TrainingAddRequestDto(
                traineeUsername,
                "tomward48",
                "TRAINING 2",
                LocalDate.of(2035, 1, 1),
                3
        );

        // when
        trainingService.save(model1);
        trainingService.save(model2);
        trainingService.save(model3);

        Set<TrainingResponseForTraineeDto> trainings = traineeService.getTrainingsByUsernameToResponse(traineeUsername, periodFrom, periodTo, trainerName, trainingType);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isNotNull(),
                () -> assertThat(trainings.isEmpty()).isFalse(),
                () -> assertThat(trainings.size()).isEqualTo(resultSize)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"non-existent", "vincestewart50"})
    void shouldNotGetTrainingsByUsernameToResponse1(String username) {
        // given
        LocalDate periodFrom = null;
        LocalDate periodTo = null;
        String trainerName = null;
        String trainingType = null;

        // when & then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThrows(NotFoundException.class, () -> traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType))
        );
    }

    private static Stream<Arguments> provideArgumentsForUpdateTrainerList() {
        return Stream.of(
                Arguments.of(
                        "johndoe1",
                        List.of("vincestewart50",
                                "umalewis49",
                                "ninahayes42",
                                "leosanders40",
                                "alicesmith3", // trainee username
                                "non-existent"),
                        4),
                Arguments.of(
                        "janedoe2",
                        List.of("leosanders40",
                                "alicesmith3", // trainee username
                                "non-existent"),
                        1),
                Arguments.of(
                        "johndoe1",
                        List.of(),
                        0
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForUpdateTrainerList")
    void shouldUpdateTrainerListByUsername1(String traineeUsername, List<String> trainerUsernames, int resultSize) {
        // when
        var trainers = traineeService.updateTrainerListByUsername(traineeUsername, trainerUsernames);
        var trainersFromDb = traineeService.findByUsername(traineeUsername);

        // then
        assertAll(
                "Assertions for 'updateTrainerListByUsername()' method",
                () -> assertThat(trainers).isNotNull(),
                () -> assertThat(trainers.size()).isEqualTo(resultSize),
                () -> assertThat(trainersFromDb.trainerList().size()).isEqualTo(trainers.size())
        );
    }
}