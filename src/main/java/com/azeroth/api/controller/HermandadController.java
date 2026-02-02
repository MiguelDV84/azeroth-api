package com.azeroth.api.controller;


import com.azeroth.api.dto.HermandadRequest;
import com.azeroth.api.dto.HermandadResponse;
import com.azeroth.api.dto.ErrorResponse;
import com.azeroth.api.service.HermandadService;
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
@RequestMapping("api/hermandades")
@RequiredArgsConstructor
@Tag(name = "Hermandades", description = "Operaciones CRUD sobre hermandades")
public class HermandadController {

    private final HermandadService hermandadService;

    @PostMapping
    @Operation(summary = "Crear hermandad", description = "Crea una nueva hermandad")
    @ApiResponse(responseCode = "200", description = "Hermandad creada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HermandadResponse.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    private ResponseEntity<HermandadResponse> crearHermandad(@RequestBody @Valid HermandadRequest request) {
        return hermandadService.guardar(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/list")
    @Operation(summary = "Listar hermandades", description = "Devuelve una página de hermandades")
    @ApiResponse(responseCode = "200", description = "Página de hermandades",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HermandadResponse.class))))
    public ResponseEntity<Page<HermandadResponse>> listarHermandades(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Page<HermandadResponse> hermandades = hermandadService.findAll(pageable);
        return ResponseEntity.ok().body(hermandades);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener hermandad por ID", description = "Devuelve una hermandad por su id")
    @ApiResponse(responseCode = "200", description = "Hermandad encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HermandadResponse.class)))
    @ApiResponse(responseCode = "404", description = "Hermandad no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<HermandadResponse> obtenerHermandadPorId(@Parameter(description = "ID de la hermandad", required = true, example = "1") @PathVariable Long id) {
        return hermandadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar hermandad", description = "Edita los datos de una hermandad")
    @ApiResponse(responseCode = "200", description = "Hermandad editada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HermandadResponse.class)))
    @ApiResponse(responseCode = "404", description = "Hermandad no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<HermandadResponse> editarHermandad(@Parameter(description = "ID de la hermandad", required = true, example = "1") @PathVariable Long id, @RequestBody @Valid HermandadRequest request) {
        return hermandadService.editar(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar hermandad", description = "Elimina una hermandad")
    @ApiResponse(responseCode = "204", description = "Hermandad eliminada")
    public ResponseEntity<Void> eliminarHermandad(@Parameter(description = "ID de la hermandad", required = true, example = "1") @PathVariable Long id) {
        hermandadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cantidad-jugadores")
    @Operation(summary = "Obtener cantidad de jugadores", description = "Devuelve la cantidad de jugadores de una hermandad por su id")
    @ApiResponse(responseCode = "200", description = "Cantidad de jugadores",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    public ResponseEntity<Long> obtenerCantidadJugadores(@Parameter(description = "ID de la hermandad", required = true, example = "1") @PathVariable Long id) {
        Long cantidad = hermandadService.obtenerCantidadJugadores(id);
        return ResponseEntity.ok(cantidad);
    }
}
