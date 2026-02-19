package com.azeroth.api.repository;

import com.azeroth.api.entity.Jugador;
import com.azeroth.api.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IJugadorRepository extends JpaRepository<Jugador, Long> {
    Optional<Jugador> findByNombre(String nombre);
    Page<Jugador> findByUsuario(Usuario usuario, Pageable pageable);
    Optional<Jugador> findByIdAndUsuario(Long id, Usuario usuario);
}
