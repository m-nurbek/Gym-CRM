package com.epam.gym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nurbek on 03.12.2024
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadEntity {
    @Id
    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;
    private boolean isActive;

    @ElementCollection
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