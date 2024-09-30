package com.epam.gym.controller;

import com.epam.gym.config.ApplicationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TraineeControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldGetProfile() throws Exception {
        // given
        String username = "johndoe1";
        String password = "password1";
        authenticate(username, password);

        // when & then
        mockMvc.perform(get("/v1/trainee/" + username).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldUpdateProfile() throws Exception {
        // given
        String username = "janedoe2";
        String password = "password2";
        authenticate(username, password);

        String firstName = "NEW_FIRST_NAME1";
        String lastName = "NEW_SECOND_NAME2";
        String dob = "2000-01-01";
        String address = "NEW ADDRESS";
        boolean isActive = true;

        // when & then
        mockMvc.perform(put("/v1/trainee/" + username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "%s",
                                  "lastName": "%s",
                                  "dob": "%s",
                                  "address": "%s",
                                  "isActive": %s
                                }
                                """.formatted(firstName, lastName, dob, address, isActive))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.dob[0]").value(2000))
                .andExpect(jsonPath("$.dob[1]").value(1))
                .andExpect(jsonPath("$.dob[2]").value(1))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.isActive").value(isActive));
    }

    @Test
    void shouldDeleteProfile() throws Exception {
        // given
        String username = "janedoe2";
        String password = "password2";
        authenticate(username, password);

        // when & then
        mockMvc.perform(delete("/v1/trainee/" + username))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteProfile() throws Exception {
        // given
        String username = "janedoe2";
        String password = "password2";
        authenticate(username, password);

        String username2 = "non-existent-user";

        // when & then
        mockMvc.perform(delete("/v1/trainee/" + username2))
                .andExpect(status().isNotFound());
    }

    ResultActions authenticate(String username, String password) throws Exception {
        return mockMvc.perform(post("/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "%s",
                                  "password": "%s"
                                }
                                """.formatted(username, password)))
                .andExpect(status().isOk());
    }
}