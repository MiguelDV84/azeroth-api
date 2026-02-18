package com.azeroth.api.exception;

import com.azeroth.api.dto.ErrorResponse;
import com.azeroth.api.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BussinesException.class)
    public ResponseEntity<ErrorResponse> handleBussinesException(BussinesException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                400,
                ex.getMessage(),
                LocalDateTime.now(),
                ex.getErrorCode(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                401,
                "Credenciales inválidas",
                LocalDateTime.now(),
                ErrorCode.NOMBRE_JUGADOR_YA_EN_USO, // Usamos un ErrorCode temporal
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                404,
                ex.getMessage(),
                LocalDateTime.now(),
                ErrorCode.NOMBRE_JUGADOR_YA_EN_USO, // Usamos un ErrorCode temporal
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
