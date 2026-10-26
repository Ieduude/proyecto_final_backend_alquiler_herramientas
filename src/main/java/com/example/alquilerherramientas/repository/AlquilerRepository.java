package com.example.alquilerherramientas.repository;

import com.example.alquilerherramientas.entity.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
}