package com.reviewmeter.tfg.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.RespuestaResena;
import com.reviewmeter.tfg.model.Resena;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.resenaRepository;
import com.reviewmeter.tfg.repository.respuestaResenaRepository;
import com.reviewmeter.tfg.repository.usuarioRepository;
import com.reviewmeter.tfg.security.JwtService;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "*")
public class RespuestaResenaController {

    @Autowired private respuestaResenaRepository respuestaRepo;
    @Autowired private resenaRepository resenaRepo;
    @Autowired private usuarioRepository usuarioRepo;
    @Autowired private JwtService jwtService;

    /**
     * GET /api/resenas/{id}/respuestas
     * Devuelve todas las respuestas de una reseña. Público.
     */
    @GetMapping("/{id}/respuestas")
    public ResponseEntity<List<RespuestaResena>> getRespuestas(@PathVariable Long id) {
        return ResponseEntity.ok(respuestaRepo.findByResena_IdResenaOrderByFechaAsc(id));
    }

    /**
     * POST /api/resenas/{id}/respuestas
     * Crea una respuesta. Requiere autenticación.
     * Body: { "texto": "..." }
     */
    @PostMapping("/{id}/respuestas")
    public ResponseEntity<?> crearRespuesta(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        String texto = body.get("texto");
        if (texto == null || texto.isBlank()) {
            return ResponseEntity.badRequest().body("El texto no puede estar vacío.");
        }
        if (texto.length() > 1000) {
            return ResponseEntity.badRequest().body("El texto no puede superar los 1000 caracteres.");
        }

        String email = jwtService.extractEmail(authHeader.substring(7));
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Resena resena = resenaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        RespuestaResena respuesta = new RespuestaResena();
        respuesta.setTexto(texto.trim());
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setResena(resena);
        respuesta.setUsuario(usuario);

        return ResponseEntity.ok(respuestaRepo.save(respuesta));
    }

    /**
     * DELETE /api/resenas/{idResena}/respuestas/{idRespuesta}
     * Borra una respuesta. Solo el autor puede borrarla.
     */
    @DeleteMapping("/{idResena}/respuestas/{idRespuesta}")
    public ResponseEntity<String> borrarRespuesta(
            @PathVariable Long idResena,
            @PathVariable Long idRespuesta,
            @RequestHeader("Authorization") String authHeader) {

        String email = jwtService.extractEmail(authHeader.substring(7));
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RespuestaResena respuesta = respuestaRepo.findById(idRespuesta)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));

        if (!respuesta.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.status(403).body("No tienes permiso para borrar esta respuesta.");
        }

        respuestaRepo.delete(respuesta);
        return ResponseEntity.ok("Respuesta eliminada.");
    }
}
