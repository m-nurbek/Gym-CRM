package com.epam.gym.repository.hibernate.impl;

import com.epam.gym.entity.UserEntity;
import com.epam.gym.repository.hibernate.UserRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class UserRepositoryImpl extends HibernateRepositoryImpl<UserEntity, BigInteger> implements UserRepository {
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}