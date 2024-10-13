package com.epam.gym.service.impl;

import com.epam.gym.Application;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Test
    void shouldNotSave1() {
        // given
        LocalDate dob = LocalDate.of(2000, 1, 1);
        String address = "ADDRESS";
        BigInteger userId = BigInteger.valueOf(26);

        // when
        TraineeResponseDto trainee = traineeService.save(dob, address, userId);

        // then
        assertAll(
                "Assertions for 'save()' method",
                () -> assertThat(trainee).isNull()
        );
    }

    @Test
    void shouldNotSave2() {
        // given
        LocalDate dob = LocalDate.of(2000, 1, 1);
        String address = "ADDRESS";
        BigInteger userId = BigInteger.valueOf(51); // non-existent user

        // when
        TraineeResponseDto trainee = traineeService.save(dob, address, userId);

        // then
        assertAll(
                "Assertions for 'save()' method",
                () -> assertThat(trainee).isNull()
        );
    }

    @Test
    void shouldFindByUsername() {
        // given
        String username = "johndoe1";

        // when
        TraineeResponseDto trainee = traineeService.findByUsername(username).orElse(null);

        // then
        assertAll(
                "Assertions for 'findByUsername()' method",
                () -> assertThat(trainee).isNotNull(),
                () -> assertThat(trainee.firstName()).isEqualTo("John"),
                () -> assertThat(trainee.lastName()).isEqualTo("Doe")
        );
    }

    @Test
    void shouldNotFindByUsername1() {
        // given
        String username = "non-existent";

        // when
        TraineeResponseDto trainee = traineeService.findByUsername(username).orElse(null);

        // then
        assertAll(
                "Assertions for 'findByUsername()' method",
                () -> assertThat(trainee).isNull()
        );
    }

    @Test
    void shouldNotFindByUsername2() {
        // given
        String username = "vincestewart50";

        // when
        TraineeResponseDto trainee = traineeService.findByUsername(username).orElse(null);

        // then
        assertAll(
                "Assertions for 'findByUsername()' method",
                () -> assertThat(trainee).isNull()
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
        TraineeResponseDto traineeResponseDto = traineeService.findByUsername(username).orElse(null);

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

    @Test
    void shouldNotUpdate1() {
        // given
        String username = "vincestewart50"; // trainer username
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
    void shouldNotUpdate2() {
        // given
        String username = "non-existent";
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

        // when
        boolean success = traineeService.deleteByUsername(username);
        var trainee = traineeService.findByUsername(username).orElse(null);

        // then
        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThat(success).isTrue(),
                () -> assertThat(trainee).isNull()
        );
    }

    @Test
    void shouldNotDeleteByUsername1() {
        // given
        String username = "non-existent";

        // when
        boolean success = traineeService.deleteByUsername(username);
        var trainee = traineeService.findByUsername(username).orElse(null);

        // then
        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThat(success).isFalse(),
                () -> assertThat(trainee).isNull()
        );
    }

    @Test
    void shouldNotDeleteByUsername2() {
        // given
        String username = "vincestewart50"; // trainer username

        // when
        boolean success = traineeService.deleteByUsername(username);
        var trainee = traineeService.findByUsername(username).orElse(null);

        // then
        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThat(success).isFalse(),
                () -> assertThat(trainee).isNull()
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

    @Test
    void shouldNotGetUnassignedTrainersByUsernameToResponse1() {
        // given
        String username = "non-existent";

        // when
        Set<SimpleTrainerResponseDto> unassignedTrainers = traineeService.getUnassignedTrainersByUsernameToResponse(username);

        // then
        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThat(unassignedTrainers).isEmpty()
        );
    }

    @Test
    void shouldNotGetUnassignedTrainersByUsernameToResponse2() {
        // given
        String username = "vincestewart50"; // trainer username

        // when
        Set<SimpleTrainerResponseDto> unassignedTrainers = traineeService.getUnassignedTrainersByUsernameToResponse(username);

        // then
        assertAll(
                "Assertions for 'delete()' method",
                () -> assertThat(unassignedTrainers).isEmpty()
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
        Set<TrainingResponseForTraineeDto> trainings = traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isNotNull(),
                () -> assertThat(trainings.isEmpty()).isFalse(),
                () -> assertThat(trainings.size()).isEqualTo(1)
        );
    }

    @Test
    void shouldGetTrainingsByUsernameToResponse2() {
        // given
        String username = "johndoe1";
        LocalDate periodFrom = null;
        LocalDate periodTo = null;
        String trainerName = null;
        String trainingType = null;

        TrainingAddRequestDto model1 = new TrainingAddRequestDto(
                username,
                "vincestewart50",
                "TRAINING 1",
                LocalDate.of(2025, 1, 1),
                3
        );

        TrainingAddRequestDto model2 = new TrainingAddRequestDto(
                username,
                "umalewis49",
                "TRAINING 2",
                LocalDate.of(2030, 1, 1),
                3
        );

        TrainingAddRequestDto model3 = new TrainingAddRequestDto(
                username,
                "tomward48",
                "TRAINING 2",
                LocalDate.of(2035, 1, 1),
                3
        );

        // when
        trainingService.save(model1);
        trainingService.save(model2);
        trainingService.save(model3);

        Set<TrainingResponseForTraineeDto> trainings = traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isNotNull(),
                () -> assertThat(trainings.isEmpty()).isFalse(),
                () -> assertThat(trainings.size()).isEqualTo(4)
        );
    }

    @Test
    void shouldGetTrainingsByUsernameToResponse3() {
        // given
        String username = "johndoe1";
        LocalDate periodFrom = LocalDate.of(2027, 1, 1);
        LocalDate periodTo = LocalDate.of(2030, 1, 1);
        String trainerName = null;
        String trainingType = null;

        TrainingAddRequestDto model1 = new TrainingAddRequestDto(
                username,
                "vincestewart50",
                "TRAINING 1",
                LocalDate.of(2025, 1, 1),
                3
        );

        TrainingAddRequestDto model2 = new TrainingAddRequestDto(
                username,
                "umalewis49",
                "TRAINING 2",
                LocalDate.of(2030, 1, 1),
                3
        );

        TrainingAddRequestDto model3 = new TrainingAddRequestDto(
                username,
                "tomward48",
                "TRAINING 2",
                LocalDate.of(2035, 1, 1),
                3
        );

        // when
        trainingService.save(model1);
        trainingService.save(model2);
        trainingService.save(model3);

        Set<TrainingResponseForTraineeDto> trainings = traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isNotNull(),
                () -> assertThat(trainings.isEmpty()).isFalse(),
                () -> assertThat(trainings.size()).isEqualTo(1)
        );
    }

    @Test
    void shouldGetTrainingsByUsernameToResponse4() {
        // given
        String username = "johndoe1";
        LocalDate periodFrom = null;
        LocalDate periodTo = null;
        String trainerName = "tomward48";
        String trainingType = null;

        TrainingAddRequestDto model1 = new TrainingAddRequestDto(
                username,
                "vincestewart50",
                "TRAINING 1",
                LocalDate.of(2025, 1, 1),
                3
        );

        TrainingAddRequestDto model2 = new TrainingAddRequestDto(
                username,
                "umalewis49",
                "TRAINING 2",
                LocalDate.of(2030, 1, 1),
                3
        );

        TrainingAddRequestDto model3 = new TrainingAddRequestDto(
                username,
                trainerName,
                "TRAINING 2",
                LocalDate.of(2035, 1, 1),
                3
        );

        // when
        trainingService.save(model1);
        trainingService.save(model2);
        trainingService.save(model3);

        Set<TrainingResponseForTraineeDto> trainings = traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isNotNull(),
                () -> assertThat(trainings.isEmpty()).isFalse(),
                () -> assertThat(trainings.size()).isEqualTo(1)
        );
    }

    @Test
    void shouldGetTrainingsByUsernameToResponse5() {
        // given
        String username = "johndoe1";
        LocalDate periodFrom = null;
        LocalDate periodTo = null;
        String trainerName = null;
        String trainingType = "SWIMMING";

        TrainingAddRequestDto model1 = new TrainingAddRequestDto(
                username,
                "vincestewart50",
                "TRAINING 1",
                LocalDate.of(2025, 1, 1),
                3
        );

        TrainingAddRequestDto model2 = new TrainingAddRequestDto(
                username,
                "umalewis49",
                "TRAINING 2",
                LocalDate.of(2030, 1, 1),
                3
        );

        TrainingAddRequestDto model3 = new TrainingAddRequestDto(
                username,
                "tomward48",
                "TRAINING 3",
                LocalDate.of(2035, 1, 1),
                3
        );

        // when
        trainingService.save(model1);
        trainingService.save(model2);
        trainingService.save(model3);

        Set<TrainingResponseForTraineeDto> trainings = traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isNotNull(),
                () -> assertThat(trainings.isEmpty()).isFalse(),
                () -> assertThat(trainings.size()).isEqualTo(1)
        );
    }

    @Test
    void shouldNotGetTrainingsByUsernameToResponse1() {
        // given
        String username = "non-existent";
        LocalDate periodFrom = null;
        LocalDate periodTo = null;
        String trainerName = null;
        String trainingType = null;

        // when
        Set<TrainingResponseForTraineeDto> trainings = traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isEmpty()
        );
    }

    @Test
    void shouldNotGetTrainingsByUsernameToResponse2() {
        // given
        String username = "vincestewart50"; // trainer username
        LocalDate periodFrom = null;
        LocalDate periodTo = null;
        String trainerName = null;
        String trainingType = null;

        // when
        Set<TrainingResponseForTraineeDto> trainings = traineeService.getTrainingsByUsernameToResponse(username, periodFrom, periodTo, trainerName, trainingType);

        // then
        assertAll(
                "Assertions for 'getTrainingsByUsernameToResponse()' method",
                () -> assertThat(trainings).isEmpty()
        );
    }

    @Test
    void updateTrainerListByUsername1() {
        // given
        String username = "johndoe1";

        // when
        var trainers = traineeService.updateTrainerListByUsername(username, List.of(
                "vincestewart50",
                "umalewis49",
                "ninahayes42",
                "leosanders40",
                "alicesmith3", // trainee username
                "non-existent"
        ));

        // then
        assertAll(
                "Assertions for 'updateTrainerListByUsername()' method",
                () -> assertThat(trainers).isNotEmpty(),
                () -> assertThat(trainers.size()).isEqualTo(4)
        );
    }

    @Test
    void updateTrainerListByUsername2() {
        // given
        String username = "johndoe1";

        // when
        var trainers = traineeService.updateTrainerListByUsername(username, List.of());

        // then
        assertAll(
                "Assertions for 'updateTrainerListByUsername()' method",
                () -> assertThat(trainers).isEmpty()
        );
    }
}