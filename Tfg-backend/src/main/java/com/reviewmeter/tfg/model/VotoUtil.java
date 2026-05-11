package com.reviewmeter.tfg.model;

import jakarta.persistence.*;

@Entity
@Table(name = "voto_util",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_resena", "id_usuario"}))
public class VotoUtil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_resena", nullable = false)
    private Resena resena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // "UTIL" o "NO_UTIL"
    @Column(nullable = false)
    private String tipo = "UTIL";

    public VotoUtil() {}

    public VotoUtil(Resena resena, Usuario usuario, String tipo) {
        this.resena = resena;
        this.usuario = usuario;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public Resena getResena() { return resena; }
    public void setResena(Resena resena) { this.resena = resena; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
