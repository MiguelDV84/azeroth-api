package com.azeroth.api.dto;

import com.azeroth.api.enums.Razas;
import com.azeroth.api.enums.Facciones;

import java.util.List;

public record RazaResponse(
        Long id,
        Razas nombre,
        Facciones faccion,
        List<String> clasesDisponibles
) {
}
