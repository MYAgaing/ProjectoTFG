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

	    // ⚡ CAMBIO CLAVE: Si no hay token o no es Bearer, 
	    // pasamos al siguiente filtro y SALIMOS de este.
	    if (header == null || !header.startsWith("Bearer ")) {
	        filterChain.doFilter(request, response);
	        return; // <--- MUY IMPORTANTE: Evita ejecutar el resto del código
	    }

	    // 2️⃣ Si llegamos aquí, es que hay un token. Intentamos validarlo.
	    try {
	        String token = header.substring(7);
	        String email = jwtService.extractEmail(token);

	        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            UserDetails user = userDetailsService.loadUserByUsername(email);

	            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
	                    user, null, user.getAuthorities());
	            
	            SecurityContextHolder.getContext().setAuthentication(auth);
	        }
	    } catch (Exception e) {
	        // Token inválido — Spring denegará el acceso en rutas protegidas
	    }

	    // 3️⃣ Continuar con la cadena
	    filterChain.doFilter(request, response);
	}

}
