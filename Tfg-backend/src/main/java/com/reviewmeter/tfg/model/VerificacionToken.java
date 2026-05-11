package com.reviewmeter.tfg.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "verificacion_token")
public class VerificacionToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime expiracion;

    public VerificacionToken() {}

    public VerificacionToken(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
        // El enlace expira en 24 horas
        this.expiracion = LocalDateTime.now().plusHours(24);
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(this.expiracion);
    }

    // ── Getters y setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getExpiracion() { return expiracion; }
    public void setExpiracion(LocalDateTime expiracion) { this.expiracion = expiracion; }
}
