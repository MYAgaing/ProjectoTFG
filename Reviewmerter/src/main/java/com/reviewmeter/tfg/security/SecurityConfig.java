package com.reviewmeter.tfg.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Importante para definir métodos
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) 
            // 1. Configuramos CORS explícitamente usando nuestro bean
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/error").permitAll()
                    // Permitir preflight (OPTIONS) para evitar bloqueos CORS
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    
                    // Endpoints públicos
                    .requestMatchers("/auth/**").permitAll()
                    
                    // Categorias, Productos y Reseñas (Lectura pública)
                    .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/resenas/**").permitAll()
                    
                    // Admin
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    
                    // Usuario logueado
                    .requestMatchers("/usuario/me").hasAnyRole("USER", "ADMIN")
                    
                    // El resto de peticiones (POST, PUT, DELETE) requieren auth
                    .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Asegúrate de que esta URL es EXACTAMENTE la de tu Angular (sin / al final)
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        // Tiempo que el navegador guarda la configuración CORS (opcional)
        configuration.setMaxAge(3600L); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}