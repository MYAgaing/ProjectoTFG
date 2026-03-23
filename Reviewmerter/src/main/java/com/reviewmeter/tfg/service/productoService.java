package com.reviewmeter.tfg.service;

import java.util.List;


import com.reviewmeter.tfg.model.Producto;

public interface productoService {

	List<Producto> getProductos();

	Producto getProducto(Long id);

	String crearProducto(Producto producto);

	String actualizarProducto(Long id, Producto producto);

	String borrarProducto(Long id);

	List<Producto> getProductosPorCategoria(Long id);
}
