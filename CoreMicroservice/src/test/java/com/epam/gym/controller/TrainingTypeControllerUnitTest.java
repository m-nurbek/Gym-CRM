package com.epam.gym.controller;

import com.epam.gym.service.TrainingTypeService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class TrainingTypeControllerUnitTest {
    @Rule
    public Timeout globalTimeout = new Timeout(10, TimeUnit.SECONDS);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrainingTypeService typeService;

    @Test
    @WithMockUser(username = "johndoe1", password = "password1")
    void getTrainingTypes() throws Exception {
        when(typeService.getAllToResponse()).thenReturn(Set.of());

        mockMvc.perform(get("/api/v1/training-types"))
                .andExpect(status().isOk());
    }
}