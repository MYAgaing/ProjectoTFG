package com.reviewmeter.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.Favorito;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.model.Producto;

public interface favoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsuario(Usuario usuario);

    Optional<Favorito> findByUsuarioAndProducto(Usuario usuario, Producto producto);

    boolean existsByUsuarioAndProducto(Usuario usuario, Producto producto);
}
