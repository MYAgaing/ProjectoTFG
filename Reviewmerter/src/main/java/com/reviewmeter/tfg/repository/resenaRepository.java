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

}
