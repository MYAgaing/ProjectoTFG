package com.reviewmeter.tfg.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Comentario")
public class Comentario {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    private String contenido;
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_resena", nullable = false)
    private Resena resena;

	public Long getIdComentario() {
		return idComentario;
	}

	public void setIdComentario(Long idComentario) {
		this.idComentario = idComentario;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Resena getResena() {
		return resena;
	}

	public void setResena(Resena resena) {
		this.resena = resena;
	}
}
