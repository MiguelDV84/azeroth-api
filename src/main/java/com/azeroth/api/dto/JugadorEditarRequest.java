package com.azeroth.api.dto;

import jakarta.validation.constraints.NotEmpty;

public record JugadorEditarRequest(
        @NotEmpty(message = "El nombre del jugador no puede estar vacío")
        String nombre
) { }
