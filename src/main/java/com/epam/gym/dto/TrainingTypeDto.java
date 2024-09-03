package com.epam.gym.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.IOException;
import java.math.BigInteger;

@Data
@Builder
@ToString
public class TrainingTypeDto implements Dto<BigInteger> {
    private BigInteger id;
    private String name;

    @JsonCreator
    public TrainingTypeDto(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("name") String name
    ) {
        this.id = id;
        this.name = name;
    }

    public static class Parser {
        private static final ObjectMapper mapper = new ObjectMapper();

        static {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        }

        public static String toJson(TrainingTypeDto trainer) {
            try {
                return mapper.writeValueAsString(trainer);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static TrainingTypeDto parseJson(String json) {
            try {
                return mapper.readValue(json, TrainingTypeDto.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}