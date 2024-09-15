package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.dto.TrainingDto;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingService;
import com.epam.gym.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TrainingServiceIntegrationTest {
    private final TrainingService trainingService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingServiceIntegrationTest(TrainingService trainingService, TrainerService trainerService, TraineeService traineeService, TrainingTypeService trainingTypeService) {
        this.trainingService = trainingService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingTypeService = trainingTypeService;
    }

    @Test
    public void shouldFindTraining() {
        // given
        var trainingId = BigInteger.ONE;
        var trainingId50 = BigInteger.valueOf(24L);

        // when
        var foundTraining = trainingService.get(trainingId).orElse(null);
        var foundTraining2 = trainingService.get(trainingId50).orElse(null);

        // then
        assertThat(foundTraining).isNotNull();
        assertThat(foundTraining2).isNotNull();
    }

    @Test
    public void shouldNotFindTraining() {
        // given
        var trainingId0 = BigInteger.ZERO;
        var trainingId51 = BigInteger.valueOf(26L);

        // when
        var foundTraining0 = trainingService.get(trainingId0).orElse(null);
        var foundTraining51 = trainingService.get(trainingId51).orElse(null);

        // then
        assertThat(foundTraining0).isNull();
        assertThat(foundTraining51).isNull();
    }

    @Test
    public void shouldAddTraining() {
        // given
        var trainingType = trainingTypeService.get(BigInteger.ONE).orElse(null);
        var trainer = trainerService.get(BigInteger.valueOf(25L)).orElse(null);
        var trainee = traineeService.get(BigInteger.valueOf(1L)).orElse(null);
        var date = LocalDate.of(2001, 1, 1);

        var training = TrainingDto.builder()
                .type(trainingType)
                .date(date)
                .duration("2 hours")
                .trainee(trainee)
                .trainer(trainer)
                .name("TEST")
                .build();

        // when
        var addedTraining = trainingService.add(training);

        // then
        assertThat(addedTraining).isNotNull();
        assertThat(addedTraining.getId()).isNotNull();
        assertThat(addedTraining.getId()).isEqualTo(BigInteger.valueOf(25L));
        assertThat(addedTraining.getType()).isEqualTo(trainingType);
        assertThat(addedTraining.getDate()).isEqualTo(date);
        assertThat(addedTraining.getDuration()).isEqualTo("2 hours");
        assertThat(addedTraining.getTrainee()).isEqualTo(trainee);
        assertThat(addedTraining.getTrainer()).isEqualTo(trainer);
        assertThat(addedTraining.getName()).isEqualTo("TEST");
    }

    @Test
    public void shouldNotAddTraining() {
        // given
        var trainingType = trainingTypeService.get(BigInteger.ONE).orElse(null);
        var trainer = trainerService.get(BigInteger.valueOf(25L)).orElse(null);
        var trainee = traineeService.get(BigInteger.valueOf(1L)).orElse(null);
        var date = LocalDate.of(3000, 1, 1);

        var training = TrainingDto.builder()
                .id(BigInteger.valueOf(1L))
                .type(trainingType)
                .date(date)
                .duration("2 hours")
                .trainee(trainee)
                .trainer(trainer)
                .name("TEST")
                .build();

        // when
        var addedTraining = trainingService.add(training);

        // then
        assertThat(addedTraining).isNotNull();
        assertThat(addedTraining).isNotEqualTo(training);
    }

    @Test
    public void shouldUpdateTraining() {
        // given
        var trainingType = trainingTypeService.get(BigInteger.ONE).orElse(null);
        var trainer = trainerService.get(BigInteger.valueOf(25L)).orElse(null);
        var trainee = traineeService.get(BigInteger.valueOf(1L)).orElse(null);
        var date = LocalDate.of(2001, 1, 1);

        var training = TrainingDto.builder()
                .id(BigInteger.valueOf(1L))
                .type(trainingType)
                .date(date)
                .duration("2 hours")
                .trainee(trainee)
                .trainer(trainer)
                .name("TEST")
                .build();

        var addedTraining = trainingService.update(training);
        assert addedTraining != null;

        // when
        addedTraining.setName("UpdatedName");
        var updatedTraining = trainingService.update(addedTraining);

        // then
        assertThat(updatedTraining).isNotNull();
        assertThat(updatedTraining.getId()).isEqualTo(BigInteger.valueOf(1L));
        assertThat(updatedTraining.getType()).isEqualTo(trainingType);
        assertThat(updatedTraining.getDate()).isEqualTo(date);
        assertThat(updatedTraining.getDuration()).isEqualTo("2 hours");
        assertThat(updatedTraining.getTrainee()).isEqualTo(trainee);
        assertThat(updatedTraining.getTrainer()).isEqualTo(trainer);
        assertThat(updatedTraining.getName()).isEqualTo("UpdatedName");
    }

    @Test
    public void shouldNotUpdateTraining() {
        // given
        var trainingType = trainingTypeService.get(BigInteger.ONE).orElse(null);
        var trainer = trainerService.get(BigInteger.valueOf(25L)).orElse(null);
        var trainee = traineeService.get(BigInteger.valueOf(1L)).orElse(null);
        var date = LocalDate.of(2001, 1, 1);

        var training = TrainingDto.builder()
                .id(BigInteger.valueOf(25L))
                .type(trainingType)
                .date(date)
                .duration("2 hours")
                .trainee(trainee)
                .trainer(trainer)
                .name("TEST")
                .build();

        // when
        var updatedTraining = trainingService.update(training);

        // then
        assertThat(updatedTraining).isNull();
    }

    @Test
    public void shouldDeleteTraining() {
        // given
        BigInteger trainingId = BigInteger.valueOf(1L);

        // when
        trainingService.delete(trainingId);

        // then
        assertThat(trainingService.get(trainingId).orElse(null)).isNull();
    }

    @Test
    public void shouldNotDeleteTraining() {
        // given
        BigInteger trainingId = BigInteger.valueOf(26L);

        // when
        trainingService.delete(trainingId);

        // then
        assertThat(trainingService.get(trainingId).orElse(null)).isNull();
    }

    @Test
    public void shouldGetAllTrainings() {
        // when
        var trainings = trainingService.getAll();

        // then
        assertThat(trainings).isNotNull();
        assertThat(trainings.size()).isEqualTo(24);
    }

    @Test
    public void shouldGetAllOnEmptyRepositoryCorrectly() {
        // given
        trainingService.getAll().forEach(t -> trainingService.delete(t.getId())); // deleted all trainings

        // when
        var trainings = trainingService.getAll();

        // then
        assertThat(trainings).isNotNull();
        assertThat(trainings).isEmpty();
    }
}