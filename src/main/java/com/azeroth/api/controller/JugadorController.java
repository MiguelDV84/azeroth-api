package com.azeroth.api.controller;

import com.azeroth.api.dto.*;
import com.azeroth.api.service.JugadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

@RestController
@RequestMapping("api/jugadores")
@RequiredArgsConstructor
@Tag(name = "Jugadores", description = "Operaciones CRUD y acciones sobre jugadores")
public class JugadorController {

    private final JugadorService jugadorService;

    @PostMapping
    @Operation(summary = "Crear jugador", description = "Crea un nuevo jugador")
    @ApiResponse(responseCode = "200", description = "Jugador creado con éxito",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JugadorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    private ResponseEntity<JugadorResponse> crearJugador(@RequestBody @Valid JugadorRequest request) {
        return jugadorService.guardar(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/list")
    @Operation(summary = "Listar jugadores", description = "Devuelve una página de jugadores")
    @ApiResponse(responseCode = "200", description = "Página de jugadores",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = JugadorResponse.class))))
    public ResponseEntity<Page<JugadorResponse>> listarJugadores(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<JugadorResponse> jugadores = jugadorService.findAll(pageable);
        return ResponseEntity.ok().body(jugadores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener jugador por ID", description = "Devuelve un jugador por su id")
    @ApiResponse(responseCode = "200", description = "Jugador encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JugadorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Jugador no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<JugadorResponse> obtenerJugadorPorId(@Parameter(description = "ID del jugador", required = true, example = "1") @PathVariable Long id) {
        return jugadorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar jugador", description = "Actualiza los datos de un jugador")
    @ApiResponse(responseCode = "200", description = "Jugador actualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JugadorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Jugador no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<JugadorResponse> actualizarJugador(@Parameter(description = "ID del jugador", required = true, example = "1") @PathVariable Long id, @RequestBody @Valid JugadorEditarRequest request) {
        return jugadorService.editar(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/hermandad/{id}")
    @Operation(summary = "Asignar hermandad", description = "Asignar una hermandad a un jugador")
    @ApiResponse(responseCode = "200", description = "Hermandad asignada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JugadorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Jugador o hermandad no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<JugadorResponse> asignarHermandad(@Parameter(description = "ID del jugador", required = true, example = "1") @PathVariable Long id, @RequestBody @Valid JugadorHermandadRequest request) {
        return jugadorService.asignarHermandad(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/remover-hermandad/{id}")
    @Operation(summary = "Remover hermandad", description = "Remover la hermandad de un jugador")
    @ApiResponse(responseCode = "200", description = "Hermandad removida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JugadorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Jugador no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<JugadorResponse> removerHermandad(@Parameter(description = "ID del jugador", required = true, example = "1") @PathVariable Long id) {
        return jugadorService.eliminarHermandad(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar jugador", description = "Elimina un jugador")
    @ApiResponse(responseCode = "204", description = "Jugador eliminado")
    public ResponseEntity<Void> eliminarJugador(@Parameter(description = "ID del jugador", required = true, example = "1") @PathVariable Long id) {
        jugadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/experiencia/{id}")
    @Operation(summary = "Ganar experiencia", description = "Añade experiencia a un jugador y puede provocar subida de nivel")
    @ApiResponse(responseCode = "200", description = "Experiencia añadida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JugadorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Jugador no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<JugadorResponse> nivelUp(@Parameter(description = "ID del jugador", required = true, example = "1") @PathVariable Long id, @Parameter(description = "Cantidad de experiencia a añadir", required = true, schema = @Schema(type = "number", format = "decimal", example = "100.5")) @RequestParam BigDecimal experiencia) {
        return jugadorService.ganarExperiencia(id, experiencia)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/inicializar-logros/{id}")
    @Operation(summary = "Inicializar logros", description = "Inicializa el progreso de logros para un jugador")
    @ApiResponse(responseCode = "200", description = "Progresos inicializados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JugadorLogrosResponse.class)))
    @ApiResponse(responseCode = "404", description = "Jugador no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<JugadorLogrosResponse> inicializarLogros(@Parameter(description = "ID del jugador", required = true, example = "1") @PathVariable Long id) {
        return jugadorService.inicializarProgresoParaJugador(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
