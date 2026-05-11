package com.reviewmeter.tfg.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.dto.LoginRequest;
import com.reviewmeter.tfg.dto.RegisterRequest;
import com.reviewmeter.tfg.model.Rol;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.model.VerificacionToken;
import com.reviewmeter.tfg.repository.rolRepository;
import com.reviewmeter.tfg.repository.usuarioRepository;
import com.reviewmeter.tfg.repository.verificacionTokenRepository;
import com.reviewmeter.tfg.security.JwtService;
import com.reviewmeter.tfg.service.EmailService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private rolRepository rolRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private verificacionTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    // ── Registro ─────────────────────────────────────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        // Crear usuario con estado=false (pendiente de verificación)
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEstado(false);
        usuario.setFechaRegistro(LocalDate.now());

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);

        usuarioRepository.save(usuario);

        // Generar token de verificación y enviarlo por email
        String tokenStr = UUID.randomUUID().toString();
        VerificacionToken verificacionToken = new VerificacionToken(tokenStr, usuario);
        tokenRepository.save(verificacionToken);

        try {
            emailService.enviarEmailVerificacion(usuario.getEmail(), usuario.getNombre(), tokenStr);
        } catch (Exception e) {
            // Si el email falla, borramos el usuario para que pueda reintentar
            tokenRepository.delete(verificacionToken);
            usuarioRepository.delete(usuario);
            return ResponseEntity.internalServerError()
                    .body("No se pudo enviar el email de verificación. Inténtalo de nuevo.");
        }

        return ResponseEntity.ok("Registro completado. Revisa tu email para activar tu cuenta.");
    }

    // ── Verificación de email ─────────────────────────────────────────────────

    @GetMapping("/verificar")
    public ResponseEntity<String> verificarEmail(@RequestParam String token) {

        VerificacionToken verificacionToken = tokenRepository.findByToken(token)
                .orElse(null);

        if (verificacionToken == null) {
            return ResponseEntity.badRequest().body("Token de verificación inválido.");
        }

        if (verificacionToken.isExpirado()) {
            tokenRepository.delete(verificacionToken);
            return ResponseEntity.badRequest()
                    .body("El enlace de verificación ha expirado. Regístrate de nuevo.");
        }

        // Activar la cuenta
        Usuario usuario = verificacionToken.getUsuario();
        usuario.setEstado(true);
        usuarioRepository.save(usuario);

        // Eliminar el token ya usado
        tokenRepository.delete(verificacionToken);

        return ResponseEntity.ok("Cuenta verificada correctamente. Ya puedes iniciar sesión.");
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        Usuario dbUser = usuarioRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (dbUser == null || !passwordEncoder.matches(request.getPassword(), dbUser.getPassword())) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        if (!Boolean.TRUE.equals(dbUser.getEstado())) {
            return ResponseEntity.status(403).body("Cuenta no verificada. Revisa tu email.");
        }

        return ResponseEntity.ok(jwtService.generateToken(dbUser));
    }
}
