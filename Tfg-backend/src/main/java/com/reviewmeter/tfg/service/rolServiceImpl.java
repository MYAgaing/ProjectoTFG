package com.reviewmeter.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reviewmeter.tfg.model.Rol;
import com.reviewmeter.tfg.repository.rolRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class rolServiceImpl implements rolService {

	@Autowired
	private rolRepository rolRepository;

	@Override
	public List<Rol> getRoles() {
		return rolRepository.findAll();
	}

	@Override
	public Rol getRol(Long id) {
		return rolRepository.findById(id).orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
	}

	@Override
	public Rol crearRol(Rol rol) {
		rolRepository.save(rol);
		return rol;
	}

	@Override
	public Rol actualizarRol(Long id, Rol rolActualizado) {
		Rol rolExistente = rolRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));

		rolExistente.setNombre(rolActualizado.getNombre());
		rolExistente.setDescripcion(rolActualizado.getDescripcion());

		rolRepository.save(rolExistente);

		return rolExistente;
	}

	@Override
	public String borrarRol(Long id) {
		Rol rol = rolRepository.findById(id).orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));

		rolRepository.delete(rol);
		return "Rol eliminado correctamente";
	}
}
