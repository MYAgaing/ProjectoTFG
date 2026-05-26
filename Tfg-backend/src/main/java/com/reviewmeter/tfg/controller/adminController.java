package com.reviewmeter.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Reporte;
import com.reviewmeter.tfg.service.reporteService;

@RestController
@RequestMapping("/admin/reportes")
@CrossOrigin(origins = "*")
public class adminController {

    @Autowired
    private reporteService reporteService;

    @GetMapping("/pendientes")
    public ResponseEntity<List<Reporte>> getPendientes() {
        return ResponseEntity.ok(reporteService.getPendientes());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Reporte>> getTodos() {
        return ResponseEntity.ok(reporteService.getTodos());
    }

    @PutMapping("/{id}/descartar")
    public ResponseEntity<?> descartar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reporteService.descartar(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/eliminar-resena")
    public ResponseEntity<String> eliminarResena(@PathVariable Long id) {
        try {
            reporteService.eliminarResena(id);
            return ResponseEntity.ok("Reseña eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
