package com.azeroth.api.controller;

import com.azeroth.api.dto.ClaseResponse;
import com.azeroth.api.dto.ErrorResponse;
import com.azeroth.api.service.ClaseService;
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
@RequestMapping("api/clases")
@RequiredArgsConstructor
@Tag(name = "Clases", description = "Operaciones sobre clases disponibles")
public class ClaseController {

    private final ClaseService claseService;

    @GetMapping("/list")
    @Operation(summary = "Listar clases", description = "Devuelve una página de clases con sus razas disponibles")
    @ApiResponse(responseCode = "200", description = "Página de clases",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClaseResponse.class))))
    public ResponseEntity<Page<ClaseResponse>> listarClases(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<ClaseResponse> clases = claseService.findAll(pageable);
        return ResponseEntity.ok().body(clases);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener clase por ID", description = "Devuelve una clase por su id")
    @ApiResponse(responseCode = "200", description = "Clase encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClaseResponse.class)))
    @ApiResponse(responseCode = "404", description = "Clase no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ClaseResponse> obtenerClasePorId(@Parameter(description = "ID de la clase", required = true, example = "1") @PathVariable Long id) {
        return claseService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

