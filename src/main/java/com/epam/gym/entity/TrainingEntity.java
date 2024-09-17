package com.epam.gym.entity;

import com.epam.gym.dto.TrainingDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRAINING")
public class TrainingEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "training_table_seq")
    @TableGenerator(
            name = "training_table_seq",
            table = "id_gen_table",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000,
            allocationSize = 1)
    @Column(name = "ID")
    private BigInteger id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "DURATION")
    private int duration;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAINEE_ID", referencedColumnName = "ID")
    private TraineeEntity trainee;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAINER_ID", referencedColumnName = "ID")
    private TrainerEntity trainer;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE", referencedColumnName = "ID")
    private TrainingTypeEntity type;

    public TrainingDto toDto() {
        return new TrainingDto(id, trainee, trainer, name, type, date, duration);
    }

    public static TrainingEntity fromDto(TrainingDto dto) {
        if (dto == null) {
            return null;
        }

        return new TrainingEntity(dto.getId(), dto.getName(), dto.getDate(), dto.getDuration(), dto.getTrainee(), dto.getTrainer(), dto.getType());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TrainingEntity that = (TrainingEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}