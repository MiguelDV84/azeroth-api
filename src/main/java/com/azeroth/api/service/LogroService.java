package com.azeroth.api.service;


import com.azeroth.api.dto.LogroResponse;
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

    public Optional<LogroResponse> findById(Long id) {
        return logroRepository.findById(id)
                .map(logroMapper::logroToLogroResponse);
    }
}
