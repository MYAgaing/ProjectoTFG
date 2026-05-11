package com.reviewmeter.tfg.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/error").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    .requestMatchers("/auth/**").permitAll()

                    .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/resenas/mis-resenas").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/resenas/destacadas").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/resenas/*/util").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/resenas/*/util").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/resenas/*/respuestas").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/resenas/*/respuestas").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/resenas/*/respuestas/*").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/resenas/**").permitAll()

                    .requestMatchers(HttpMethod.GET,    "/admin/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,   "/admin/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT,    "/admin/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/admin/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH,  "/admin/**").hasRole("ADMIN")

                    .requestMatchers("/api/reportes/**").authenticated()

                    .requestMatchers("/usuario/me").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/usuario/me").hasAnyRole("USER", "ADMIN")

                    .requestMatchers("/api/favoritos/**").authenticated()

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
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://lively-imagination-production-0c1a.up.railway.app"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
