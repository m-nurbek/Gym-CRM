package com.epam.repository;

import com.epam.entity.TrainerEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class TrainerRepository extends MapRepositoryImpl<TrainerEntity, BigInteger> {
}