package com.azeroth.api.repository;

import com.azeroth.api.dto.JugadorResponse;
import com.azeroth.api.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IJugadorRepository extends JpaRepository<Jugador, Long> {
    Optional<Jugador> findByNombre(String nombre);
}
