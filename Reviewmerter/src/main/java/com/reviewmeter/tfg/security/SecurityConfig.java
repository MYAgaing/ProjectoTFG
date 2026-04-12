package com.reviewmeter.tfg.security;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
            .cors(cors -> {}) // ⚡ habilitar CORS usando el bean CorsConfigurationSource
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless JWT
            .authorizeHttpRequests(auth -> auth
                    // Endpoints públicos
                    .requestMatchers("/auth/login","/auth/register").permitAll()
                    // Admin
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    // Usuario logueado
                    .requestMatchers("/usuario/me").hasAnyRole("USER","ADMIN")
                    // Categorias
                    .requestMatchers("/api/categorias/**").permitAll()
                    // Productos
                    .requestMatchers("/api/productos/**").permitAll()
                    // Resto protegido
                    .anyRequest().authenticated()
            );

        // Añadir JwtFilter antes del filtro de Spring Security
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para AuthenticationManager (si necesitas inyectarlo)
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // ⚡ Bean CORS global: para permitir Angular
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // tu Angular
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
