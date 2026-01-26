package com.azeroth.api.repository;

import com.azeroth.api.entity.Faccion;
import com.azeroth.api.enums.Facciones;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IFaccionRepository extends JpaRepository<Faccion, Long> {
    Optional<Faccion> findByNombre(Facciones nombre);
}

