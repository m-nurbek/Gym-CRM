package com.epam.gym.controller;

import com.epam.gym.dto.request.TraineeUpdateRequestDto;
import com.epam.gym.dto.response.TraineeResponseDto;
import com.epam.gym.dto.response.TraineeUpdateResponseDto;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class TraineeControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TraineeService traineeService;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void getProfile() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(Optional.of(new TraineeResponseDto(
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "address",
                        true,
                        List.of()
                )));

        mockMvc.perform(get("/api/v1/trainees/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedGetProfile() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/trainees/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void updateProfile() throws Exception {
        when(traineeService.update(anyString(), any(TraineeUpdateRequestDto.class)))
                .thenReturn(Optional.of(new TraineeUpdateResponseDto(
                        "username",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "address",
                        true,
                        List.of()
                )));

        mockMvc.perform(put("/api/v1/trainees/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "firstName",
                                "lastName": "lastName",
                                "dob": "2000-01-01",
                                "address": "address",
                                "isActive": true
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedUpdateProfile() throws Exception {
        when(traineeService.update(anyString(), any(TraineeUpdateRequestDto.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/trainees/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "firstName",
                                "lastName": "lastName",
                                "dob": "2000-01-01",
                                "address": "address",
                                "isActive": true
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void deleteProfile() throws Exception {
        when(traineeService.deleteByUsername(anyString())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/trainees/username"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedDeleteProfile() throws Exception {
        when(traineeService.deleteByUsername(anyString())).thenReturn(false);

        mockMvc.perform(delete("/api/v1/trainees/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void getNotAssignedActiveTrainers() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(Optional.of(new TraineeResponseDto(
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "address",
                        true,
                        List.of()
                )));
        when(traineeService.getUnassignedTrainersByUsernameToResponse(anyString())).thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/trainees/trainers/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedGetNotAssignedActiveTrainers() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(Optional.empty());
        when(traineeService.getUnassignedTrainersByUsernameToResponse(anyString())).thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/trainees/trainers/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void updateTrainersList() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(Optional.of(new TraineeResponseDto(
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "address",
                        true,
                        List.of()
                )));
        when(traineeService.updateTrainerListByUsername(anyString(), any()))
                .thenReturn(Set.of());

        mockMvc.perform(put("/api/v1/trainees/update-trainers/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                [
                                  "quincyramirez45",
                                  "raybailey46",
                                  "saracooper47",
                                  "bobjohnson4"
                                ]"""))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedUpdateTrainersList() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(Optional.empty());
        when(traineeService.updateTrainerListByUsername(anyString(), any()))
                .thenReturn(Set.of());

        mockMvc.perform(put("/api/v1/trainees/update-trainers/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                [
                                  "quincyramirez45",
                                  "raybailey46",
                                  "saracooper47",
                                  "bobjohnson4"
                                ]"""))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void getTrainingsList() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(Optional.of(new TraineeResponseDto(
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "address",
                        true,
                        List.of()
                )));
        when(traineeService.getTrainingsByUsernameToResponse(anyString(), any(LocalDate.class), any(LocalDate.class), anyString(), anyString()))
                .thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/trainees/trainings/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedGetTrainingsList() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(Optional.empty());
        when(traineeService.getTrainingsByUsernameToResponse(anyString(), any(LocalDate.class), any(LocalDate.class), anyString(), anyString()))
                .thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/trainees/trainings/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void changeProfileActiveState() throws Exception {
        when(userService.updateActiveState(anyString(), anyBoolean())).thenReturn(true);

        mockMvc.perform(patch("/api/v1/trainees/active-state/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                true
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedChangeProfileActiveState() throws Exception {
        when(userService.updateActiveState(anyString(), anyBoolean())).thenReturn(false);

        mockMvc.perform(patch("/api/v1/trainees/active-state/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                true
                                """))
                .andExpect(status().isNotFound());
    }
}