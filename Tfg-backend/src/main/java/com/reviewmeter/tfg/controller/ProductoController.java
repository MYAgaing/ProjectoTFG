package com.reviewmeter.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reviewmeter.tfg.model.Producto;
import com.reviewmeter.tfg.service.productoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    @Autowired
    private productoService productoService;

    // 1. Obtener todos los productos
    // GET http://localhost:8080/api/productos
    @GetMapping
    public List<Producto> getProductos() {
        return productoService.getProductos();
    }

    // 2. Obtener un producto por ID (EL QUE TE DABA EL ERROR 404)
    // GET http://localhost:8080/api/productos/1
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProducto(@PathVariable Long id) {
        try {
            Producto producto = productoService.getProducto(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. Obtener productos por categoría
    // GET http://localhost:8080/api/productos/categoria/2
    @GetMapping("/categoria/{id}")
    public List<Producto> getProductosPorCategoria(@PathVariable("id") Long idCategoria) {
        return productoService.getProductosPorCategoria(idCategoria);
    }

    // 4. Crear un producto
    // POST http://localhost:8080/api/productos
    @PostMapping
    public ResponseEntity<String> crearProducto(@RequestBody Producto producto) {
        String mensaje = productoService.crearProducto(producto);
        return ResponseEntity.ok(mensaje);
    }

    // 5. Actualizar un producto
    // PUT http://localhost:8080/api/productos/1
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            String mensaje = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. Borrar un producto
    // DELETE http://localhost:8080/api/productos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarProducto(@PathVariable Long id) {
        try {
            String mensaje = productoService.borrarProducto(id);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}