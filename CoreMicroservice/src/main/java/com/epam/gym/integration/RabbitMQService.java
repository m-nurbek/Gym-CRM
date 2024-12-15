package com.epam.gym.integration;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.integration.dto.TrainerWorkloadRequest;
import com.epam.gym.integration.dto.WorkloadDeleteRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Async
    public void sendReportRequest(TrainerWorkloadRequest request) {
        try {
            String jsonRequest = objectMapper.writeValueAsString(request);
            sendReport(jsonRequest, EXCHANGE_NAME, ADD_REPORT_ROUTING_KEY);
        } catch (JsonProcessingException ex) {
            log.error("Failed to process json");
            throw new BadRequestException("Failed to process json");
        }
    }

    @Async
    public void sendDeleteReportRequest(WorkloadDeleteRequest request) {
        try {
            String jsonRequest = objectMapper.writeValueAsString(request);
            sendReport(jsonRequest, EXCHANGE_NAME, DELETE_REPORT_ROUTING_KEY);
        } catch (JsonProcessingException ex) {
            log.error("Failed to process json");
            throw new BadRequestException("Failed to process json");
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

            Optional.ofNullable(tracer.currentTraceContext().context()).ifPresent(traceContext -> {
                messageProperties.setHeader("traceId", traceContext.traceId());
                messageProperties.setHeader("spanId", traceContext.spanId());
            });

            return message;
        };
    }
}