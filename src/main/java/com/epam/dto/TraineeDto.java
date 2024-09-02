package com.epam.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class TraineeDto implements Dto<BigInteger> {
    private BigInteger id;
    private Date dob;
    private String address;
    @JsonIgnore
    private UserDto user;

    @JsonCreator
    public TraineeDto(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("dob") Date dob,
            @JsonProperty("address") String address,
            @JsonProperty("user") UserDto user
    ) {
        this.id = id;
        this.dob = dob;
        this.address = address;
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

        public static String toJson(TraineeDto trainee) {
            try {
                return mapper.writeValueAsString(trainee);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static TraineeDto parseJson(String json) {
            try {
                return mapper.readValue(json, TraineeDto.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}