package com.epam.gym.repository;

import com.epam.gym.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, BigInteger> {
    @Transactional
    @Modifying
    @Query("delete from RefreshTokenEntity r where r.username = :username")
    void deleteByUsername(String username);

    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByUsername(String username);
}