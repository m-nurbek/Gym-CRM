package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.entity.TraineeEntity;
import com.epam.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Long, TraineeEntity> traineeMap = new HashMap<>();

        traineeMap.put(1L, new TraineeEntity(1L, Date.valueOf("2001-01-01"), "Malheim Avenue 1E", 4L));
        traineeMap.put(2L, new TraineeEntity(2L, Date.valueOf("1995-05-15"), "Baker Street 221B", 7L));
        traineeMap.put(3L, new TraineeEntity(3L, Date.valueOf("1998-11-23"), "Elm Street 13", 2L));
        traineeMap.put(4L, new TraineeEntity(4L, Date.valueOf("2000-07-07"), "Sunset Boulevard 45", 5L));
        traineeMap.put(5L, new TraineeEntity(5L, Date.valueOf("2003-03-03"), "Oak Avenue 9", 6L));
        traineeMap.put(6L, new TraineeEntity(6L, Date.valueOf("1999-09-09"), "Maple Lane 17A", 3L));
        traineeMap.put(7L, new TraineeEntity(7L, Date.valueOf("2002-12-12"), "Pine Street 22", 8L));
        traineeMap.put(8L, new TraineeEntity(8L, Date.valueOf("1997-04-04"), "Cedar Road 5B", 9L));
        traineeMap.put(9L, new TraineeEntity(9L, Date.valueOf("2001-08-08"), "Birch Avenue 33C", 10L));
        traineeMap.put(10L, new TraineeEntity(10L, Date.valueOf("2004-02-28"), "Willow Drive 12A", 1L));

        traineeRepository.saveAll(traineeMap);
    }

    @Test
    public void shouldFindCorrectEntity() {
        // given
        TraineeEntity trainee = new TraineeEntity(4L, Date.valueOf("2000-07-07"), "Sunset Boulevard 45", 5L);

        // when
        TraineeEntity foundTrainee = traineeRepository.findById(4L).orElse(null);

        // then
        assertThat(foundTrainee).isNotNull();
        assertThat(foundTrainee.getId()).isEqualTo(4L);
        assertThat(foundTrainee).isEqualTo(trainee);
    }

    @Test void shouldDeleteEntity() {
        // given
        Map<Long, TraineeEntity> expectedResult = new HashMap<>();

        expectedResult.put(1L, new TraineeEntity(1L, Date.valueOf("2001-01-01"), "Malheim Avenue 1E", 4L));
        expectedResult.put(2L, new TraineeEntity(2L, Date.valueOf("1995-05-15"), "Baker Street 221B", 7L));
        expectedResult.put(6L, new TraineeEntity(6L, Date.valueOf("1999-09-09"), "Maple Lane 17A", 3L));
        expectedResult.put(7L, new TraineeEntity(7L, Date.valueOf("2002-12-12"), "Pine Street 22", 8L));
        expectedResult.put(8L, new TraineeEntity(8L, Date.valueOf("1997-04-04"), "Cedar Road 5B", 9L));
        expectedResult.put(10L, new TraineeEntity(10L, Date.valueOf("2004-02-28"), "Willow Drive 12A", 1L));


        // when
        traineeRepository.deleteById(3L);
        traineeRepository.deleteById(4L);
        traineeRepository.deleteById(5L);
        traineeRepository.deleteById(9L);

        // then
        assertThat(traineeRepository.findById(3L)).isEmpty();
        assertThat(traineeRepository.findById(4L)).isEmpty();
        assertThat(traineeRepository.findById(5L)).isEmpty();
        assertThat(traineeRepository.findById(9L)).isEmpty();
        assertThat(traineeRepository.findAll()).containsExactlyInAnyOrderElementsOf(expectedResult.values());
    }

    @Test
    public void shouldSaveEntity() {
        // given
        TraineeEntity trainee = new TraineeEntity(11L, Date.valueOf("2005-05-05"), "Palm Street 11", 2L);

        // when
        traineeRepository.save(11L, trainee);

        // then
        assertThat(traineeRepository.findById(11L)).isNotEmpty();
        assertThat(traineeRepository.findById(11L).get()).isEqualTo(trainee);
    }

    @Test
    public void shouldNotUpdateEntityWhenSaving() {
        // given
        // data in repository: TraineeEntity(4L, Date.valueOf("2000-07-07"), "Sunset Boulevard 45", 5L));
        TraineeEntity trainee = new TraineeEntity(4L, Date.valueOf("2020-09-10"), "Sunset 50", 5L);

        // when
        traineeRepository.save(4L, trainee);

        // then
        assertThat(traineeRepository.findById(4L)).isNotEmpty();
        assertThat(traineeRepository.findById(4L).get()).isNotEqualTo(trainee);
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
        // data in repository: TraineeEntity(4L, Date.valueOf("2000-07-07"), "Sunset Boulevard 45", 5L));
        TraineeEntity updatedTrainee = new TraineeEntity(4L, Date.valueOf("2000-07-07"), "Sunset Boulevard 45", 6L);

        // when
        traineeRepository.update(4L, updatedTrainee);

        // then
        assertThat(traineeRepository.findById(4L)).isNotEmpty();
        assertThat(traineeRepository.findById(4L).get()).isEqualTo(updatedTrainee);
    }

    @Test
    public void shouldNotAddEntityWhenUpdating() {
        // given
        TraineeEntity updatedTrainee = new TraineeEntity(11L, Date.valueOf("2000-07-07"), "Sunset Boulevard 45", 6L);

        // when
        traineeRepository.update(11L, updatedTrainee);

        // then
        assertThat(traineeRepository.findById(11L)).isEmpty();
    }

    @Test
    public void shouldReturnCorrectSize() {
        // given
        TraineeEntity trainee = new TraineeEntity(11L, Date.valueOf("2005-05-05"), "Palm Street 11", 2L);

        // when
        traineeRepository.save(11L, trainee);
        long size = traineeRepository.count();

        // then
        assertThat(size).isEqualTo(11);
    }

    @Test
    public void shouldCorrectlyCheckExistanceOfEntities() {
        // then
        List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)
                .forEach(id -> assertThat(traineeRepository.existsById(id)).isTrue());
        List.of(0L, 11L, 12L, 14L, 15L, 16L, 17L, 18L, 19L, 20L)
                .forEach(id -> assertThat(traineeRepository.existsById(id)).isFalse());
    }

    @Test
    public void shouldFindAllByIds() {
        // given
        List<Long> ids = List.of(1L, 3L, 5L, 7L, 9L, 11L, 20L, 21L, 22L, 33L, 44L);

        // when
        List<TraineeEntity> trainees = (List<TraineeEntity>) traineeRepository.findAllById(ids);

        // then
        assertThat(trainees).containsExactlyInAnyOrder(
                new TraineeEntity(1L, Date.valueOf("2001-01-01"), "Malheim Avenue 1E", 4L),
                new TraineeEntity(3L, Date.valueOf("1998-11-23"), "Elm Street 13", 2L),
                new TraineeEntity(5L, Date.valueOf("2003-03-03"), "Oak Avenue 9", 6L),
                new TraineeEntity(7L, Date.valueOf("2002-12-12"), "Pine Street 22", 8L),
                new TraineeEntity(9L, Date.valueOf("2001-08-08"), "Birch Avenue 33C", 10L)
        );
    }
}