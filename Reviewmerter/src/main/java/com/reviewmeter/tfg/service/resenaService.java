package com.reviewmeter.tfg.service;

import java.time.LocalDate;
import java.util.List;


import com.reviewmeter.tfg.model.Resena;

public interface resenaService {

	List<Resena> getResenas();

	Resena getResena(Long id);

	Resena crearResena(Resena resena);

	Resena actualizarResena(Long id, Resena resenaActualizada);

	String borrarResena(Long id);

	List<Resena> filtrarPorFechas(LocalDate inicio, LocalDate fin);

	List<Resena> filtrarPorPuntuacion(int min);

	List<Resena> ordenarPorPuntuacionDesc();

	List<Resena> ordenarPorPuntuacionAsc();

	List<Resena> getResenasPorProducto(Long idProducto);

	List<Resena> getResenasPorProductoOrdenDesc(Long idProducto);

	List<Resena> getResenasPorProductoOrdenAsc(Long idProducto);

	List<Resena> getResenasPorProductoYPuntuacion(Long idProducto, int min);

	List<Resena> getResenasPorUsuario(Long idUsuario);
}
