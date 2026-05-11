package com.reviewmeter.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.RespuestaResena;

public interface respuestaResenaRepository extends JpaRepository<RespuestaResena, Long> {

    List<RespuestaResena> findByResena_IdResenaOrderByFechaAsc(Long idResena);
}
