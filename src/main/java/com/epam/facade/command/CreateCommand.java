package com.epam.facade.command;

import com.epam.dto.*;
import com.epam.service.*;
import com.epam.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class CreateCommand implements Command {
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
            case 1 -> createTrainee();
            case 2 -> createTrainer();
            case 3 -> createTrainingType();
            case 4 -> createTraining();
            case 5 -> createUser();
            case -1 -> shell.writeOutput("Canceled.");
        }
    }

    private void createTrainee() {
        shell.writeOutput("Creating a new trainee...");

        Date dob = shell.readDate("Enter the date of birth (yyyy-MM-dd): ");
        String address = shell.readInput("Enter the address: ");
        UserDto user = userService.get(shell.readBigInteger("Enter the user ID: ")).orElse(null);

        traineeService.add(
                TraineeDto.builder().dob(dob).address(address).user(user).build()
        );
    }

    private void createTrainer() {
        shell.writeOutput("Creating a new trainer...");

        UserDto user = userService.get(shell.readBigInteger("Enter the user ID: ")).orElse(null);
        TrainingTypeDto specialization = trainingTypeService.get(shell.readBigInteger("Enter the training type ID: ")).orElse(null);

        trainerService.add(
                TrainerDto.builder().specialization(specialization).user(user).build()
        );
    }

    private void createTrainingType() {
        shell.writeOutput("Creating a new training type...");

        String name = shell.readInput("Enter the training type name: ");

        trainingTypeService.add(
                TrainingTypeDto.builder().name(name).build()
        );
    }

    private void createTraining() {
        shell.writeOutput("Creating a new training...");

        TrainingTypeDto specialization = trainingTypeService.get(shell.readBigInteger("Enter the training type ID: ")).orElse(null);
        Date date = shell.readDate("Enter the date of birth (yyyy-MM-dd): ");
        String name = shell.readInput("Enter the training name: ");
        String duration = shell.readInput("Enter the training duration: ");
        TrainerDto trainer = trainerService.get(shell.readBigInteger("Enter the trainer ID: ")).orElse(null);
        TraineeDto trainee = traineeService.get(shell.readBigInteger("Enter the trainee ID: ")).orElse(null);

        trainingService.add(
                TrainingDto.builder().date(date).name(name).duration(duration).trainer(trainer).trainee(trainee).type(specialization).build()
        );
    }

    private void createUser() {
        shell.writeOutput("Creating a new user...");

        String firstName = shell.readInput("Enter the first name: ");
        String lastName = shell.readInput("Enter the last name: ");
        boolean isActive = shell.readBoolean("Is the user active? (yes/no): ");

        userService.add(
                UserDto.builder().firstName(firstName).lastName(lastName).isActive(isActive).build()
        );
    }
}