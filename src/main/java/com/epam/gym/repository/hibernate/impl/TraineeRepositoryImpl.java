package com.epam.gym.repository.hibernate.impl;

import com.epam.gym.entity.TraineeEntity;
import com.epam.gym.repository.hibernate.TraineeRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TraineeRepositoryImpl extends HibernateRepositoryImpl<TraineeEntity, BigInteger> implements TraineeRepository {
    public TraineeRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}