package com.epam.gym.integration;

import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.dto.WorkloadDeleteRequest;
import com.epam.gym.service.TrainerWorkloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
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
        Span span = retrieveCurrentSpan(message, "addReportConsumer");
        try (var ws = tracer.withSpan(span)) {
            String request = retrieveRequest(message);
            trainerWorkloadService.addWorkloadReport(processTrainerWorkloadRequest(request));
        } finally {
            span.end();
        }
    }

    @RabbitListener(queues = "deleteReportQueue")
    public void handleDeleteRequest(Message message) {
        Span span = retrieveCurrentSpan(message, "deleteReportConsumer");
        try (var ws = tracer.withSpan(span)) {
            String request = retrieveRequest(message);
            trainerWorkloadService.deleteWorkloadReport(processWorkloadDeleteRequest(request));
        } finally {
            span.end();
        }
    }

    private Span retrieveCurrentSpan(Message message, String spanName) {
        String traceId = message.getMessageProperties().getHeader("X-B3-TraceId");
        String parentId = message.getMessageProperties().getHeader("X-B3-SpanId");
        String isSampled = message.getMessageProperties().getHeader("X-B3-Sampled");
        String spanId = tracer.nextSpan().context().spanId();

        Span span;
        if (traceId == null || parentId == null || isSampled == null) {
            span = tracer.nextSpan().name(spanName).start();
        } else {
            TraceContext traceContextParent = tracer.traceContextBuilder()
                    .sampled("1".equals(isSampled))
                    .traceId(traceId)
                    .spanId(spanId)
                    .parentId(parentId)
                    .build();
            span = tracer.spanBuilder().name(spanName).setParent(traceContextParent).start();
        }

        return span;
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