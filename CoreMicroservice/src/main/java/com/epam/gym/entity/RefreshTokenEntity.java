package com.epam.gym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "REFRESH_TOKEN_TABLE")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
    @SequenceGenerator(name = "refresh_token_seq", sequenceName = "refresh_token_seq", allocationSize = 1)
    @Column(name = "ID")
    private BigInteger id;

    @Column(name = "TOKEN")
    private String token;
    @Column(name = "USERNAME", unique = true)
    private String username;
    @Column(name = "EXPIRY_DATE")
    private LocalDateTime expiryDate;
}