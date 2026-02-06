package com.reviewmeter.tfg.service;

import java.util.List;


import com.reviewmeter.tfg.model.Suscripcion;

public interface suscripcionService {

	List<Suscripcion> getSuscripciones();

	Suscripcion getSuscripcion(Long id);

	Suscripcion crearSuscripcion(Suscripcion suscripcion);

	Suscripcion actualizarSuscripcion(Long id, Suscripcion suscripcionActualizada);

	String borrarSuscripcion(Long id);
}
