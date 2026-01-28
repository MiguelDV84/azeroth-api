package com.azeroth.api.service;


import com.azeroth.api.dto.HermandadRequest;
import com.azeroth.api.dto.HermandadResponse;
import com.azeroth.api.entity.Faccion;
import com.azeroth.api.entity.Hermandad;
import com.azeroth.api.mapper.HermandadMapper;
import com.azeroth.api.repository.IFaccionRepository;
import com.azeroth.api.repository.IHermandadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<HermandadResponse> findAll(Pageable pageable) {
        return hermandadRepository.findAll(pageable)
                .map(hermandadMapper::hermandadToHermandadResponse);
    }

    public Optional<HermandadResponse> editar(Long id, HermandadRequest request) {
        Hermandad hermandad = hermandadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hermandad no encontrada con id: " + id));
        hermandad.setNombre(request.nombre());

        Hermandad hermandadEditada = hermandadRepository.save(hermandad);
        return Optional.of(hermandadMapper.hermandadToHermandadResponse(hermandadEditada));
    }

    public void eliminar(Long id) {
        hermandadRepository.deleteById(id);
    }

}
