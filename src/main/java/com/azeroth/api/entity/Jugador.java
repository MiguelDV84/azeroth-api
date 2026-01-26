package com.azeroth.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Builder.Default
    @Column(name = "nivel", nullable = false)
    @Min(1)
    private int nivel = 1;

    @Builder.Default
    @Column(name = "experiencia", nullable = false)
    private BigDecimal experiencia = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "hermandad_id")
    private Hermandad hermandad;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "faccion_id", nullable = false)
    private Faccion faccion;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "clase_id", nullable = false)
    private Clase clase;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "raza_id", nullable = false)
    private Raza raza;
}
