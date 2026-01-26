package com.azeroth.api.service;

import com.azeroth.api.dto.RazaResponse;
import com.azeroth.api.entity.Raza;
import com.azeroth.api.repository.IRazaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RazaService {

    private final IRazaRepository razaRepository;

    public List<RazaResponse> findAll() {
        return razaRepository.findAll().stream()
                .map(this::toRazaResponse)
                .toList();
    }

    public Optional<RazaResponse> findById(Long id) {
        return razaRepository.findById(id)
                .map(this::toRazaResponse);
    }

    private RazaResponse toRazaResponse(Raza raza) {
        return RazaResponse.builder()
                .id(raza.getId())
                .nombre(raza.getNombre())
                .faccion(raza.getFaccion() != null ? raza.getFaccion().getNombre() : null)
                .clasesDisponibles(
                    raza.getClasesDisponibles().stream()
                        .map(clase -> new RazaResponse.ClaseInfo(clase.getId(), clase.getNombre()))
                        .toList()
                )
                .build();
    }
}

