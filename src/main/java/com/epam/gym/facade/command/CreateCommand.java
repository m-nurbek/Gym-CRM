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

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class CreateCommand implements Command {
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

        try {
            LocalDate dob = shell.readDate("Enter the date of birth (yyyy-MM-dd): ");
            String address = shell.readInput("Enter the address: ");
            UserDto user = userService.get(shell.readBigInteger("Enter the user ID: ")).orElse(null);

            if (user == null) {
                shell.writeOutput("User not found.");
                return;
            }

            traineeService.save(
                    new TraineeDto(null, dob, address, UserEntity.fromDto(user), List.of())
            );

        } catch (Exception e) {
            shell.writeOutput("Invalid input⛔");
        }
    }

    private void createTrainer() {
        shell.writeOutput("Creating a new trainer...");

        try {
            UserDto user = userService.get(shell.readBigInteger("Enter the user ID: ")).orElse(null);
            TrainingTypeDto specialization = trainingTypeService.get(shell.readBigInteger("Enter the training type ID: ")).orElse(null);

            if (user == null) {
                shell.writeOutput("User not found.");
                return;
            }

            if (specialization == null) {
                shell.writeOutput("Training type not found.");
                return;
            }

            trainerService.save(
                    new TrainerDto(null, TrainingTypeEntity.fromDto(specialization), UserEntity.fromDto(user), List.of())
            );

        } catch (Exception e) {
            shell.writeOutput("Invalid input⛔");
        }
    }

    private void createTrainingType() {
        shell.writeOutput("Creating a new training type...");

        String name = shell.readInput("Enter the training type name: ");

        trainingTypeService.save(
                TrainingTypeDto.builder().name(TrainingTypeEnum.valueOf(name)).build()
        );
    }

    private void createTraining() {
        shell.writeOutput("Creating a new training...");

        try {
            TrainingTypeDto type = trainingTypeService.get(shell.readBigInteger("Enter the training type ID: ")).orElse(null);
            TrainerDto trainer = trainerService.get(shell.readBigInteger("Enter the trainer ID: ")).orElse(null);
            TraineeDto trainee = traineeService.get(shell.readBigInteger("Enter the trainee ID: ")).orElse(null);

            if (type == null) {
                shell.writeOutput("Training type not found.");
                return;
            }

            if (trainer == null) {
                shell.writeOutput("Trainer not found.");
                return;
            }

            if (trainee == null) {
                shell.writeOutput("Trainee not found.");
                return;
            }

            LocalDate date = shell.readDate("Enter the date of birth (yyyy-MM-dd): ");
            String name = shell.readInput("Enter the training name: ");

            int duration = 0;

            try {
                duration = Integer.parseInt(shell.readInput("Enter the training duration: "));
            } catch (NumberFormatException e) {
                shell.writeOutput("Invalid input.");
                return;
            }

            trainingService.save(
                    new TrainingDto(null,
                            TraineeEntity.fromDto(trainee),
                            TrainerEntity.fromDto(trainer),
                            name,
                            TrainingTypeEntity.fromDto(type),
                            date,
                            duration
                    )
            );

        } catch (Exception e) {
            shell.writeOutput("Invalid input⛔");
        }
    }

    private void createUser() {
        shell.writeOutput("Creating a new user...");

        String firstName = shell.readInput("Enter the first name: ");
        String lastName = shell.readInput("Enter the last name: ");
        boolean isActive = shell.readBoolean("Is the user active? (yes/no): ");

        userService.save(
                UserDto.builder().firstName(firstName).lastName(lastName).isActive(isActive).build()
        );
    }
}