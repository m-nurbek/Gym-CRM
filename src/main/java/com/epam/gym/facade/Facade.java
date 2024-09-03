package com.epam.gym.facade;

import com.epam.gym.facade.command.Command;
import com.epam.gym.facade.command.CommandFactory;
import com.epam.gym.util.Shell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Facade {
    private final Shell shell;
    private final CommandFactory commandFactory;

    @Autowired
    public Facade(Shell shell, CommandFactory commandFactory) {
        this.shell = shell;
        this.commandFactory = commandFactory;
    }

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