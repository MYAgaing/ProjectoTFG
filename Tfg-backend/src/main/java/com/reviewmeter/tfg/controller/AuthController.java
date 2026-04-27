package com.reviewmeter.tfg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.dto.LoginRequest;
import com.reviewmeter.tfg.dto.RegisterRequest;
import com.reviewmeter.tfg.model.Rol;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.rolRepository;
import com.reviewmeter.tfg.repository.usuarioRepository;
import com.reviewmeter.tfg.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;

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


    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return "El email ya está registrado";
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEstado(true);

        // Asignar rol
        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);

        usuarioRepository.save(usuario);

        return "Usuario creado correctamente";
    }


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        Usuario dbUser = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return jwtService.generateToken(dbUser);
    }
}
