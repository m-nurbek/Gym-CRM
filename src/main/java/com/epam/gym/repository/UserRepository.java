package com.epam.gym.repository;

import com.epam.gym.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends HibernateRepository<UserEntity, BigInteger> {
    Optional<UserEntity> findByUsername(String username);

    boolean updateActiveState(BigInteger id, boolean isActive);

    boolean updatePassword(BigInteger id, String newPassword);
}