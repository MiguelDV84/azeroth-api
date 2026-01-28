package com.azeroth.api.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "logros")
public class Logros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "puntos_de_logro", nullable = false)
    private BigDecimal puntosDeLogro;

    @Column(name = "valor_objetivo", nullable = false)
    private int valorObjetivo;
}
