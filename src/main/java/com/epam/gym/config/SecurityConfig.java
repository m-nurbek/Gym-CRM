package com.epam.gym.config;

import com.epam.gym.config.filter.BruteForceProtectorFilter;
import com.epam.gym.config.filter.JwtFilter;
import com.epam.gym.dto.UserDto;
import com.epam.gym.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    @Value(value = "${security.cors.allowed-origins}")
    private List<String> allowedOrigins;

    private final JwtFilter jwtFilter;
    private final BruteForceProtectorFilter bruteForceProtectorFilter;

    public SecurityConfig(@Lazy JwtFilter jwtFilter, BruteForceProtectorFilter bruteForceProtectorFilter) {
        this.jwtFilter = jwtFilter;
        this.bruteForceProtectorFilter = bruteForceProtectorFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> {
            log.info("Trying to find user with username: {}", username);

            UserDto userDto = userService.findByUsername(username).orElse(null);

            if (userDto == null) {
                log.error("Failed to search for the user with username: {}", username);
                throw new UsernameNotFoundException("User with username '%s' not found".formatted(username));
            }

            log.info("Found user with username: {}", username);
            return userDto;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();

                    corsConfiguration.setAllowedOriginPatterns(allowedOrigins);
                    corsConfiguration.setAllowedMethods(List.of("*"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);

                    return corsConfiguration;
                }))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/auth/register/**",
                                "/api/v1/auth/refresh-token",

                                "/h2/**",
                                "/h2-console/**",

                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",

                                "/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(bruteForceProtectorFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Profile({"prod", "stg"})
    @Order(1)
    public SecurityFilterChain filterChainForProduction(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        return http
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();

                    corsConfiguration.setAllowedOriginPatterns(allowedOrigins);
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    corsConfiguration.setAllowCredentials(true);

                    return corsConfiguration;
                }))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/auth/register/**",
                                "/api/v1/auth/refresh-token"
                        ).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(bruteForceProtectorFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}