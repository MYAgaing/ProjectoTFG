package com.reviewmeter.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.usuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class usuarioServiceImpl implements usuarioService {

	@Autowired
	private usuarioRepository usuarioRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public List<Usuario> getUsuarios() {
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario getUsuario(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con id" + id));
	}

	@Override
	public String crearUsuario(Usuario usuario) {
		if (usuario == null) {
			return "Usuario inválido";
		}
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		usuarioRepository.save(usuario);
		return "Usuario creado correctamente";
	}

	@Override
	public String actualizarUsuario(Long id, Usuario usuarioActualizado) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrada con id: " + id));

			usuario.setNombre(usuarioActualizado.getNombre());
			usuario.setEmail(usuarioActualizado.getEmail());
			usuario.setPassword(usuarioActualizado.getPassword());
			usuario.setEstado(usuarioActualizado.getEstado());
			usuario.setFechaRegistro(usuarioActualizado.getFechaRegistro());
			usuario.setRol(usuarioActualizado.getRol());

			usuarioRepository.save(usuario);
			return "Usuario actualizado correctamente";
	}

	@Override
	public String borrarUsuario(Long id) {
		Usuario usuarioExistente = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
		usuarioRepository.delete(usuarioExistente);
		return "Suscripcion eliminada correctamente";
	}
	
	@Override
	public Usuario getUsuarioPorEmail(String email) {
	    return usuarioRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}

	@Override
	public List<Usuario> getUsuariosPorRol(Long idRol) {
	    return usuarioRepository.findByRolIdRol(idRol);
	}

}
