package com.epam.gym.hibernate;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.repository.hibernate.TraineeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateRepositoryTest {
    @Autowired
    private TraineeRepository traineeRepository;

    @Test
    void shouldSaveEntity() {
        TraineeEntity entity = new TraineeEntity(null, Date.valueOf("2001-02-02"), "ADDRESS", BigInteger.ZERO);
        var e = traineeRepository.save(entity);

        assertThat(entity.getId()).isNotNull();
    }
}