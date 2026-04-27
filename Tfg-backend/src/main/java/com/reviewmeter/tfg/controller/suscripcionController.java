package com.reviewmeter.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewmeter.tfg.model.Suscripcion;
import com.reviewmeter.tfg.service.suscripcionService;

@RestController
@RequestMapping("/api/suscripciones")
@CrossOrigin(origins = "*")
public class suscripcionController {
	
	@Autowired
	private suscripcionService suscripcionService;

	// Obtener todos
	@GetMapping
	public ResponseEntity<List<Suscripcion>> getAll() {
		return ResponseEntity.ok(suscripcionService.getSuscripciones()) ;
	}

	// Obtener por ID
	@GetMapping("/{id}")
	public ResponseEntity<Suscripcion> getById(@PathVariable Long id) {
		return ResponseEntity.ok(suscripcionService.getSuscripcion(id)) ;
	}

	// Crear
	@PostMapping
	public ResponseEntity<Suscripcion> crear(@RequestBody Suscripcion suscripcion) {
		return ResponseEntity.ok(suscripcionService.crearSuscripcion(suscripcion)) ;
	}

	// Actualizar
	@PutMapping("/{id}")
	public ResponseEntity<Suscripcion> actualizar(@PathVariable Long id, @RequestBody Suscripcion suscripcionActualizada) {
		return ResponseEntity.ok(suscripcionService.actualizarSuscripcion(id, suscripcionActualizada)) ;
	}

	// Borrar
	@DeleteMapping("/{id}")
	public ResponseEntity<String> borrar(@PathVariable Long id) {
		return ResponseEntity.ok(suscripcionService.borrarSuscripcion(id));
	}
}
