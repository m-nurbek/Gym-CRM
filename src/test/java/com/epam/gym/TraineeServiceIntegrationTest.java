package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.dto.TraineeDto;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;

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
        BigInteger traineeId50 = BigInteger.valueOf(24L);

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
        BigInteger traineeId51 = BigInteger.valueOf(25L);

        // when
        var foundTrainee0 = traineeService.get(traineeId0).orElse(null);
        var foundTrainee51 = traineeService.get(traineeId51).orElse(null);

        // then
        assertThat(foundTrainee0).isNull();
        assertThat(foundTrainee51).isNull();
    }

    @Test
    public void shouldAddTrainee() throws ParseException {
        // given
        var trainee = TraineeDto.builder().address("Address 1").dob(LocalDate.of(2001, 1, 1)).user(userService.get(BigInteger.ONE).orElse(null)).build();

        // when
        var addedTrainee = traineeService.add(trainee);

        // then
        assertThat(addedTrainee).isNotNull();
        trainee.setId(BigInteger.valueOf(25L)); // id is auto-generated
        assertThat(addedTrainee).isEqualTo(trainee);
    }

    @Test
    public void shouldNotAddTrainee() {
        // given
        var trainee = TraineeDto.builder().id(BigInteger.ONE).address("NEW ADDRESS").dob(LocalDate.of(2001, 6, 1)).user(userService.get(BigInteger.valueOf(24L)).orElse(null)).build();

        // when
        var addedTrainee = traineeService.add(trainee);

        // then
        assertThat(addedTrainee).isNotNull();
        assertThat(addedTrainee).isNotEqualTo(trainee);
    }

    @Test
    public void shouldUpdateTrainee() {
        // given
        BigInteger traineeId = BigInteger.ONE;
        var trainee = traineeService.get(traineeId).orElse(null);
        assert trainee != null;

        // when
        trainee.setAddress("UpdatedAddress");
        trainee.setDob(LocalDate.of(2002, 2, 2));
        TraineeDto updatedTrainee = traineeService.update(trainee);

        // then
        assertThat(updatedTrainee).isNotNull();
        assertThat(updatedTrainee.getId()).isEqualTo(traineeId);
        assertThat(updatedTrainee.getAddress()).isEqualTo("UpdatedAddress");
        assertThat(updatedTrainee.getDob()).isEqualTo(LocalDate.of(2002, 2, 2));
        assertThat(traineeService.get(traineeId).orElse(null)).isEqualTo(updatedTrainee);
    }

    @Test
    public void shouldNotUpdateTrainee() {
        // given
        BigInteger traineeId = BigInteger.valueOf(25L);
        var trainee = TraineeDto.builder().id(traineeId).address("Address 1").dob(LocalDate.of(2001, 1, 1)).user(userService.get(BigInteger.ONE).orElse(null)).build();

        // when
        var updatedTrainee = traineeService.update(trainee);

        // then
        assertThat(updatedTrainee).isNull();
    }

    @Test
    public void shouldDeleteTrainee() {
        // given
        BigInteger traineeId = BigInteger.ONE;

        // when
        traineeService.delete(traineeId);

        // then
        assertThat(traineeService.get(traineeId).orElse(null)).isNull();
    }

    @Test
    public void shouldNotDeleteTrainee() {
        // given
        BigInteger traineeId = BigInteger.valueOf(25L);

        // when
        traineeService.delete(traineeId);

        // then
        assertThat(traineeService.get(traineeId).orElse(null)).isNull();
    }

    @Test
    public void shouldGetAllTrainees() {
        // when
        var trainees = traineeService.getAll();

        // then
        assertThat(trainees).isNotNull();
        assertThat(trainees).isNotEmpty();
    }

    @Test
    public void shouldGetAllOnEmptyRepositoryCorrectly() {
        // given
        traineeService.getAll().forEach(t -> traineeService.delete(t.getId())); // deleted all trainees

        // when
        var trainees = traineeService.getAll();

        // then
        assertThat(trainees).isNotNull();
        assertThat(trainees).isEmpty();
    }
}