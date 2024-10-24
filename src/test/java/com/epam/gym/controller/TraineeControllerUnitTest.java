package com.epam.gym.controller;

import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TraineeUpdateRequestDto;
import com.epam.gym.dto.response.RegistrationResponseDto;
import com.epam.gym.dto.response.TraineeResponseDto;
import com.epam.gym.dto.response.TraineeUpdateResponseDto;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.UserService;
import com.epam.gym.service.WebAuthService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @MockBean
    private WebAuthService authService;

    @Test
    void registerTrainee() throws Exception {
        when(authService.registerTrainee(any(TraineeRegistrationDto.class)))
                .thenReturn(new RegistrationResponseDto("username"));

        mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "firstname",
                                  "lastName": "lastname",
                                  "dob": "2000-01-01",
                                  "address": "address",
                                  "password": "Password123!@"
                                }"""))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void getProfile() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(new TraineeResponseDto(
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "address",
                        true,
                        List.of()
                ));

        mockMvc.perform(get("/api/v1/trainees/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedGetProfile() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenThrow(NotFoundException.class);

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
                .thenThrow(NotFoundException.class);

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
        doNothing().when(traineeService).deleteByUsername(anyString());

        mockMvc.perform(delete("/api/v1/trainees/username"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedDeleteProfile() throws Exception {
        doThrow(NotFoundException.class).when(traineeService).deleteByUsername(anyString());

        mockMvc.perform(delete("/api/v1/trainees/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void getNotAssignedActiveTrainers() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(new TraineeResponseDto(
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "address",
                        true,
                        List.of()
                ));
        when(traineeService.getUnassignedTrainersByUsernameToResponse(anyString())).thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/trainees/trainers/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedGetNotAssignedActiveTrainers() throws Exception {
        when(traineeService.getUnassignedTrainersByUsernameToResponse(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/trainees/trainers/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void updateTrainersList() throws Exception {
        when(traineeService.findByUsername(anyString()))
                .thenReturn(new TraineeResponseDto(
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "address",
                        true,
                        List.of()
                ));
        when(traineeService.updateTrainerListByUsername(anyString(), any()))
                .thenReturn(Set.of());

        mockMvc.perform(put("/api/v1/trainees/trainers/username")
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
        when(traineeService.updateTrainerListByUsername(anyString(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(put("/api/v1/trainees/trainers/username")
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
        when(traineeService.getTrainingsByUsernameToResponse(anyString(), any(LocalDate.class), any(LocalDate.class), anyString(), anyString()))
                .thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/trainees/trainings/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedGetTrainingsList() throws Exception {
        when(traineeService.getTrainingsByUsernameToResponse(anyString(), any(), any(), any(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/trainees/trainings/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void changeProfileActiveState() throws Exception {
        doNothing().when(userService).updateActiveState(anyString(), anyBoolean());

        mockMvc.perform(patch("/api/v1/trainees/active-state/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                true
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINEE")
    void failedChangeProfileActiveState() throws Exception {
        doThrow(NotFoundException.class).when(userService).updateActiveState(anyString(), anyBoolean());

        mockMvc.perform(patch("/api/v1/trainees/active-state/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                true
                                """))
                .andExpect(status().isNotFound());
    }
}