package com.reviewmeter.tfg.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reviewmeter.tfg.model.Reporte;
import com.reviewmeter.tfg.model.Resena;
import com.reviewmeter.tfg.model.Usuario;
import com.reviewmeter.tfg.repository.reporteRepository;
import com.reviewmeter.tfg.repository.resenaRepository;
import com.reviewmeter.tfg.repository.usuarioRepository;

@Service
public class reporteService {

    @Autowired
    private reporteRepository reporteRepo;

    @Autowired
    private resenaRepository resenaRepo;

    @Autowired
    private usuarioRepository usuarioRepo;

    /** Reportar una reseña */
    public Reporte reportar(Long idResena, Long idUsuario, String motivo, String descripcion) {

        reporteRepo.findByResena_IdResenaAndUsuarioReporta_IdUsuario(idResena, idUsuario)
                .ifPresent(r -> {
                    throw new IllegalStateException("Ya has reportado esta reseña anteriormente");
                });

        Resena resena = resenaRepo.findById(idResena)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Reporte reporte = new Reporte();
        reporte.setResena(resena);
        reporte.setUsuarioReporta(usuario);
        reporte.setMotivo(motivo);
        reporte.setDescripcion(descripcion);
        reporte.setFecha(LocalDateTime.now());
        reporte.setEstado("PENDIENTE");

        return reporteRepo.save(reporte);
    }

    /** Reportes pendientes para el panel admin */
    public List<Reporte> getPendientes() {
        return reporteRepo.findByEstadoOrderByFechaDesc("PENDIENTE");
    }

    /** Todos los reportes */
    public List<Reporte> getTodos() {
        return reporteRepo.findAll();
    }

    /** Descartar reporte — la reseña se mantiene */
    public Reporte descartar(Long idReporte) {
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        reporte.setEstado("DESCARTADA");
        return reporteRepo.save(reporte);
    }

    /**
     * Eliminar la reseña asociada al reporte.
     * Se usa resenaRepo.delete(entity) en lugar de deleteById para que
     * JPA cargue la entidad en contexto y el CascadeType.ALL elimine
     * los reportes asociados antes de borrar la reseña.
     */
    public void eliminarResena(Long idReporte) {
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        Resena resena = resenaRepo.findById(reporte.getResena().getIdResena())
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        resenaRepo.delete(resena);
    }

    /** Comprobar si el usuario ya reportó esta reseña */
    public boolean yaReporto(Long idResena, Long idUsuario) {
        return reporteRepo.findByResena_IdResenaAndUsuarioReporta_IdUsuario(idResena, idUsuario).isPresent();
    }
}
