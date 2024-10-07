package com.epam.gym.service.serviceImpl;

import com.epam.gym.dto.response.TrainingTypeResponseDto;
import com.epam.gym.repository.TrainingTypeRepository;
import com.epam.gym.service.TrainingTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public Set<TrainingTypeResponseDto> getAllToResponse() {
        return trainingTypeRepository.findAll().stream().map(
                type -> new TrainingTypeResponseDto(
                        type.getName().name(),
                        type.getId()
                )
        ).collect(Collectors.toSet());
    }
}