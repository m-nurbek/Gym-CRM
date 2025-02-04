package com.epam.gym.controller;

import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.service.TrainingService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class TrainingControllerUnitTest {
    @Rule
    public Timeout globalTimeout = new Timeout(10, TimeUnit.SECONDS);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrainingService trainingService;

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void addTraining() throws Exception {
        doNothing().when(trainingService).save(any(TrainingAddRequestDto.class));

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "traineeUsername": "traineeUsername",
                                "trainerUsername": "traineeUsername",
                                "trainingName": "trainingName",
                                "date": "4000-01-01",
                                "duration": 3
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void failedAddTraining() throws Exception {
        doThrow(BadRequestException.class).when(trainingService).save(any(TrainingAddRequestDto.class));

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "traineeUsername": "traineeUsername",
                                "trainerUsername": "traineeUsername",
                                "trainingName": "trainingName",
                                "date": "2025-01-01",
                                "duration": 3
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}