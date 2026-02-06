package com.reviewmeter.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Comentario;
import com.reviewmeter.tfg.service.comentarioService;

@RestController
@RequestMapping("/comentarios")
public class comentarioController {
	
	@Autowired
    private comentarioService comentarioService;

    @GetMapping
    public ResponseEntity<List<Comentario>> getComentarios() {
        return ResponseEntity.ok(comentarioService.getComentarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comentario> getComentario(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.getComentario(id));
    }


    @GetMapping("/resena/{idResena}")
    public ResponseEntity<List<Comentario>> getComentariosPorResena(@PathVariable Long idResena) {
        return ResponseEntity.ok(comentarioService.getComentariosPorResena(idResena));
    }

    @PostMapping
    public ResponseEntity<String> crearComentario(@RequestBody Comentario comentario) {
        return ResponseEntity.ok(comentarioService.crearComentario(comentario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarComentario(
            @PathVariable Long id,
            @RequestBody Comentario comentarioActualizado) {

        return ResponseEntity.ok(
                comentarioService.actualizarComentario(id, comentarioActualizado)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarComentario(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.borrarComentario(id));
    }
}
