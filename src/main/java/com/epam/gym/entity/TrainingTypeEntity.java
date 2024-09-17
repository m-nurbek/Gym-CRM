package com.epam.gym.entity;

import com.epam.gym.dto.TrainingTypeDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "TRAINING_TYPE")
public class TrainingTypeEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "training_type_table_seq")
    @TableGenerator(
            name = "training_type_table_seq",
            table = "id_gen_table",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000,
            allocationSize = 1)
    @Column(name = "ID")
    private BigInteger id;
    @Column(name = "NAME")
    @Enumerated(EnumType.STRING)
    private TrainingTypeEnum name;

    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<TrainerEntity> trainers;
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<TrainingEntity> trainings;

    public TrainingTypeDto toDto() {
        return new TrainingTypeDto(id, name, trainers, trainings);
    }

    public static TrainingTypeEntity fromDto(TrainingTypeDto dto) {
        if (dto == null) {
            return null;
        }

        return new TrainingTypeEntity(dto.getId(), dto.getName(), dto.getTrainers(), dto.getTrainings());
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