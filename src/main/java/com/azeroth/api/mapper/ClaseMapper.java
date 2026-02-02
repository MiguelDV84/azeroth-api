package com.azeroth.api.mapper;

import com.azeroth.api.dto.ClaseResponse;
import com.azeroth.api.entity.Clase;
import com.azeroth.api.entity.Raza;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ClaseMapper {

    @Mapping(target = "razasDisponibles", expression = "java(mapRazasDisponibles(clase.getRazas()))")
    ClaseResponse claseToClaseResponse(Clase clase);

    default List<String> mapRazasDisponibles(List<Raza> razas) {
        if (razas == null) {
            return List.of();
        }
        return razas.stream()
                .map(raza -> raza.getNombre().name())
                .collect(Collectors.toList());
    }
}
