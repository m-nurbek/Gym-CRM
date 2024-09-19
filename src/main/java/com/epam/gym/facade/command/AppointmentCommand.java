package com.epam.gym.facade.command;

import com.epam.gym.aop.Authenticated;
import com.epam.gym.service.AuthService;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingService;
import com.epam.gym.util.Shell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class AppointmentCommand implements Command {
    private final Shell shell;
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthService authService;

    @Override
    @Authenticated
    public void execute() {
        int option = shell.printAndGetOption("new", "cancel", "my", "trainers", "my trainers");

        switch (option) {
            case 1 -> createAppointment();
            case 2 -> cancelAppointment();
            case 3 -> listMyAppointments();
            case 4 -> listAllTrainers();
            case 5 -> listMyTrainers();
            case -1 -> shell.writeOutput("Canceled.");
        }
    }

    private void createAppointment() {
        shell.writeOutput("Creating a new appointment...");

        String username = authService.getUsernameOfAuthenticatedAccount();

        traineeService.getUnassignedTrainersByUsername(username).forEach(
                x -> trainerService.getUserProfile(x.getId()).ifPresent(
                        y -> shell.writeOutput(y.toString())
                )
        );

        try {
            BigInteger trainerId = shell.readBigInteger("Enter the ID of the trainer you want to assign to: ");
            LocalDate date = shell.readDate("Enter the date of the appointment (yyyy-MM-dd): ");
            String name = shell.readInput("Enter the name of the appointment: ");
            String type = shell.readInput("Enter the type of the training: ");
            int duration = Integer.parseInt(shell.readInput("Enter the duration of the training: "));

            trainingService.assignTrainer(username, trainerId, name, type, date, duration);
        } catch (Exception e) {
            shell.writeOutput("Invalid input. Please try again.");
        }
    }

    private void cancelAppointment() {
        shell.writeOutput("Canceling an appointment...");

        String username = authService.getUsernameOfAuthenticatedAccount();

        traineeService.getTrainers(username).forEach(
                x -> trainerService.getUserProfile(x.getId()).ifPresent(
                        y -> shell.writeOutput(y.toString())
                )
        );

        try {
            BigInteger trainerId = shell.readBigInteger("Enter the ID of the trainer you want to unassign: ");

            trainingService.unassignTrainer(username, trainerId);
        } catch (Exception e) {
            shell.writeOutput("Invalid input. Please try again.");
        }
    }

    private void listMyAppointments() {
        shell.writeOutput("Listing my appointments...");

        String username = authService.getUsernameOfAuthenticatedAccount();

        traineeService.getTrainingsByUsername(username).forEach(
                x -> shell.writeOutput(x.toString())
        );
    }

    /**
     * Lists all trainers that are not assigned to a trainee by username
     */
    private void listAllTrainers() {
        shell.writeOutput("Listing all trainers you are not assigned to...");

        String username = authService.getUsernameOfAuthenticatedAccount();

        traineeService.getUnassignedTrainersByUsername(username).forEach(
                x -> trainerService.getUserProfile(x.getId()).ifPresent(
                        y -> shell.writeOutput(y.toString())
                )
        );
    }

    private void listMyTrainers() {
        shell.writeOutput("Listing my trainers...");

        String username = authService.getUsernameOfAuthenticatedAccount();

        traineeService.getTrainers(username).forEach(
                x -> trainerService.getUserProfile(x.getId()).ifPresent(
                        y -> shell.writeOutput(y.toString())
                )
        );
    }
}