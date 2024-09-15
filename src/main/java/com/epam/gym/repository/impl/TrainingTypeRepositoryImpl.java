package com.epam.gym.repository.impl;

import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.repository.TrainingTypeRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TrainingTypeRepositoryImpl extends HibernateRepositoryImpl<TrainingTypeEntity, BigInteger> implements TrainingTypeRepository {
    public TrainingTypeRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}