package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RepositoryTest {
    private final TraineeRepository traineeRepository;

    @Autowired
    public RepositoryTest(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @BeforeEach
    void setUp() {
        traineeRepository.deleteAll();
        List<TraineeEntity> traineeList = new ArrayList<>();

        traineeList.add(new TraineeEntity(null, LocalDate.of(2001, 1, 1), "Malheim Avenue 1E", BigInteger.valueOf(10L))); // id = 1
        traineeList.add(new TraineeEntity(null, LocalDate.of(1995, 5, 15), "Baker Street 221B", BigInteger.valueOf(9L))); // id = 2
        traineeList.add(new TraineeEntity(null, LocalDate.of(1998, 11, 23), "Elm Street 13", BigInteger.valueOf(8L))); // id = 3
        traineeList.add(new TraineeEntity(null, LocalDate.of(2000, 7, 7), "Sunset Boulevard 45", BigInteger.valueOf(7L))); // id = 4
        traineeList.add(new TraineeEntity(null, LocalDate.of(2003, 3, 3), "Oak Avenue 9", BigInteger.valueOf(6L))); // id = 5
        traineeList.add(new TraineeEntity(null, LocalDate.of(1999, 9, 9), "Maple Lane 17A", BigInteger.valueOf(5L))); // id = 6
        traineeList.add(new TraineeEntity(null, LocalDate.of(2002, 12, 12), "Pine Street 22", BigInteger.valueOf(4L))); // id = 7
        traineeList.add(new TraineeEntity(null, LocalDate.of(1997, 4, 4), "Cedar Road 5B", BigInteger.valueOf(3L))); // id = 8
        traineeList.add(new TraineeEntity(null, LocalDate.of(2001, 8, 8), "Birch Avenue 33C", BigInteger.valueOf(2L))); // id = 9
        traineeList.add(new TraineeEntity(null, LocalDate.of(2004, 2, 28), "Willow Drive 12A", BigInteger.valueOf(1L))); // id = 10

        traineeRepository.saveAll(traineeList);
    }

    @Test
    public void shouldFindCorrectEntity() {
        // given
        TraineeEntity trainee1 = new TraineeEntity(BigInteger.valueOf(4L), LocalDate.of(2000, 7, 7), "Sunset Boulevard 45", BigInteger.valueOf(7L));
        TraineeEntity trainee2 = new TraineeEntity(BigInteger.valueOf(5L), LocalDate.of(2003, 3, 3), "Oak Avenue 9", BigInteger.valueOf(6L));

        // when
        TraineeEntity foundTrainee1 = traineeRepository.findById(BigInteger.valueOf(4L)).orElse(null);
        TraineeEntity foundTrainee2 = traineeRepository.findById(BigInteger.valueOf(5L)).orElse(null);

        // then
        assertThat(foundTrainee1).isNotNull();
        assertThat(foundTrainee2).isNotNull();
        assertThat(foundTrainee1.getId()).isEqualTo(4L);
        assertThat(foundTrainee2.getId()).isEqualTo(5L);
        assertThat(foundTrainee1).isEqualTo(trainee1);
        assertThat(foundTrainee2).isEqualTo(trainee2);
    }

    @Test
    public void shouldNotFindEntity() {
        // when
        TraineeEntity foundTrainee = traineeRepository.findById(BigInteger.valueOf(11L)).orElse(null);

        // then
        assertThat(foundTrainee).isNull();
    }

    @Test
    void shouldDeleteEntity() {
        // given
        List<TraineeEntity> expectedResult = new ArrayList<>();

        expectedResult.add(new TraineeEntity(BigInteger.valueOf(1L), LocalDate.of(2001, 1, 1), "Malheim Avenue 1E", BigInteger.valueOf(10L))); // id = 1
        expectedResult.add(new TraineeEntity(BigInteger.valueOf(2L), LocalDate.of(1995, 5, 15), "Baker Street 221B", BigInteger.valueOf(9L))); // id = 2
        expectedResult.add(new TraineeEntity(BigInteger.valueOf(6L), LocalDate.of(1999, 9, 9), "Maple Lane 17A", BigInteger.valueOf(5L))); // id = 6
        expectedResult.add(new TraineeEntity(BigInteger.valueOf(7L), LocalDate.of(2002, 12, 12), "Pine Street 22", BigInteger.valueOf(4L))); // id = 7
        expectedResult.add(new TraineeEntity(BigInteger.valueOf(8L), LocalDate.of(1997, 4, 4), "Cedar Road 5B", BigInteger.valueOf(3L))); // id = 8
        expectedResult.add(new TraineeEntity(BigInteger.valueOf(10L), LocalDate.of(2004, 2, 28), "Willow Drive 12A", BigInteger.valueOf(1L))); // id = 10

        // when
        traineeRepository.deleteById(BigInteger.valueOf(3L));
        traineeRepository.deleteById(BigInteger.valueOf(4L));
        traineeRepository.deleteById(BigInteger.valueOf(5L));
        traineeRepository.deleteById(BigInteger.valueOf(9L));

        traineeRepository.deleteById(BigInteger.valueOf(11L)); // does not exist in the repository
        traineeRepository.deleteById(BigInteger.valueOf(0L)); // does not exist in the repository

        // then
        assertThat(traineeRepository.findById(BigInteger.valueOf(3L))).isEmpty();
        assertThat(traineeRepository.findById(BigInteger.valueOf(4L))).isEmpty();
        assertThat(traineeRepository.findById(BigInteger.valueOf(5L))).isEmpty();
        assertThat(traineeRepository.findById(BigInteger.valueOf(9L))).isEmpty();
        assertThat(traineeRepository.findAll()).containsExactlyInAnyOrderElementsOf(expectedResult);
    }

    @Test
    public void shouldSaveEntity() {
        // given
        TraineeEntity trainee = new TraineeEntity(BigInteger.valueOf(11L), LocalDate.of(2005, 05, 05), "Palm Street 11", BigInteger.valueOf(1L)); // new entity with id = 11

        // when
        traineeRepository.save(trainee);

        // then
        assertThat(traineeRepository.findById(BigInteger.valueOf(11L))).isNotEmpty();
        assertThat(traineeRepository.findById(BigInteger.valueOf(11L)).get()).isEqualTo(trainee);
    }

    @Test
    public void shouldNotUpdateEntityWhenSaving() {
        // given
        // original in repository: TraineeEntity(BigInteger.valueOf(4L), Date.valueOf("2000-07-07"), "Sunset Boulevard 45", BigInteger.valueOf(7L)));
        TraineeEntity trainee = new TraineeEntity(BigInteger.valueOf(4L), LocalDate.of(2020, 9, 10), "Sunset 50", BigInteger.valueOf(7L));

        // when
        traineeRepository.save(trainee);

        // then
        var originalTrainee = traineeRepository.findById(BigInteger.valueOf(4L));

        assertThat(originalTrainee).isPresent();
        assertThat(originalTrainee).isNotEmpty();
        assertThat(traineeRepository.findById(BigInteger.valueOf(11L))).isEmpty();
        assertThat(originalTrainee.get().getAddress()).isNotEqualTo(trainee.getAddress());
    }

    @Test
    public void shouldEmptyRepository() {
        // when
        traineeRepository.deleteAll();

        // then
        assertThat(traineeRepository.findAll()).isEmpty();
    }

    @Test
    public void shouldUpdateEntity() {
        // given
        // original in repository: TraineeEntity(BigInteger.valueOf(4L), Date.valueOf("2000-07-07"), "Sunset Boulevard 45", BigInteger.valueOf(7L)));
        TraineeEntity updatedTrainee = new TraineeEntity(BigInteger.valueOf(4L), LocalDate.of(2020, 7, 7), "Sunset 50", BigInteger.valueOf(7L));

        // when
        traineeRepository.update(BigInteger.valueOf(4L), updatedTrainee);

        // then
        assertThat(traineeRepository.findById(BigInteger.valueOf(4L))).isNotEmpty();
        assertThat(traineeRepository.findById(BigInteger.valueOf(4L)).get()).isEqualTo(updatedTrainee);
    }

    @Test
    public void shouldNotAddEntityWhenUpdating() {
        // given
        TraineeEntity updatedTrainee = new TraineeEntity(BigInteger.valueOf(11L), LocalDate.of(2000, 7, 7), "Sunset Boulevard 45", BigInteger.valueOf(4L));

        // when
        traineeRepository.update(BigInteger.valueOf(11L), updatedTrainee);

        // then
        assertThat(traineeRepository.findById(BigInteger.valueOf(11L))).isEmpty();
    }

    @Test
    public void shouldReturnCorrectSize() {
        // given
        TraineeEntity trainee = new TraineeEntity(BigInteger.valueOf(11L), LocalDate.of(2005, 5, 5), "Palm Street 11", BigInteger.valueOf(2L));

        // when
        traineeRepository.save(trainee);
        long size = traineeRepository.count();

        // then
        assertThat(size).isEqualTo(11);
    }

    @Test
    public void shouldCorrectlyCheckExistenceOfEntities() {
        // then
        Stream.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)
                .map(BigInteger::valueOf)
                .forEach(id -> assertThat(traineeRepository.existsById(id)).isTrue());
        Stream.of(0L, 11L, 12L, 14L, 15L, 16L, 17L, 18L, 19L, 20L)
                .map(BigInteger::valueOf)
                .forEach(id -> assertThat(traineeRepository.existsById(id)).isFalse());
    }

    @Test
    public void shouldFindAllByIds() {
        // given
        List<BigInteger> ids = Stream.of(1L, 3L, 5L, 7L, 9L, 11L, 20L, 21L, 22L, 33L, 44L)
                .map(BigInteger::valueOf).toList();

        // when
        List<TraineeEntity> trainees = (List<TraineeEntity>) traineeRepository.findAllById(ids);

        // then
        assertThat(trainees).isNotEmpty();
        assertThat(trainees).containsExactlyInAnyOrder(
                new TraineeEntity(BigInteger.valueOf(1L), LocalDate.of(2001, 1, 1), "Malheim Avenue 1E", BigInteger.valueOf(10L)),
                new TraineeEntity(BigInteger.valueOf(3L), LocalDate.of(1998, 11, 23), "Elm Street 13", BigInteger.valueOf(8L)),
                new TraineeEntity(BigInteger.valueOf(5L), LocalDate.of(2003, 3, 3), "Oak Avenue 9", BigInteger.valueOf(6L)),
                new TraineeEntity(BigInteger.valueOf(7L), LocalDate.of(2002, 12, 12), "Pine Street 22", BigInteger.valueOf(4L)),
                new TraineeEntity(BigInteger.valueOf(9L), LocalDate.of(2001, 8, 8), "Birch Avenue 33C", BigInteger.valueOf(2L))
        );
    }
}