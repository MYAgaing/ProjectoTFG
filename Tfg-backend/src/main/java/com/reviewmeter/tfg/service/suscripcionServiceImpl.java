package com.reviewmeter.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reviewmeter.tfg.model.Suscripcion;
import com.reviewmeter.tfg.repository.suscripcionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class suscripcionServiceImpl implements suscripcionService {

	@Autowired
	private suscripcionRepository suscripcionRepository;

	@Override
	public List<Suscripcion> getSuscripciones() {
		return suscripcionRepository.findAll();
	}

	@Override
	public Suscripcion getSuscripcion(Long id) {
		return suscripcionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Suscripción no encontrada con id: " + id));
	}

	@Override
	public Suscripcion crearSuscripcion(Suscripcion suscripcion) {
		if (suscripcion == null) {
			return null;
		}
		suscripcionRepository.save(suscripcion);
		return suscripcion;
	}

	@Override
	public Suscripcion actualizarSuscripcion(Long id, Suscripcion suscripcionActualizada) {
		Suscripcion suscripcionExistente = suscripcionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Suscripción no encontrada con id: " + id));
		
		suscripcionExistente.setTipo(suscripcionActualizada.getTipo());
		suscripcionExistente.setPrecio(suscripcionActualizada.getPrecio());
		suscripcionExistente.setFechaInicio(suscripcionActualizada.getFechaInicio());
		suscripcionExistente.setFechaFin(suscripcionActualizada.getFechaFin());
		suscripcionExistente.setEstado(suscripcionActualizada.getEstado());

		if (suscripcionActualizada.getUsuario() != null) {
			suscripcionExistente.setUsuario(suscripcionActualizada.getUsuario());
		}

		suscripcionRepository.save(suscripcionExistente);

		return suscripcionExistente;
	}

	@Override
	public String borrarSuscripcion(Long id) {
		Suscripcion suscripcion = suscripcionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Suscripción no encontrada con id: " + id));

		suscripcionRepository.delete(suscripcion);
		return "Suscripción eliminada correctamente";
	}
}
