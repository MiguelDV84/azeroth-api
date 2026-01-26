package com.azeroth.api.controller;

import com.azeroth.api.dto.JugadorRequest;
import com.azeroth.api.dto.JugadorResponse;
import com.azeroth.api.service.JugadorService;
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
    private ResponseEntity<JugadorResponse> crearJugador(@RequestBody JugadorRequest request) {
        return jugadorService.guardar(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/list")
    public ResponseEntity<Iterable<JugadorResponse>> listarJugadores() {
        Iterable<JugadorResponse> jugadores = jugadorService.findAll();
        return ResponseEntity.ok().body(jugadores);
    }
}
