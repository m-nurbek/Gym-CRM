package com.epam.gym.config;

import com.epam.gym.converter.YearMonthReadConverter;
import com.epam.gym.converter.YearMonthWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

/**
 * @author Nurbek on 05.01.2025
 */
@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new YearMonthReadConverter(),
                new YearMonthWriteConverter()
        ));
    }
}