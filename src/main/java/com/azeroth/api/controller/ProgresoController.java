package com.azeroth.api.controller;

import com.azeroth.api.dto.ProgresoResponse;
import com.azeroth.api.service.ProgresoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/progreso")
@RequiredArgsConstructor
public class ProgresoController {

    private final ProgresoService progresoService;

    @PutMapping("/actualizar/{jugadorId}/{logroId}")
    public ResponseEntity<ProgresoResponse> actualizarProgreso(@PathVariable @Valid Long jugadorId,@PathVariable @Valid Long logroId) {
        return progresoService.actualizarProgreso(jugadorId, logroId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
