package com.epam.gym.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nurbek on 03.12.2024
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document("trainerWorkload")
public class TrainerWorkloadEntity {
    @Id
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private Map<YearMonth, Integer> yearlyMonthlySummary;

    @Transient
    private int MIN_SUMMARY = 0;

    public void addTraining(LocalDate date, int duration) {
        YearMonth yearMonth = YearMonth.from(date);

        if (yearlyMonthlySummary == null) {
            yearlyMonthlySummary = new HashMap<>();
            yearlyMonthlySummary.put(yearMonth, duration);
        } else {
            yearlyMonthlySummary.merge(yearMonth, duration, Integer::sum);
        }
    }

    public void removeTraining(LocalDate date, int duration) {
        YearMonth yearMonth = YearMonth.from(date);

        if (yearlyMonthlySummary != null) {
            yearlyMonthlySummary.computeIfPresent(yearMonth, (ym, total) -> Math.max(total - duration, MIN_SUMMARY));
        }
    }
}