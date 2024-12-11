package com.epam.gym.integration;

import com.epam.gym.integration.dto.TrainerWorkloadRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Nurbek on 03.12.2024
 */
@FeignClient(name = "TRAINERREPORTMICROSERVICE", path = "/api/v1/trainers", configuration = FeignConfig.class)
public interface TrainerReportFeignClient {

    Logger log = LoggerFactory.getLogger(TrainerReportFeignClient.class);

    @PostMapping("/workload")
    @CircuitBreaker(name = "CircuitBreakerConfig1", fallbackMethod = "fallbackHandleTrainerWorkload")
    void handleTrainerWorkload(@Valid @RequestBody TrainerWorkloadRequest request);

    default void fallbackHandleTrainerWorkload(String request, Throwable throwable) {
        log.error(MarkerFactory.getMarker("CIRCUIT BREAKER FALLBACK"),
                "Fallback method is triggered. Request: {}\nERROR: {}", request, throwable.getMessage());
    }
}