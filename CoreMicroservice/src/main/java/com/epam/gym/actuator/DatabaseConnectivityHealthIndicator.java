package com.epam.gym.actuator;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 *
 * @author Nurbek on 03.02.2025
 */
@Component
@AllArgsConstructor
public class DatabaseConnectivityHealthIndicator implements HealthIndicator {
    private DataSource dataSource;

    @Override
    public Health health() {
        DataSourceHealthIndicator dataSourceHealthIndicator = new DataSourceHealthIndicator(dataSource, "SELECT 1;");
        return dataSourceHealthIndicator.health();
    }
}