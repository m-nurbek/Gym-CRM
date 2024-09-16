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
public class FindCommand implements Command{
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
//            case 2 -> findEntity(trainerService);
//            case 3 -> findEntity(trainingTypeService);
//            case 4 -> findEntity(trainingService);
//            case 5 -> findEntity(userService);
            default -> shell.writeOutput("Canceled.");
        }
    }

    private void findEntity(TraineeService service) {
        var output = shell.readInput("Find by ID or by username(1/2)? ");

        try {
            int option = Integer.parseInt(output);

            if (option == 1) {
                shell.writeOutput("Finding a trainee by ID...");
                service.get(shell.readBigInteger("Enter the ID: ")).ifPresentOrElse(
                        x -> shell.writeOutput(x.toString()),
                        () -> shell.writeOutput("Trainee not found.")
                );
            } else if (option == 2) {
                shell.writeOutput("Finding a trainee by username...");
                service.findByUsername(shell.readInput("Enter the username: ")).ifPresentOrElse(
                        x -> shell.writeOutput(x.toString()),
                        () -> shell.writeOutput("Trainee not found.")
                );
            } else {
                shell.writeOutput("Invalid input.");
            }
        } catch (NumberFormatException e) {
            shell.writeOutput("Invalid input.");
            return;
        }
    }

//    private void findEntity(CrudService<?, BigInteger> service) {
//        shell.writeOutput("Finding an entity...");
//
//        service.get(shell.readBigInteger("Enter the ID: ")).ifPresentOrElse(
//                x -> shell.writeOutput(x.toString()),
//                () -> shell.writeOutput("Entity not found.")
//        );
//    }
}