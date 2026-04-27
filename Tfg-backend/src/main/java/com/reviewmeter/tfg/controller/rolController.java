package com.reviewmeter.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewmeter.tfg.model.Rol;
import com.reviewmeter.tfg.service.rolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/rol")
@CrossOrigin(origins = "*")
public class rolController {

	@Autowired
	private rolService rolService;

	// Listar todos
	@GetMapping
	public ResponseEntity<List<Rol>> getRols() {
		return ResponseEntity.ok(rolService.getRoles());
	}

	// Obtener por id
	@GetMapping("/{id}")
	public ResponseEntity<Rol> getRol(@PathVariable Long id) {
		return ResponseEntity.ok(rolService.getRol(id));
	}

	// Crear
	@PostMapping
	public ResponseEntity<Rol> crear(@RequestBody Rol rol) {
		return ResponseEntity.ok(rolService.crearRol(rol));
	}
	
	// Actualizar
		@PutMapping("/{id}")
		public ResponseEntity<Rol> actualizar(@PathVariable Long id, @RequestBody Rol rol) {
			return ResponseEntity.ok(rolService.actualizarRol(id, rol));
		}
	
	// Borrar
		@DeleteMapping("/{id}")
		public ResponseEntity<String> borrar(@PathVariable Long id){
			return ResponseEntity.ok("Su cuenta fue borrada exitosamente");
		}

}
