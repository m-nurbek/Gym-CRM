package com.epam.gym.hibernate;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static com.epam.gym.util.DtoEntityCreationUtil.getNewTraineeEntityInstance;
import static com.epam.gym.util.DtoEntityCreationUtil.getNewUserEntityInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test2.properties")
public class HibernateRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TraineeRepository traineeRepository;

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 10; i++) {
            var entity = getNewUserEntityInstance(i);
            userRepository.save(entity);
        }
    }

    @Test
    void shouldCorrectlyFindEntity() {
        // when
        Optional<UserEntity> user = userRepository.findById(BigInteger.valueOf(1000 + 2)); // id is generated starting from 1000

        // then
        assertAll(
                "Assert found user entity",
                () -> assertThat(user).isPresent(),
                () -> assertThat(user.get().getId()).isEqualTo(BigInteger.valueOf(1000 + 2)),
                () -> assertThat(user.get().getFirstName()).isEqualTo("Name2")
        );
    }

    @Test
    void shouldNotFindEntity() {
        // when
        Optional<UserEntity> user = userRepository.findById(BigInteger.valueOf(50_000));

        // then
        assertAll(
                "Assert found user entity that does not exist",
                () -> assertThat(user).isEmpty()
        );
    }

    @Test
    void shouldSaveEntity() {
        // given
        var entity11 = getNewUserEntityInstance(11);
        var entity12 = new UserEntity(
                null,
                "Name12", "Surname12",
                "username12", "password12",
                false, null, null);

        var entity13 = new UserEntity(
                BigInteger.valueOf(0),
                "Name13", "Surname13",
                "username13", "password13",
                false, null, null);

        // when
        userRepository.save(entity11);
        userRepository.save(entity12);
        userRepository.save(entity13);

        // then
        assertAll(
                "Assertions for entity11",
                () -> assertThat(entity11.getId()).isNotNull(),
                () -> assertThat(entity11.getId()).isEqualTo(BigInteger.valueOf(1000 + 11)),
                () -> assertThat(entity11.getFirstName()).isEqualTo("Name11")
        );
        assertAll(
                "Assertions for entity12 (edge case: id is null)",
                () -> assertThat(entity12.getId()).isNotNull(),
                () -> assertThat(entity12.getId()).isEqualTo(BigInteger.valueOf(1000 + 12)),
                () -> assertThat(entity12.getFirstName()).isEqualTo("Name12")
        );
        assertAll(
                "Assertions for entity13 (edge case: id is 0)",
                () -> assertThat(entity13.getId()).isNotNull(),
                () -> assertThat(entity13.getId()).isEqualTo(BigInteger.valueOf(1000 + 13)),
                () -> assertThat(entity13.getFirstName()).isEqualTo("Name13")
        );
    }

    @Test
    void shouldNotSaveEntity() {
        // given
        var id = BigInteger.valueOf(1000 + 1);
        var newEntity = new UserEntity(
                id,
                "NewName", "NewSurname",
                "NewUsername", "NewPassword",
                false, null, null);

        // when
        userRepository.save(newEntity);

        // then
        assertAll(
                "Assertions for newEntity (edge case: id is 1 that is present already)",
                () -> assertThat(userRepository.findById(id).get().getId()).isNotNull(),
                () -> assertThat(userRepository.findById(id).get().getFirstName()).isNotEqualTo(newEntity.getFirstName()),
                () -> assertThat(userRepository.count()).isEqualTo(10)
        );
    }

    @Test
    void shouldUpdateEntity() {
        // given
        var updatedName = "Updated FirstName";
        var updatedPassword = "Updated Password";
        Optional<UserEntity> user = userRepository.findById(BigInteger.valueOf(1000 + 2));
        user.ifPresent((entity) -> {
            entity.setFirstName(updatedName);
            entity.setPassword(updatedPassword);
        });

        // when
        final boolean[] response = new boolean[1];
        user.ifPresent((entity) -> response[0] = userRepository.update(entity));

        // then
        var u = userRepository.findById(BigInteger.valueOf(1000 + 2));
        assertAll(
                "Assert updated entity",
                () -> assertThat(response[0]).isTrue(),
                () -> assertThat(u).isPresent(),
                () -> assertThat(u.get().getFirstName()).isEqualTo(updatedName),
                () -> assertThat(u.get().getPassword()).isEqualTo(updatedPassword)
        );
    }

    @Test
    void shouldNotUpdateEntity() {
        // given
        var updatedName = "Updated FirstName";
        var updatedPassword = "Updated Password";
        Optional<UserEntity> user = userRepository.findById(BigInteger.valueOf(1000 + 2));
        user.ifPresent((entity) -> {
            entity.setId(BigInteger.valueOf(500_000)); // entity with this id doesn't exist
            entity.setFirstName(updatedName);
            entity.setPassword(updatedPassword);
        });

        // when
        final boolean[] response = new boolean[1];
        user.ifPresent((entity) -> response[0] = userRepository.update(entity));

        // then
        var u2 = userRepository.findById(BigInteger.valueOf(1000 + 2));
        var u500 = userRepository.findById(BigInteger.valueOf(500_000));
        assertAll(
                "Assert updated entity",
                () -> assertThat(response[0]).isFalse(),
                () -> assertThat(u2.get().getFirstName()).isNotEqualTo(updatedName),
                () -> assertThat(u2.get().getPassword()).isNotEqualTo(updatedPassword),
                () -> assertThat(u500).isEmpty()
        );
    }

    @Test
    void shouldDeleteEntity() {
        // when
        userRepository.deleteById(BigInteger.valueOf(1000 + 2));

        // then
        var u = userRepository.findById(BigInteger.valueOf(1000 + 2));
        var size = userRepository.count();

        assertAll(
                "Assert delete entity",
                () -> assertThat(u).isEmpty(),
                () -> assertThat(size).isEqualTo(9)
        );
    }

    @Test
    void shouldDeleteAll() {
        // when
        userRepository.deleteAll();

        // then
        var u = userRepository.findById(BigInteger.TWO);
        var size = userRepository.count();

        assertAll(
                "Assert deleteAll",
                () -> assertThat(size).isEqualTo(0),
                () -> assertThat(u).isEmpty()
        );
    }

    @Test
    void shouldAddTrainee() {
        // given
        var user1 = userRepository.findById(BigInteger.valueOf(1000 + 1));
        var user2 = userRepository.findById(BigInteger.valueOf(1000 + 2));
        var trainee1 = getNewTraineeEntityInstance(1, user1.get(), List.of());
        var trainee2 = getNewTraineeEntityInstance(2, user2.get(), List.of());

        // when
        traineeRepository.save(trainee1);
        traineeRepository.save(trainee2);

        // then
        var t1 = traineeRepository.findById(BigInteger.valueOf(1000 + 1));
        var t2 = traineeRepository.findById(BigInteger.valueOf(1000 + 2));

        var u1 = userRepository.findById(BigInteger.valueOf(1000 + 1));
        var u2 = userRepository.findById(BigInteger.valueOf(1000 + 2));

        assertAll(
                "Assertions for adding a trainee",
                () -> assertThat(t1).isPresent(),
                () -> assertThat(t2).isPresent(),
                () -> assertThat(t1.get().getAddress()).isEqualTo(trainee1.getAddress()),
                () -> assertThat(t2.get().getAddress()).isEqualTo(trainee2.getAddress()),

                () -> assertThat(u1.get().getTrainee()).isNotNull(),
                () -> assertThat(u2.get().getTrainee()).isNotNull(),
                () -> assertThat(u1.get().getTrainee().getAddress()).isEqualTo(trainee1.getAddress()),
                () -> assertThat(u2.get().getTrainee().getAddress()).isEqualTo(trainee2.getAddress())
        );
    }
}