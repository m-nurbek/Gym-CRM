package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TrainingServiceIntegrationTest {
    private final TrainingService trainingService;

    @Autowired
    public TrainingServiceIntegrationTest(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Test
    public void shouldFindTraining() {

    }

    @Test
    public void shouldNotFindTraining() {

    }

    @Test
    public void shouldAddTraining() {

    }

    @Test
    public void shouldNotAddTraining() {

    }

    @Test
    public void shouldUpdateTraining() {

    }

    @Test
    public void shouldNotUpdateTraining() {

    }

    @Test
    public void shouldDeleteTraining() {

    }

    @Test
    public void shouldNotDeleteTraining() {

    }

    @Test
    public void shouldGetAllTrainings() {

    }

    @Test
    public void shouldGetAllOnEmptyRepositoryCorrectly() {

    }
}