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
        TrainerWorkloadEntity trainer = trainerWorkloadRepository.findByUsername(request.username())
                .orElseGet(() -> defineTrainer(request));

        TrainingAction trainingAction = new TrainingAction(request.actionType());
        trainingAction.execute(trainer, request);

        trainerWorkloadRepository.save(trainer);
        log.trace("Saved the trainer workload successfully");
    }

    @Override
    public void deleteWorkloadReport(WorkloadDeleteRequest request) {
        trainerWorkloadRepository.deleteByUsernameIn(request.trainerUsernames());
        log.trace("Saved the trainer workload successfully");
    }
}