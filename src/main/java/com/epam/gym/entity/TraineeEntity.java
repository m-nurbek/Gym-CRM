package com.epam.gym.entity;

import com.epam.gym.dto.TraineeDto;
import com.epam.gym.dto.UserDto;
import com.epam.gym.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "TRAINEE")
public class TraineeEntity implements EntityInterface<BigInteger> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private BigInteger id;
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private LocalDate dob;
    @Column(name = "ADDRESS")
    private String address;

    @Transient
    private BigInteger userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "trainee")
    @ToString.Exclude
    List<TrainingEntity> trainings;

    @ManyToMany(mappedBy = "trainees")
    @ToString.Exclude
    List<TrainerEntity> trainers;

    @JsonCreator
    public TraineeEntity(
            @JsonProperty("id") BigInteger id,
            @JsonProperty("dob") @JsonFormat(pattern = "yyyy-MM-dd")
            @JsonSerialize(using = LocalDateSerializer.class)
            @JsonDeserialize(using = LocalDateDeserializer.class)
            LocalDate dob,
            @JsonProperty("address") String address,
            @JsonProperty("userId") BigInteger userId
    ) {
        this.id = id;
        this.dob = dob;
        this.address = address;
        this.userId = userId;
    }

    @Deprecated(since = "2024-09-12", forRemoval = false)
    public TraineeDto toDto(UserRepository userRepository) {
        AtomicReference<UserDto> userDto = new AtomicReference<>(null);
        userRepository.findById(userId).ifPresent(u -> userDto.set(u.toDto()));

        return new TraineeDto(id, dob, address, userDto.get());
    }

    public static TraineeEntity fromDto(TraineeDto traineeDto) {
        if (traineeDto == null) {
            return null;
        }

        Assert.notNull(traineeDto.getUser(), "'user' attribute must not be null");

        return new TraineeEntity(
                traineeDto.getId(),
                traineeDto.getDob(),
                traineeDto.getAddress(),
                EntityInterface.getIdFromDto(traineeDto.getUser()),
                UserEntity.fromDto(traineeDto.getUser()),
                traineeDto.getTrainings() == null ? null : traineeDto.getTrainings().stream().map(TrainingEntity::fromDto).toList(),
                traineeDto.getTrainers() == null ? null : traineeDto.getTrainers().stream().map(TrainerEntity::fromDto).toList()
        );
    }

    public TraineeDto toDto() {
        Assert.notNull(user, "'user' attribute must not be null");

        return new TraineeDto(
                id,
                dob,
                address,
                user.toDto(),
                trainings == null ? null : trainings.stream().map(TrainingEntity::toDto).toList(),
                trainers == null ? null : trainers.stream().map(TrainerEntity::toDto).toList()
        );
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TraineeEntity entity = (TraineeEntity) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}