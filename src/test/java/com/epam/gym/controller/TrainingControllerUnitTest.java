package com.epam.gym.controller;

import com.epam.gym.dto.request.TrainingAddRequestDto;
import com.epam.gym.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrainingControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrainingService trainingService;

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void addTraining() throws Exception {
        when(trainingService.save(any(TrainingAddRequestDto.class))).thenReturn(true);

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
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void failedAddTraining() throws Exception {
        when(trainingService.save(any(TrainingAddRequestDto.class))).thenReturn(false);

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