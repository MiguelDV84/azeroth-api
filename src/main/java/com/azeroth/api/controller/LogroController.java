package com.azeroth.api.controller;

import com.azeroth.api.dto.LogroRequest;
import com.azeroth.api.dto.LogroResponse;
import com.azeroth.api.dto.ErrorResponse;
import com.azeroth.api.service.LogroService;
import jakarta.validation.Valid;
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
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<LogroResponse> logros = logroService.findAll(pageable);
        return ResponseEntity.ok(logros);
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

    @PostMapping
    @Operation(summary = "Crear logro", description = "Crea un nuevo logro")
    @ApiResponse(responseCode = "201", description = "Logro creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LogroResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<LogroResponse> create(@Valid @RequestBody LogroRequest logroRequest) {
        LogroResponse logroCreado = logroService.create(logroRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(logroCreado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar logro", description = "Actualiza un logro existente")
    @ApiResponse(responseCode = "200", description = "Logro actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LogroResponse.class)))
    @ApiResponse(responseCode = "404", description = "Logro no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<LogroResponse> update(
            @Parameter(description = "ID del logro", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody LogroRequest logroRequest) {
        LogroResponse logroActualizado = logroService.update(id, logroRequest);
        return ResponseEntity.ok(logroActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar logro", description = "Elimina un logro existente")
    @ApiResponse(responseCode = "204", description = "Logro eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Logro no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> delete(@Parameter(description = "ID del logro", required = true, example = "1") @PathVariable Long id) {
        logroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
