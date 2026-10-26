package com.example.alquilerherramientas.service;

import com.example.alquilerherramientas.dto.AlquilerRequestDTO;
import com.example.alquilerherramientas.entity.Alquiler;
import com.example.alquilerherramientas.entity.Herramienta;
import com.example.alquilerherramientas.exception.DevolucionYaRegistradaException;
import com.example.alquilerherramientas.exception.FechasInvalidasException;
import com.example.alquilerherramientas.exception.RecursoNoDisponibleException;
import com.example.alquilerherramientas.exception.RecursoNoEncontradoException;
import com.example.alquilerherramientas.repository.AlquilerRepository;
import com.example.alquilerherramientas.repository.HerramientaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.alquilerherramientas.dto.ResumenAlquileresDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AlquilerService {

    private final AlquilerRepository alquilerRepository;
    private final HerramientaRepository herramientaRepository;

    public AlquilerService(
            AlquilerRepository alquilerRepository,
            HerramientaRepository herramientaRepository) {
        this.alquilerRepository = alquilerRepository;
        this.herramientaRepository = herramientaRepository;
    }

    public List<Alquiler> listar() {
        return alquilerRepository.findAll();
    }

    public Alquiler buscarPorId(Long id) {
        return alquilerRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Alquiler no encontrado con ID: " + id));
    }

    @Transactional
    public Alquiler guardar(AlquilerRequestDTO dto) {

        if (dto.getFechaAlquiler() == null
                || dto.getFechaPrevistaDevolucion() == null) {
            throw new FechasInvalidasException(
                    "Las fechas del alquiler son obligatorias");
        }

        if (dto.getFechaAlquiler().isAfter(LocalDate.now())) {
            throw new FechasInvalidasException(
                    "La fecha de alquiler no puede ser futura");
        }

        long dias = ChronoUnit.DAYS.between(
                dto.getFechaAlquiler(),
                dto.getFechaPrevistaDevolucion()
        );

        if (dias <= 0) {
            throw new FechasInvalidasException(
                    "La fecha prevista debe ser posterior a la fecha de alquiler");
        }

        if (dto.getHerramientaId() == null
                || dto.getHerramientaId() <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una herramienta válida");
        }

        Herramienta herramienta = herramientaRepository
                .findById(dto.getHerramientaId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Herramienta no encontrada con ID: "
                                        + dto.getHerramientaId()));

        if (!herramienta.isDisponible()) {
            throw new RecursoNoDisponibleException(
                    "La herramienta no está disponible para alquilar");
        }

        double costo = dias * herramienta.getTarifaDiaria();

        Alquiler alquiler = new Alquiler();
        alquiler.setNombreCliente(dto.getNombreCliente().trim());
        alquiler.setHerramienta(herramienta);
        alquiler.setFechaAlquiler(dto.getFechaAlquiler());
        alquiler.setFechaPrevistaDevolucion(
                dto.getFechaPrevistaDevolucion());
        alquiler.setFechaDevolucion(null);
        alquiler.setCostoEstimado(costo);

        herramienta.setDisponible(false);
        herramientaRepository.save(herramienta);

        return alquilerRepository.save(alquiler);
    }

    @Transactional
    public void eliminar(Long id) {
        Alquiler alquiler = buscarPorId(id);

        if (alquiler.getFechaDevolucion() == null) {
            throw new RecursoNoDisponibleException(
                    "No se puede eliminar un alquiler activo; "
                            + "primero debe registrarse su devolución");
        }

        alquilerRepository.delete(alquiler);
    }

    @Transactional
    public Alquiler registrarDevolucion(Long id) {
        Alquiler alquiler = buscarPorId(id);

        if (alquiler.getFechaDevolucion() != null) {
            throw new DevolucionYaRegistradaException(
                    "Este alquiler ya tiene registrada su devolución");
        }

        LocalDate hoy = LocalDate.now();

        alquiler.setFechaDevolucion(hoy);

        Herramienta herramienta = alquiler.getHerramienta();
        herramienta.setDisponible(true);
        herramientaRepository.save(herramienta);

        return alquilerRepository.save(alquiler);
    }

    public List<Alquiler> listarAtrasados() {
        LocalDate hoy = LocalDate.now();

        return alquilerRepository.findAll().stream()
                .filter(alquiler ->
                        alquiler.getFechaDevolucion() == null
                                && alquiler.getFechaPrevistaDevolucion()
                                .isBefore(hoy))
                .toList();
    }

    public List<Alquiler> listarActivos() {
        return alquilerRepository.findAll().stream()
                .filter(alquiler ->
                        alquiler.getFechaDevolucion() == null)
                .toList();
    }

    public ResumenAlquileresDTO obtenerResumen() {

        List<Alquiler> alquileres = alquilerRepository.findAll();

        long totalAlquileres = alquileres.size();

        long activos = alquileres.stream()
                .filter(alquiler -> alquiler.getFechaDevolucion() == null)
                .count();

        LocalDate hoy = LocalDate.now();

        long atrasados = alquileres.stream()
                .filter(alquiler ->
                        alquiler.getFechaDevolucion() == null
                                && alquiler.getFechaPrevistaDevolucion() != null
                                && alquiler.getFechaPrevistaDevolucion().isBefore(hoy))
                .count();

        double costoTotal = alquileres.stream()
                .mapToDouble(alquiler ->
                        alquiler.getCostoEstimado() == null
                                ? 0.0
                                : alquiler.getCostoEstimado())
                .sum();

        return new ResumenAlquileresDTO(
                totalAlquileres,
                activos,
                atrasados,
                costoTotal
        );
    }
}