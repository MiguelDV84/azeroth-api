package com.azeroth.api.entity;

import com.azeroth.api.enums.Razas;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "razas")
public class Raza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre", nullable = false, unique = true)
    private Razas nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faccion_id", nullable = false)
    private Faccion faccion;

    @OneToMany(mappedBy = "raza", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Jugador> jugador;

    @ManyToMany
    @JoinTable(
        name = "raza_clase",
        joinColumns = @JoinColumn(name = "raza_id"),
        inverseJoinColumns = @JoinColumn(name = "clase_id")
    )
    @Builder.Default
    private List<Clase> clasesDisponibles = new ArrayList<>();
}
