package com.epam.gym.service.hibernate.serviceImpl;

import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.repository.hibernate.TrainerRepository;
import com.epam.gym.service.hibernate.TrainerService;
import com.epam.gym.service.hibernate.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    private final UserService userService;
    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerServiceImpl(UserService userService, TrainerRepository trainerRepository) {
        this.userService = userService;
        this.trainerRepository = trainerRepository;
    }

    @Override
    @Transactional
    public Optional<TrainerDto> findByUsername(String username) {
        Optional<UserDto> userDto = userService.findByUsername(username);

        if (userDto.isPresent()) {
            var trainer = trainerRepository.findById(userDto.get().getId());

            if (trainer.isPresent()) {
                TrainerEntity trainerEntity = trainer.get();
                return Optional.of(trainerEntity.toDto());
            }
        }

        return Optional.empty();
    }
}