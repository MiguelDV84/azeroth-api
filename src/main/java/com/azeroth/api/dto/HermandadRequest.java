package com.azeroth.api.dto;

import com.azeroth.api.enums.Reino;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record HermandadRequest(
        @NotEmpty(message = "El nombre de la hermandad no puede estar vacío")
        String nombre,
        @NotNull(message = "El reino de la hermandad no puede estar nulo")
        Reino reino,
        @NotNull(message = "El ID de la faccion no puede ser nulo")
        Long faccionId
) {
}
