package com.azeroth.api.service;


import com.azeroth.api.dto.LogroRequest;
import com.azeroth.api.dto.LogroResponse;
import com.azeroth.api.entity.Logros;
import com.azeroth.api.mapper.LogroMapper;
import com.azeroth.api.repository.ILogroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogroService {

    private final ILogroRepository logroRepository;
    private final LogroMapper logroMapper;

    @Transactional(readOnly = true)
    public Page<LogroResponse> findAll(Pageable pageable) {
        return logroRepository.findAll(pageable)
                .map(logroMapper::logroToLogroResponse);
    }

    @Transactional(readOnly = true)
    public Optional<LogroResponse> findById(Long id) {
        return logroRepository.findById(id)
                .map(logroMapper::logroToLogroResponse);
    }

    @Transactional
    public LogroResponse create(LogroRequest logroRequest) {
        Logros logro = logroMapper.logroRequestToLogros(logroRequest);
        Logros logroGuardado = logroRepository.save(logro);
        return logroMapper.logroToLogroResponse(logroGuardado);
    }

    @Transactional
    public LogroResponse update(Long id, LogroRequest logroRequest) {
        Logros logro = logroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Logro no encontrado con id: " + id));

        logroMapper.updateLogroFromRequest(logroRequest, logro);
        Logros logroActualizado = logroRepository.save(logro);
        return logroMapper.logroToLogroResponse(logroActualizado);
    }

    @Transactional
    public void delete(Long id) {
        if (!logroRepository.existsById(id)) {
            throw new RuntimeException("Logro no encontrado con id: " + id);
        }
        logroRepository.deleteById(id);
    }
}
