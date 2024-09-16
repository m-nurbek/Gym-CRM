package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TraineeServiceIntegrationTest {
    @Autowired
    private TraineeService traineeService;

    @Test
    void shouldFindTraineeEntityByUsername() {
        // given
        String username1 = "johndoe1";
        String username3 = "alicesmith3";
        var address1 = "123 Main St, Cityville";
        var address3 = "789 Oak St, Villageham";

        // when
        var trainee1 = traineeService.findByUsername(username1);
        var trainee3 = traineeService.findByUsername(username3);

        // then
        assertAll(
                "Assertions for find trainee by username",
                () -> assertThat(trainee1).isPresent(),
                () -> assertThat(trainee3).isPresent(),
                () -> assertThat(trainee1.get().getAddress()).isEqualTo(address1),
                () -> assertThat(trainee3.get().getAddress()).isEqualTo(address3)
        );
    }

    @Test
    void shouldNotFindTraineeEntityByUsername() {
        // given
        String username = "nonexistent";

        // when
        var trainee = traineeService.findByUsername(username);

        // then
        assertThat(trainee).isEmpty();
    }
}