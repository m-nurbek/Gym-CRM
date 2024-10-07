package com.epam.gym.service.serviceImpl;

import com.epam.gym.Application;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class WebAuthServiceImplIntegrationTest {
    @Autowired
    private WebAuthServiceImpl webAuthService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    @Test
    void shouldAuthenticate() {
        // given
        String username = "johndoe1";
        String password = "password1";

        // when
        boolean success = webAuthService.authenticate(username, password);

        // then
        assertAll(
                "Assertions for 'authenticate()' method",
                () -> assertThat(success).isTrue()
        );
    }

    @Test
    void shouldNotAuthenticate1() {
        // given
        String username = "non-existent";
        String password = "password1";

        // when
        boolean success = webAuthService.authenticate(username, password);

        // then
        assertAll(
                "Assertions for 'authenticate()' method",
                () -> assertThat(success).isFalse()
        );
    }

    @Test
    void shouldNotAuthenticate2() {
        // given
        String username = "johndoe1";
        String password = "wrongPassword";

        // when
        boolean success = webAuthService.authenticate(username, password);

        // then
        assertAll(
                "Assertions for 'authenticate()' method",
                () -> assertThat(success).isFalse()
        );
    }

    @Test
    void shouldChangePassword() {
        // given
        String username = "johndoe1";
        String oldPassword = "password1";
        String newPassword = "NEW_PASSWORD";

        // when
        boolean success = webAuthService.changePassword(username, oldPassword, newPassword);

        // then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertThat(success).isTrue()
        );
    }

    @Test
    void shouldNotChangePassword1() {
        // given
        String username = "non-existent";
        String oldPassword = "password1";
        String newPassword = "NEW_PASSWORD";

        // when
        boolean success = webAuthService.changePassword(username, oldPassword, newPassword);

        // then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertThat(success).isFalse()
        );
    }

    @Test
    void shouldNotChangePassword2() {
        // given
        String username = "johndoe1";
        String oldPassword = "wrongPassword";
        String newPassword = "NEW_PASSWORD";

        // when
        boolean success = webAuthService.changePassword(username, oldPassword, newPassword);

        // then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertThat(success).isFalse()
        );
    }

    @Test
    void registerTrainee() {
        // given
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        LocalDate dob = LocalDate.of(2001, 1, 1);
        String address = "ADDRESS";

        // when
        String[] usernamePassword = webAuthService.registerTrainee(firstName, lastName, dob, address);
        String username = usernamePassword[0];

        var user = userRepository.findByUsername(username).orElse(null);
        var trainee = traineeRepository.findByUser_Username(username).orElse(null);

        // then
        assertAll(
                "Assertions for 'registerTrainee()' method",
                () -> assertThat(username).isEqualTo(firstName + "." + lastName),
                () -> assertThat(user).isNotNull(),
                () -> assertThat(trainee).isNotNull(),
                () -> assertThat(user.getUsername()).isEqualTo(username),
                () -> assertThat(user.getIsActive()).isTrue(),
                () -> assertThat(user.getFirstName()).isEqualTo(firstName),
                () -> assertThat(user.getLastName()).isEqualTo(lastName),
                () -> assertThat(trainee.getDob()).isEqualTo(dob),
                () -> assertThat(trainee.getAddress()).isEqualTo(address),
                () -> assertThat(user.getTrainee().getId()).isEqualTo(trainee.getId()),
                () -> assertThat(trainee.getUser().getId()).isEqualTo(user.getId())
        );
    }

    @Test
    void registerTrainer() {
        // given
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        var type = TrainingTypeEnum.AEROBICS;

        // when
        String[] usernamePassword = webAuthService.registerTrainer(firstName, lastName, type);
        String username = usernamePassword[0];

        var user = userRepository.findByUsername(username).orElse(null);
        var trainer = trainerRepository.findByUser_Username(username).orElse(null);

        // then
        assertAll(
                "Assertions for 'registerTrainer()' method",
                () -> assertThat(username).isEqualTo(firstName + "." + lastName),
                () -> assertThat(user).isNotNull(),
                () -> assertThat(trainer).isNotNull(),
                () -> assertThat(user.getUsername()).isEqualTo(username),
                () -> assertThat(user.getIsActive()).isTrue(),
                () -> assertThat(user.getFirstName()).isEqualTo(firstName),
                () -> assertThat(user.getLastName()).isEqualTo(lastName),
                () -> assertThat(trainer.getSpecialization().getName()).isEqualTo(type),
                () -> assertThat(user.getTrainer().getId()).isEqualTo(trainer.getId()),
                () -> assertThat(trainer.getUser().getId()).isEqualTo(user.getId())
        );
    }
}