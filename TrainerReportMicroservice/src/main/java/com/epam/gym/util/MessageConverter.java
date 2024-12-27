package com.epam.gym.util;

import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.dto.WorkloadDeleteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

/**
 * @author Nurbek on 27.12.2024
 */
@Slf4j
@UtilityClass
public class MessageConverter {
    public static Span retrieveCurrentSpan(Tracer tracer, Message message, String spanName) {
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

    public static String retrieveRequest(Message message) {
        String request = new String(message.getBody());
        log.debug("Received request: {}", request);
        return request;
    }

    public static TrainerWorkloadRequest processTrainerWorkloadRequest(ObjectMapper objectMapper, String request) {
        try {
            log.debug("Processing request {}", request);

            // Deserializing the request
            var workloadRequest = objectMapper.readValue(request, TrainerWorkloadRequest.class);
            log.debug("Deserialized the request: {}", workloadRequest);
            return workloadRequest;

        } catch (Exception ex) {
            log.error("Failed to process request with exception: {}", ex.getMessage());
            throw new RuntimeException("Failed to process request with exception", ex);
        }
    }

    public WorkloadDeleteRequest processWorkloadDeleteRequest(ObjectMapper objectMapper, String request) {
        try {
            log.debug("Processing request {}", request);

            // Deserializing the request
            var workloadRequest = objectMapper.readValue(request, WorkloadDeleteRequest.class);
            log.debug("Deserialized the request: {}", workloadRequest);
            return workloadRequest;

        } catch (Exception ex) {
            log.error("Failed to process request with exception: {}", ex.getMessage());
            throw new RuntimeException("Failed to process request with exception", ex);
        }
    }
}