package com.reviewmeter.tfg.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Favorito;
import com.reviewmeter.tfg.model.Producto;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.favoritoRepository;
import com.reviewmeter.tfg.repository.productoRepository;
import com.reviewmeter.tfg.repository.usuarioRepository;
import com.reviewmeter.tfg.security.JwtService;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "*")
public class favoritoController {

    @Autowired
    private favoritoRepository favoritoRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private productoRepository productoRepository;

    @Autowired
    private JwtService jwtService;

    private Usuario getUsuarioFromToken(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // GET: listar favoritos del usuario
    @GetMapping
    public ResponseEntity<List<Favorito>> getMisFavoritos(@RequestHeader("Authorization") String authHeader) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        return ResponseEntity.ok(favoritoRepository.findByUsuario(usuario));
    }

    // GET: comprobar si un producto es favorito
    @GetMapping("/check/{idProducto}")
    public ResponseEntity<Boolean> esFavorito(@PathVariable Long idProducto,
                                               @RequestHeader("Authorization") String authHeader) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return ResponseEntity.ok(favoritoRepository.existsByUsuarioAndProducto(usuario, producto));
    }

    // POST: añadir favorito
    @PostMapping("/{idProducto}")
    public ResponseEntity<Favorito> agregarFavorito(@PathVariable Long idProducto,
                                                     @RequestHeader("Authorization") String authHeader) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (favoritoRepository.existsByUsuarioAndProducto(usuario, producto)) {
            return ResponseEntity.ok(favoritoRepository.findByUsuarioAndProducto(usuario, producto).get());
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setProducto(producto);
        return ResponseEntity.ok(favoritoRepository.save(favorito));
    }

    // DELETE: eliminar favorito
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<String> eliminarFavorito(@PathVariable Long idProducto,
                                                    @RequestHeader("Authorization") String authHeader) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Optional<Favorito> favorito = favoritoRepository.findByUsuarioAndProducto(usuario, producto);
        favorito.ifPresent(favoritoRepository::delete);
        return ResponseEntity.ok("Favorito eliminado");
    }
}
