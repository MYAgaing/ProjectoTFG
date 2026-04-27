package com.reviewmeter.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.Producto;

public interface productoRepository extends JpaRepository<Producto, Long>{

	List<Producto> findByCategoria_IdCategoria(Long idCategoria);
	
}
