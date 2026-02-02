package com.azeroth.api.service;

import com.azeroth.api.dto.FaccionResponse;
import com.azeroth.api.mapper.FaccionMapper;
import com.azeroth.api.repository.IFaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FaccionService {

    private final FaccionMapper faccionMapper;
    private final IFaccionRepository faccionRepository;

    @Transactional(readOnly = true)
    public Optional<FaccionResponse> obtenerPorId(Long id) {
        return faccionRepository.findById(id)
                .map(faccionMapper::faccionToFaccionResponse);
    }

    @Transactional(readOnly = true)
    public Page<FaccionResponse> findAll(Pageable pageable) {
        return faccionRepository.findAll(pageable)
                .map(faccionMapper::faccionToFaccionResponse);
    }
}
