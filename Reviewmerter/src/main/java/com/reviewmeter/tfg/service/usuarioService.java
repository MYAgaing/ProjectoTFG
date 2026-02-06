package com.reviewmeter.tfg.service;

import java.util.List;

import com.reviewmeter.tfg.model.Usuario;

public interface usuarioService {

	List<Usuario> getUsuarios();

	Usuario getUsuario(Long id);

	String crearUsuario(Usuario usuario);

	String actualizarUsuario(Long id ,Usuario usuarioActualizado);

	String borrarUsuario(Long id);
	
	Usuario getUsuarioPorEmail(String email);

	List<Usuario> getUsuariosPorRol(Long idRol);

}
