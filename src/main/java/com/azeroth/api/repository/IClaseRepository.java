package com.azeroth.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.azeroth.api.entity.Clase;
import com.azeroth.api.enums.Clases;

import java.util.Optional;

public interface IClaseRepository extends JpaRepository<Clase, Long> {
    Optional<Clase> findByNombre(Clases nombre);
}




