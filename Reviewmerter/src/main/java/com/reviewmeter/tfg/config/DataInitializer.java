package com.reviewmeter.tfg.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.reviewmeter.tfg.model.Rol;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.rolRepository;
import com.reviewmeter.tfg.repository.usuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataInitializer implements CommandLineRunner {

    private final rolRepository rolRepository;
    private final usuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(rolRepository rolRepository,
                           usuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // ====================
        // 1️⃣ Inicializar roles
        // ====================
        if (rolRepository.count() == 0) {
            Rol userRol = new Rol();
            userRol.setNombre("USER");
            rolRepository.save(userRol);

            Rol adminRol = new Rol();
            adminRol.setNombre("ADMIN");
            rolRepository.save(adminRol);

            System.out.println("Roles inicializados");
        }

        // ====================
        // 2️⃣ Crear usuario admin de prueba
        // ====================
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setEstado(true);

            Rol adminRol = rolRepository.findByNombre("ADMIN").get();
            admin.setRol(adminRol);

            usuarioRepository.save(admin);

            System.out.println("Usuario admin creado: admin@mail.com / 1234");
        }
    }
}
