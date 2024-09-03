package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.dto.TrainerDto;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TrainerServiceIntegrationTest {
    private final TrainerService trainerService;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainerServiceIntegrationTest(TrainerService trainerService, UserService userService, TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.userService = userService;
        this.trainingTypeService = trainingTypeService;
    }

    @Test
    public void shouldFindTrainer() {
        // given
        BigInteger trainerId = BigInteger.valueOf(25L);
        BigInteger trainerId50 = BigInteger.valueOf(50L);

        // when
        var foundTrainer = trainerService.get(trainerId).orElse(null);
        var foundTrainer2 = trainerService.get(trainerId50).orElse(null);

        // then
        assertThat(foundTrainer).isNotNull();
        assertThat(foundTrainer2).isNotNull();
    }

    @Test
    public void shouldNotFindTrainer() {
        // given
        BigInteger trainerId0 = BigInteger.valueOf(24L);
        BigInteger trainerId51 = BigInteger.valueOf(51L);

        // when
        var foundTrainer0 = trainerService.get(trainerId0).orElse(null);
        var foundTrainer51 = trainerService.get(trainerId51).orElse(null);

        // then
        assertThat(foundTrainer0).isNull();
        assertThat(foundTrainer51).isNull();
    }

    @Test
    public void shouldAddTrainer() {
        // given
        var user = userService.get(BigInteger.valueOf(25L)).orElse(null);
        var specialization = trainingTypeService.get(BigInteger.valueOf(1L)).orElse(null);
        var trainer = TrainerDto.builder().specialization(specialization).user(user).build();

        // when
        var addedTrainer = trainerService.add(trainer);

        // then
        assertThat(addedTrainer).isNotNull();
        assertThat(addedTrainer.getId()).isNotNull();
        assertThat(addedTrainer.getSpecialization()).isEqualTo(trainer.getSpecialization());
        assertThat(addedTrainer.getUser()).isEqualTo(trainer.getUser());
    }

    @Test
    public void shouldNotAddTrainer() {
        // given
        var user = userService.get(BigInteger.valueOf(29L)).orElse(null);
        var specialization = trainingTypeService.get(BigInteger.TWO).orElse(null);
        var trainer = TrainerDto.builder().id(BigInteger.valueOf(25L)).specialization(specialization).user(user).build();

        // when
        var addedTrainer = trainerService.add(trainer);

        // then
        assertThat(addedTrainer).isNotNull();
        assertThat(addedTrainer).isNotEqualTo(trainer);
    }

    @Test
    public void shouldUpdateTrainer() {
        // given
        var newSpecialization = trainingTypeService.get(BigInteger.valueOf(5L)).orElse(null);
        BigInteger trainerId = BigInteger.valueOf(25L);
        var trainer = trainerService.get(trainerId).orElse(null);
        assert trainer != null;

        // when
        trainer.setSpecialization(newSpecialization);
        TrainerDto updatedTrainer = trainerService.update(trainer);

        // then
        assertThat(updatedTrainer).isNotNull();
        assertThat(updatedTrainer.getId()).isEqualTo(trainerId);
        assertThat(updatedTrainer.getSpecialization()).isEqualTo(newSpecialization);
        assertThat(trainerService.get(trainerId).orElse(null)).isEqualTo(updatedTrainer);
    }

    @Test
    public void shouldNotUpdateTrainer() {
        // given
        var user = userService.get(BigInteger.valueOf(25L)).orElse(null);
        var specialization = trainingTypeService.get(BigInteger.ONE).orElse(null);
        var trainer = TrainerDto.builder().id(BigInteger.valueOf(51L)).specialization(specialization).user(user).build();

        // when
        var updatedTrainer = trainerService.update(trainer);

        // then
        assertThat(updatedTrainer).isNull();
    }

    @Test
    public void shouldDeleteTrainer() {
        // given
        BigInteger trainerId = BigInteger.valueOf(25L);

        // when
        trainerService.delete(trainerId);

        // then
        assertThat(trainerService.get(trainerId).orElse(null)).isNull();
    }

    @Test
    public void shouldNotDeleteTrainer() {
        // given
        BigInteger trainerId = BigInteger.valueOf(51L);

        // when
        trainerService.delete(trainerId);

        // then
        assertThat(trainerService.get(trainerId).orElse(null)).isNull();
    }

    @Test
    public void shouldGetAllTrainers() {
        // when
        var trainers = trainerService.getAll();

        // then
        assertThat(trainers).isNotNull();
        assertThat(trainers).isNotEmpty();
    }

    @Test
    public void shouldGetAllOnEmptyRepositoryCorrectly() {
        // given
        trainerService.getAll().forEach(t -> trainerService.delete(t.getId())); // deleted all trainers

        // when
        var trainers = trainerService.getAll();

        // then
        assertThat(trainers).isNotNull();
        assertThat(trainers).isEmpty();
    }
}