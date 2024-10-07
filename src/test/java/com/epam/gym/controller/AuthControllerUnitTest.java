package com.epam.gym.controller;

import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.service.WebAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WebAuthService authService;

    @Test
    void login() throws Exception {
        when(authService.authenticate(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "johndoe1",
                                  "password": "password"
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    void failedLogin() throws Exception {
        when(authService.authenticate(anyString(), anyString())).thenReturn(false);

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
        when(authService.registerTrainee(anyString(), anyString(), any(LocalDate.class), anyString()))
                .thenReturn(new String[]{"username", "password"});

        mockMvc.perform(post("/api/v1/auth/register/trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "firstname",
                                  "lastName": "lastname",
                                  "dob": "2000-01-01",
                                  "address": "address"
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    void registerTrainer() throws Exception {
        when(authService.registerTrainer(anyString(), anyString(), any(TrainingTypeEnum.class)))
                .thenReturn(new String[]{"username", "password"});

        mockMvc.perform(post("/api/v1/auth/register/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "firstname",
                                  "lastName": "lastname",
                                  "specialization": "SWIMMING"
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    void changeLogin() throws Exception {
        when(authService.changePassword(anyString(), anyString(), anyString()))
                .thenReturn(true);

        mockMvc.perform(post("/api/v1/auth/change-password/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "oldPassword": "old",
                                  "newPassword": "newPassword1!@"
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    void failedChangeLogin() throws Exception {
        when(authService.changePassword(anyString(), anyString(), anyString()))
                .thenReturn(false);

        mockMvc.perform(post("/api/v1/auth/change-password/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "oldPassword": "old",
                                  "newPassword": "new"
                                }"""))
                .andExpect(status().isBadRequest());
    }
}