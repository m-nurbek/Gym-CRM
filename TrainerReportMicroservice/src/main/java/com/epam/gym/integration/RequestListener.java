package com.epam.gym.integration;

import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.dto.WorkloadDeleteRequest;
import com.epam.gym.service.TrainerWorkloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author Nurbek on 12.12.2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RequestListener {
    private final TrainerWorkloadService trainerWorkloadService;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    @RabbitListener(queues = "addReportQueue")
    public void handleAddRequest(Message message) {
        Span newSpan = tracer.nextSpan().name("handleAddRequest").start();
        try (var ws = tracer.withSpan(newSpan)) {
            String request = retrieveRequest(message);
            trainerWorkloadService.addWorkloadReport(processTrainerWorkloadRequest(request));
        } finally {
            newSpan.end();
        }
    }

    @RabbitListener(queues = "deleteReportQueue")
    public void handleDeleteRequest(Message message) {
        Span newSpan = tracer.nextSpan().name("handleDeleteRequest").start();
        try (var ws = tracer.withSpan(newSpan)) {
            String request = retrieveRequest(message);
            trainerWorkloadService.deleteWorkloadReport(processWorkloadDeleteRequest(request));
        } finally {
            newSpan.end();
        }
    }

    private String retrieveRequest(Message message) {
        String request = new String(message.getBody());
        log.debug("Received request: {}", request);
        return request;
    }

    private TrainerWorkloadRequest processTrainerWorkloadRequest(String request) {
        try {
            log.debug("Processing request {}", request);

            // Deserializing the request
            var workloadRequest = objectMapper.readValue(request, TrainerWorkloadRequest.class);
            log.debug("Deserialized the request: {}", workloadRequest);
            return workloadRequest;

        } catch (Exception ex) {
            log.error("Failed to process request with exception: {}", ex.getMessage());
            throw new RuntimeException("Failed to process request", ex);
        }
    }

    private WorkloadDeleteRequest processWorkloadDeleteRequest(String request) {
        try {
            log.debug("Processing request {}", request);

            // Deserializing the request
            var workloadRequest = objectMapper.readValue(request, WorkloadDeleteRequest.class);
            log.debug("Deserialized the request: {}", workloadRequest);
            return workloadRequest;

        } catch (Exception ex) {
            log.error("Failed to process request with exception: {}", ex.getMessage());
            throw new RuntimeException("Failed to process request", ex);
        }
    }
}