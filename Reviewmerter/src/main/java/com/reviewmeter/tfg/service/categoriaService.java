package com.reviewmeter.tfg.service;

import java.util.List;

import com.reviewmeter.tfg.model.Categoria;

public interface categoriaService {

	List<Categoria> getCategorias();
	
	Categoria getCategoria(Long id);
	
	String crearCategoria(Categoria categoria);
	
	String actualizarCategoria(Long id, Categoria categoria);
	
	String borrarCategoria(Long id);
	
}
