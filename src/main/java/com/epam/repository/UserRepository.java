package com.epam.repository;

import com.epam.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class UserRepository extends MapRepositoryImpl<UserEntity, BigInteger> {
}