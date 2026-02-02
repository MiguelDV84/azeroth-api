package com.azeroth.api.mapper;

import com.azeroth.api.dto.RazaRequest;
import com.azeroth.api.dto.RazaResponse;
import com.azeroth.api.entity.Clase;
import com.azeroth.api.entity.Raza;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RazaMapper {

    @Mapping(target = "faccion", source = "faccion.nombre")
    @Mapping(target = "clasesDisponibles", expression = "java(mapClasesDisponibles(raza.getClasesDisponibles()))")
    RazaResponse razaToRazaResponse(Raza raza);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "faccion", ignore = true)
    @Mapping(target = "jugador", ignore = true)
    @Mapping(target = "clasesDisponibles", ignore = true)
    Raza razaRequestToRaza(RazaRequest request);

    default List<String> mapClasesDisponibles(List<Clase> clases) {
        if (clases == null) {
            return List.of();
        }
        return clases.stream()
                .map(clase -> clase.getNombre().name())
                .collect(Collectors.toList());
    }
}
