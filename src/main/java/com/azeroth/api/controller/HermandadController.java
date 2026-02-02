package com.azeroth.api.controller;


import com.azeroth.api.dto.HermandadRequest;
import com.azeroth.api.dto.HermandadResponse;
import com.azeroth.api.service.HermandadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/hermandades")
@RequiredArgsConstructor
public class HermandadController {

    private final HermandadService hermandadService;

    @PostMapping
    private ResponseEntity<HermandadResponse> crearHermandad(@RequestBody @Valid HermandadRequest request) {
        return hermandadService.guardar(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/list")
    public ResponseEntity<Page<HermandadResponse>> listarHermandades(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Page<HermandadResponse> hermandades = hermandadService.findAll(pageable);
        return ResponseEntity.ok().body(hermandades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HermandadResponse> obtenerHermandadPorId(@PathVariable Long id) {
        return hermandadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HermandadResponse> editarHermandad(@PathVariable Long id, @RequestBody @Valid HermandadRequest request) {
        return hermandadService.editar(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHermandad(@PathVariable Long id) {
        hermandadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cantidad-jugadores")
    public ResponseEntity<Long> obtenerCantidadJugadores(@PathVariable Long id) {
        Long cantidad = hermandadService.obtenerCantidadJugadores(id);
        return ResponseEntity.ok(cantidad);
    }
}
