package com.azeroth.api.controller;

import com.azeroth.api.dto.RazaResponse;
import com.azeroth.api.service.RazaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/razas")
@RequiredArgsConstructor
public class RazaController {

    private final RazaService razaService;

    @GetMapping
    public ResponseEntity<List<RazaResponse>> getAllRazas() {
        List<RazaResponse> razas = razaService.findAll();
        return ResponseEntity.ok(razas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RazaResponse> getRazaById(@PathVariable Long id) {
        return razaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

