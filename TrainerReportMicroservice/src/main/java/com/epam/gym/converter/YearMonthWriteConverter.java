package com.epam.gym.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.YearMonth;

/**
 * @author Nurbek on 05.01.2025
 */
public class YearMonthWriteConverter implements Converter<YearMonth, String> {
    @Override
    public String convert(YearMonth source) {
        return source.toString();
    }
}