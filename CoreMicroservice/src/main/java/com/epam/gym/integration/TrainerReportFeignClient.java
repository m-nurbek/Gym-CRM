package com.epam.gym.integration;

import com.epam.gym.integration.dto.TrainerWorkloadRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Nurbek on 03.12.2024
 */
@FeignClient(name = "TRAINERREPORTMICROSERVICE", path = "/api/v1/trainers", configuration = FeignConfig.class)
public interface TrainerReportFeignClient {

    @PostMapping("/workload")
    void handleTrainerWorkload(@Valid @RequestBody TrainerWorkloadRequest request);
}