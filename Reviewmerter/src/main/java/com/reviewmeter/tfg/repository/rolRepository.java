package com.reviewmeter.tfg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.Rol;

public interface rolRepository extends JpaRepository<Rol, Long>{

	Optional<Rol> findByNombre(String nombre);
	
}
