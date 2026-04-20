package com.reviewmeter.tfg.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reviewmeter.tfg.model.Resena;
import com.reviewmeter.tfg.repository.resenaRepository;

@Service
public class resenaServiceImpl implements resenaService {

	@Autowired
	private resenaRepository resenaRepository;

	@Override
	public List<Resena> getResenas() {
		return resenaRepository.findAll();
	}

	@Override
	public Resena getResena(Long id) {
		return resenaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Reseña no encontrada con id: " + id));
	}

	@Override
	public Resena crearResena(Resena resena) {
		resenaRepository.save(resena);
		return resena;
	}

	@Override
	public Resena actualizarResena(Long id, Resena resenaActualizada) {
		Resena resenaExistente = resenaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Reseña no encontrada con id: " + id));

		resenaExistente.setTitulo(resenaActualizada.getTitulo());
		resenaExistente.setComentario(resenaActualizada.getComentario());
		resenaExistente.setPuntuacion(resenaActualizada.getPuntuacion());
		resenaExistente.setFecha(resenaActualizada.getFecha());

		resenaRepository.save(resenaExistente);

		return resenaExistente;
	}

	@Override
	public String borrarResena(Long id) {
		Resena resena = resenaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Reseña no encontrada con id: " + id));

		resenaRepository.delete(resena);
		return "Reseña eliminada correctamente";
	}

	@Override
	public List<Resena> filtrarPorFechas(LocalDate inicio, LocalDate fin) {
		return resenaRepository.findByFechaBetween(inicio, fin);
	}

	@Override
	public List<Resena> filtrarPorPuntuacion(int min) {
		return resenaRepository.findByPuntuacionGreaterThanEqual(min);
	}

	@Override
	public List<Resena> ordenarPorPuntuacionDesc() {
		return resenaRepository.findAllByOrderByPuntuacionDesc();
	}

	@Override
	public List<Resena> ordenarPorPuntuacionAsc() {
		return resenaRepository.findAllByOrderByPuntuacionAsc();
	}
	
	@Override
	public List<Resena> getResenasPorProducto(Long idProducto) {
	    return resenaRepository.findByProducto_IdProducto(idProducto);
	}

	@Override
	public List<Resena> getResenasPorProductoOrdenDesc(Long idProducto) {
	    return resenaRepository.findByProducto_IdProductoOrderByPuntuacionDesc(idProducto);
	}

	@Override
	public List<Resena> getResenasPorProductoOrdenAsc(Long idProducto) {
	    return resenaRepository.findByProducto_IdProductoOrderByPuntuacionAsc(idProducto);
	}

	@Override
	public List<Resena> getResenasPorProductoYPuntuacion(Long idProducto, int min) {
	    return resenaRepository.findByProducto_IdProductoAndPuntuacionGreaterThanEqual(idProducto, min);
	}

	@Override
	public List<Resena> getResenasPorUsuario(Long idUsuario) {
	    return resenaRepository.findByUsuario_IdUsuario(idUsuario);
	}

}
