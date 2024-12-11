package com.epam.gym.integration;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.integration.dto.TrainerWorkloadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Nurbek on 11.12.2024
 */
@Service
@Slf4j
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${rabbitmq.exchange-name}")
    private String exchangeName;
    private static final String routingKey = "routing.key";

    public RabbitMQService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Async
    public void sendReportRequest(TrainerWorkloadRequest request) {
        try {
            String jsonRequest = objectMapper.writeValueAsString(request);
            log.debug("Sending request to RabbitMQ {}", jsonRequest);
            rabbitTemplate.convertSendAndReceive(exchangeName, routingKey, jsonRequest);
        } catch (Exception ex) {
            throw new BadRequestException("Error communicating with RabbitMQ: " + ex.getMessage());
        }
    }
}