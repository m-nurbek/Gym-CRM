package com.epam.gym.cucumber.glue;

import com.epam.gym.integration.RequestListener;
import com.epam.gym.repository.TrainerWorkloadRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nurbek on 05.01.2025
 */
@RequiredArgsConstructor
public class RequestListenerSteps {
    private final RequestListener requestListener;
    private final TrainerWorkloadRepository repository;
    private Exception exception;

    @When("the message comes to addReportQueue with message: {string}")
    public void whenMessageComesToAddReportQueue(String message) {
        try {
            requestListener.handleAddRequest(new Message(message.getBytes()));
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @When("the message comes to deleteReportQueue with message: {string}")
    public void whenMessageComesToDeleteReportQueue(String message) {
        try {
            requestListener.handleAddRequest(new Message(message.getBytes()));
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("the process is finished and no exceptions are thrown")
    public void thenProcessIsFinishedAndNoExceptionsAreThrown() {
        if (exception != null) {
            throw new AssertionError("Exception was thrown during message processing", exception);
        }
    }

    @Then("a trainer with username {string} exists in the database")
    public void thenTrainerWorkloadExistsInDatabase(String username) {
        assertThat(repository.findByUsername(username).isPresent()).isTrue();
    }
}