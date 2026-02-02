package com.azeroth.api.dto;

import com.azeroth.api.enums.Clases;

import java.util.List;

public record ClaseResponse(
        Long id,
        Clases nombre,
        List<String> razasDisponibles
) {
}
