package com.reviewmeter.tfg.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReporte;

    /** Motivo del reporte: "FALSA", "INAPROPIADA", "SPAM", "OTRO" */
    @Column(nullable = false)
    private String motivo;

    /** Descripción opcional que añade el usuario */
    private String descripcion;

    /** Fecha y hora del reporte */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /**
     * Estado del reporte: "PENDIENTE", "REVISADA", "DESCARTADA"
     */
    @Column(nullable = false)
    private String estado = "PENDIENTE";

    @ManyToOne
    @JoinColumn(name = "id_resena", nullable = false)
    @JsonIgnoreProperties({"usuario", "producto"})
    private Resena resena;

    @ManyToOne
    @JoinColumn(name = "id_usuario_reporta", nullable = false)
    @JsonIgnoreProperties({"password", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
    private Usuario usuarioReporta;

    // ── Getters y Setters ────────────────────────────────────────────────────────

    public Long getIdReporte() { return idReporte; }
    public void setIdReporte(Long idReporte) { this.idReporte = idReporte; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Resena getResena() { return resena; }
    public void setResena(Resena resena) { this.resena = resena; }

    public Usuario getUsuarioReporta() { return usuarioReporta; }
    public void setUsuarioReporta(Usuario usuarioReporta) { this.usuarioReporta = usuarioReporta; }
}
