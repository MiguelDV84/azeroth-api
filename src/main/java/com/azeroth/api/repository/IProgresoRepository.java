package com.azeroth.api.repository;

import com.azeroth.api.entity.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProgresoRepository extends JpaRepository<Progreso, Long> {
    Optional<Progreso> findByJugadorIdAndLogroId(Long jugadorId, Long logroId);
}
