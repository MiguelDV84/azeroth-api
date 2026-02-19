package com.azeroth.api.entity;


import com.azeroth.api.enums.EstadoLogro;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "progreso",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_jugador_logro",
                columnNames = {"jugador_id", "logro_id"}
        ))
public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_progreso")
    private Long idProgreso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoLogro estado = EstadoLogro.NO_INICIADO;

    @Column(name = "valor_actual", nullable = false)
    private int valorActual;

    @Column(name = "valor_objetivo", nullable = false)
    private int valorObjetivo;

    @Column(name = "fecha_completado")
    private LocalDate fechaCompletado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logro_id", nullable = false)
    private Logros logro;
}
