package com.reviewmeter.tfg.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Resena;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.model.VotoUtil;
import com.reviewmeter.tfg.repository.resenaRepository;
import com.reviewmeter.tfg.repository.usuarioRepository;
import com.reviewmeter.tfg.repository.votoUtilRepository;
import com.reviewmeter.tfg.security.JwtService;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "*")
public class VotoUtilController {

    @Autowired private votoUtilRepository votoRepo;
    @Autowired private resenaRepository resenaRepo;
    @Autowired private usuarioRepository usuarioRepo;
    @Autowired private JwtService jwtService;

    /**
     * GET /api/resenas/{id}/util
     * Devuelve conteo de 👍 y 👎, y el voto actual del usuario si está logueado.
     */
    @GetMapping("/{id}/util")
    public ResponseEntity<Map<String, Object>> getVotos(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        long util    = votoRepo.countByResena_IdResenaAndTipo(id, "UTIL");
        long noUtil  = votoRepo.countByResena_IdResenaAndTipo(id, "NO_UTIL");
        String miVoto = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String email = jwtService.extractEmail(authHeader.substring(7));
                Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);
                if (usuario != null) {
                    votoRepo.findByResena_IdResenaAndUsuario_IdUsuario(id, usuario.getIdUsuario())
                            .ifPresent(v -> {});
                    var voto = votoRepo.findByResena_IdResenaAndUsuario_IdUsuario(id, usuario.getIdUsuario());
                    if (voto.isPresent()) miVoto = voto.get().getTipo();
                }
            } catch (Exception ignored) {}
        }

        return ResponseEntity.ok(Map.of(
            "util",   util,
            "noUtil", noUtil,
            "miVoto", miVoto != null ? miVoto : ""
        ));
    }

    /**
     * POST /api/resenas/{id}/util
     * Body: { "tipo": "UTIL" | "NO_UTIL" }
     * Toggle: si ya votó ese tipo lo quita, si votó el otro lo cambia.
     */
    @PostMapping("/{id}/util")
    public ResponseEntity<Map<String, Object>> toggleVoto(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        String tipo = body.getOrDefault("tipo", "UTIL");
        if (!tipo.equals("UTIL") && !tipo.equals("NO_UTIL")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tipo inválido"));
        }

        String email = jwtService.extractEmail(authHeader.substring(7));
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Resena resena = resenaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        var votoExistente = votoRepo.findByResena_IdResenaAndUsuario_IdUsuario(id, usuario.getIdUsuario());

        if (votoExistente.isPresent()) {
            if (votoExistente.get().getTipo().equals(tipo)) {
                // Mismo tipo → quitar voto
                votoRepo.delete(votoExistente.get());
            } else {
                // Tipo distinto → cambiar voto
                votoExistente.get().setTipo(tipo);
                votoRepo.save(votoExistente.get());
            }
        } else {
            // No había voto → crear
            votoRepo.save(new VotoUtil(resena, usuario, tipo));
        }

        long util   = votoRepo.countByResena_IdResenaAndTipo(id, "UTIL");
        long noUtil = votoRepo.countByResena_IdResenaAndTipo(id, "NO_UTIL");
        var votoFinal = votoRepo.findByResena_IdResenaAndUsuario_IdUsuario(id, usuario.getIdUsuario());
        String miVoto = votoFinal.map(VotoUtil::getTipo).orElse("");

        return ResponseEntity.ok(Map.of(
            "util",   util,
            "noUtil", noUtil,
            "miVoto", miVoto
        ));
    }
}
