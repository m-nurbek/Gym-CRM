package com.epam.dto;

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
public class TrainerDto implements Dto<BigInteger> {
    private BigInteger id;
    private TrainingTypeDto specialization;
    private UserDto user;

    @JsonCreator
    public TrainerDto(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("specialization") TrainingTypeDto specialization,
            @JsonProperty("user") UserDto user
    ) {
        this.id = id;
        this.specialization = specialization;
        this.user = user;
    }

    public static class Parser {
        private static final ObjectMapper mapper = new ObjectMapper();

        static {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        }

        public static String toJson(TrainerDto trainer) {
            try {
                return mapper.writeValueAsString(trainer);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static TrainerDto parseJson(String json) {
            try {
                return mapper.readValue(json, TrainerDto.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}