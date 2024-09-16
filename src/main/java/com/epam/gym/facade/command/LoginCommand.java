package com.epam.gym.facade.command;

import com.epam.gym.service.AuthService;
import com.epam.gym.service.UserService;
import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LoginCommand implements Command {
    private final UserService userService;
    private final AuthService authService;
    private final Shell shell;

    @Override
    public void execute() {
        String username = shell.readInput("Enter the username: ");
        String password = shell.readInput("Enter the password: ");

        if (authService.authenticate(username, password)) {
            shell.writeOutput("Login successful.");

            var userEntity = userService.findByUsername(username);
            assert userEntity.isPresent(); // userEntity has to be present since the user has been authenticated

            String firstName = userEntity.get().getFirstName();
            String lastName = userEntity.get().getLastName();

            shell.writeOutput("Welcome, %s %s!".formatted(firstName, lastName));
        } else {
            shell.writeOutput("Login failed.");
        }

    }
}