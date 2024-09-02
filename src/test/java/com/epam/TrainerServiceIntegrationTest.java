package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TrainerServiceIntegrationTest {
    private final TrainerService trainerService;

    @Autowired
    public TrainerServiceIntegrationTest(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Test
    public void shouldFindTrainer() {
        System.out.println(trainerService.getAll());

    }

    @Test
    public void shouldNotFindTrainer() {

    }

    @Test
    public void shouldAddTrainer() {

    }

    @Test
    public void shouldNotAddTrainer() {

    }

    @Test
    public void shouldUpdateTrainer() {

    }

    @Test
    public void shouldNotUpdateTrainer() {

    }

    @Test
    public void shouldDeleteTrainer() {

    }

    @Test
    public void shouldNotDeleteTrainer() {

    }

    @Test
    public void shouldGetAllTrainers() {

    }

    @Test
    public void shouldGetAllOnEmptyRepositoryCorrectly() {

    }
}