package com.azeroth.api.dto;

import com.azeroth.api.enums.Clases;
import com.azeroth.api.enums.Facciones;
import com.azeroth.api.enums.Razas;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record JugadorResponse(
        Long id,
        String nombre,
        Clases clase,
        Razas raza,
        Facciones faccion,
        int nivel,
        BigDecimal experiencia,
        String hermandad
) { }
