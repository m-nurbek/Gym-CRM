package com.epam.gym.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

/**
 * DTO for {@link com.epam.gym.entity.UserEntity}
 */
public record UserDto(
        BigInteger id,
        String firstName,
        String lastName,
        String username,
        String password,
        Boolean isActive,
        List<UserRole> roles
) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(x -> new SimpleGrantedAuthority(x.toString()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}