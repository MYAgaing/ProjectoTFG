package com.reviewmeter.tfg.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewmeter.tfg.model.Resena;

public interface resenaRepository extends JpaRepository<Resena, Long>{

	List<Resena> findByFechaBetween(LocalDate inicio, LocalDate fin);

    List<Resena> findByPuntuacionGreaterThanEqual(int min);

    List<Resena> findAllByOrderByPuntuacionDesc();

    List<Resena> findAllByOrderByPuntuacionAsc();
    
    List<Resena> findByProducto_IdProducto(Long idProducto);

    List<Resena> findByUsuario_IdUsuario(Long idUsuario);

    List<Resena> findByProducto_IdProductoOrderByPuntuacionDesc(Long idProducto);

    List<Resena> findByProducto_IdProductoOrderByPuntuacionAsc(Long idProducto);

    List<Resena> findByProducto_IdProductoAndPuntuacionGreaterThanEqual(Long idProducto, int min);

}
