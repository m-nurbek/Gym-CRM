package com.epam.gym.controller;

import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.service.WebAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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
    @WithMockUser(username = "johndoe1", password = "password1")
    void failedChangeLogin() throws Exception {
        doNothing().when(authService).changePassword(anyString(), anyString(), anyString());

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