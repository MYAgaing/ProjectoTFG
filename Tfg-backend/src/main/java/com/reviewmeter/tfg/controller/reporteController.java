package com.reviewmeter.tfg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Reporte;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.usuarioRepository;
import com.reviewmeter.tfg.security.JwtService;
import com.reviewmeter.tfg.service.reporteService;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class reporteController {

    @Autowired
    private reporteService reporteService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private usuarioRepository usuarioRepo;

    @PostMapping("/{idResena}")
    public ResponseEntity<?> reportar(
            @PathVariable Long idResena,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        Long idUsuario = extractIdUsuario(authHeader);
        String motivo = body.getOrDefault("motivo", "OTRO");
        String descripcion = body.getOrDefault("descripcion", "");

        try {
            Reporte reporte = reporteService.reportar(idResena, idUsuario, motivo, descripcion);
            return ResponseEntity.ok(reporte);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/check/{idResena}")
    public ResponseEntity<Boolean> check(
            @PathVariable Long idResena,
            @RequestHeader("Authorization") String authHeader) {

        Long idUsuario = extractIdUsuario(authHeader);
        return ResponseEntity.ok(reporteService.yaReporto(idResena, idUsuario));
    }

    // Extrae el id del usuario a partir del token JWT del header
    private Long extractIdUsuario(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getIdUsuario();
    }
}
