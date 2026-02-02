package com.azeroth.api.dto;

import com.azeroth.api.enums.Razas;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RazaRequest(
        @NotNull(message = "El nombre de la raza no puede ser nulo")
        Razas nombre,
        @NotNull(message = "El ID de la facción no puede ser nulo")
        Long faccionId,
        @NotNull(message = "Las clases disponibles no pueden ser nulas")
        List<Long> clasesDisponiblesIds
) {
}
