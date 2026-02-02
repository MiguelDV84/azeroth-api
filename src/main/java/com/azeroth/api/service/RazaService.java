package com.azeroth.api.service;

import com.azeroth.api.dto.RazaRequest;
import com.azeroth.api.dto.RazaResponse;
import com.azeroth.api.entity.Clase;
import com.azeroth.api.entity.Faccion;
import com.azeroth.api.entity.Raza;
import com.azeroth.api.enums.ErrorCode;
import com.azeroth.api.exception.BussinesException;
import com.azeroth.api.mapper.RazaMapper;
import com.azeroth.api.repository.IClaseRepository;
import com.azeroth.api.repository.IFaccionRepository;
import com.azeroth.api.repository.IRazaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RazaService {

    private final RazaMapper razaMapper;
    private final IRazaRepository razaRepository;
    private final IFaccionRepository faccionRepository;
    private final IClaseRepository claseRepository;

    @Transactional
    public Optional<RazaResponse> guardar(RazaRequest request) {
        Raza raza = razaMapper.razaRequestToRaza(request);

        Faccion faccion = faccionRepository.findById(request.faccionId())
                .orElseThrow(() -> new RuntimeException("Facción no encontrada con id: " + request.faccionId()));

        List<Clase> clasesDisponibles = new ArrayList<>();
        for (Long claseId : request.clasesDisponiblesIds()) {
            Clase clase = claseRepository.findById(claseId)
                    .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + claseId));
            clasesDisponibles.add(clase);
        }

        raza.setFaccion(faccion);
        raza.setClasesDisponibles(clasesDisponibles);

        Raza razaGuardada = razaRepository.save(raza);
        return Optional.of(razaMapper.razaToRazaResponse(razaGuardada));
    }

    @Transactional(readOnly = true)
    public Optional<RazaResponse> obtenerPorId(Long id) {
        return razaRepository.findById(id)
                .map(razaMapper::razaToRazaResponse);
    }

    @Transactional(readOnly = true)
    public Page<RazaResponse> findAll(Pageable pageable) {
        return razaRepository.findAll(pageable)
                .map(razaMapper::razaToRazaResponse);
    }

    @Transactional
    public Optional<RazaResponse> editar(Long id, RazaRequest request) {
        Raza raza = razaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada con id: " + id));

        raza.setNombre(request.nombre());

        Faccion faccion = faccionRepository.findById(request.faccionId())
                .orElseThrow(() -> new RuntimeException("Facción no encontrada con id: " + request.faccionId()));

        List<Clase> clasesDisponibles = new ArrayList<>();
        for (Long claseId : request.clasesDisponiblesIds()) {
            Clase clase = claseRepository.findById(claseId)
                    .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + claseId));
            clasesDisponibles.add(clase);
        }

        raza.setFaccion(faccion);
        raza.setClasesDisponibles(clasesDisponibles);

        Raza razaEditada = razaRepository.save(raza);
        return Optional.of(razaMapper.razaToRazaResponse(razaEditada));
    }

    @Transactional
    public void eliminar(Long id) {
        razaRepository.deleteById(id);
    }
}
