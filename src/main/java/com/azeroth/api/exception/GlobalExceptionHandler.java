package com.azeroth.api.exception;

import com.azeroth.api.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BussinesException.class)
    public ResponseEntity<ErrorResponse> handleBussinesException(BussinesException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                400,
                ex.getMessage(),
                LocalTime.now(),
                ex.getErrorCode(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
