package com.epam.repository;

import com.epam.entity.TrainerEntity;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerRepository extends MapRepositoryImpl<TrainerEntity, Long> {
}