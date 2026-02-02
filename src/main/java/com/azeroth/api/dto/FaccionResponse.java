package com.azeroth.api.dto;

import com.azeroth.api.enums.Facciones;

public record FaccionResponse(
        Long id,
        Facciones nombre
) {
}
