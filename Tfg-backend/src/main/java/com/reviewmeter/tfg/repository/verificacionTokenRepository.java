package com.reviewmeter.tfg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.VerificacionToken;

public interface verificacionTokenRepository extends JpaRepository<VerificacionToken, Long> {

    Optional<VerificacionToken> findByToken(String token);

    void deleteByUsuario_IdUsuario(Long idUsuario);
}
