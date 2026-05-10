package com.reviewmeter.tfg.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Reseña")
public class Resena {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResena;

    private String titulo;
    private String comentario;
    private Integer puntuacion;
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({"password", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "resenas"})
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonIgnoreProperties({"resenas"})
    private Producto producto;

    /** Al borrar una reseña se borran sus reportes en cascada */
    @JsonIgnore
    @OneToMany(mappedBy = "resena", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reporte> reportes;

	public Long getIdResena() { return idResena; }
	public void setIdResena(Long idResena) { this.idResena = idResena; }

	public String getTitulo() { return titulo; }
	public void setTitulo(String titulo) { this.titulo = titulo; }

	public String getComentario() { return comentario; }
	public void setComentario(String comentario) { this.comentario = comentario; }

	public LocalDate getFecha() { return fecha; }
	public void setFecha(LocalDate fecha) { this.fecha = fecha; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Producto getProducto() { return producto; }
	public void setProducto(Producto producto) { this.producto = producto; }

	public Integer getPuntuacion() { return puntuacion; }
	public void setPuntuacion(Integer puntuacion) { this.puntuacion = puntuacion; }

	public List<Reporte> getReportes() { return reportes; }
	public void setReportes(List<Reporte> reportes) { this.reportes = reportes; }
}
