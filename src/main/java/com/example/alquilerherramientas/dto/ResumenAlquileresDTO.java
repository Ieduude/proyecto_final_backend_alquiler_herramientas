package com.example.alquilerherramientas.dto;

public class ResumenAlquileresDTO {

    private long totalAlquileres;
    private long alquileresActivos;
    private long alquileresAtrasados;
    private double costoEstimadoTotal;

    public ResumenAlquileresDTO(
            long totalAlquileres,
            long alquileresActivos,
            long alquileresAtrasados,
            double costoEstimadoTotal) {
        this.totalAlquileres = totalAlquileres;
        this.alquileresActivos = alquileresActivos;
        this.alquileresAtrasados = alquileresAtrasados;
        this.costoEstimadoTotal = costoEstimadoTotal;
    }

    public long getTotalAlquileres() {
        return totalAlquileres;
    }

    public long getAlquileresActivos() {
        return alquileresActivos;
    }

    public long getAlquileresAtrasados() {
        return alquileresAtrasados;
    }

    public double getCostoEstimadoTotal() {
        return costoEstimadoTotal;
    }
}