package com.azeroth.api.service;


import com.azeroth.api.dto.HermandadRequest;
import com.azeroth.api.dto.HermandadResponse;
import com.azeroth.api.entity.Faccion;
import com.azeroth.api.entity.Hermandad;
import com.azeroth.api.mapper.HermandadMapper;
import com.azeroth.api.repository.IFaccionRepository;
import com.azeroth.api.repository.IHermandadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HermandadService {

    private final HermandadMapper hermandadMapper;
    private final IHermandadRepository hermandadRepository;
    private final IFaccionRepository faccionRepository;

    public Optional<HermandadResponse> guardar(HermandadRequest request) {
        Hermandad hermandad = hermandadMapper.hermandadRequestToHermandad(request);
        Faccion faccion = faccionRepository.findById(request.faccionId())
                .orElseThrow(() -> new RuntimeException("Facción no encontrada con id: " + request.faccionId()));
        hermandad.setFaccion(faccion);
        Hermandad hermandadGuardada = hermandadRepository.save(hermandad);
        return Optional.of(hermandadMapper.hermandadToHermandadResponse(hermandadGuardada));
    }

    public Optional<HermandadResponse> obtenerPorId(Long id) {
        return hermandadRepository.findById(id)
                .map(hermandadMapper::hermandadToHermandadResponse);
    }

    public List<HermandadResponse> obtenerTodas() {
        return hermandadRepository.findAll()
                .stream()
                .map(hermandadMapper::hermandadToHermandadResponse)
                .toList();
    }
}
