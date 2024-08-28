package com.epam.repository;

import com.epam.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends MapRepositoryImpl<UserEntity, Long> {
}