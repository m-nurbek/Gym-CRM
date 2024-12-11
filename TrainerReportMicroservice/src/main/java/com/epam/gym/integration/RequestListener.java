package com.epam.gym.integration;

import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author Nurbek on 12.12.2024
 */
@Service
@Slf4j
@AllArgsConstructor
public class RequestListener {
    private final TrainerService trainerService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "firstQueue")
    public void handleRequest(String request) {
        try {
            log.debug("Processing request {}", request);

            // Deserializing the request
            TrainerWorkloadRequest workloadRequest = objectMapper.readValue(request, TrainerWorkloadRequest.class);
            log.debug("Deserialized the request: {}", workloadRequest);

            trainerService.processWorkload(workloadRequest);

        } catch (Exception ex) {
            log.error("Failed to process request with exception: {}", ex.getMessage());
        }
    }
}