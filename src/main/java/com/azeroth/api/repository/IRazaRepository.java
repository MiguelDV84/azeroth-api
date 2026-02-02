package com.azeroth.api.repository;

import com.azeroth.api.entity.Raza;
import com.azeroth.api.enums.Razas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IRazaRepository extends JpaRepository<Raza, Long> {

    Optional<Raza> findByNombre(Razas nombre);

    @Query("SELECT DISTINCT r FROM Raza r LEFT JOIN FETCH r.clasesDisponibles")
    List<Raza> findAllWithClases();
}

