package com.epam.gym.integration;

import com.epam.gym.service.TrainerWorkloadService;
import com.epam.gym.util.MessageConverter;
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
        Span span = MessageConverter.retrieveCurrentSpan(tracer, message, "addReportConsumer");
        try (var ws = tracer.withSpan(span)) {
            String request = MessageConverter.retrieveRequest(message);
            trainerWorkloadService.addWorkloadReport(MessageConverter.processTrainerWorkloadRequest(objectMapper, request));
        } finally {
            span.end();
        }
    }

    @RabbitListener(queues = "deleteReportQueue")
    public void handleDeleteRequest(Message message) {
        Span span = MessageConverter.retrieveCurrentSpan(tracer, message, "deleteReportConsumer");
        try (var ws = tracer.withSpan(span)) {
            String request = MessageConverter.retrieveRequest(message);
            trainerWorkloadService.deleteWorkloadReport(MessageConverter.processWorkloadDeleteRequest(objectMapper, request));
        } finally {
            span.end();
        }
    }


}