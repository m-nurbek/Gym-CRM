package com.epam.gym.service;

import com.epam.gym.dto.response.JwtTokenResponseDto;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.function.Function;

public interface JwtService {

    String generateToken(String username);

    Key getSignKey();

    String extractUsername(String token);

    LocalDateTime extractExpiration(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    Claims extractAllClaims(String token);

    boolean isTokenExpired(String token);

    boolean validateToken(String token, UserDetails userDetails);

    String createRefreshToken(String username);

    String refreshAccessToken(String refreshToken);

    void deleteRefreshToken(String username);

    JwtTokenResponseDto generateAccessAndRefreshTokens(String username);
}