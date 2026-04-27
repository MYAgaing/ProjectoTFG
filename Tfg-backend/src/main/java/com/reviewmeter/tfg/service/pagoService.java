package com.reviewmeter.tfg.service;

import java.util.List;

import com.reviewmeter.tfg.model.Pago;

public interface pagoService {
	
	List<Pago> getPagos();
	
	Pago getPago(Long id);

	String crearPago(Pago pago);

	String actualizarPago(Long id, Pago pago);

	String borrarPago(Long id);
}
