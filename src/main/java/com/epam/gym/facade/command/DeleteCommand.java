package com.epam.gym.facade.command;

import com.epam.gym.aop.Authenticated;
import com.epam.gym.service.CrudService;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingService;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.service.UserService;
import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@AllArgsConstructor
public class DeleteCommand implements Command {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final TrainingService trainingService;
    private final UserService userService;
    private final Shell shell;

    @Override
    @Authenticated
    public void execute() {
        int option = shell.printAndGetOption("trainee", "trainer", "training type", "training", "user");

        switch (option) {
            case 1 -> deleteEntity(traineeService);
            case 2 -> deleteEntity(trainerService);
            case 3 -> deleteEntity(trainingTypeService);
            case 4 -> deleteEntity(trainingService);
            case 5 -> deleteEntity(userService);
            case -1 -> shell.writeOutput("Canceled.");
        }
    }

    private void deleteEntity(CrudService<?, BigInteger> service) {
        shell.writeOutput("Deleting an entity...");
        service.delete(shell.readBigInteger("Enter the ID: "));
    }
}