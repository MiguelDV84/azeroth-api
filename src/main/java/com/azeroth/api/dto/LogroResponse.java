package com.azeroth.api.dto;

import java.math.BigDecimal;

public record LogroResponse(
        String titulo,
        String descripcion,
        BigDecimal puntosDeLogro,
        int valorObjetivo
) {
}
