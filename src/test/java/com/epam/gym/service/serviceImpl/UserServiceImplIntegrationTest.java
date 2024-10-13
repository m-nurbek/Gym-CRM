package com.epam.gym.service.serviceImpl;

import com.epam.gym.Application;
import com.epam.gym.dto.UserDto;
import com.epam.gym.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceImplIntegrationTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

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
        boolean success1 = userService.changePassword(username, oldPassword1, newPassword1);
        boolean success2 = userService.changePassword(id, oldPassword2, newPassword2);

        var user1 = userRepository.findByUsername(username).orElse(null);
        var user2 = userRepository.findById(id).orElse(null);

        // then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertThat(success1).isTrue(),
                () -> assertThat(success2).isTrue(),
                () -> assertThat(user1).isNotNull(),
                () -> assertThat(user2).isNotNull(),
                () -> assertThat(user1.getPassword()).isEqualTo(newPassword1),
                () -> assertThat(user2.getPassword()).isEqualTo(newPassword2)
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

        // when
        boolean success1 = userService.changePassword(username1, oldPassword1, newPassword1);
        boolean success2 = userService.changePassword(id1, oldPassword2, newPassword2);
        boolean success3 = userService.changePassword(username2, oldPassword3, newPassword3);
        boolean success4 = userService.changePassword(id2, oldPassword3, newPassword3);

        var user1 = userRepository.findByUsername(username1).orElse(null);
        var user2 = userRepository.findById(id1).orElse(null);
        var user3 = userRepository.findByUsername(username2).orElse(null);
        var user4 = userRepository.findById(id2).orElse(null);

        // then
        assertAll(
                "Assertions for 'changePassword()' method",
                () -> assertThat(success1).isFalse(),
                () -> assertThat(success2).isFalse(),
                () -> assertThat(success3).isFalse(),
                () -> assertThat(success4).isFalse(),
                () -> assertThat(user1).isNull(),
                () -> assertThat(user2).isNull(),
                () -> assertThat(user3).isNotNull(),
                () -> assertThat(user4).isNotNull(),
                () -> assertThat(user3.getPassword()).isNotEqualTo(newPassword3),
                () -> assertThat(user4.getPassword()).isNotEqualTo(newPassword3)
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
        boolean success1 = userService.updateActiveState(username, isActive);
        boolean success2 = userService.updateActiveState(id, isActive);

        var user1 = userRepository.findByUsername(username).orElse(null);
        var user2 = userRepository.findById(id).orElse(null);

        // then
        assertAll(
                "Assertions for 'updateActiveState()' method",
                () -> assertThat(success1).isTrue(),
                () -> assertThat(success2).isTrue(),
                () -> assertThat(user1).isNotNull(),
                () -> assertThat(user2).isNotNull(),
                () -> assertThat(user1.getIsActive()).isEqualTo(isActive),
                () -> assertThat(user2.getIsActive()).isEqualTo(isActive)
        );
    }

    @Test
    void shouldNotUpdateActiveState() {
        // given
        String username = "non-existent";
        BigInteger id = BigInteger.valueOf(51); // non-existent id
        boolean isActive = false;

        // when
        boolean success1 = userService.updateActiveState(username, isActive);
        boolean success2 = userService.updateActiveState(id, isActive);

        var user1 = userRepository.findByUsername(username).orElse(null);
        var user2 = userRepository.findById(id).orElse(null);

        // then
        assertAll(
                "Assertions for 'updateActiveState()' method",
                () -> assertThat(success1).isFalse(),
                () -> assertThat(success2).isFalse(),
                () -> assertThat(user1).isNull(),
                () -> assertThat(user2).isNull()
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