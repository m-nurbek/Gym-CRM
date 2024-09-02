package com.epam.facade.command;

import com.epam.service.*;
import com.epam.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@AllArgsConstructor
public class FindCommand implements Command {
    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingTypeService trainingTypeService;
    private TrainingService trainingService;
    private UserService userService;
    private Shell shell;

    @Override
    public void execute() {
        int option = shell.printAndGetOption("trainee", "trainer", "training type", "training", "user");

        switch (option) {
            case 1 -> findEntity(traineeService);
            case 2 -> findEntity(trainerService);
            case 3 -> findEntity(trainingTypeService);
            case 4 -> findEntity(trainingService);
            case 5 -> findEntity(userService);
            case -1 -> shell.writeOutput("Canceled.");
        }
    }

    private void findEntity(CrudService<?, BigInteger> service) {
        shell.writeOutput("Finding an entity...");

        service.get(shell.readBigInteger("Enter the ID: ")).ifPresentOrElse(
                x -> shell.writeOutput(x.toString()),
                () -> shell.writeOutput("Entity not found.")
        );
    }
}