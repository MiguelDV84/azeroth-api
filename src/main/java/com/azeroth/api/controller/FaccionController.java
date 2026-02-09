package com.azeroth.api.controller;

import com.azeroth.api.dto.FaccionResponse;
import com.azeroth.api.dto.ErrorResponse;
import com.azeroth.api.service.FaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

@RestController
@RequestMapping("api/facciones")
@RequiredArgsConstructor
@Tag(name = "Facciones", description = "Operaciones sobre facciones disponibles")

public class FaccionController {

    private final FaccionService faccionService;

    @GetMapping("/list")
    @Operation(summary = "Listar facciones", description = "Devuelve una página de facciones")
    @ApiResponse(responseCode = "200", description = "Página de facciones",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FaccionResponse.class))))
    public ResponseEntity<Page<FaccionResponse>> listarFacciones(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<FaccionResponse> facciones = faccionService.findAll(pageable);
        return ResponseEntity.ok().body(facciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener facción por ID", description = "Devuelve una facción por su id")
    @ApiResponse(responseCode = "200", description = "Facción encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FaccionResponse.class)))
    @ApiResponse(responseCode = "404", description = "Facción no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<FaccionResponse> obtenerFaccionPorId(@Parameter(description = "ID de la facción", required = true, example = "1") @PathVariable Long id) {
        return faccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
