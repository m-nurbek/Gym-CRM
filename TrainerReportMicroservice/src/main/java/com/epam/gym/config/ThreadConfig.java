package com.epam.gym.config;

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
public class ThreadConfig implements AsyncConfigurer {
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor.setCorePoolSize(availableProcessors);
        threadPoolExecutor.setMaxPoolSize(availableProcessors * 3);
        threadPoolExecutor.initialize();

        return threadPoolExecutor;
    }
}