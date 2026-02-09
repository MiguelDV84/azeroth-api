package com.azeroth.api.mapper;

import com.azeroth.api.dto.HermandadRequest;
import com.azeroth.api.dto.HermandadResponse;
import com.azeroth.api.entity.Hermandad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {JugadorMapper.class})
public interface HermandadMapper {
    @Mapping(target = "idHermandad", source = "id")
    @Mapping(target = "nombre", expression = "java(hermandad.getNombre() != null ? hermandad.getNombre() : null)")
    @Mapping(target = "reino", expression = "java(hermandad.getReino() != null ? hermandad.getReino() : null)")
    @Mapping(target = "faccion", expression = "java(hermandad.getFaccion() != null ? hermandad.getFaccion().getNombre() : null)")
    HermandadResponse hermandadToHermandadResponse(Hermandad hermandad);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "faccion", ignore = true)
    @Mapping(target = "jugadores", ignore = true)
    Hermandad hermandadRequestToHermandad(HermandadRequest hermandadRequest);
}
