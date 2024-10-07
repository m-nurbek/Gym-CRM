package com.epam.gym.service.serviceImpl;

import com.epam.gym.Application;
import com.epam.gym.dto.response.TrainingTypeResponseDto;
import com.epam.gym.entity.TrainingTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class TrainingTypeServiceImplIntegrationTest {
    @Autowired
    private TrainingTypeServiceImpl typeService;

    @Test
    void getAllToResponse() {
        // when
        Set<TrainingTypeResponseDto> typeDtos = typeService.getAllToResponse();

        // then
        assertThat(typeDtos.stream().map(TrainingTypeResponseDto::type).toList())
                .containsExactlyInAnyOrderElementsOf(Arrays.stream(TrainingTypeEnum.values()).map(Enum::name).toList());
    }
}