package com.epam.gym;

import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.dto.UserDto;
import com.epam.gym.service.UserService;
import com.epam.gym.util.DtoEntityCreationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@WebAppConfiguration
@SpringJUnitConfig(classes = ApplicationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Test
    void shouldFindByUsername() {
        // given
        String username1 = "alicesmith3";
        String username2 = "gracetaylor9";

        // when
        Optional<UserDto> u1 = userService.findByUsername(username1);
        Optional<UserDto> u2 = userService.findByUsername(username2);

        // then
        assertAll(
                "Assertions for find user by username",
                () -> assertThat(u1).isPresent(),
                () -> assertThat(u2).isPresent(),
                () -> assertThat(u1.get().getUsername()).isEqualTo(username1),
                () -> assertThat(u2.get().getUsername()).isEqualTo(username2)
        );
    }

    @Test
    void shouldNotFindByWrongUsername() {
        // given
        String username = "nonexistent";

        // when
        Optional<UserDto> u = userService.findByUsername(username);

        // then
        assertThat(u).isEmpty();
    }

    @Test
    void shouldUpdateTheUserProfile() {
        // given
        Optional<UserDto> u = userService.findByUsername("alicesmith3");

        // when
        u.ifPresent(user -> {
            user.setFirstName("NEW FIRSTNAME");
            user.setLastName("NEW LASTNAME");
            user.setUsername("UPDATED EMAIL");
            user.setIsActive(false);
        });

        boolean isUpdated = userService.updateProfile(u.get());

        // then
        var userByEmail = userService.findByUsername("UPDATED EMAIL");

        assertAll(
                "Assertions for update user profile",
                () -> assertThat(isUpdated).isTrue(),
                () -> assertThat(userByEmail).isPresent(),
                () -> assertThat(userByEmail.get().getFirstName()).isEqualTo("NEW FIRSTNAME"),
                () -> assertThat(userByEmail.get().getLastName()).isEqualTo("NEW LASTNAME"),
                () -> assertThat(userByEmail.get().getUsername()).isEqualTo("UPDATED EMAIL"),
                () -> assertThat(userByEmail.get().getIsActive()).isTrue() // should not update the active state
        );
    }

    @Test
    @DisplayName("Should not update the user profile if the ID is not found")
    void shouldNotUpdateTheUserProfile() {
        // given
        Optional<UserDto> u = userService.findByUsername("alicesmith3");

        u.ifPresent((user) -> {
            user.setId(BigInteger.valueOf(9999));
            user.setFirstName("NEW FIRSTNAME");
            user.setLastName("NEW LASTNAME");
            user.setUsername("UPDATED EMAIL");
            user.setIsActive(true);
        });

        // when
        boolean isUpdated = userService.updateProfile(u.get());

        // then
        assertThat(isUpdated).isFalse();
    }

    @Test
    void shouldChangeThePassword() {
        // given
        UserDto u = userService.findByUsername("frankmoore8").orElse(null);
        assert u != null;

        // when
        boolean isChanged = userService.changePassword(u.getId(), "password8", "NEW PASSWORD");

        // then
        Optional<UserDto> user = userService.findByUsername("frankmoore8");
        assertAll(
                "Assertions for change password",
                () -> assertThat(isChanged).isTrue(),
                () -> assertThat(user).isPresent(),
                () -> assertThat(user.get().getPassword()).isEqualTo("NEW PASSWORD")
        );
    }

    @Test
    @DisplayName("Should not change the password if the old password is incorrect")
    void shouldNotChangeThePassword() {
        // given
        UserDto u = userService.findByUsername("frankmoore8").orElse(null);
        assert u != null;

        // when
        boolean isChanged = userService.changePassword(u.getId(), "wrong password", "NEW PASSWORD");

        // then
        assertAll(
                "Assertions for change password",
                () -> assertThat(isChanged).isFalse(),
                () -> assertThat(userService.findByUsername("frankmoore8").get().getPassword()).isNotEqualTo("NEW PASSWORD")
        );
    }

    @Test
    void shouldChangeTheActiveState() {
        // given
        UserDto u = userService.findByUsername("frankmoore8").orElse(null);
        assert u != null;

        // when
        boolean isActive = u.getIsActive();
        boolean isChanged = userService.updateActiveState(u.getId(), !isActive);

        // then
        Optional<UserDto> user = userService.findByUsername("frankmoore8");
        assertAll(
                "Assertions for change active state",
                () -> assertThat(isChanged).isTrue(),
                () -> assertThat(user).isPresent(),
                () -> assertThat(user.get().getIsActive()).isNotEqualTo(isActive)
        );
    }

    @Test
    void shouldSaveUser() {
        // given
        UserDto user = DtoEntityCreationUtil.getNewUserDtoInstance(51);

        System.out.println(user);
        // when
        userService.save(user);

        // then
        Optional<UserDto> savedUser = userService.findByUsername(user.getUsername());
        assertAll(
                "Assertions for save user",
                () -> assertThat(savedUser).isPresent(),
                () -> assertThat(savedUser.get().getUsername()).isEqualTo(user.getUsername()),
                () -> assertThat(savedUser.get().getFirstName()).isEqualTo(user.getFirstName()),
                () -> assertThat(savedUser.get().getLastName()).isEqualTo(user.getLastName()),
                () -> assertThat(userService.count()).isEqualTo(51)
        );
    }

    @Test
    void shouldGetAllUsers() {
        // when
        var users = userService.getAll();

        // then
        assertAll(
                "Assertions for get all users",
                () -> assertThat(users).isNotEmpty(),
                () -> assertThat(users.size()).isEqualTo(50)
        );
    }

    @Test
    void shouldDeleteUser() {
        // given
        UserDto user = DtoEntityCreationUtil.getNewUserDtoInstance(51);
        userService.save(user);

        // when
        boolean isDeleted = userService.delete(BigInteger.valueOf(1001)); // generation of new ids starts from 1000

        // then
        Optional<UserDto> deletedUser = userService.findByUsername(user.getUsername());
        assertAll(
                "Assertions for delete user",
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(deletedUser).isEmpty(),
                () -> assertThat(userService.count()).isEqualTo(50)
        );
    }

    @Test
    void shouldNotDeleteUser() {
        // when
        boolean isDeleted = userService.delete(BigInteger.valueOf(1001)); // generation of new ids starts from 1000

        // then
        assertThat(isDeleted).isFalse();
    }
}