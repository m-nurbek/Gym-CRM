package com.epam.gym.facade.command;

import com.epam.gym.dto.*;
import com.epam.gym.service.*;
import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;

@Component
@AllArgsConstructor
public class UpdateCommand implements Command {
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
            case 1 -> updateTrainee();
            case 2 -> updateTrainer();
            case 3 -> updateTrainingType();
            case 4 -> updateTraining();
            case 5 -> updateUser();
            case -1 -> shell.writeOutput("Canceled.");
        }
    }

    private void updateTrainee() {
        shell.writeOutput("Updating a trainee...");

        BigInteger id = shell.readBigInteger("Enter the ID: ");
        Date dob = shell.readDate("Enter the date of birth (yyyy-MM-dd): ");
        String address = shell.readInput("Enter the address: ");
        UserDto user = userService.get(shell.readBigInteger("Enter the user ID: ")).orElse(null);

        traineeService.update(
                TraineeDto.builder()
                        .id(id)
                        .dob(dob)
                        .address(address)
                        .user(user)
                        .build()
        );
    }

    private void updateTrainer() {
        shell.writeOutput("Updating a trainer...");

        BigInteger id = shell.readBigInteger("Enter the ID: ");
        UserDto user = userService.get(shell.readBigInteger("Enter the user ID: ")).orElse(null);
        TrainingTypeDto specialization = trainingTypeService.get(shell.readBigInteger("Enter the training type ID: ")).orElse(null);

        trainerService.update(
                TrainerDto.builder()
                        .id(id)
                        .specialization(specialization)
                        .user(user)
                        .build()
        );
    }

    private void updateTrainingType() {
        shell.writeOutput("Updating a training type...");

        BigInteger id = shell.readBigInteger("Enter the ID: ");
        String name = shell.readInput("Enter the training type name: ");

        trainingTypeService.update(
                TrainingTypeDto.builder()
                        .id(id)
                        .name(name)
                        .build()
        );
    }

    private void updateTraining() {
        shell.writeOutput("Updating a training...");

        BigInteger id = shell.readBigInteger("Enter the ID: ");
        TrainingTypeDto specialization = trainingTypeService.get(shell.readBigInteger("Enter the training type ID: ")).orElse(null);
        Date date = shell.readDate("Enter the date of birth (yyyy-MM-dd): ");
        String name = shell.readInput("Enter the training name: ");
        String duration = shell.readInput("Enter the training duration: ");
        TrainerDto trainer = trainerService.get(shell.readBigInteger("Enter the trainer ID: ")).orElse(null);
        TraineeDto trainee = traineeService.get(shell.readBigInteger("Enter the trainee ID: ")).orElse(null);

        trainingService.update(
                TrainingDto.builder()
                        .id(id)
                        .date(date)
                        .name(name)
                        .duration(duration)
                        .trainer(trainer)
                        .trainee(trainee)
                        .type(specialization)
                        .build()
        );
    }

    private void updateUser() {
        shell.writeOutput("Updating a user...");

        BigInteger id = shell.readBigInteger("Enter the ID: ");
        String firstName = shell.readInput("Enter the first name: ");
        String lastName = shell.readInput("Enter the last name: ");
        boolean isActive = shell.readBoolean("Is the user active? (yes/no): ");

        userService.update(
                UserDto.builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .isActive(isActive)
                        .build()
        );
    }
}