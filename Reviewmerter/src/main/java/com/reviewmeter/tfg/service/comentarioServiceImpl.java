package com.reviewmeter.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reviewmeter.tfg.model.Comentario;
import com.reviewmeter.tfg.repository.comentarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class comentarioServiceImpl implements comentarioService {

    @Autowired
    private comentarioRepository comentarioRepository;

    @Override
    public List<Comentario> getComentarios() {
        return comentarioRepository.findAll();
    }

    @Override
    public Comentario getComentario(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con id: " + id));
    }

    @Override
    public String crearComentario(Comentario comentario) {
        comentarioRepository.save(comentario);
        return "Comentario creado correctamente";
    }

    @Override
    public String actualizarComentario(Long id,Comentario comentario) {
        Comentario comentarioExistente = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con id: " + id));
        
        comentarioExistente.setContenido(comentario.getContenido());
        comentarioExistente.setFecha(comentario.getFecha());
        comentarioExistente.setResena(comentario.getResena());

        comentarioRepository.save(comentario);
        return "Comentario actualizado correctamente";
    }

    @Override
    public String borrarComentario(Long id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con id: " + id));

        comentarioRepository.delete(comentario);
        return "Comentario eliminado correctamente";
    }
    
    @Override
    public List<Comentario> getComentariosPorResena(Long idResena) {
        return comentarioRepository.findByResenaIdResena(idResena);
    }

}
