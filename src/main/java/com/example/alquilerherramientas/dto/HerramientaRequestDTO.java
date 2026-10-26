package com.example.alquilerherramientas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class HerramientaRequestDTO {

    @NotBlank(message = "El nombre de la herramienta es obligatorio")
    @Size(max = 80, message = "El nombre admite como máximo 80 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción admite como máximo 255 caracteres")
    private String descripcion;

    @NotNull(message = "La tarifa diaria es obligatoria")
    @Positive(message = "La tarifa diaria debe ser mayor que cero")
    private Double tarifaDiaria;

    @NotNull(message = "Debe indicar si la herramienta está disponible")
    private Boolean disponible;

    public HerramientaRequestDTO() {
    }

    public HerramientaRequestDTO(
            String nombre,
            String descripcion,
            Double tarifaDiaria,
            Boolean disponible) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tarifaDiaria = tarifaDiaria;
        this.disponible = disponible;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(Double tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}