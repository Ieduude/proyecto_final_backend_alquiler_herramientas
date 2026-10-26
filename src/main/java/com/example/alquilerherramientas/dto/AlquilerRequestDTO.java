package com.example.alquilerherramientas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AlquilerRequestDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre del cliente admite como máximo 100 caracteres")
    private String nombreCliente;

    @NotNull(message = "Debe seleccionar una herramienta")
    @Positive(message = "El ID de la herramienta debe ser mayor que cero")
    private Long herramientaId;

    @NotNull(message = "La fecha de alquiler es obligatoria")
    @PastOrPresent(message = "La fecha de alquiler no puede ser futura")
    private LocalDate fechaAlquiler;

    @NotNull(message = "La fecha prevista de devolución es obligatoria")
    private LocalDate fechaPrevistaDevolucion;

    public AlquilerRequestDTO() {
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Long getHerramientaId() {
        return herramientaId;
    }

    public void setHerramientaId(Long herramientaId) {
        this.herramientaId = herramientaId;
    }

    public LocalDate getFechaAlquiler() {
        return fechaAlquiler;
    }

    public void setFechaAlquiler(LocalDate fechaAlquiler) {
        this.fechaAlquiler = fechaAlquiler;
    }

    public LocalDate getFechaPrevistaDevolucion() {
        return fechaPrevistaDevolucion;
    }

    public void setFechaPrevistaDevolucion(LocalDate fechaPrevistaDevolucion) {
        this.fechaPrevistaDevolucion = fechaPrevistaDevolucion;
    }
}