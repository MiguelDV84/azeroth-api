package com.azeroth.api.controller;

import com.azeroth.api.dto.LogroResponse;
import com.azeroth.api.service.LogroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/logros")
@RequiredArgsConstructor
public class LogroController {

    private final LogroService logroService;

    @GetMapping("/list")
    public ResponseEntity<Page<LogroResponse>> listarLogros(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Page<LogroResponse> logros = logroService.findAll(pageable);
        return ResponseEntity.ok().body(logros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogroResponse> findById(@PathVariable Long id) {
        return logroService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
