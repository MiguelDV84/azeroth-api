package com.azeroth.api.dto;

import com.azeroth.api.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String message,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        ErrorCode errorCode,
        String path
) {
}
