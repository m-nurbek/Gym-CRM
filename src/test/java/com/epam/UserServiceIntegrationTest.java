package com.epam;

import com.epam.config.ApplicationConfig;
import com.epam.dto.UserDto;
import com.epam.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = ApplicationConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {
    private final UserService userService;

    @Autowired
    public UserServiceIntegrationTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void shouldFindUser() {
        // given
        BigInteger userId = BigInteger.ONE;
        BigInteger userId50 = BigInteger.valueOf(50L);

        // when
        UserDto foundUser = userService.get(userId).orElse(null);
        UserDto foundUser50 = userService.get(userId50).orElse(null);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser50).isNotNull();
    }

    @Test
    public void shouldNotFindUser() {
        // given
        BigInteger userId0 = BigInteger.ZERO;
        BigInteger userId51 = BigInteger.valueOf(51L);

        // when
        UserDto foundUser0 = userService.get(userId0).orElse(null);
        UserDto foundUser51 = userService.get(userId51).orElse(null);

        // then
        assertThat(foundUser0).isNull();
        assertThat(foundUser51).isNull();
    }

    @Test
    public void shouldAddUser() {
        // given
        UserDto user = UserDto.builder().firstName("NewUser").lastName("NewUserSurname").isActive(true).build();
        UserDto user2 = UserDto.builder().firstName("NewUser").lastName("NewUserSurname").isActive(true).build();
        UserDto user3 = UserDto.builder().firstName("NewUser").lastName("NewUserSurname").isActive(true).build();

        // when
        UserDto addedUser = userService.add(user);
        UserDto addedUser2 = userService.add(user2);
        UserDto addedUser3 = userService.add(user3);

        // then
        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isNotNull();
        assertThat(addedUser.getFirstName()).isEqualTo("NewUser");
        assertThat(addedUser.getLastName()).isEqualTo("NewUserSurname");
        assertThat(addedUser.isActive()).isTrue();
        assertThat(addedUser.getUsername()).isNotNull();
        assertThat(addedUser.getPassword()).isNotNull();
        assertThat(addedUser.getId()).isEqualTo(BigInteger.valueOf(51));
        assertThat(addedUser.getUsername()).isEqualTo("NewUser.NewUserSurname");
        assertThat(userService.get(addedUser.getId()).orElse(null)).isEqualTo(addedUser);

        assertThat(addedUser2).isNotNull();
        assertThat(addedUser2.getId()).isNotNull();
        assertThat(addedUser2.getFirstName()).isEqualTo("NewUser");
        assertThat(addedUser2.getLastName()).isEqualTo("NewUserSurname");
        assertThat(addedUser2.isActive()).isTrue();
        assertThat(addedUser2.getUsername()).isNotNull();
        assertThat(addedUser2.getPassword()).isNotNull();
        assertThat(addedUser2.getId()).isEqualTo(BigInteger.valueOf(52));
        assertThat(addedUser2.getUsername()).isEqualTo("NewUser.NewUserSurname1");
        assertThat(userService.get(addedUser2.getId()).orElse(null)).isEqualTo(addedUser2);


        assertThat(addedUser3).isNotNull();
        assertThat(addedUser3.getUsername()).isEqualTo("NewUser.NewUserSurname2");
    }

    @Test
    public void shouldNotAddUser() {
        // given
        UserDto user = UserDto.builder().id(BigInteger.ONE).firstName("NewUser").lastName("NewUserSurname").isActive(true).build();

        // when
        UserDto addedUser = userService.add(user);

        // then
        assertThat(addedUser).isNotNull();
        assertThat(addedUser).isNotEqualTo(user);
    }

    @Test
    public void shouldUpdateUser() {
        // given
        BigInteger userId = BigInteger.ONE;
        UserDto user = userService.get(userId).orElse(null);
        assert user != null;

        // when
        user.setFirstName("UpdatedFirstname");
        user.setLastName("UpdatedLastname");
        user.setActive(false);
        UserDto updatedUser = userService.update(user);

        // then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(userId);
        assertThat(updatedUser.getFirstName()).isEqualTo("UpdatedFirstname");
        assertThat(updatedUser.getLastName()).isEqualTo("UpdatedLastname");
        assertThat(updatedUser.isActive()).isFalse();
        assertThat(userService.get(userId).orElse(null)).isEqualTo(updatedUser);
    }

    @Test
    public void shouldNotUpdateUser() {
        // given
        BigInteger userId = BigInteger.valueOf(51L);
        UserDto user = UserDto.builder().id(userId).firstName("NewUser").lastName("NewUserSurname").isActive(true).build();

        // when
        UserDto updatedUser = userService.update(user);

        // then
        assertThat(updatedUser).isNull();
    }

    @Test
    public void shouldDeleteUser() {
        // given
        BigInteger userId = BigInteger.ONE;

        // when
        userService.delete(userId);

        // then
        assertThat(userService.get(userId).orElse(null)).isNull();
    }

    @Test
    public void shouldNotDeleteUser() {
        // given
        BigInteger userId = BigInteger.valueOf(51L);

        // when
        userService.delete(userId);

        // then
        assertThat(userService.get(userId).orElse(null)).isNull();
    }

    @Test
    public void shouldGetAllUsers() {
        // when
        var users = userService.getAll();

        // then
        assertThat(users).isNotNull();
        assertThat(users).isNotEmpty();
    }

    @Test
    public void shouldGetAllOnEmptyRepositoryCorrectly() {
        // given
        userService.getAll().forEach(u -> userService.delete(u.getId())); // deleted all users

        // when
        var users = userService.getAll();

        // then
        assertThat(users).isNotNull();
        assertThat(users).isEmpty();
    }
}