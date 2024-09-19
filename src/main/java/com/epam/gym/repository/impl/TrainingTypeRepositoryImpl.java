package com.epam.gym.repository.impl;

import com.epam.gym.entity.TrainingTypeEntity;
import com.epam.gym.entity.TrainingTypeEnum;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.TrainingTypeRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public class TrainingTypeRepositoryImpl extends HibernateRepositoryImpl<TrainingTypeEntity, BigInteger> implements TrainingTypeRepository {
    @PersistenceContext
    private Session session;
    private final TrainerRepository trainerRepository;

    public TrainingTypeRepositoryImpl(TrainerRepository trainerRepository) {
        super();
        this.trainerRepository = trainerRepository;
    }

    @Override
    public boolean deleteById(BigInteger id) {
        Assert.notNull(id, HibernateRepositoryImpl.ID_MUST_NOT_BE_NULL);

        var entity = session.get(TrainingTypeEntity.class, id);

        if (entity != null) {
            if (entity.getTrainers() != null) {
                entity.getTrainers().forEach(trainer -> trainerRepository.deleteById(trainer.getId()));
            }

            session.remove(entity);
            return true;
        }

        return false;
    }

    @Override
    public Optional<TrainingTypeEntity> findByName(String name) {
        var query = session.createQuery("from TrainingTypeEntity where name = :name", TrainingTypeEntity.class);
        query.setParameter("name", TrainingTypeEnum.valueOf(name));

        return Optional.ofNullable(query.uniqueResult());
    }
}