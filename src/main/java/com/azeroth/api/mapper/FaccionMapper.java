package com.azeroth.api.mapper;

import com.azeroth.api.dto.FaccionResponse;
import com.azeroth.api.entity.Faccion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FaccionMapper {
    FaccionResponse faccionToFaccionResponse(Faccion faccion);
}
