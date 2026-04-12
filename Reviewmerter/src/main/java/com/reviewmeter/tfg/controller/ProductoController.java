package com.reviewmeter.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewmeter.tfg.model.Producto;
import com.reviewmeter.tfg.service.productoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

	    @Autowired
	    private productoService productoService;

	    // GET /api/productos/categoria/{idCategoria}
	    @GetMapping("/categoria/{id}")
	    public List<Producto> getProductosPorCategoria(@PathVariable("id") Long idCategoria) {
	        return productoService.getProductosPorCategoria(idCategoria);
	    }	
}
