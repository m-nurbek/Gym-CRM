package com.epam.gym.service.impl;

import com.epam.gym.controller.exception.UnauthorizedException;
import com.epam.gym.dto.response.JwtTokenResponseDto;
import com.epam.gym.entity.RefreshTokenEntity;
import com.epam.gym.repository.RefreshTokenRepository;
import com.epam.gym.service.JwtService;
import com.epam.gym.util.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret.key}")
    private String SECRET;

    @Value("${jwt.access-token.expiration.minutes}")
    private int minutes = 0;
    @Value("${jwt.access-token.expiration.hours}")
    private int hours = 0;
    @Value("${jwt.access-token.expiration.days}")
    private int days = 0;

    @Value("${jwt.refresh-token.expiration.minutes}")
    private int refreshMinutes = 0;
    @Value("${jwt.refresh-token.expiration.hours}")
    private int refreshHours = 0;
    @Value("${jwt.refresh-token.expiration.days}")
    private int refreshDays = 0;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(DateUtil.asDate(LocalDateTime.now()))
                .setExpiration(DateUtil.asDate(LocalDateTime.now().plusMinutes(minutes).plusHours(hours).plusDays(days)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public LocalDateTime extractExpiration(String token) {
        return DateUtil.asLocalDateTime(extractClaim(token, Claims::getExpiration));
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now());
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @Override
    @Transactional
    public String createRefreshToken(String username) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();

        refreshTokenRepository.findByUsername(username)
                .ifPresent(refreshTokenEntity -> refreshTokenRepository.deleteByUsername(refreshTokenEntity.getUsername()));

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUsername(username);
        refreshToken.setExpiryDate(LocalDateTime.now().plusMinutes(refreshMinutes).plusHours(refreshHours).plusDays(refreshDays));

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        Optional<RefreshTokenEntity> optionalRefreshToken = refreshTokenRepository.findByToken(refreshToken);

        if (optionalRefreshToken.isPresent() && optionalRefreshToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            return generateToken(optionalRefreshToken.get().getUsername());
        }

        throw new UnauthorizedException("Invalid refresh token");
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String username) {
        if (refreshTokenRepository.findByUsername(username).isEmpty()) {
            throw new UnauthorizedException("Username doesn't exist");
        }

        refreshTokenRepository.deleteByUsername(username);
    }

    @Override
    @Transactional
    public JwtTokenResponseDto generateAccessAndRefreshTokens(String username) {
        String accessToken = generateToken(username);
        String refreshToken = createRefreshToken(username);

        return new JwtTokenResponseDto(accessToken, refreshToken);
    }
}