package com.reviewmeter.tfg.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Resena;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.usuarioRepository;
import com.reviewmeter.tfg.security.JwtService;
import com.reviewmeter.tfg.service.resenaService;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "*")
public class resenasController {

	@Autowired
	private resenaService resenaService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private usuarioRepository usuarioRepository;

	// Listar todas
	@GetMapping
	public ResponseEntity<List<Resena>> getAll() {
		return ResponseEntity.ok(resenaService.getResenas());
	}

	// Mis reseñas — va ANTES de /{id} para evitar conflicto de rutas
	@GetMapping("/mis-resenas")
	public ResponseEntity<List<Resena>> misResenas(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.substring(7);
		String email = jwtService.extractEmail(token);
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		return ResponseEntity.ok(resenaService.getResenasPorUsuario(usuario.getIdUsuario()));
	}

	// Obtener por ID
	@GetMapping("/{id}")
	public ResponseEntity<Resena> getById(@PathVariable Long id) {
		return ResponseEntity.ok(resenaService.getResena(id));
	}

	// Crear — extrae el usuario del token para evitar el 403
	@PostMapping
	public ResponseEntity<Resena> crear(@RequestBody Resena resena,
										@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.substring(7);
		String email = jwtService.extractEmail(token);
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		resena.setUsuario(usuario);
		resena.setFecha(LocalDate.now());
		return ResponseEntity.ok(resenaService.crearResena(resena));
	}

	// Actualizar — solo el autor puede editar
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable Long id,
										@RequestBody Resena resenaActualizada,
										@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.substring(7);
		String email = jwtService.extractEmail(token);
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		Resena existente = resenaService.getResena(id);
		if (!existente.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
			return ResponseEntity.status(403).body("No tienes permiso para editar esta reseña");
		}
		return ResponseEntity.ok(resenaService.actualizarResena(id, resenaActualizada));
	}

	// Borrar — solo el autor puede borrar
	@DeleteMapping("/{id}")
	public ResponseEntity<String> borrar(@PathVariable Long id,
										 @RequestHeader("Authorization") String authHeader) {
		String token = authHeader.substring(7);
		String email = jwtService.extractEmail(token);
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		Resena existente = resenaService.getResena(id);
		if (!existente.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
			return ResponseEntity.status(403).body("No tienes permiso para borrar esta reseña");
		}
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
	public List<Resena> listarPorProducto(
			@PathVariable Long id,
			@RequestParam(required = false) String orden,
			@RequestParam(required = false) Integer minPuntuacion) {
		
		// Si hay filtro de puntuación mínima
		if (minPuntuacion != null && minPuntuacion > 0) {
			return resenaService.getResenasPorProductoYPuntuacion(id, minPuntuacion);
		}
		
		// Si hay orden especificado
		if ("desc".equals(orden)) {
			return resenaService.getResenasPorProductoOrdenDesc(id);
		} else if ("asc".equals(orden)) {
			return resenaService.getResenasPorProductoOrdenAsc(id);
		}
		
		// Por defecto sin filtros
		return resenaService.getResenasPorProducto(id);
	}
}
