package com.epam.gym.repository.impl;

import com.epam.gym.entity.TrainingEntity;
import com.epam.gym.repository.TrainingRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TrainingRepositoryImpl extends HibernateRepositoryImpl<TrainingEntity, BigInteger> implements TrainingRepository {
    public TrainingRepositoryImpl() {
        super();
    }
}