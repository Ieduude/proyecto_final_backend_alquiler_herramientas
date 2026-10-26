package com.example.alquilerherramientas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresValidacion(
            MethodArgumentNotValidException excepcion) {

        Map<String, String> errores = new LinkedHashMap<>();

        excepcion.getBindingResult().getFieldErrors().forEach(error ->
                errores.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("estado", 400);
        respuesta.put("mensaje", "Los datos enviados no son válidos");
        respuesta.put("errores", errores);

        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException excepcion) {

        return crearRespuesta(
                HttpStatus.NOT_FOUND,
                excepcion.getMessage()
        );
    }

    @ExceptionHandler(RecursoNoDisponibleException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoDisponible(
            RecursoNoDisponibleException excepcion) {

        return crearRespuesta(
                HttpStatus.CONFLICT,
                excepcion.getMessage()
        );
    }

    @ExceptionHandler(FechasInvalidasException.class)
    public ResponseEntity<Map<String, Object>> manejarFechasInvalidas(
            FechasInvalidasException excepcion) {

        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                excepcion.getMessage()
        );
    }

    @ExceptionHandler(DevolucionYaRegistradaException.class)
    public ResponseEntity<Map<String, Object>> manejarDevolucionDuplicada(
            DevolucionYaRegistradaException excepcion) {

        return crearRespuesta(
                HttpStatus.CONFLICT,
                excepcion.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumentoInvalido(
            IllegalArgumentException excepcion) {

        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                excepcion.getMessage()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarCuerpoInvalido(
            HttpMessageNotReadableException excepcion) {

        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud está vacío o tiene un formato inválido"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorInesperado(
            Exception excepcion) {

        return crearRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor"
        );
    }

    private ResponseEntity<Map<String, Object>> crearRespuesta(
            HttpStatus estado, String mensaje) {

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("estado", estado.value());
        respuesta.put("mensaje", mensaje);

        return ResponseEntity.status(estado).body(respuesta);
    }
}