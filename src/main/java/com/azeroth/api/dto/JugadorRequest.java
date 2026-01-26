package com.azeroth.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record JugadorRequest(
        @NotEmpty(message = "El nombre del jugador no puede estar vacío")
        String nombre,
        @NotNull(message = "El ID de la clase no puede ser nulo")
        Long claseId,
        @NotNull(message = "El ID de la raza no puede ser nulo")
        Long razaId,
        @NotNull(message = "El ID de la faccion no puede ser nulo")
        Long faccionId
) {
}
