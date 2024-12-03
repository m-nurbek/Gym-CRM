package com.epam.gym.actuator;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductionEnvironmentHealthIndicator implements HealthIndicator {
    private final Environment environment;

    @Override
    public Health health() {
        for (String profileName : environment.getActiveProfiles()) {
            if (profileName.equalsIgnoreCase("prod")) {
                return Health.up().withDetail("Production profile", "Active").build();
            }
        }

        return Health.down().withDetail("Production profile", "Not Active").build();
    }
}