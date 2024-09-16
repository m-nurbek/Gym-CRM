package com.epam.gym.facade.command;

import com.epam.gym.aop.Authenticated;
import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.TrainingDto;
import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.entity.UserEntity;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingService;
import com.epam.gym.service.TrainingTypeService;
import com.epam.gym.service.UserService;
import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
    @Authenticated
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

        try {
            BigInteger id = shell.readBigInteger("Enter the ID: ");
            LocalDate dob = shell.readDate("Enter the date of birth (yyyy-MM-dd): ");
            String address = shell.readInput("Enter the address: ");
            UserDto user = userService.get(shell.readBigInteger("Enter the user ID: ")).orElse(null);

            traineeService.update(
                    new TraineeDto(id, dob, address, UserEntity.fromDto(user), List.of())
            );

        } catch (Exception e) {
            shell.writeOutput("Invalid input⛔");
        }
    }

    private void updateTrainer() {
        shell.writeOutput("Updating a trainer...");

        try {
            BigInteger id = shell.readBigInteger("Enter the ID: ");
            TrainingTypeDto specialization = trainingTypeService.get(shell.readBigInteger("Enter the training type ID: ")).orElse(null);
            UserDto user = userService.get(shell.readBigInteger("Enter the user ID: ")).orElse(null);

            trainerService.update(
                    new TrainerDto(id, TrainingTypeEntity.fromDto(specialization), UserEntity.fromDto(user), List.of()) // TODO: List.of()?
            );

        } catch (Exception e) {
            shell.writeOutput("Invalid input⛔");
        }
    }

    private void updateTrainingType() {
        shell.writeOutput("Updating a training type...");

        try {
            BigInteger id = shell.readBigInteger("Enter the ID: ");
            TrainingTypeEnum name = TrainingTypeEnum.valueOf(shell.readInput("Enter the training type name: "));

            trainingTypeService.update(
                    new TrainingTypeDto(id, name, Set.of(), Set.of()) // TODO: Set.of()?
            );

        } catch (IllegalArgumentException e) {
            shell.writeOutput("Invalid input⛔");
        }
    }

    private void updateTraining() {
        shell.writeOutput("Updating a training...");

        try {
            BigInteger id = shell.readBigInteger("Enter the ID: ");
            TrainingTypeDto specialization = trainingTypeService.get(shell.readBigInteger("Enter the training type ID: ")).orElse(null);
            LocalDate date = shell.readDate("Enter the date of birth (yyyy-MM-dd): ");
            String name = shell.readInput("Enter the training name: ");
            int duration = Integer.parseInt(shell.readInput("Enter the training duration: "));

            TrainerDto trainer = trainerService.get(shell.readBigInteger("Enter the trainer ID: ")).orElse(null);
            TraineeDto trainee = traineeService.get(shell.readBigInteger("Enter the trainee ID: ")).orElse(null);

            trainingService.update(
                    new TrainingDto(id, TraineeEntity.fromDto(trainee), TrainerEntity.fromDto(trainer), name, TrainingTypeEntity.fromDto(specialization), date, duration)
            );

        } catch (Exception e) {
            shell.writeOutput("Invalid input⛔");
        }
    }

    private void updateUser() {
        shell.writeOutput("Updating a user...");

        try {
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
        } catch (Exception e) {
            shell.writeOutput("Invalid input⛔");
        }
    }
}