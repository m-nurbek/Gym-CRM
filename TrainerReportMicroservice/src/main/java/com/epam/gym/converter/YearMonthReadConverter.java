package com.epam.gym.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.time.YearMonth;

/**
 * @author Nurbek on 05.01.2025
 */
public class YearMonthReadConverter implements Converter<String, YearMonth> {
    @Override
    public YearMonth convert(@NonNull String source) {
        return YearMonth.parse(source);
    }
}