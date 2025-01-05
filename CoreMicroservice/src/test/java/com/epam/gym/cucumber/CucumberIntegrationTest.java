package com.epam.gym.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * @author Nurbek on 05.01.2025
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/", glue = {"com.epam.gym.cucumber.glue"})
public class CucumberIntegrationTest {
}