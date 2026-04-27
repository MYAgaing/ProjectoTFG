package com.reviewmeter.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reviewmeter.tfg.model.Pago;
import com.reviewmeter.tfg.repository.pagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class pagoServiceImpl implements pagoService {

    @Autowired
    private pagoRepository pagoRepository;

    @Override
    public List<Pago> getPagos() {
        return pagoRepository.findAll();
    }

    @Override
    public Pago getPago(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
    }

    @Override
    public String crearPago(Pago pago) {
        pagoRepository.save(pago);
        return "Pago creado correctamente";
    }

    @Override
    public String actualizarPago(Long id, Pago pago) {
        Pago pagoExistente = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
        
        pagoExistente.setEstado(pago.getEstado());
        pagoExistente.setFechaPago(pago.getFechaPago());
        pagoExistente.setImporte(pago.getImporte());
        pagoExistente.setMetodoPago(pago.getMetodoPago());
        pagoExistente.setSuscripcion(pago.getSuscripcion());
        pagoExistente.setUsuario(pago.getUsuario());

        pagoRepository.save(pago);
        return "Pago actualizado correctamente";
    }

    @Override
    public String borrarPago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));

        pagoRepository.delete(pago);
        return "Pago eliminado correctamente";
    }
}
