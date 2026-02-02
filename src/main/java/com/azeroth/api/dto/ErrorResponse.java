package com.azeroth.api.dto;

import com.azeroth.api.enums.ErrorCode;


import java.time.LocalTime;

public record ErrorResponse(
        int status,
        String message,
        LocalTime timestamp,
        ErrorCode errorCode,
        String path
) {
}
