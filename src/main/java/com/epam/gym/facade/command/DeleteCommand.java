package com.epam.gym.facade.command;

import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingService;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.service.UserService;
import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteCommand implements Command {
    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingTypeService trainingTypeService;
    private TrainingService trainingService;
    private UserService userService;
    private Shell shell;

    @Override
    public void execute() {
        int option = shell.printAndGetOption("trainee", "trainer", "training type", "training", "user");

//        switch (option) {
//            case 1 -> deleteEntity(traineeService);
//            case 2 -> deleteEntity(trainerService);
//            case 3 -> deleteEntity(trainingTypeService);
//            case 4 -> deleteEntity(trainingService);
//            case 5 -> deleteEntity(userService);
//            case -1 -> shell.writeOutput("Canceled.");
//        }
    }

}