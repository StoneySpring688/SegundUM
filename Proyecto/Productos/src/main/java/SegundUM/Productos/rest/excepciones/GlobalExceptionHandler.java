package SegundUM.Productos.rest.excepciones;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import SegundUM.Productos.repositorio.EntidadNoEncontrada;
import SegundUM.Productos.servicio.categorias.CargaCategoriaXMLException;

/** Manejador global de excepciones que traduce errores del dominio a respuestas HTTP uniformes. */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntidadNoEncontrada.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFound(EntidadNoEncontrada ex) {
        logger.warn("Entidad no encontrada: {}", ex.getMessage());
        return new ErrorResponse(404, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleBadRequest(IllegalArgumentException ex) {
        logger.warn("Argumento inválido: {}", ex.getMessage());
        return new ErrorResponse(400, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleConflict(IllegalStateException ex) {
        logger.warn("Conflicto de estado: {}", ex.getMessage());
        return new ErrorResponse(409, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleForbidden(AccessDeniedException ex) {
        logger.warn("Acceso denegado: {}", ex.getMessage());
        return new ErrorResponse(403, ex.getMessage());
    }

    @ExceptionHandler(CargaCategoriaXMLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleCargaXML(CargaCategoriaXMLException ex) {
        logger.error("Error al cargar categorías XML: {}", ex.getMessage());
        return new ErrorResponse(500, ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleIOException(java.io.IOException ex) {
        logger.error("Error de entrada/salida: {}", ex.getMessage());
        return new ErrorResponse(500, "Error interno del servidor: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Construir un mapa campo→mensaje para devolver los errores de validación de forma estructurada
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Error de validacion: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
