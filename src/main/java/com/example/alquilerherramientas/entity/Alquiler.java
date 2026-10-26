package com.example.alquilerherramientas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombreCliente;

    @NotNull(message = "Debe seleccionar una herramienta")
    @ManyToOne
    private Herramienta herramienta;

    @NotNull(message = "La fecha de alquiler es obligatoria")
    @PastOrPresent(message = "La fecha de alquiler no puede ser futura")
    private LocalDate fechaAlquiler;

    @NotNull(message = "La fecha prevista de devolución es obligatoria")
    private LocalDate fechaPrevistaDevolucion;

    private LocalDate fechaDevolucion;

    private Double costoEstimado;

    public Alquiler() {
    }

    public Alquiler(String nombreCliente, Herramienta herramienta,
                    LocalDate fechaAlquiler, LocalDate fechaPrevistaDevolucion,
                    LocalDate fechaDevolucion, Double costoEstimado) {
        this.nombreCliente = nombreCliente;
        this.herramienta = herramienta;
        this.fechaAlquiler = fechaAlquiler;
        this.fechaPrevistaDevolucion = fechaPrevistaDevolucion;
        this.fechaDevolucion = fechaDevolucion;
        this.costoEstimado = costoEstimado;
    }

    public Long getId() {
        return id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Herramienta getHerramienta() {
        return herramienta;
    }

    public void setHerramienta(Herramienta herramienta) {
        this.herramienta = herramienta;
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

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Double getCostoEstimado() {
        return costoEstimado;
    }

    public void setCostoEstimado(Double costoEstimado) {
        this.costoEstimado = costoEstimado;
    }
}