package com.epam.repository;

import com.epam.entity.TrainingEntity;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingRepository extends MapRepositoryImpl<TrainingEntity, Long> {
}