package com.reviewmeter.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewmeter.tfg.model.Categoria;
import com.reviewmeter.tfg.service.categoriaService;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriaController {

	private final categoriaService categoriaService;

    public CategoriaController(categoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> getCategorias() {
        return categoriaService.getCategorias();
    }

    @GetMapping("/{id}")
    public Categoria getCategoria(@PathVariable Long id) {
        return categoriaService.getCategoria(id);
    }
    
    @PostMapping
    public String crearCategoria(@RequestBody Categoria categoria) {
        return categoriaService.crearCategoria(categoria);
    }

    @PutMapping("/{id}")
    public String actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.actualizarCategoria(id, categoria);
    }

    @DeleteMapping("/{id}")
    public String borrarCategoria(@PathVariable Long id) {
        return categoriaService.borrarCategoria(id);
    }

}
