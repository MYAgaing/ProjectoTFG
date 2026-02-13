package com.reviewmeter.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.usuarioRepository;
import com.reviewmeter.tfg.service.usuarioService;
import com.reviewmeter.tfg.security.JwtService;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class usuarioController {
	
	@Autowired
    private usuarioService usuarioService;
	
	@Autowired
	private usuarioRepository usuarioRepository;
	
	@Autowired
	private JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }
    
    @GetMapping("/me")
    public Usuario getMiPerfil(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // quitar "Bearer "
        String email = jwtService.extractEmail(token);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }



    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getUsuario(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getUsuarioPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.getUsuarioPorEmail(email));
    }

    @GetMapping("/rol/{idRol}")
    public ResponseEntity<List<Usuario>> getUsuariosPorRol(@PathVariable Long idRol) {
        return ResponseEntity.ok(usuarioService.getUsuariosPorRol(idRol));
    }

    @PostMapping
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.crearUsuario(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuarioActualizado) {

        return ResponseEntity.ok(
                usuarioService.actualizarUsuario(id, usuarioActualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.borrarUsuario(id));
    }
}
