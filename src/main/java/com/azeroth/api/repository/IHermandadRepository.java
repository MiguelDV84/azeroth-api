package com.azeroth.api.repository;

import com.azeroth.api.entity.Hermandad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IHermandadRepository extends JpaRepository<Hermandad, Long> {
    @Query("SELECT COUNT(j) FROM Hermandad h JOIN h.jugadores j WHERE h.id = :hermandadId")
    long countJugadoresByHermandadId(@Param("hermandadId") Long hermandadId);
}
