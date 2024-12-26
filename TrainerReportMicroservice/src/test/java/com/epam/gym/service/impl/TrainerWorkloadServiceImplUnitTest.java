package com.epam.gym.service.impl;

import com.epam.gym.dto.ActionType;
import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.dto.WorkloadDeleteRequest;
import com.epam.gym.entity.TrainerWorkloadEntity;
import com.epam.gym.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Nurbek on 25.12.2024
 */
class TrainerWorkloadServiceImplUnitTest {
    @InjectMocks
    private TrainerWorkloadServiceImpl trainerWorkloadService;
    @Mock
    private TrainerWorkloadRepository trainerWorkloadRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provideWorkloadReportArguments")
    void addWorkloadReport(String username, String firstName, String lastName, boolean isActive, LocalDate date, int duration) {
        // given
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                username,
                firstName,
                lastName,
                isActive,
                date,
                duration,
                ActionType.ADD
        );

        TrainerWorkloadRequest deleteRequest = new TrainerWorkloadRequest(
                username,
                firstName,
                lastName,
                isActive,
                date,
                duration,
                ActionType.DELETE
        );

        TrainerWorkloadEntity trainer = new TrainerWorkloadEntity();
        when(trainerWorkloadRepository.findByUsername(request.username())).thenReturn(Optional.of(trainer));

        // when
        trainerWorkloadService.addWorkloadReport(request);
        trainerWorkloadService.addWorkloadReport(request);
        trainerWorkloadService.addWorkloadReport(request);
        trainerWorkloadService.addWorkloadReport(deleteRequest);

        // then
        verify(trainerWorkloadRepository, times(4)).save(any(TrainerWorkloadEntity.class));
        assertThat(trainer.getYearlyMonthlySummary().get(YearMonth.of(date.getYear(), date.getMonth()))).isEqualTo(duration * 2);
    }

    private static Stream<Arguments> provideWorkloadReportArguments() {
        return Stream.of(
                Arguments.of("trainerUsername1", "trainerFirstName1", "trainerLastName1", true, LocalDate.of(2025, 2, 2), 30),
                Arguments.of("trainerUsername2", "trainerFirstName2", "trainerLastName2", true, LocalDate.of(2025, 2, 2), 50),
                Arguments.of("trainerUsername3", "trainerFirstName3", "trainerLastName3", true, LocalDate.of(2025, 2, 2), 10),
                Arguments.of("trainerUsername4", "trainerFirstName4", "trainerLastName4", true, LocalDate.of(2025, 2, 2), 25)
        );
    }

    @Test
    void deleteWorkloadReport() {
        // given
        WorkloadDeleteRequest request = new WorkloadDeleteRequest(
                List.of("trainerUsername1", "trainerUsername2")
        );

        // when
        trainerWorkloadService.deleteWorkloadReport(request);

        // then
        verify(trainerWorkloadRepository, times(1)).deleteByUsernameIn(request.trainerUsernames());
    }
}