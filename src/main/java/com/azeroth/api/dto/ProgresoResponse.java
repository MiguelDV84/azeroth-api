package com.azeroth.api.dto;

public record ProgresoResponse(
    Long idProgreso,
    Long idJugador,
    String nombreJugador,
    String estado,
    int valorActual,
    int valorObjetivo,
    String fechaCompletado,
    Long logroId
) {
}
