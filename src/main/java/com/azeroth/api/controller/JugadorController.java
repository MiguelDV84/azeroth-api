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

@RestController
@RequestMapping("api/jugadores")
@RequiredArgsConstructor
public class JugadorController {

    private final JugadorService jugadorService;

    @PostMapping
    private ResponseEntity<JugadorResponse> crearJugador(@RequestBody @Valid JugadorRequest request) {
        return jugadorService.guardar(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/list")
    public ResponseEntity<Page<JugadorResponse>> listarJugadores(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Page<JugadorResponse> jugadores = jugadorService.findAll(pageable);
        return ResponseEntity.ok().body(jugadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugadorResponse> obtenerJugadorPorId(@PathVariable Long id) {
        return jugadorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<JugadorResponse> actualizarJugador(@PathVariable Long id, @RequestBody @Valid JugadorEditarRequest request) {
        return jugadorService.editar(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/hermandad/{id}")
    public ResponseEntity<JugadorResponse> asignarHermandad(@PathVariable Long id, @RequestBody @Valid JugadorHermandadRequest request) {
        return jugadorService.asignarHermandad(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/remover-hermandad/{id}")
    public ResponseEntity<JugadorResponse> removerHermandad(@PathVariable Long id) {
        return jugadorService.eliminarHermandad(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarJugador(@PathVariable Long id) {
        jugadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/experiencia/{id}")
    public ResponseEntity<JugadorResponse> nivelUp(@PathVariable Long id, @RequestParam BigDecimal experiencia) {
        return jugadorService.ganarExperiencia(id, experiencia)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/inicializar-logros/{id}")
    public ResponseEntity<JugadorLogrosResponse> inicializarLogros(@PathVariable Long id) {
        return jugadorService.inicializarProgresoParaJugador(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
