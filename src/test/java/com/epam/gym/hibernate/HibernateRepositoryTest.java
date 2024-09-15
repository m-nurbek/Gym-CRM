package com.epam.gym.hibernate;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.hibernate.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;

import static com.epam.gym.util.DtoEntityCreationUtil.getNewUserEntityInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveEntity() {
        // given
        var entity1 = getNewUserEntityInstance(1);
        var entity2 = getNewUserEntityInstance(2);
        var entity3 = new UserEntity(
                null,
                "Name3", "Surname3",
                "username3", "password3",
                false);

        var entity4 = new UserEntity(
                BigInteger.valueOf(0),
                "Name4", "Surname4",
                "username4", "password4",
                false);

        // when
        userRepository.save(entity1);
        userRepository.save(entity2);
        userRepository.save(entity3);
        userRepository.save(entity4);

        // then
        assertAll(
                "Assertions for entity1",
                () -> assertThat(entity1.getId()).isNotNull(),
                () -> assertThat(entity1.getId()).isEqualTo(BigInteger.valueOf(1)),
                () -> assertThat(entity1.getFirstName()).isEqualTo("Name1")
        );
        assertAll(
                "Assertions for entity2",
                () -> assertThat(entity2.getId()).isNotNull(),
                () -> assertThat(entity2.getId()).isEqualTo(BigInteger.valueOf(2)),
                () -> assertThat(entity2.getFirstName()).isEqualTo("Name2")
        );
        assertAll(
                "Assertions for entity3 (edge case: id is null)",
                () -> assertThat(entity3.getId()).isNotNull(),
                () -> assertThat(entity3.getId()).isEqualTo(BigInteger.valueOf(3)),
                () -> assertThat(entity3.getFirstName()).isEqualTo("Name3")
        );
        assertAll(
                "Assertions for entity4 (edge case: id is 0)",
                () -> assertThat(entity4.getId()).isNotNull(),
                () -> assertThat(entity4.getId()).isEqualTo(BigInteger.valueOf(4)),
                () -> assertThat(entity4.getFirstName()).isEqualTo("Name4")
        );
    }

    @Test
    void shouldNotSaveEntity() {
        // given
        var entity1 = getNewUserEntityInstance(1);
        var id = BigInteger.valueOf(1);
        var entity5 = new UserEntity(
                id,
                "Name5", "Surname5",
                "username5", "password5",
                false);

        // when
        userRepository.save(entity1);
        userRepository.save(entity5);

        assertAll(
                "Assertions for entity5 (edge case: id is 1 that is present already)",
                () -> assertThat(userRepository.findById(id).get().getId()).isNotNull(),
                () -> assertThat(userRepository.findById(id).get().getFirstName()).isNotEqualTo(entity5.getFirstName()),
                () -> assertThat(userRepository.count()).isEqualTo(1)
        );
    }
}