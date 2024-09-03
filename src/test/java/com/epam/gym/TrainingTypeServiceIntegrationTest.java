package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.service.TrainingTypeService;
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
public class TrainingTypeServiceIntegrationTest {
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingTypeServiceIntegrationTest(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @Test
    public void shouldFindTrainingType() {
        // given
        BigInteger trainingTypeId = BigInteger.ONE;
        BigInteger trainingTypeId50 = BigInteger.valueOf(20L);

        // when
        var foundTrainingType = trainingTypeService.get(trainingTypeId).orElse(null);
        var foundTrainingType2 = trainingTypeService.get(trainingTypeId50).orElse(null);

        // then
        assertThat(foundTrainingType).isNotNull();
        assertThat(foundTrainingType2).isNotNull();
    }

    @Test
    public void shouldNotFindTrainingType() {
        // given
        BigInteger trainingTypeId0 = BigInteger.ZERO;
        BigInteger trainingTypeId21 = BigInteger.valueOf(21L);

        // when
        var foundTrainingType0 = trainingTypeService.get(trainingTypeId0).orElse(null);
        var foundTrainingType21 = trainingTypeService.get(trainingTypeId21).orElse(null);

        // then
        assertThat(foundTrainingType0).isNull();
        assertThat(foundTrainingType21).isNull();
    }

    @Test
    public void shouldAddTrainingType() {
        // given
        var trainingType = TrainingTypeDto.builder().name("TEST").build();

        // when
        var addedTrainingType = trainingTypeService.add(trainingType);

        // then
        assertThat(addedTrainingType).isNotNull();
        assertThat(addedTrainingType.getName()).isEqualTo("TEST");
    }

    @Test
    public void shouldNotAddTrainingType() {
        // given
        var trainingType = TrainingTypeDto.builder().id(BigInteger.ONE).name("TEST").build();

        // when
        var addedTrainingType = trainingTypeService.add(trainingType);

        // then
        assertThat(addedTrainingType).isNotNull();
        assertThat(addedTrainingType.getName()).isNotEqualTo("TEST");
    }

    @Test
    public void shouldUpdateTrainingType() {
        // given
        BigInteger trainingTypeId = BigInteger.ONE;
        var trainingType = trainingTypeService.get(trainingTypeId).orElse(null);
        assert trainingType != null;

        // when
        trainingType.setName("UpdatedName");
        var updatedTrainingType = trainingTypeService.update(trainingType);

        // then
        assertThat(updatedTrainingType).isNotNull();
        assertThat(updatedTrainingType.getId()).isEqualTo(trainingTypeId);
        assertThat(updatedTrainingType.getName()).isEqualTo("UpdatedName");
        assertThat(trainingTypeService.get(trainingTypeId).orElse(null)).isEqualTo(updatedTrainingType);
    }

    @Test
    public void shouldNotUpdateTrainingType() {
        // given
        BigInteger trainingTypeId = BigInteger.valueOf(25L);
        var trainingType = TrainingTypeDto.builder().id(trainingTypeId).name("TEST").build();

        // when
        var updatedTrainingType = trainingTypeService.update(trainingType);

        // then
        assertThat(updatedTrainingType).isNull();
    }

    @Test
    public void shouldDeleteTrainingType() {
        // given
        BigInteger trainingTypeId = BigInteger.ONE;

        // when
        trainingTypeService.delete(trainingTypeId);

        // then
        assertThat(trainingTypeService.get(trainingTypeId).orElse(null)).isNull();
    }

    @Test
    public void shouldNotDeleteTrainingType() {
        // given
        BigInteger trainingTypeId = BigInteger.valueOf(25L);

        // when
        trainingTypeService.delete(trainingTypeId);

        // then
        assertThat(trainingTypeService.get(trainingTypeId).orElse(null)).isNull();
    }

    @Test
    public void shouldGetAllTrainingTypes() {
        // when
        var trainingTypes = trainingTypeService.getAll();

        // then
        assertThat(trainingTypes).isNotNull();
        assertThat(trainingTypes).isNotEmpty();
    }

    @Test
    public void shouldGetAllOnEmptyRepositoryCorrectly() {
        // given
        trainingTypeService.getAll().forEach(t -> trainingTypeService.delete(t.getId())); // deleted all training types

        // when
        var trainingTypes = trainingTypeService.getAll();

        // then
        assertThat(trainingTypes).isNotNull();
    }
}