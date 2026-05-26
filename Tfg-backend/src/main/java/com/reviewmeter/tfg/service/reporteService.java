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
import com.reviewmeter.tfg.repository.votoUtilRepository;

@Service
public class reporteService {

    @Autowired
    private reporteRepository reporteRepo;

    @Autowired
    private resenaRepository resenaRepo;

    @Autowired
    private usuarioRepository usuarioRepo;

    @Autowired
    private votoUtilRepository votoUtilRepo;

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

    public List<Reporte> getPendientes() {
        return reporteRepo.findByEstadoOrderByFechaDesc("PENDIENTE");
    }

    public List<Reporte> getTodos() {
        return reporteRepo.findAll();
    }

    public Reporte descartar(Long idReporte) {
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        reporte.setEstado("DESCARTADA");
        return reporteRepo.save(reporte);
    }

    // Hay que borrar primero los votos porque no tienen cascade en Resena,
    // luego la reseña que por cascade elimina sus reportes
    @org.springframework.transaction.annotation.Transactional
    public void eliminarResena(Long idReporte) {
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        Resena resena = resenaRepo.findById(reporte.getResena().getIdResena())
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        votoUtilRepo.deleteByResena_IdResena(resena.getIdResena());
        resenaRepo.delete(resena);
    }

    public boolean yaReporto(Long idResena, Long idUsuario) {
        return reporteRepo.findByResena_IdResenaAndUsuarioReporta_IdUsuario(idResena, idUsuario).isPresent();
    }
}
