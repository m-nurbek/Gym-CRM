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
import java.util.Date;

@Data
@Builder
@ToString
public class TrainingDto implements Dto<BigInteger> {
    private BigInteger id;
    private TraineeDto trainee;
    private TrainerDto trainer;

    private String name;
    private TrainingTypeDto type;
    private Date date;
    private String duration;

    @JsonCreator
    public TrainingDto(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("trainee") TraineeDto trainee,
            @JsonProperty("trainer") TrainerDto trainer,
            @JsonProperty("name") String name,
            @JsonProperty("type") TrainingTypeDto type,
            @JsonProperty("date") Date date,
            @JsonProperty("duration") String duration
    ) {
        this.id = id;
        this.trainee = trainee;
        this.trainer = trainer;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public static class Parser {
        private static final ObjectMapper mapper = new ObjectMapper();

        static {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        }

        public static String toJson(TrainingDto trainer) {
            try {
                return mapper.writeValueAsString(trainer);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static TrainingDto parseJson(String json) {
            try {
                return mapper.readValue(json, TrainingDto.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}