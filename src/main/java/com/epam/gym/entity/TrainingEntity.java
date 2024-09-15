package com.epam.gym.entity;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.TrainerDto;
import com.epam.gym.dto.TrainingDto;
import com.epam.gym.dto.TrainingTypeDto;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.repository.TrainingTypeRepository;
import com.epam.gym.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRAINING")
public class TrainingEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private BigInteger id;

    @Transient
    private BigInteger traineeId;
    @Transient
    private BigInteger trainerId;

    @Column(name = "NAME")
    private String name;

    @Transient
    private BigInteger type; // training type

    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "DURATION")
    private String duration;

    @ManyToOne
    @JoinColumn(name = "TRAINEE_ID", referencedColumnName = "ID", nullable = false)
    private TraineeEntity trainee;
    @ManyToOne
    @JoinColumn(name = "TRAINER_ID", referencedColumnName = "ID", nullable = false)
    private TrainerEntity trainer;
    @ManyToOne
    @JoinColumn(name = "TYPE", referencedColumnName = "ID", nullable = false)
    private TrainingTypeEntity trainingType;

    @JsonCreator
    public TrainingEntity(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("traineeId") BigInteger traineeId,
            @JsonProperty("trainerId") BigInteger trainerId,
            @JsonProperty("name") String name,
            @JsonProperty("type") BigInteger type,
            @JsonProperty("date") @JsonFormat(pattern = "yyyy-MM-dd")
            @JsonSerialize(using = LocalDateSerializer.class)
            @JsonDeserialize(using = LocalDateDeserializer.class)
            LocalDate date,
            @JsonProperty("duration") String duration
    ) {
        this.id = id;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    @Deprecated(since = "2024-09-12", forRemoval = false)
    public TrainingDto toDto(TrainerRepository trainerRepository, TraineeRepository traineeRepository, UserRepository userRepository, TrainingTypeRepository trainingTypeRepository) {
        AtomicReference<TrainerDto> trainerDto = new AtomicReference<>();
        AtomicReference<TraineeDto> traineeDto = new AtomicReference<>();
        AtomicReference<TrainingTypeDto> trainingTypeDto = new AtomicReference<>();

        trainerRepository.findById(trainerId).ifPresent(t -> trainerDto.set(t.toDto(trainingTypeRepository, userRepository)));
        traineeRepository.findById(traineeId).ifPresent(t -> traineeDto.set(t.toDto(userRepository)));
        trainingTypeRepository.findById(type).ifPresent(t -> trainingTypeDto.set(t.toDto()));

        return new TrainingDto(id, traineeDto.get(), trainerDto.get(), name, trainingTypeDto.get(), date, duration);
    }

    public static TrainingEntity fromDto(TrainingDto trainingDto) {
        if (trainingDto == null) {
            return null;
        }

        return new TrainingEntity(
                trainingDto.getId(),
                EntityInterface.getIdFromDto(trainingDto.getTrainee()),
                EntityInterface.getIdFromDto(trainingDto.getTrainer()),
                trainingDto.getName(),
                EntityInterface.getIdFromDto(trainingDto.getType()),
                trainingDto.getDate(),
                trainingDto.getDuration(),
                trainingDto.getTrainee() == null ? null : TraineeEntity.fromDto(trainingDto.getTrainee()),
                trainingDto.getTrainer() == null ? null : TrainerEntity.fromDto(trainingDto.getTrainer()),
                trainingDto.getType() == null ? null : TrainingTypeEntity.fromDto(trainingDto.getType())
        );
    }

    public TrainingDto toDto() {
        return new TrainingDto(
                id,
                trainee == null ? null : trainee.toDto(),
                trainer == null ? null : trainer.toDto(),
                name,
                trainingType == null ? null : trainingType.toDto(),
                date,
                duration
        );
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