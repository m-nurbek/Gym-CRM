package com.epam.gym.entity;

import com.epam.gym.dto.TrainingTypeDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "TRAINING_TYPE")
public class TrainingTypeEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private BigInteger id;
    @Column(name = "NAME")
    @Enumerated(EnumType.STRING)
    private TrainingTypeEnum name;

    @OneToMany(mappedBy = "trainingType")
    @Transient
    @ToString.Exclude
    private List<TrainerEntity> trainers;
    @OneToMany(mappedBy = "trainingType")
    @Transient
    @ToString.Exclude
    private List<TrainingEntity> trainings;

    @JsonCreator
    public TrainingTypeEntity(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("name") String name
    ) {
        this.id = id;
        this.name = TrainingTypeEnum.valueOf(name);
    }

    public TrainingTypeDto toDto() {
        return new TrainingTypeDto(
                id,
                name,
                trainers == null ? null : trainers.stream().map(TrainerEntity::toDto).toList(),
                trainings == null ? null : trainings.stream().map(TrainingEntity::toDto).toList()
        );
    }

    public static TrainingTypeEntity fromDto(TrainingTypeDto type) {
        if (type == null) {
            return null;
        }

        return new TrainingTypeEntity(
                type.getId(),
                type.getName(),
                type.getTrainers() == null ? null : type.getTrainers().stream().map(TrainerEntity::fromDto).toList(),
                type.getTrainings() == null ? null : type.getTrainings().stream().map(TrainingEntity::fromDto).toList()
        );
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TrainingTypeEntity that = (TrainingTypeEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}