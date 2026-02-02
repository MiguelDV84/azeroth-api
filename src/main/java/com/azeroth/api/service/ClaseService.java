package com.azeroth.api.service;

import com.azeroth.api.dto.ClaseResponse;
import com.azeroth.api.mapper.ClaseMapper;
import com.azeroth.api.repository.IClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClaseService {

    private final ClaseMapper claseMapper;
    private final IClaseRepository claseRepository;

    @Transactional(readOnly = true)
    public Optional<ClaseResponse> obtenerPorId(Long id) {
        return claseRepository.findById(id)
                .map(claseMapper::claseToClaseResponse);
    }

    @Transactional(readOnly = true)
    public Page<ClaseResponse> findAll(Pageable pageable) {
        return claseRepository.findAll(pageable)
                .map(claseMapper::claseToClaseResponse);
    }
}
