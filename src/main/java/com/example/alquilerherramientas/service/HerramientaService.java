package com.example.alquilerherramientas.service;

import com.example.alquilerherramientas.dto.HerramientaRequestDTO;
import com.example.alquilerherramientas.entity.Herramienta;
import com.example.alquilerherramientas.entity.Alquiler;
import com.example.alquilerherramientas.exception.RecursoNoDisponibleException;
import com.example.alquilerherramientas.exception.RecursoNoEncontradoException;
import com.example.alquilerherramientas.repository.AlquilerRepository;
import com.example.alquilerherramientas.repository.HerramientaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HerramientaService {

    private final HerramientaRepository herramientaRepository;
    private final AlquilerRepository alquilerRepository;

    public HerramientaService(
            HerramientaRepository herramientaRepository,
            AlquilerRepository alquilerRepository) {
        this.herramientaRepository = herramientaRepository;
        this.alquilerRepository = alquilerRepository;
    }

    public List<Herramienta> listar() {
        return herramientaRepository.findAll();
    }

    public List<Herramienta> listarDisponibles() {
        return herramientaRepository.findAll()
                .stream()
                .filter(Herramienta::isDisponible)
                .toList();
    }

    public Herramienta buscarPorId(Long id) {
        return herramientaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Herramienta no encontrada con ID: " + id));
    }

    public Herramienta registrar(HerramientaRequestDTO dto) {
        Herramienta herramienta = new Herramienta();

        herramienta.setNombre(dto.getNombre());
        herramienta.setDescripcion(dto.getDescripcion());
        herramienta.setTarifaDiaria(dto.getTarifaDiaria());
        herramienta.setDisponible(dto.getDisponible());

        return herramientaRepository.save(herramienta);
    }

    public Herramienta actualizar(Long id, HerramientaRequestDTO dto) {
        Herramienta existente = buscarPorId(id);

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setTarifaDiaria(dto.getTarifaDiaria());

        boolean tieneAlquilerActivo = alquilerRepository.findAll()
                .stream()
                .anyMatch(alquiler ->
                        alquiler.getHerramienta() != null
                                && alquiler.getHerramienta().getId().equals(id)
                                && alquiler.getFechaDevolucion() == null);

        // No permitir marcar como disponible una herramienta alquilada.
        if (tieneAlquilerActivo) {
            existente.setDisponible(false);
        } else {
            existente.setDisponible(dto.getDisponible());
        }

        return herramientaRepository.save(existente);
    }

    public void eliminar(Long id) {
        Herramienta herramienta = buscarPorId(id);

        boolean tieneAlquilerActivo = alquilerRepository.findAll()
                .stream()
                .anyMatch(alquiler ->
                        alquiler.getHerramienta() != null
                                && alquiler.getHerramienta().getId().equals(id)
                                && alquiler.getFechaDevolucion() == null);

        if (tieneAlquilerActivo) {
            throw new RecursoNoDisponibleException(
                    "No se puede eliminar una herramienta con un alquiler activo");
        }

        herramientaRepository.delete(herramienta);
    }
}