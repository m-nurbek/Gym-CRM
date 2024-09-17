package com.epam.gym.hibernate;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.repository.TrainingTypeRepository;
import com.epam.gym.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class EntityCascadeTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    @DisplayName(value = "delete `user` => `trainee` in a cascading way")
    void shouldCascadinglyDeleteUser1() {
        // when
        boolean isDeleted = userRepository.deleteById(BigInteger.ONE);

        // then
        var u = userRepository.findById(BigInteger.ONE);
        var t = traineeRepository.findById(BigInteger.ONE);
        var tr = trainingRepository.findById(BigInteger.ONE);
        var type = trainingTypeRepository.findById(BigInteger.ONE);

        assertAll(
                "Assertions of cascading delete user -> trainee",
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(u).isEmpty(),
                () -> assertThat(t).isEmpty(),
                () -> assertThat(tr).isEmpty(),
                () -> assertThat(type).isNotEmpty()
        );
    }

    @Test
    @DisplayName(value = "delete `user` => `trainer` in a cascading way")
    void shouldCascadinglyDeleteUser2() {
        // when
        boolean isDeleted = userRepository.deleteById(BigInteger.valueOf(26));

        // then
        var u = userRepository.findById(BigInteger.valueOf(26));
        var t = trainerRepository.findById(BigInteger.ONE);
        var tr = trainingRepository.findById(BigInteger.ONE);
        var type = trainingTypeRepository.findById(BigInteger.ONE);

        assertAll(
                "Assertions of cascading delete user -> trainer",
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(u).isEmpty(),
                () -> assertThat(t).isEmpty(),
                () -> assertThat(tr).isEmpty(),
                () -> assertThat(type).isNotEmpty()
        );
    }

    @Test
    @DisplayName(value = "delete `trainee` => `training` in a cascading way")
    void shouldCascadinglyDeleteTrainee1() {
        // when
        boolean isDeleted = traineeRepository.deleteById(BigInteger.ONE);

        // then
        var u = userRepository.findById(BigInteger.ONE);
        var t = traineeRepository.findById(BigInteger.ONE);
        var tr = trainingRepository.findById(BigInteger.ONE);
        var type = trainingTypeRepository.findById(BigInteger.ONE);

        assertAll(
                "Assertions of cascading delete trainee -> training",
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(u).isEmpty(),
                () -> assertThat(t).isEmpty(),
                () -> assertThat(tr).isEmpty(),
                () -> assertThat(type).isNotEmpty()
        );
    }

    @Test
    @DisplayName(value = "delete `trainee` => `training` in a cascading way")
    void shouldCascadinglyDeleteTrainee2() {
        // when
        boolean isDeleted = traineeRepository.deleteById(BigInteger.TWO);

        // then
        var u = userRepository.findById(BigInteger.TWO);
        var t = traineeRepository.findById(BigInteger.TWO);
        var tr = trainingRepository.findById(BigInteger.TWO);
        var type = trainingTypeRepository.findById(BigInteger.TWO);

        assertAll(
                "Assertions of cascading delete trainee -> training",
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(u).isEmpty(),
                () -> assertThat(t).isEmpty(),
                () -> assertThat(tr).isEmpty(),
                () -> assertThat(type).isNotEmpty()
        );
    }

    @Test
    @DisplayName(value = "delete `training` must not delete `trainer` or `trainee`")
    void shouldNotCascadinglyDeleteTraining() {
        // when
        boolean isDeleted = trainingRepository.deleteById(BigInteger.ONE);

        // then
        var u = userRepository.findById(BigInteger.ONE);
        var t = traineeRepository.findById(BigInteger.ONE);
        var trainer = trainerRepository.findById(BigInteger.valueOf(26));
        var tr = trainingRepository.findById(BigInteger.ONE);
        var type = trainingTypeRepository.findById(BigInteger.ONE);

        assertAll(
                "Assertions of delete training",
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(u).isNotEmpty(),
                () -> assertThat(t).isNotEmpty(),
                () -> assertThat(trainer).isNotEmpty(),
                () -> assertThat(tr).isEmpty(),
                () -> assertThat(type).isNotEmpty()
        );
    }

    @Test
    @DisplayName(value = "delete `trainingType` must delete 'training', 'trainer' and corresponding `user`")
    void shouldCascadinglyDeleteTrainingType() {
        // when
        boolean isDeleted = trainingTypeRepository.deleteById(BigInteger.ONE);

        // then
        var u = userRepository.findById(BigInteger.valueOf(26));
        var t = traineeRepository.findById(BigInteger.ONE);
        var trainer = trainerRepository.findById(BigInteger.valueOf(26));
        var tr = trainingRepository.findById(BigInteger.ONE);
        var type = trainingTypeRepository.findById(BigInteger.ONE);

        assertAll(
                "Assertions of cascading delete trainingType",
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(type).isEmpty(),
                () -> assertThat(t).isNotEmpty(),
                () -> assertThat(trainer).isEmpty(),
                () -> assertThat(tr).isEmpty(),
                () -> assertThat(u).isEmpty()
        );
    }
}