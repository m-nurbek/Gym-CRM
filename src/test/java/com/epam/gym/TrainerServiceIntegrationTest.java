package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@WebAppConfiguration
@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TrainerServiceIntegrationTest {
    @Autowired
    private TrainerService trainerService;

    @Test
    void shouldFindTrainerEntityByUsername() {
        // given
        String username = "vincestewart50";

        // when
        var trainer = trainerService.findByUsername(username);

        // then
        assertAll(
                "Assertions for find trainer by username",
                () -> assertThat(trainer).isPresent()
        );
    }
}