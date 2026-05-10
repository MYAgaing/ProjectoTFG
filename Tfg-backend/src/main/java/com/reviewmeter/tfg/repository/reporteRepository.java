package com.reviewmeter.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.Reporte;

public interface reporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByEstadoOrderByFechaDesc(String estado);

    Optional<Reporte> findByResena_IdResenaAndUsuarioReporta_IdUsuario(Long idResena, Long idUsuario);
}
