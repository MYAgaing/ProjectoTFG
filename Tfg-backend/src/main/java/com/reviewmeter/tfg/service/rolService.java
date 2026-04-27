package com.reviewmeter.tfg.service;

import java.util.List;


import com.reviewmeter.tfg.model.Rol;

public interface rolService {

	List<Rol> getRoles();

	Rol getRol(Long id);

	Rol crearRol(Rol rol);

	Rol actualizarRol(Long id, Rol rolActualizado);

	String borrarRol(Long id);

}
