package com.epam.gym.facade;

import com.epam.gym.facade.command.Command;
import com.epam.gym.facade.command.CommandFactory;
import com.epam.gym.util.Shell;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Facade {
    private final Shell shell;
    private final CommandFactory commandFactory;

    public void run() {
        while (true) {
            String input = shell.readInput();

            if (!shell.isValidCommand(input)) {
                shell.writeOutput("Invalid command. Type 'help' to see the list of available commands.");
                continue;
            }

            if (shell.equals(input, Shell.EXIT)) {
                break;
            }

            if (shell.equals(input, Shell.HELP)) {
                shell.printHelp();
                continue;
            }

            Command command = commandFactory.getCommand(input);

            if (command != null) {
                command.execute();
            }
        }
    }
}