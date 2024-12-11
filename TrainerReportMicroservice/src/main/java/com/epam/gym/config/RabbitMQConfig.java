package com.epam.gym.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nurbek on 11.12.2024
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {
    @Value("${rabbitmq.queue-name}")
    private String queueName;
    @Value("${rabbitmq.exchange-name}")
    private String exchangeName;
    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public Queue myQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange(exchangeName, false, false);
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}