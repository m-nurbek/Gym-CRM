package com.epam.gym.config;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author Nurbek on 14.12.2024
 */
@Configuration
@EnableAsync
@AllArgsConstructor
public class ThreadConfig implements AsyncConfigurer {
    private final Tracer tracer;

    @Bean(name = "tracingAsyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor.setCorePoolSize(availableProcessors);
        threadPoolExecutor.setMaxPoolSize(availableProcessors * 3);
        threadPoolExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolExecutor.setAwaitTerminationSeconds(5);
        threadPoolExecutor.setTaskDecorator(runnable -> {
            Span span = tracer.nextSpan().name("rabbitMqProducer").start();
            return () -> {
                try (var ws = tracer.withSpan(span)) {
                    runnable.run();
                } finally {
                    span.end();
                }
            };
        });
        threadPoolExecutor.initialize();

        return threadPoolExecutor;
    }
}