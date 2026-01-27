package com.azeroth.api.controller;

import com.azeroth.api.dto.JugadorEditarRequest;
import com.azeroth.api.dto.JugadorHermandadRequest;
import com.azeroth.api.dto.JugadorRequest;
import com.azeroth.api.dto.JugadorResponse;
import com.azeroth.api.service.JugadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Iterable<JugadorResponse>> listarJugadores() {
        Iterable<JugadorResponse> jugadores = jugadorService.findAll();
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

}
