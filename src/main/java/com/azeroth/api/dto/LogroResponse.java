package com.azeroth.api.dto;

import com.azeroth.api.enums.EstadoLogro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LogroResponse(
        Long id,
        String titulo,
        String descripcion,
        BigDecimal puntosDeLogro,
        int valorActual,
        EstadoLogro estado,
        LocalDate fechaCompletado,
        int valorObjetivo
) {
}
