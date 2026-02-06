package com.reviewmeter.tfg.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.usuarioRepository;
import com.reviewmeter.tfg.security.UsuarioDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UsuarioDetailsServiceImpl userDetailsService;

	@Autowired
	private usuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1️⃣ Leer header Authorization
		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7); // quitar "Bearer "

			try {
				// 2️⃣ Extraer email del token
				String email = jwtService.extractEmail(token);

				// 3️⃣ Cargar usuario desde BD
				UserDetails user = userDetailsService.loadUserByUsername(email);

				// 4️⃣ Crear autenticación para Spring Security
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());

				// 5️⃣ Poner en SecurityContext
				SecurityContextHolder.getContext().setAuthentication(auth);

			} catch (Exception e) {
				// Token inválido o expirado
				System.out.println("Token no válido: " + e.getMessage());
			}
		}

		// 6️⃣ Continuar con la cadena de filtros
		filterChain.doFilter(request, response);
	}
}
