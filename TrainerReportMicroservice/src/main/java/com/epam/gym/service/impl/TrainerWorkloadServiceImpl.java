package com.epam.gym.service.impl;

import com.epam.gym.dto.TrainerWorkloadRequest;
import com.epam.gym.dto.WorkloadDeleteRequest;
import com.epam.gym.entity.TrainerWorkloadEntity;
import com.epam.gym.repository.TrainerWorkloadRepository;
import com.epam.gym.service.TrainerWorkloadService;
import com.epam.gym.service.strategy.TrainingAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nurbek on 03.12.2024
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {
    private final TrainerWorkloadRepository trainerWorkloadRepository;

    @Override
    public void addWorkloadReport(TrainerWorkloadRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("TrainerWorkloadRequest cannot be null");
        }

        try {
            TrainerWorkloadEntity trainer = trainerWorkloadRepository.findByUsername(request.username())
                    .orElseGet(() -> defineTrainer(request));

            TrainingAction trainingAction = new TrainingAction(request.actionType());
            trainingAction.execute(trainer, request);

            trainerWorkloadRepository.save(trainer);
            log.trace("Saved the trainer workload successfully with username: {}", trainer.getUsername());
        } catch (Exception ex) {
            log.error("Unable to save the trainer workload from request: {}. Exception: {}", request, ex.getMessage());
        }
    }

    /**
     * Method deletes all trainers from database with usernames from the request
     * @param request - {@link WorkloadDeleteRequest} contains trainer usernames to delete
     */
    @Override
    public void deleteWorkloadReport(WorkloadDeleteRequest request) {
        try {
            trainerWorkloadRepository.deleteByUsernameIn(request.trainerUsernames());
            log.trace("Successfully deleted report for all trainers in the request: {}", request);
        } catch (Exception ex) {
            log.error("Unable to save the trainer workload from request: {}. Exception: {}", request, ex.getMessage());
        }
    }
}