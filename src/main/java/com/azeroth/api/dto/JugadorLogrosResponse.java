package com.azeroth.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record JugadorLogrosResponse(
        String nombre,
        int nivel,
        BigDecimal experiencia,
        BigDecimal experienciaParaProximoNivel,
        List<LogroResponse> logros
) {
}
