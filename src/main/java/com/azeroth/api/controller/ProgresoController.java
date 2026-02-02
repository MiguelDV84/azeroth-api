package com.azeroth.api.controller;

import com.azeroth.api.dto.ProgresoResponse;
import com.azeroth.api.dto.ErrorResponse;
import com.azeroth.api.service.ProgresoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("api/progreso")
@RequiredArgsConstructor
@Tag(name = "Progresos", description = "Operaciones sobre el progreso de logros")
public class ProgresoController {

    private final ProgresoService progresoService;

    @PutMapping("/actualizar/{jugadorId}/{logroId}")
    @Operation(summary = "Actualizar progreso", description = "Actualiza el progreso de un logro para un jugador")
    @ApiResponse(responseCode = "200", description = "Progreso actualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProgresoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Jugador o logro no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ProgresoResponse> actualizarProgreso(@Parameter(description = "ID del jugador", required = true, example = "1") @PathVariable @Valid Long jugadorId,@Parameter(description = "ID del logro", required = true, example = "1") @PathVariable @Valid Long logroId) {
        return progresoService.actualizarProgreso(jugadorId, logroId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
