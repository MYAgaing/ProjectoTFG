package com.reviewmeter.tfg.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Producto;
import com.reviewmeter.tfg.model.Resena;
import com.reviewmeter.tfg.repository.productoRepository;
import com.reviewmeter.tfg.repository.resenaRepository;

@RestController
@RequestMapping("/admin/estadisticas")
@CrossOrigin(origins = "*")
public class adminEstadisticasController {

    @Autowired
    private resenaRepository resenaRepo;

    @Autowired
    private productoRepository productoRepo;

    /**
     * GET /admin/estadisticas/buscar?nombre=xxx
     * Busca productos por nombre para el buscador del panel.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam String nombre) {
        return ResponseEntity.ok(productoRepo.findByNombreContainingIgnoreCase(nombre));
    }

    /**
     * GET /admin/estadisticas/producto/{id}
     * Devuelve todas las estadísticas de un producto.
     */
    @GetMapping("/producto/{id}")
    public ResponseEntity<?> estadisticasProducto(@PathVariable Long id) {

        if (!productoRepo.existsById(id)) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        Producto producto = productoRepo.findById(id).get();
        Map<String, Object> stats = new LinkedHashMap<>();

        // ── Métricas básicas ─────────────────────────────────────────────────────
        long total = resenaRepo.countByProducto(id);
        Double avg = resenaRepo.avgPuntuacionByProducto(id);
        double media = avg != null
                ? BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP).doubleValue()
                : 0.0;

        stats.put("idProducto", producto.getIdProducto());
        stats.put("nombreProducto", producto.getNombre());
        stats.put("marca", producto.getMarca());
        stats.put("categoria", producto.getCategoria() != null ? producto.getCategoria().getNombre() : "");
        stats.put("imageUrl", producto.getImageUrl());
        stats.put("totalResenas", total);
        stats.put("puntuacionMedia", media);

        // ── Distribución por estrellas ───────────────────────────────────────────
        Map<Integer, Long> distribucion = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) distribucion.put(i, 0L);
        for (Object[] row : resenaRepo.distribucionPuntuacionByProducto(id)) {
            distribucion.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue());
        }
        stats.put("distribucion", distribucion);

        // ── Porcentaje por estrella ──────────────────────────────────────────────
        Map<Integer, Double> porcentajes = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) {
            double pct = total > 0
                    ? BigDecimal.valueOf((double) distribucion.get(i) / total * 100)
                              .setScale(1, RoundingMode.HALF_UP).doubleValue()
                    : 0.0;
            porcentajes.put(i, pct);
        }
        stats.put("porcentajes", porcentajes);

        // ── Evolución mensual últimos 6 meses ────────────────────────────────────
        LocalDate desde = LocalDate.now().minusMonths(6).withDayOfMonth(1);
        List<Object[]> rawEvolucion = resenaRepo.evolucionMensualByProducto(id, desde);

        // Construir mapa mes->datos para rellenar meses sin reseñas
        Map<String, Object[]> mapaEvol = new HashMap<>();
        for (Object[] row : rawEvolucion) {
            String key = row[1] + "-" + String.format("%02d", ((Number) row[0]).intValue());
            mapaEvol.put(key, row);
        }

        List<Map<String, Object>> evolucion = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate mes = LocalDate.now().minusMonths(i).withDayOfMonth(1);
            String key = mes.getYear() + "-" + String.format("%02d", mes.getMonthValue());
            Map<String, Object> punto = new LinkedHashMap<>();
            punto.put("anio", mes.getYear());
            punto.put("mes", mes.getMonthValue());
            punto.put("etiqueta", nombreMes(mes.getMonthValue()) + " " + mes.getYear());
            if (mapaEvol.containsKey(key)) {
                Object[] row = mapaEvol.get(key);
                punto.put("totalResenas", ((Number) row[2]).longValue());
                double avgMes = row[3] != null
                        ? BigDecimal.valueOf(((Number) row[3]).doubleValue())
                                  .setScale(1, RoundingMode.HALF_UP).doubleValue()
                        : 0.0;
                punto.put("puntuacionMedia", avgMes);
            } else {
                punto.put("totalResenas", 0);
                punto.put("puntuacionMedia", 0.0);
            }
            evolucion.add(punto);
        }
        stats.put("evolucionMensual", evolucion);

        // ── Últimas 5 reseñas ────────────────────────────────────────────────────
        List<Resena> ultimas = resenaRepo.findTop5ByProductoOrderByFechaDesc(
                id, PageRequest.of(0, 5));
        List<Map<String, Object>> ultimasDto = new ArrayList<>();
        for (Resena r : ultimas) {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("idResena", r.getIdResena());
            dto.put("titulo", r.getTitulo());
            dto.put("comentario", r.getComentario());
            dto.put("puntuacion", r.getPuntuacion());
            dto.put("fecha", r.getFecha());
            dto.put("autor", r.getUsuario() != null ? r.getUsuario().getNombre() : "Anónimo");
            ultimasDto.add(dto);
        }
        stats.put("ultimasResenas", ultimasDto);

        return ResponseEntity.ok(stats);
    }

    private String nombreMes(int mes) {
        String[] meses = {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"};
        return meses[mes - 1];
    }
}
