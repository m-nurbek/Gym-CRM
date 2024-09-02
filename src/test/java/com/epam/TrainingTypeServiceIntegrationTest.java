package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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

    }

    @Test
    public void shouldNotFindTrainingType() {

    }

    @Test
    public void shouldAddTrainingType() {

    }

    @Test
    public void shouldNotAddTrainingType() {

    }

    @Test
    public void shouldUpdateTrainingType() {

    }

    @Test
    public void shouldNotUpdateTrainingType() {

    }

    @Test
    public void shouldDeleteTrainingType() {

    }

    @Test
    public void shouldNotDeleteTrainingType() {

    }

    @Test
    public void shouldGetAllTrainingTypes() {

    }

    @Test
    public void shouldGetAllOnEmptyRepositoryCorrectly() {

    }
}