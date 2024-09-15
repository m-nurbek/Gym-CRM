package com.epam.gym.repository.impl;

import com.epam.gym.entity.TrainerEntity;
import com.epam.gym.repository.TrainerRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TrainerRepositoryImpl extends HibernateRepositoryImpl<TrainerEntity, BigInteger> implements TrainerRepository {
    public TrainerRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}