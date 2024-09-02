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
public class UserDto implements Dto<BigInteger> {
    private BigInteger id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

    @JsonCreator
    public UserDto(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("isActive") boolean isActive
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public static class Parser {
        private static final ObjectMapper mapper = new ObjectMapper();

        static {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        }

        public static String toJson(UserDto trainer) {
            try {
                return mapper.writeValueAsString(trainer);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static UserDto parseJson(String json) {
            try {
                return mapper.readValue(json, UserDto.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}