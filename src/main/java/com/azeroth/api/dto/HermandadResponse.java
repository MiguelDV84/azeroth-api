package com.azeroth.api.dto;

import com.azeroth.api.enums.Facciones;
import com.azeroth.api.enums.Reino;

import java.util.List;

public record HermandadResponse(
        Long idHermandad,
        String nombre,
        Reino reino,
        Facciones faccion,
        List<JugadorResponse> jugadores
) {
}
