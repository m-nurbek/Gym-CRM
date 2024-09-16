package com.epam.gym.facade.command;

import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommandFactory {
    private CreateCommand createCommand;
    private FindCommand findCommand;
    private UpdateCommand updateCommand;
    private DeleteCommand deleteCommand;
    private ListCommand listCommand;
    private RegisterCommand registerCommand;
    private LoginCommand loginCommand;
    private LogoutCommand logoutCommand;

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