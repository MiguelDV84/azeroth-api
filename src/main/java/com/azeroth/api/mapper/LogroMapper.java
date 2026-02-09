package com.azeroth.api.mapper;

import com.azeroth.api.dto.LogroRequest;
import com.azeroth.api.dto.LogroResponse;
import com.azeroth.api.entity.Logros;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LogroMapper {
    LogroResponse logroToLogroResponse(Logros logros);

    @Mapping(target = "id", ignore = true)
    Logros logroResponseToLogros(LogroResponse logroResponse);

    @Mapping(target = "id", ignore = true)
    Logros logroRequestToLogros(LogroRequest logroRequest);

    @Mapping(target = "id", ignore = true)
    void updateLogroFromRequest(LogroRequest logroRequest, @MappingTarget Logros logros);
}
