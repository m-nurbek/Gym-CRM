package com.epam.gym.facade.command;

import com.epam.gym.aop.Authenticated;
import com.epam.gym.service.AuthService;
import com.epam.gym.service.UserService;
import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountCommand implements Command {
    private final Shell shell;
    private final UserService userService;
    private final AuthService authService;

    @Override
    @Authenticated
    public void execute() {
        int option = shell.printAndGetOption("activate", "deactivate");

        switch (option) {
            case 1 -> setActiveStateOfAnAccount(true);
            case 2 -> setActiveStateOfAnAccount(false);
            case -1 -> shell.writeOutput("Canceled.");
        }
    }

    private void setActiveStateOfAnAccount(boolean isActive) {
        shell.writeOutput(isActive ? "Activating account..." : "Deactivating account...");

        String username = authService.getUsernameOfAuthenticatedAccount();

        var user = userService.findByUsername(username);

        if (user.isEmpty()) {
            shell.writeOutput("User not found.");
            return;
        }

        if (user.get().getIsActive() == isActive) {
            shell.writeOutput("Account is already %s".formatted(isActive ? "active." : "inactive."));
            return;
        }

        try {
            userService.updateActiveState(user.get().getId(), isActive);
        } catch (Exception e) {
            shell.writeOutput("Invalid input. Please try again.");
        }
    }
}