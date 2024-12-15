package com.epam.gym.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nurbek on 11.12.2024
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {
    private final String trainerWorkloadQueue = "addReportQueue";
    private final String trainerWorkloadRoutingKey = "add.report.key.#";

    private final String deleteWorkloadQueue = "deleteReportQueue";
    private final String deleteWorkloadRoutingKey = "delete.report.key.#";

    @Bean
    public Exchange exchange() {
        return new TopicExchange("workloadReportExchange", false, false);
    }

    // Queue Beans
    @Bean
    public Queue trainerWorkloadQueue() {
        return new Queue(trainerWorkloadQueue, false);
    }

    @Bean
    public Queue deleteWorkloadQueue() {
        return new Queue(deleteWorkloadQueue, false);
    }

    @Bean
    public Binding trainerWorkloadBinding(Queue trainerWorkloadQueue, Exchange exchange) {
        return BindingBuilder
                .bind(trainerWorkloadQueue)
                .to(exchange)
                .with(trainerWorkloadRoutingKey)
                .noargs();
    }

    @Bean
    public Binding deleteWorkloadBinding(Queue deleteWorkloadQueue, Exchange exchange) {
        return BindingBuilder
                .bind(deleteWorkloadQueue)
                .to(exchange)
                .with(deleteWorkloadRoutingKey)
                .noargs();
    }
}