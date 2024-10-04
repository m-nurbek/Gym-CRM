package com.epam.gym.entity;

import com.epam.gym.dto.UserDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "USER_TABLE")
public class UserEntity {
    @Value("${table-generator.initial-value}")
    private static final int ID_INITIAL_VALUE = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_table_seq")
    @TableGenerator(
            name = "user_table_seq",
            table = "id_gen_table",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = ID_INITIAL_VALUE,
            allocationSize = 1)
    @Column(name = "ID")
    private BigInteger id;
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive = true;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private TraineeEntity trainee;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private TrainerEntity trainer;

    public UserDto toDto() {
        return new UserDto(id, firstName, lastName, username, password, isActive, trainee, trainer);
    }

    public static UserEntity fromDto(UserDto dto) {
        if (dto == null) {
            return null;
        }

        return new UserEntity(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getUsername(), dto.getPassword(), dto.getIsActive(), dto.getTrainee(), dto.getTrainer());
    }

    public boolean isValid() {
        return !(this.trainer != null && this.trainee != null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}