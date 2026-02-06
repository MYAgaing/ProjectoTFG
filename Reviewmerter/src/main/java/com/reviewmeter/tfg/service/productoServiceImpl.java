package com.reviewmeter.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reviewmeter.tfg.model.Producto;
import com.reviewmeter.tfg.repository.productoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class productoServiceImpl implements productoService {

    @Autowired
    private productoRepository productoRepository;

    @Override
    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto getProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Override
    public String crearProducto(Producto producto) {
        productoRepository.save(producto);
        return "Producto creado correctamente";
    }

    @Override
    public String actualizarProducto(Long id, Producto producto) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        
        productoExistente.setCategoria(producto.getCategoria());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setFechaLanzamiento(producto.getFechaLanzamiento());
        productoExistente.setMarca(producto.getMarca());
        productoExistente.setNombre(producto.getNombre());

        productoRepository.save(producto);
        return "Producto actualizado correctamente";
    }

    @Override
    public String borrarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        productoRepository.delete(producto);
        return "Producto eliminado correctamente";
    }
}
