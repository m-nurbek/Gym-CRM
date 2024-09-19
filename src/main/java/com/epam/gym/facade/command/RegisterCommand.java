package com.epam.gym.facade.command;

import com.epam.gym.service.AuthService;
import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RegisterCommand implements Command {
    private final AuthService authService;
    private final Shell shell;

    @Override
    public void execute() {
        String firstName = shell.readInput("Enter your first name: ");
        String lastName = shell.readInput("Enter your last name: ");

        String[] usernameAndPassword = authService.register(firstName, lastName);

        shell.writeOutput("Registration successful.");
        shell.writeOutput("\nYOUR USERNAME: %s\nYOUR PASSWORD: %s\n".formatted(usernameAndPassword[0], usernameAndPassword[1]));
        shell.writeOutput("Please remember your username and password.\nYou can proceed to login now✨✨✨");
    }
}