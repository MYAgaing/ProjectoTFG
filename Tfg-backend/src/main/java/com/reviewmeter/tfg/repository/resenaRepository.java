package com.reviewmeter.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reviewmeter.tfg.model.Resena;

public interface resenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByFechaBetween(java.time.LocalDate inicio, java.time.LocalDate fin);
    List<Resena> findByPuntuacionGreaterThanEqual(int min);
    List<Resena> findAllByOrderByPuntuacionDesc();
    List<Resena> findAllByOrderByPuntuacionAsc();
    List<Resena> findByProducto_IdProducto(Long idProducto);
    List<Resena> findByUsuario_IdUsuario(Long idUsuario);
    List<Resena> findByProducto_IdProductoOrderByPuntuacionDesc(Long idProducto);
    List<Resena> findByProducto_IdProductoOrderByPuntuacionAsc(Long idProducto);
    List<Resena> findByProducto_IdProductoAndPuntuacionGreaterThanEqual(Long idProducto, int min);

    // ── Estadísticas de producto ─────────────────────────────────────────────────

    @Query("SELECT COUNT(r) FROM Resena r WHERE r.producto.idProducto = :id")
    long countByProducto(@Param("id") Long id);

    @Query("SELECT AVG(r.puntuacion) FROM Resena r WHERE r.producto.idProducto = :id")
    Double avgPuntuacionByProducto(@Param("id") Long id);

    @Query("SELECT r.puntuacion, COUNT(r) FROM Resena r WHERE r.producto.idProducto = :id GROUP BY r.puntuacion ORDER BY r.puntuacion ASC")
    List<Object[]> distribucionPuntuacionByProducto(@Param("id") Long id);

    @Query("SELECT FUNCTION('MONTH', r.fecha), FUNCTION('YEAR', r.fecha), COUNT(r), AVG(r.puntuacion) " +
           "FROM Resena r WHERE r.producto.idProducto = :id " +
           "AND r.fecha >= :desde " +
           "GROUP BY FUNCTION('YEAR', r.fecha), FUNCTION('MONTH', r.fecha) " +
           "ORDER BY FUNCTION('YEAR', r.fecha) ASC, FUNCTION('MONTH', r.fecha) ASC")
    List<Object[]> evolucionMensualByProducto(@Param("id") Long id, @Param("desde") java.time.LocalDate desde);

    @Query("SELECT r FROM Resena r WHERE r.producto.idProducto = :id ORDER BY r.fecha DESC")
    List<Resena> findTop5ByProductoOrderByFechaDesc(@Param("id") Long id,
            org.springframework.data.domain.Pageable pageable);
}
