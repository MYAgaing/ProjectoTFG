package com.reviewmeter.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.Usuario;

public interface usuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRolIdRol(Long idRol);
	
}
