package com.epam.gym.controller;

import com.epam.gym.dto.response.TrainerResponseDto;
import com.epam.gym.dto.response.TrainerUpdateResponseDto;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrainerControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrainerService trainerService;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void getProfile() throws Exception {
        when(trainerService.findByUsername(anyString())).thenReturn(Optional.of(new TrainerResponseDto(
                "firstName",
                "lastName",
                "specialization",
                true,
                List.of()
        )));

        mockMvc.perform(get("/api/v1/trainers/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void failedGetProfile() throws Exception {
        when(trainerService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/trainers/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void updateProfile() throws Exception {
        when(trainerService.update(anyString(), any())).thenReturn(Optional.of(new TrainerUpdateResponseDto(
                "username",
                "firstName",
                "lastName",
                "specialization",
                true,
                List.of()
        )));

        mockMvc.perform(put("/api/v1/trainers/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "firstName",
                                "lastName": "lastName",
                                "specialization": "SWIMMING",
                                "isActive": true
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void failedUpdateProfile() throws Exception {
        when(trainerService.update(anyString(), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/trainers/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "firstName",
                                "lastName": "lastName",
                                "specialization": "SWIMMING",
                                "isActive": true
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void getTrainingsList() throws Exception {
        when(trainerService.findByUsername(anyString()))
                .thenReturn(Optional.of(new TrainerResponseDto(
                        "firstName",
                        "lastName",
                        "specialization",
                        true,
                        List.of()
                )));
        when(trainerService.getTrainingsByUsernameToResponse(anyString(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/trainers/trainings/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void failedGetTrainingsList() throws Exception {
        when(trainerService.findByUsername(anyString()))
                .thenReturn(Optional.empty());
        when(trainerService.getTrainingsByUsernameToResponse(anyString(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/trainers/trainings/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void changeProfileActiveState() throws Exception {
        when(userService.updateActiveState(anyString(), anyBoolean())).thenReturn(true);

        mockMvc.perform(patch("/api/v1/trainers/active-state/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                true
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void failedChangeProfileActiveState() throws Exception {
        when(userService.updateActiveState(anyString(), anyBoolean())).thenReturn(false);

        mockMvc.perform(patch("/api/v1/trainers/active-state/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                true
                                """))
                .andExpect(status().isNotFound());
    }
}