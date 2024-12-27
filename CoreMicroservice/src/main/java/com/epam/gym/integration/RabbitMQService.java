package com.epam.gym.integration;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.integration.dto.TrainerWorkloadRequest;
import com.epam.gym.integration.dto.WorkloadDeleteRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Nurbek on 11.12.2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    private final String EXCHANGE_NAME = "workloadReportExchange";
    private final String ADD_REPORT_ROUTING_KEY = "add.report.key";
    private final String DELETE_REPORT_ROUTING_KEY = "delete.report.key";

    @Async("tracingAsyncExecutor")
    public void sendReportRequest(TrainerWorkloadRequest request) {
        Span span = tracer.nextSpan().name("RabbitMQ message").start();
        try (var ws = tracer.withSpan(span)) {
            String jsonRequest = objectMapper.writeValueAsString(request);
            sendReport(jsonRequest, EXCHANGE_NAME, ADD_REPORT_ROUTING_KEY);

            span.tag("exchange", EXCHANGE_NAME);
            span.tag("routingKey", ADD_REPORT_ROUTING_KEY);
        } catch (JsonProcessingException ex) {
            log.error("Failed to process json");
            throw new BadRequestException("Failed to process json");
        } finally {
            span.end();
        }
    }

    @Async("tracingAsyncExecutor")
    public void sendDeleteReportRequest(WorkloadDeleteRequest request) {
        Span span = tracer.nextSpan().name("RabbitMQ message").start();
        try (var ws = tracer.withSpan(span)) {
            String jsonRequest = objectMapper.writeValueAsString(request);
            sendReport(jsonRequest, EXCHANGE_NAME, DELETE_REPORT_ROUTING_KEY);

            span.tag("exchange", EXCHANGE_NAME);
            span.tag("routingKey", DELETE_REPORT_ROUTING_KEY);
        } catch (JsonProcessingException ex) {
            log.error("Failed to process json");
            throw new BadRequestException("Failed to process json");
        } finally {
            span.end();
        }
    }

    private void sendReport(String jsonRequest, String exchange, String routingKey) {
        try {
            log.debug("Sending request to RabbitMQ {}", jsonRequest);
            rabbitTemplate.convertSendAndReceive(exchange, routingKey, jsonRequest, getMessagePostProcessor());
            log.debug("Request is sent to RabbitMQ");
        } catch (Exception ex) {
            throw new BadRequestException("Error communicating with RabbitMQ: " + ex.getMessage());
        }
    }

    private MessagePostProcessor getMessagePostProcessor() {
        return message -> {
            MessageProperties messageProperties = message.getMessageProperties();

            log.debug("Trying to retrieve traceId and spanId from currentTraceContext to set them as message headers");
            Optional.ofNullable(tracer.currentTraceContext().context()).ifPresent(traceContext -> {
                String traceId = traceContext.traceId();
                String spanId = traceContext.spanId();
                boolean isSampled = traceContext.sampled();
                messageProperties.setHeader("X-B3-TraceId", traceId);
                messageProperties.setHeader("X-B3-SpanId", spanId);
                messageProperties.setHeader("X-B3-Sampled", isSampled ? "1" : "0");

                log.debug("Setting traceId: {}, spanId: {} and isSampled: {} as message headers", traceId, spanId, isSampled);
            });

            return message;
        };
    }
}