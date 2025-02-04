package com.epam.gym;

import com.epam.gym.util.annotation.DevelopmentProfile;
import com.epam.gym.util.annotation.LocalProfile;
import com.epam.gym.util.annotation.ProductionProfile;
import com.epam.gym.util.annotation.StagingProfile;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class Application {

    @Bean
    public TimedAspect timedAspect(MeterRegistry meterRegistry) {
        return new TimedAspect(meterRegistry);
    }

    @Bean
    @ProductionProfile
    public Object productionWarningLog() {
        log.info("====================== RUNNING THE APPLICATION IN PRODUCTION MODE! ======================");
        return null;
    }

    @Bean
    @StagingProfile
    public Object stagingWarningLog() {
        log.info("====================== RUNNING THE APPLICATION IN STAGING MODE! ======================");
        return null;
    }

    @Bean
    @LocalProfile
    public Object localWarningLog() {
        log.info("====================== RUNNING THE APPLICATION IN LOCAL MODE! ======================");
        return null;
    }

    @Bean
    @DevelopmentProfile
    public Object devWarningLog() {
        log.info("====================== RUNNING THE APPLICATION IN DEVELOPMENT MODE! ======================");
        return null;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}