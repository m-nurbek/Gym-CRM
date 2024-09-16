package com.epam.gym.facade.command;

import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommandFactory {
    private final CreateCommand createCommand;
    private final FindCommand findCommand;
    private final UpdateCommand updateCommand;
    private final DeleteCommand deleteCommand;
    private final ListCommand listCommand;
    private final RegisterCommand registerCommand;
    private final LoginCommand loginCommand;
    private final LogoutCommand logoutCommand;

    public Command getCommand(String command) {
        return switch (command) {
            case Shell.CREATE -> createCommand;
            case Shell.FIND -> findCommand;
            case Shell.UPDATE -> updateCommand;
            case Shell.DELETE -> deleteCommand;
            case Shell.LIST -> listCommand;
            case Shell.REGISTER -> registerCommand;
            case Shell.LOGIN -> loginCommand;
            case Shell.LOGOUT -> logoutCommand;
            default -> null;
        };
    }
}