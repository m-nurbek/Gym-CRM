package com.epam.gym.service;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.util.DtoEntityCreationUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@WebAppConfiguration
@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TraineeServiceIntegrationTest {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private UserService userService;

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

    @Test
    void shouldSaveTraineeEntity1() {
        // given
        var trainee = DtoEntityCreationUtil.getNewTraineeDtoInstance(1, null, Set.of(), Set.of());
        trainee.setId(null);

        // when
        var savedTrainee = traineeService.save(trainee);

        // then
        assertAll(
                "Assertions for save trainee",
                () -> assertThat(savedTrainee).isNotNull(),
                () -> assertThat(savedTrainee.getId()).isNotNull(),
                () -> assertThat(savedTrainee.getId()).isEqualTo(BigInteger.valueOf(1001))
        );
    }

    @Test
    void shouldSaveTraineeEntity2() {
        // given
        var user = DtoEntityCreationUtil.getNewUserDtoInstance(40);
        var trainee = DtoEntityCreationUtil.getNewTraineeDtoInstance(1, UserEntity.fromDto(user), Set.of(), Set.of());
        trainee.setId(null);

        // when
        var savedTrainee = traineeService.save(trainee);

        // then
        var u = userService.get(BigInteger.valueOf(40)).orElse(null);
        assert u != null;

        assertAll(
                "Assertions for save trainee",
                () -> assertThat(savedTrainee).isNotNull(),
                () -> assertThat(savedTrainee.getId()).isNotNull(),
                () -> assertThat(savedTrainee.getId()).isEqualTo(BigInteger.valueOf(1001)),
                () -> assertThat(savedTrainee.getUser()).isEqualTo(UserEntity.fromDto(u)),
                () -> assertThat(u.getTrainee().toDto().getId()).isEqualTo(savedTrainee.getId()),
                () -> assertThat(u.getTrainee().toDto().getAddress()).isEqualTo(savedTrainee.getAddress())
        );
    }

    @Test
    void shouldCorrectlyGiveManyToManyData() {
        // when
        var trainers = traineeService.getTrainers(BigInteger.ONE);

        // then
        assertAll(
                "Assertions for many-to-many data",
                () -> assertThat(trainers.size()).isEqualTo(1)
        );
    }

    @Test
    void shouldReturnUnassignedTrainers() {
        // given
        var username = "johndoe1";

        // when
        var trainers = traineeService.getUnassignedTrainersByUsername(username);

        // then
        assertAll(
                "Assertions for unassigned trainers",
                () -> assertThat(trainers.size()).isEqualTo(24) // because for user with username "johndoe1" there is only one assigned trainer out of 25
        );
    }
}