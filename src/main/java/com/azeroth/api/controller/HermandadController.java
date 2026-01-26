package com.azeroth.api.controller;


import com.azeroth.api.dto.HermandadRequest;
import com.azeroth.api.dto.HermandadResponse;
import com.azeroth.api.service.HermandadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Iterable<HermandadResponse>> listarHermandades() {
        Iterable<HermandadResponse> hermandades = hermandadService.obtenerTodas();
        return ResponseEntity.ok().body(hermandades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HermandadResponse> obtenerHermandadPorId(@PathVariable Long id) {
        return hermandadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
