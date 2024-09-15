package com.epam.gym.entity;

import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.repository.TrainingTypeRepository;
import com.epam.gym.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "TRAINER")
public class TrainerEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private BigInteger id;

    @Transient
    private BigInteger specialization; // training type
    @Transient
    private BigInteger userId;

    @ManyToOne
    @JoinColumn(name = "SPECIALIZATION", referencedColumnName = "ID")
    private TrainingTypeEntity trainingType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserEntity user;

    @OneToMany(mappedBy = "trainer")
    @ToString.Exclude
    List<TrainingEntity> trainings;

    @ManyToMany
    @JoinTable(
            name = "TRAINEE_TRAINER_TABLE",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    @ToString.Exclude
    List<TraineeEntity> trainees;

    @JsonCreator
    public TrainerEntity(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("specialization") BigInteger specialization,
            @JsonProperty("userId") BigInteger userId
    ) {
        this.id = id;
        this.specialization = specialization;
        this.userId = userId;
    }

    @Deprecated(since = "2024-09-12", forRemoval = false)
    public TrainerDto toDto(TrainingTypeRepository trainingTypeRepository, UserRepository userRepository) {
        AtomicReference<TrainingTypeDto> trainingTypeDto = new AtomicReference<>();
        AtomicReference<UserDto> userDto = new AtomicReference<>();

        trainingTypeRepository.findById(specialization).ifPresent(s -> trainingTypeDto.set(s.toDto()));
        userRepository.findById(userId).ifPresent(u -> userDto.set(u.toDto()));

        return new TrainerDto(id, trainingTypeDto.get(), userDto.get());
    }

    public static TrainerEntity fromDto(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }

        Assert.notNull(trainerDto.getSpecialization(), "'trainingType' attribute must not be null");
        Assert.notNull(trainerDto.getUser(), "'user' attribute must not be null");

        return new TrainerEntity(
                trainerDto.getId(),
                EntityInterface.getIdFromDto(trainerDto.getSpecialization()),
                EntityInterface.getIdFromDto(trainerDto.getUser()),
                trainerDto.getSpecialization() == null ? null : TrainingTypeEntity.fromDto(trainerDto.getSpecialization()),
                trainerDto.getUser() == null ? null : UserEntity.fromDto(trainerDto.getUser()),
                trainerDto.getTrainings() == null ? null : trainerDto.getTrainings().stream().map(TrainingEntity::fromDto).toList(),
                trainerDto.getTrainees() == null ? null : trainerDto.getTrainees().stream().map(TraineeEntity::fromDto).toList()
        );
    }

    public TrainerDto toDto() {
        Assert.notNull(trainingType, "'trainingType' attribute must not be null");
        Assert.notNull(user, "'user' attribute must not be null");

        return new TrainerDto(
                id,
                trainingType.toDto(),
                user.toDto(),
                trainings == null ? null : trainings.stream().map(TrainingEntity::toDto).toList(),
                trainees == null ? null : trainees.stream().map(TraineeEntity::toDto).toList()
        );
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TrainerEntity that = (TrainerEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}