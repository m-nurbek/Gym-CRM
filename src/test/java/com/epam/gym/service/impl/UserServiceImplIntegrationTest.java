package com.epam.gym.service.impl;

import com.epam.gym.Application;
import com.epam.gym.controller.exception.BadRequestException;
import com.epam.gym.controller.exception.NotFoundException;
import com.epam.gym.dto.UserDto;
import com.epam.gym.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceImplIntegrationTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldMatchUsernameAndPassword() {
        // given
        String username = "johndoe1";
        String password = "password1";

        // when
        boolean success = userService.isUsernameAndPasswordMatch(username, password);

        // then
        assertAll(
                "Assertions for 'isUsernameAndPasswordMatch()' method",
                () -> assertThat(success).isTrue()
        );
    }

    @Test
    void shouldNotMatchUsernameAndPassword() {
        // given
        String username = "johndoe1";
        String password = "wrongPassword";

        // when
        boolean success = userService.isUsernameAndPasswordMatch(username, password);

        // then
        assertAll(
                "Assertions for 'isUsernameAndPasswordMatch()' method",
                () -> assertThat(success).isFalse()
        );
    }

    @Test
    void shouldChangePassword() {
        // given
        String username = "johndoe1";
        String oldPassword1 = "password1";
        String newPassword1 = "NewPassword1";

        BigInteger id = BigInteger.valueOf(2);
        String oldPassword2 = "password2";
        String newPassword2 = "NewPassword2";

        // when
        userService.changePassword(username, oldPassword1, newPassword1);
        userService.changePassword(id, oldPassword2, newPassword2);

        var user1 = userRepository.findByUsername(username).orElse(null);
        var user2 = userRepository.findById(id).orElse(null);

        // then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertThat(user1).isNotNull(),
                () -> assertThat(user2).isNotNull(),
                () -> assertThat(passwordEncoder.matches(newPassword1, user1.getPassword())).isTrue(),
                () -> assertThat(passwordEncoder.matches(newPassword2, user2.getPassword())).isTrue()
        );
    }

    @Test
    void shouldNotChangePassword() {
        // given
        String username1 = "nonExistentUser";
        String oldPassword1 = "password1";
        String newPassword1 = "NewPassword1";

        BigInteger id1 = BigInteger.valueOf(51); // non-existent id
        String oldPassword2 = "password2";
        String newPassword2 = "NewPassword2";

        String username2 = "johndoe1";
        String oldPassword3 = "wrongOldPassword";
        String newPassword3 = "NewPassword3";

        BigInteger id2 = BigInteger.valueOf(2);

        // when & then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertThrows(NotFoundException.class, () -> userService.changePassword(username1, oldPassword1, newPassword1)),
                () -> assertThrows(NotFoundException.class, () -> userService.changePassword(id1, oldPassword2, newPassword2)),
                () -> assertThrows(BadRequestException.class, () -> userService.changePassword(username2, oldPassword3, newPassword3)),
                () -> assertThrows(BadRequestException.class, () -> userService.changePassword(id2, oldPassword3, newPassword3))
        );
    }

    @Test
    void shouldUpdateProfile() {
        // given
        BigInteger id = BigInteger.valueOf(1);
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        boolean isActive = false;

        // when
        boolean success = userService.updateProfile(id, firstName, lastName, isActive);

        var user = userRepository.findById(id).orElse(null);

        // then
        assertAll(
                "Assertions for 'updateProfile()' method",
                () -> assertThat(success).isTrue(),
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getFirstName()).isEqualTo(firstName),
                () -> assertThat(user.getLastName()).isEqualTo(lastName),
                () -> assertThat(user.getIsActive()).isEqualTo(isActive)
        );
    }

    @Test
    void shouldNotUpdateProfile() {
        // given
        BigInteger id = BigInteger.valueOf(51); // non-existent id
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        boolean isActive = false;

        // when
        boolean success = userService.updateProfile(id, firstName, lastName, isActive);

        var user = userRepository.findById(id).orElse(null);

        // then
        assertAll(
                "Assertions for 'updateProfile()' method",
                () -> assertThat(success).isFalse(),
                () -> assertThat(user).isNull()
        );
    }

    @Test
    void shouldUpdateActiveState() {
        // given
        String username = "johndoe1";
        BigInteger id = BigInteger.valueOf(2);
        boolean isActive = false;

        // when
        boolean success = userService.updateActiveState(id, isActive);
        var user = userRepository.findById(id).orElse(null);

        // then
        assertAll(
                "Assertions for 'updateActiveState()' method",
                () -> assertDoesNotThrow(() -> userService.updateActiveState(username, isActive)),
                () -> assertThat(success).isTrue(),
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getIsActive()).isEqualTo(isActive)
        );
    }

    @Test
    void shouldNotUpdateActiveState() {
        // given
        String username = "non-existent";
        BigInteger id = BigInteger.valueOf(51); // non-existent id
        boolean isActive = false;

        // when
        boolean success = userService.updateActiveState(id, isActive);
        var user = userRepository.findById(id).orElse(null);

        // then
        assertAll(
                "Assertions for 'updateActiveState()' method",
                () -> assertThrows(NotFoundException.class, () -> userService.updateActiveState(username, isActive)),
                () -> assertThat(success).isFalse(),
                () -> assertThat(user).isNull()
        );
    }

    @Test
    void shouldSave() {
        // given
        String firstName = "FIRSTNAME";
        String lastName = "LASTNAME";
        boolean isActive = false;
        String password = "password";

        // when
        UserDto userDto = userService.save(firstName, lastName, password, isActive);

        // then
        assertAll(
                "Assertions for 'save()' method",
                () -> assertThat(userDto).isNotNull(),
                () -> assertThat(userDto.id()).isEqualTo(BigInteger.valueOf(1001)),
                () -> assertThat(userDto.firstName()).isEqualTo(firstName),
                () -> assertThat(userDto.lastName()).isEqualTo(lastName),
                () -> assertThat(userDto.isActive()).isEqualTo(isActive),
                () -> assertThat(userDto.username()).isEqualTo("%s.%s".formatted(firstName, lastName))
        );
    }
}