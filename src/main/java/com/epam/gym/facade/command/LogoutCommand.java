package com.epam.gym.facade.command;

import com.epam.gym.service.AuthService;
import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LogoutCommand implements Command {
    private final AuthService authService;
    private final Shell shell;

    @Override
    public void execute() {
        var s = shell.readInput("Type username: ");

        if (authService.logout(s)) {
            shell.writeOutput("Logout successful.");
        } else {
            shell.writeOutput("Logout failed.");
        }
    }
}