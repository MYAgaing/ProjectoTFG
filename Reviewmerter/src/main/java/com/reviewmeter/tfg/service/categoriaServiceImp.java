package com.reviewmeter.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reviewmeter.tfg.model.Categoria;
import com.reviewmeter.tfg.repository.categoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class categoriaServiceImp implements categoriaService {

    @Autowired
    private categoriaRepository categoriaRepository;

    @Override
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria getCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
    }

    @Override
    public String crearCategoria(Categoria categoria) {
    	if (categoria == null) {
    		return "Categoria invalida";
    	}
        categoriaRepository.save(categoria);
        return "Categoría creada correctamente";
    }

    @Override
    public String actualizarCategoria(Long id, Categoria categoria) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
        categoriaExistente.setNombre(categoria.getNombre());
        categoriaExistente.setDescripcion(categoria.getDescripcion());
        
        categoriaRepository.save(categoria);
        return "Categoría actualizada correctamente";
    }

    @Override
    public String borrarCategoria(Long id) {
    	Categoria categoriaExistente = categoriaRepository.findById(id)
    			.orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
    	categoriaRepository.delete(categoriaExistente);
        return "Categoría eliminada correctamente";
    }
}
