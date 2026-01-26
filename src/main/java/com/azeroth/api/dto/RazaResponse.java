package com.azeroth.api.dto;

import com.azeroth.api.enums.Clases;
import com.azeroth.api.enums.Facciones;
import com.azeroth.api.enums.Razas;
import lombok.Builder;

import java.util.List;

@Builder
public record RazaResponse(
        Long id,
        Razas nombre,
        Facciones faccion,
        List<ClaseInfo> clasesDisponibles
) {
    @Builder
    public record ClaseInfo(
            Long id,
            Clases nombre
    ) {}
}

