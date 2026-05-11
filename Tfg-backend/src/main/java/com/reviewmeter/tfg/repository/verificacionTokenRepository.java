package com.reviewmeter.tfg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.reviewmeter.tfg.model.VerificacionToken;

public interface verificacionTokenRepository extends JpaRepository<VerificacionToken, Long> {

    Optional<VerificacionToken> findByToken(String token);

    @Transactional
    void deleteByUsuario_IdUsuario(Long idUsuario);
}
