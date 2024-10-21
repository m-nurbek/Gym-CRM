package com.epam.gym.controller;

import com.epam.gym.dto.request.TraineeRegistrationDto;
import com.epam.gym.dto.request.TrainerRegistrationDto;
import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.service.WebAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class AuthControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WebAuthService authService;

    @Test
    void login() throws Exception {
        when(authService.authenticate(anyString(), anyString())).thenReturn(new JwtTokenResponseDto("accessToken", "refreshToken"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "johndoe1",
                                  "password": "password1"
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    void failedLogin() throws Exception {
        doThrow(BadCredentialsException.class).when(authService).authenticate(anyString(), anyString());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "johndoe1",
                                  "password": "password"
                                }"""))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerTrainee() throws Exception {
        when(authService.registerTrainee(any(TraineeRegistrationDto.class)))
                .thenReturn(new String[]{"username", "password"});

        mockMvc.perform(post("/api/v1/auth/register/trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "firstname",
                                  "lastName": "lastname",
                                  "dob": "2000-01-01",
                                  "address": "address",
                                  "password": "password"
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    void registerTrainer() throws Exception {
        when(authService.registerTrainer(any(TrainerRegistrationDto.class)))
                .thenReturn(new String[]{"username", "password"});

        mockMvc.perform(post("/api/v1/auth/register/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "firstname",
                                  "lastName": "lastname",
                                  "specialization": "SWIMMING",
                                  "password": "password"
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void changeLogin() throws Exception {
        when(authService.changePassword(anyString(), anyString(), anyString()))
                .thenReturn(true);

        mockMvc.perform(post("/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "oldPassword": "old",
                                  "newPassword": "newPassword1!@"
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void failedChangeLogin() throws Exception {
        when(authService.changePassword(anyString(), anyString(), anyString()))
                .thenReturn(false);

        mockMvc.perform(post("/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "oldPassword": "old",
                                  "newPassword": "new"
                                }"""))
                .andExpect(status().isBadRequest());
    }
}