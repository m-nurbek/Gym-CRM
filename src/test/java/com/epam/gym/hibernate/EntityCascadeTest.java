package com.epam.gym.hibernate;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.repository.hibernate.TraineeRepository;
import com.epam.gym.repository.hibernate.UserRepository;
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

    @Test
    @DisplayName(value = "delete`user` => delete `trainee` in a cascading way")
    void shouldCascadinglyDeleteTrainee() {
        // when
        boolean isDeleted = traineeRepository.deleteById(BigInteger.ONE);

        // then
        var t = traineeRepository.findById(BigInteger.ONE);
        assertAll(
                "Assertions of cascading delete user -> trainee",
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(t).isEmpty()
        );
    }

    @Test
    @DisplayName(value = "delete `user` => `trainer` in a cascading way")
    void shouldCascadinglyDeleteTrainer() {

    }

    @Test
    @DisplayName(value = "when deleting the 'trainee' should also delete the corresponding `training` in a cascading way")
    void shouldCascadinglyDeleteTraining1() {

    }

    @Test
    @DisplayName(value = "when deleting the 'trainer' should also delete the corresponding `training` in a cascading way")
    void shouldCascadinglyDeleteTraining2() {

    }

    @Test
    @DisplayName(value = "when deleting the 'trainingType' should also delete the corresponding `training` in a cascading way")
    void shouldCascadinglyDeleteTraining3() {

    }

    @Test
    @DisplayName(value = "when deleting the 'trainingType' should also delete the corresponding `trainer` in a cascading way")
    void shouldCascadinglyDeleteTrainer2() {

    }
}