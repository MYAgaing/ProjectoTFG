package com.reviewmeter.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.Comentario;

public interface comentarioRepository extends JpaRepository<Comentario, Long>{

	List<Comentario> findByResenaIdResena(Long idResena);
	
}
