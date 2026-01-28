package com.azeroth.api.mapper;

import com.azeroth.api.dto.JugadorRequest;
import com.azeroth.api.dto.JugadorResponse;
import com.azeroth.api.entity.Jugador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JugadorMapper {

    @Mapping(target = "clase", expression = "java(jugador.getClase() != null ? jugador.getClase().getNombre() : null)")
    @Mapping(target = "raza", expression = "java(jugador.getRaza() != null ? jugador.getRaza().getNombre() : null)")
    @Mapping(target = "faccion", expression = "java(jugador.getFaccion() != null ? jugador.getFaccion().getNombre() : null)")
    @Mapping(target = "hermandad", expression = "java(jugador.getHermandad() != null ? jugador.getHermandad().getNombre() : null)")
    @Mapping(target = "experienciaParaProximoNivel", expression = "java(jugador.calcularXpRequerida(jugador.getNivel()))")
    JugadorResponse jugadorToJugadorResponse(Jugador jugador);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nivel", ignore = true)
    @Mapping(target = "experiencia", ignore = true)
    @Mapping(target = "hermandad", ignore = true)
    @Mapping(target = "faccion", ignore = true)
    @Mapping(target = "clase", ignore = true)
    @Mapping(target = "raza", ignore = true)
    Jugador jugadorRequestToJugador(JugadorRequest jugadorRequest);
}
