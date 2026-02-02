package com.azeroth.api.controller;

import com.azeroth.api.dto.LogroResponse;
import com.azeroth.api.dto.ErrorResponse;
import com.azeroth.api.service.LogroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

@RestController
@RequestMapping("api/logros")
@RequiredArgsConstructor
@Tag(name = "Logros", description = "Operaciones sobre logros disponibles")
public class LogroController {

    private final LogroService logroService;

    @GetMapping("/list")
    @Operation(summary = "Listar logros", description = "Devuelve una página de logros")
    @ApiResponse(responseCode = "200", description = "Página de logros",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LogroResponse.class))))
    public ResponseEntity<Page<LogroResponse>> listarLogros(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Page<LogroResponse> logros = logroService.findAll(pageable);
        return ResponseEntity.ok().body(logros);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener logro por ID", description = "Devuelve un logro por su id")
    @ApiResponse(responseCode = "200", description = "Logro encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LogroResponse.class)))
    @ApiResponse(responseCode = "404", description = "Logro no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<LogroResponse> findById(@Parameter(description = "ID del logro", required = true, example = "1") @PathVariable Long id) {
        return logroService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
