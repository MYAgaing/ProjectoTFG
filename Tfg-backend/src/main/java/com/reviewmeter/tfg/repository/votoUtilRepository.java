package com.reviewmeter.tfg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.reviewmeter.tfg.model.VotoUtil;

public interface votoUtilRepository extends JpaRepository<VotoUtil, Long> {

    Optional<VotoUtil> findByResena_IdResenaAndUsuario_IdUsuario(Long idResena, Long idUsuario);

    long countByResena_IdResenaAndTipo(Long idResena, String tipo);

    boolean existsByResena_IdResenaAndUsuario_IdUsuario(Long idResena, Long idUsuario);

    @Transactional
    void deleteByResena_IdResenaAndUsuario_IdUsuario(Long idResena, Long idUsuario);

    @Transactional
    void deleteByResena_IdResena(Long idResena);
}
