package com.reviewmeter.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.Categoria;
import com.reviewmeter.tfg.model.Producto;

public interface categoriaRepository extends JpaRepository<Categoria, Long>{
	
}
