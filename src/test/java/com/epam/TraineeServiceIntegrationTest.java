package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.dto.TraineeDto;
import com.epam.service.TraineeService;
import com.epam.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TraineeServiceIntegrationTest {
    private final TraineeService traineeService;
    private final UserService userService;

    @Autowired
    public TraineeServiceIntegrationTest(TraineeService traineeService, UserService userService) {
        this.traineeService = traineeService;
        this.userService = userService;
    }

    @Test
    public void shouldFindTrainee() {
        // given
        BigInteger traineeId = BigInteger.ONE;
        BigInteger traineeId50 = BigInteger.valueOf(50L);

        // when
        var foundTrainee = traineeService.get(traineeId).orElse(null);
        var foundTrainee2 = traineeService.get(traineeId50).orElse(null);

        // then
        assertThat(foundTrainee).isNotNull();
        assertThat(foundTrainee2).isNotNull();
    }

    @Test
    public void shouldNotFindTrainee() {
        // given
        BigInteger traineeId0 = BigInteger.ZERO;
        BigInteger traineeId51 = BigInteger.valueOf(51L);

        // when
        var foundTrainee0 = traineeService.get(traineeId0).orElse(null);
        var foundTrainee51 = traineeService.get(traineeId51).orElse(null);

        // then
        assertThat(foundTrainee0).isNull();
        assertThat(foundTrainee51).isNull();
    }

    @Test
    public void shouldAddTrainee() {
        // given
        var trainee = TraineeDto.builder().address("Address 1").dob(new Date("2001-01-01")).user(userService.get(BigInteger.ONE).orElse(null)).build();

        // when
        var addedTrainee = traineeService.add(trainee);

        // then
        assertThat(addedTrainee).isNotNull();
        assertThat(addedTrainee).isEqualTo(trainee);
    }

    @Test
    public void shouldNotAddTrainee() {
        // given
        var trainee = TraineeDto.builder().id(BigInteger.ONE).address("Address 1").dob(new Date("2001-01-01")).user(userService.get(BigInteger.ONE).orElse(null)).build();

        // when
        var addedTrainee = traineeService.add(trainee);

        // then
        assertThat(addedTrainee).isNotNull();
        assertThat(addedTrainee).isNotEqualTo(trainee);
    }

    @Test
    public void shouldUpdateTrainee() {

    }

    @Test
    public void shouldNotUpdateTrainee() {

    }

    @Test
    public void shouldDeleteTrainee() {

    }

    @Test
    public void shouldNotDeleteTrainee() {

    }

    @Test
    public void shouldGetAllTrainees() {

    }

    @Test
    public void shouldGetAllOnEmptyRepositoryCorrectly() {

    }
}