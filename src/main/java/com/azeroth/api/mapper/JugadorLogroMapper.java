package com.azeroth.api.mapper;

import com.azeroth.api.dto.JugadorLogrosResponse;
import com.azeroth.api.entity.Jugador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProgresoMapper.class})
public interface JugadorLogroMapper {

    @Mapping(target = "experienciaParaProximoNivel", expression = "java(jugador.calcularXpRequerida(jugador.getNivel()))")
    @Mapping(target = "logros", source = "progresos")
    JugadorLogrosResponse jugadorToJugadorLogrosResponse(Jugador jugador);

    Jugador logroResponseToJugadorLogros(JugadorLogrosResponse jugadorLogrosResponse);
}

