package com.epam.gym.cucumber.glue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Nurbek on 05.01.2025
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@CucumberContextConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
public class SpringIntegrationTest {
}