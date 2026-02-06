package com.reviewmeter.tfg.service;

import java.util.List;

import com.reviewmeter.tfg.model.Comentario;

public interface comentarioService {

	List<Comentario> getComentarios();
	
	Comentario getComentario(Long id);

	String crearComentario(Comentario comentario);

	String actualizarComentario(Long id, Comentario comentario);

	String borrarComentario(Long id);
	
	List<Comentario> getComentariosPorResena(Long idResena);

}
