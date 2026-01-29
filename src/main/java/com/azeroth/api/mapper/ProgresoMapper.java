package com.azeroth.api.mapper;

import com.azeroth.api.dto.LogroResponse;
import com.azeroth.api.dto.ProgresoResponse;
import com.azeroth.api.entity.Progreso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgresoMapper {

    @Mapping(target = "id", source = "logro.id")
    @Mapping(target = "titulo", source = "logro.titulo")
    @Mapping(target = "descripcion", source = "logro.descripcion")
    @Mapping(target = "puntosDeLogro", source = "logro.puntosDeLogro")
    @Mapping(target = "valorObjetivo", source = "logro.valorObjetivo")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "valorActual", source = "valorActual")
    @Mapping(target = "fechaCompletado", source = "fechaCompletado")
    LogroResponse progresoToLogroResponse(Progreso progreso);

    @Mapping(target = "idJugador", expression = "java(progreso.getJugador() != null ? progreso.getJugador().getId() : null)")
    @Mapping(target = "nombreJugador", expression = "java(progreso.getJugador() != null ? progreso.getJugador().getNombre() : null)")
    ProgresoResponse progresoToProgresoResponse(Progreso progreso);

    Progreso progresoResponseToProgreso(ProgresoResponse progresoResponse);
}
