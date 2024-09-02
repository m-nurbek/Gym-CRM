package com.epam.facade.command;

import com.epam.service.*;
import com.epam.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListCommand implements Command {
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
            case 1 -> listEntities(traineeService);
            case 2 -> listEntities(trainerService);
            case 3 -> listEntities(trainingTypeService);
            case 4 -> listEntities(trainingService);
            case 5 -> listEntities(userService);
            case -1 -> shell.writeOutput("Canceled.");
        }
    }

    private void listEntities(CrudService<?, ?> service) {
        shell.writeOutput("Listing all trainees...");
        service.getAll().forEach(x -> shell.writeOutput(x.toString()));
    }
}