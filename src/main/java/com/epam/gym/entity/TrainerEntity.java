package com.epam.gym.entity;

import com.epam.gym.dto.TrainerDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "TRAINER")
public class TrainerEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "trainer_table_seq")
    @TableGenerator(
            name = "trainer_table_seq",
            table = "id_gen_table",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000,
            allocationSize = 1)
    @Column(name = "ID")
    private BigInteger id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SPECIALIZATION", referencedColumnName = "ID")
    @ToString.Exclude
    private TrainingTypeEntity specialization;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ToString.Exclude
    private UserEntity user;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    List<TrainingEntity> trainings;

    public TrainerDto toDto() {
        return new TrainerDto(id, specialization, user, trainings);
    }

    public static TrainerEntity fromDto(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }

        return new TrainerEntity(trainerDto.getId(), trainerDto.getSpecialization(), trainerDto.getUser(), trainerDto.getTrainings());
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