package com.reviewmeter.tfg.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Resena;
import com.reviewmeter.tfg.service.resenaService;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "*")
public class resenasController {

	@Autowired
	private resenaService resenaService;

	// Listar todas
	@GetMapping
	public ResponseEntity<List<Resena>> getAll() {
		return ResponseEntity.ok(resenaService.getResenas());
	}

	// Obtener por ID
	@GetMapping("/{id}")
	public ResponseEntity<Resena> getById(@PathVariable Long id) {
		return ResponseEntity.ok(resenaService.getResena(id));
	}

	// Crear
	@PostMapping
	public ResponseEntity<Resena> crear(@RequestBody Resena resena) {
		return ResponseEntity.ok(resenaService.crearResena(resena));
	}

	// Actualizar
	@PutMapping("/{id}")
	public ResponseEntity<Resena> actualizar(@PathVariable Long id, @RequestBody Resena resenaActualizada) {
		return ResponseEntity.ok(resenaService.actualizarResena(id, resenaActualizada));
	}

	// Borrar
	@DeleteMapping("/{id}")
	public ResponseEntity<String> borrar(@PathVariable Long id) {
		return ResponseEntity.ok(resenaService.borrarResena(id));
	}

	// Filtrar por rango de fechas
	@GetMapping("/fechas")
	public ResponseEntity<List<Resena>> filtrarPorFechas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

		return ResponseEntity.ok(resenaService.filtrarPorFechas(inicio, fin));
	}

	// Filtrar por puntuación mínima
	@GetMapping("/puntuacion")
	public ResponseEntity<List<Resena>> filtrarPorPuntuacion(@RequestParam int min) {
		return ResponseEntity.ok(resenaService.filtrarPorPuntuacion(min));
	}

	// Ordenar por mejor valoradas
	@GetMapping("/top")
	public ResponseEntity<List<Resena>> mejoresValoradas() {
		return ResponseEntity.ok(resenaService.ordenarPorPuntuacionDesc());
	}

	// Ordenar por peor valoradas
	@GetMapping("/peores")
	public ResponseEntity<List<Resena>> peoresValoradas() {
		return ResponseEntity.ok(resenaService.ordenarPorPuntuacionAsc());
	}
	
	@GetMapping("/producto/{id}")
	public List<Resena> listarPorProducto(@PathVariable Long id) {
	    return resenaService.getResenasPorProducto(id);
	}
}
