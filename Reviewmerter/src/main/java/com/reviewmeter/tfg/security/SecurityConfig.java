package com.reviewmeter.tfg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF porque es API REST
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless JWT
            .authorizeHttpRequests(auth -> auth
                    // Endpoints públicos
                    .requestMatchers("/auth/**").permitAll()
                    //admin
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    //usuario/me
                    .requestMatchers("/usuario/me").hasAnyRole("USER","ADMIN")
                    // Resto protegido
                    .anyRequest().authenticated()
            );

        // Añadir JwtFilter antes del filtro de Spring Security
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Permite inyectar AuthenticationManager en otros beans si es necesario
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
