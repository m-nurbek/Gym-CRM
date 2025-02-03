package com.epam.gym.controller;

import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.response.RegistrationResponseDto;
import com.epam.gym.dto.response.TrainerResponseDto;
import com.epam.gym.dto.response.TrainerUpdateResponseDto;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import com.epam.gym.service.WebAuthService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class TrainerControllerUnitTest {
    @Rule
    public Timeout globalTimeout = new Timeout(10, TimeUnit.SECONDS);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrainerService trainerService;
    @MockBean
    private UserService userService;
    @MockBean
    private WebAuthService authService;

    @Test
    void registerTrainer() throws Exception {
        when(authService.registerTrainer(any(TrainerRegistrationDto.class)))
                .thenReturn(new RegistrationResponseDto("username"));

        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "firstname",
                                  "lastName": "lastname",
                                  "specialization": "SWIMMING",
                                  "password": "Password123!@"
                                }"""))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINER")
    void getProfile() throws Exception {
        when(trainerService.findByUsername(anyString())).thenReturn(new TrainerResponseDto(
                "firstName",
                "lastName",
                "specialization",
                true,
                List.of()
        ));

        mockMvc.perform(get("/api/v1/trainers/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINER")
    void failedGetProfile() throws Exception {
        when(trainerService.findByUsername(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/trainers/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINER")
    void updateProfile() throws Exception {
        when(trainerService.update(anyString(), any())).thenReturn(new TrainerUpdateResponseDto(
                "username",
                "firstName",
                "lastName",
                "specialization",
                true,
                List.of()
        ));

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
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINER")
    void failedUpdateProfile() throws Exception {
        when(trainerService.update(anyString(), any())).thenThrow(NotFoundException.class);

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
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINER")
    void getTrainingsList() throws Exception {
        when(trainerService.getTrainingsByUsernameToResponse(anyString(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/trainers/trainings/username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINER")
    void failedGetTrainingsList() throws Exception {
        when(trainerService.getTrainingsByUsernameToResponse(anyString(), any(), any(), any()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/trainers/trainings/username"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINER")
    void changeProfileActiveState() throws Exception {
        doNothing().when(userService).updateActiveState(anyString(), anyBoolean());

        mockMvc.perform(patch("/api/v1/trainers/active-state/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "isActive": true
                                }
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1", roles = "TRAINER")
    void failedChangeProfileActiveState() throws Exception {
        doThrow(NotFoundException.class).when(userService).updateActiveState(anyString(), anyBoolean());

        mockMvc.perform(patch("/api/v1/trainers/active-state/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "isActive": true
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}