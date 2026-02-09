package com.azeroth.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record LogroRequest(
        @NotEmpty(message = "El título del logro no puede estar vacío")
        String titulo,
        @NotEmpty(message = "La descripción del logro no puede estar vacía")
        String descripcion,
        @NotNull(message = "Los puntos de logro no pueden ser nulos")
        @Positive(message = "Los puntos de logro deben ser positivos")
        BigDecimal puntosDeLogro,
        @Positive(message = "El valor objetivo debe ser positivo")
        int valorObjetivo
) {
}
