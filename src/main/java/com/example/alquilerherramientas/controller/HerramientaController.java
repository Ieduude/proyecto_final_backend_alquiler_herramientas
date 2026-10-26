package com.example.alquilerherramientas.controller;

import com.example.alquilerherramientas.entity.Herramienta;
import com.example.alquilerherramientas.service.HerramientaService;
import com.example.alquilerherramientas.dto.HerramientaRequestDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/herramientas")
public class HerramientaController {

    private final HerramientaService herramientaService;

    public HerramientaController(HerramientaService herramientaService) {
        this.herramientaService = herramientaService;
    }

    @GetMapping
    public List<Herramienta> listar() {
        return herramientaService.listar();
    }

    @GetMapping("/disponibles")
    public List<Herramienta> listarDisponibles() {
        return herramientaService.listarDisponibles();
    }

    @GetMapping("/{id}")
    public Herramienta buscarPorId(@PathVariable Long id) {
        return herramientaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Herramienta> registrar(
            @Valid @RequestBody HerramientaRequestDTO dto) {

        Herramienta nuevaHerramienta = herramientaService.registrar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaHerramienta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        herramientaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Herramienta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HerramientaRequestDTO dto) {

        Herramienta herramientaActualizada =
                herramientaService.actualizar(id, dto);

        return ResponseEntity.ok(herramientaActualizada);
    }
}