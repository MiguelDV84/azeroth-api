package com.azeroth.api.repository;

import com.azeroth.api.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJugadorRepository extends JpaRepository<Jugador, Long> {
}
