package com.epam.gym.service.serviceImpl;

import com.epam.gym.aop.Loggable;
import com.epam.gym.dto.UserDto;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.AuthService;
import com.epam.gym.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor
@Component
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserService userService;

    /*
     * This map is used to store the authentication status of the user.
     * The key is the username and the value is the authentication status.
     * will contain only one entry at a time.
     */
    private final ConcurrentMap<String, Boolean> map = new ConcurrentHashMap<>(1);

    @Loggable
    @Override
    public boolean authenticate(String username, String password) {
        if (isAuthenticated(username)) {
            return true;
        }

        boolean status = userRepository.isUsernameAndPasswordMatch(username, password);

        if (status) {
            logoutOfAllAccounts();
            map.put(username, true);
        }

        return status;
    }

    @Loggable
    @Override
    public boolean isAuthenticated(String username) {
        return map.getOrDefault(username, false);
    }

    @Loggable
    @Override
    public boolean logout(String username) {
        return map.remove(username);
    }

    @Override
    public void logoutOfAllAccounts() {
        map.clear();
    }

    @Override
    public String[] register(String firstName, String lastName) {
        var user = userService.save(
                new UserDto(null, firstName, lastName, "username", "password", true, null, null)
        );

        return new String[]{user.getUsername(), user.getPassword()};
    }

    @Override
    public String getUsernameOfAuthenticatedAccount() {
        return map.keySet().stream().findFirst().orElse(null);
    }
}