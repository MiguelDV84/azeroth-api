package com.azeroth.api.repository;

import com.azeroth.api.entity.Logros;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILogroRepository extends JpaRepository <Logros, Long> {
}
