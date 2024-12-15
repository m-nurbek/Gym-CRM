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
    private final String TRAINER_WORKLOAD_QUEUE = "addReportQueue";
    private final String TRAINER_WORKLOAD_ROUTING_KEY = "add.report.key.#";

    private final String DELETE_WORKLOAD_QUEUE = "deleteReportQueue";
    private final String DELETE_WORKLOAD_ROUTING_KEY = "delete.report.key.#";

    @Bean
    public Exchange exchange() {
        return new TopicExchange("workloadReportExchange", false, false);
    }

    // Queue Beans
    @Bean
    public Queue trainerWorkloadQueue() {
        return new Queue(TRAINER_WORKLOAD_QUEUE, false);
    }

    @Bean
    public Queue deleteWorkloadQueue() {
        return new Queue(DELETE_WORKLOAD_QUEUE, false);
    }

    @Bean
    public Binding trainerWorkloadBinding(Queue trainerWorkloadQueue, Exchange exchange) {
        return BindingBuilder
                .bind(trainerWorkloadQueue)
                .to(exchange)
                .with(TRAINER_WORKLOAD_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Binding deleteWorkloadBinding(Queue deleteWorkloadQueue, Exchange exchange) {
        return BindingBuilder
                .bind(deleteWorkloadQueue)
                .to(exchange)
                .with(DELETE_WORKLOAD_ROUTING_KEY)
                .noargs();
    }
}