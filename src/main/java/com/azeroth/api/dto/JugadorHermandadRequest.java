package com.azeroth.api.dto;

import jakarta.validation.constraints.NotNull;

public record JugadorHermandadRequest(
        @NotNull(message = "El ID de la hermandad no puede ser nulo")
        Long hermandadId
) {
}
