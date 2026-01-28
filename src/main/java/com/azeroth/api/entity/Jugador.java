package com.azeroth.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "jugadores")
public class Jugador {

    private static final int XP_BASE = 500;
    private static final double EXPONENTE = 1.5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Builder.Default
    @Column(name = "nivel", nullable = false)
    @Min(1)
    @Max(70)
    private int nivel = 1;

    @Builder.Default
    @Column(name = "experiencia", nullable = false)
    private BigDecimal experiencia = BigDecimal.ZERO.setScale(0, RoundingMode.DOWN);

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

    public BigDecimal calcularXpRequerida(int nivelActual) {
        // Formula:  500 * (nivelActual ^ 1.5)
        double xpRequerida = XP_BASE * Math.pow(nivelActual, EXPONENTE);
        return BigDecimal.valueOf(xpRequerida).setScale(0, RoundingMode.DOWN);
    }

    public void subirNivel(Jugador jugador) {
        if(jugador.getNivel() >= 70) {
            jugador.setExperiencia(BigDecimal.valueOf(0));
            return;
        }

        BigDecimal experienciaActual = jugador.getExperiencia()
                .subtract(jugador.calcularXpRequerida(jugador.getNivel()));
        jugador.setExperiencia(experienciaActual.setScale(0, RoundingMode.DOWN));

        jugador.setNivel(jugador.getNivel() + 1);
    }
}
