package com.azeroth.api.controller;

import com.azeroth.api.dto.RazaRequest;
import com.azeroth.api.dto.RazaResponse;
import com.azeroth.api.dto.ErrorResponse;
import com.azeroth.api.service.RazaService;
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
@RequestMapping("api/razas")
@RequiredArgsConstructor
@Tag(name = "Razas", description = "Operaciones CRUD sobre razas disponibles")
public class RazaController {

    private final RazaService razaService;

    @PostMapping
    @Operation(summary = "Crear raza", description = "Crea una nueva raza")
    @ApiResponse(responseCode = "200", description = "Raza creada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RazaResponse.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<RazaResponse> crearRaza(@RequestBody @Valid RazaRequest request) {
        return razaService.guardar(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/list")
    @Operation(summary = "Listar razas", description = "Devuelve una página de razas con sus clases disponibles")
    @ApiResponse(responseCode = "200", description = "Página de razas",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RazaResponse.class))))
    public ResponseEntity<Page<RazaResponse>> listarRazas(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<RazaResponse> razas = razaService.findAll(pageable);
        return ResponseEntity.ok().body(razas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener raza por ID", description = "Devuelve una raza por su id")
    @ApiResponse(responseCode = "200", description = "Raza encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RazaResponse.class)))
    @ApiResponse(responseCode = "404", description = "Raza no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<RazaResponse> obtenerRazaPorId(@Parameter(description = "ID de la raza", required = true, example = "1") @PathVariable Long id) {
        return razaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar raza", description = "Edita los datos de una raza")
    @ApiResponse(responseCode = "200", description = "Raza editada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RazaResponse.class)))
    @ApiResponse(responseCode = "404", description = "Raza no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<RazaResponse> editarRaza(@Parameter(description = "ID de la raza", required = true, example = "1") @PathVariable Long id, @RequestBody @Valid RazaRequest request) {
        return razaService.editar(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar raza", description = "Elimina una raza")
    @ApiResponse(responseCode = "204", description = "Raza eliminada")
    public ResponseEntity<Void> eliminarRaza(@Parameter(description = "ID de la raza", required = true, example = "1") @PathVariable Long id) {
        razaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
