package com.azeroth.api.entity;


import com.azeroth.api.enums.Reino;
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
@Table(name = "hermandades")
public class Hermandad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "reino", nullable = false)
    private Reino reino;

    @OneToMany(mappedBy = "hermandad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Jugador> jugadores = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faccion_id", nullable = false)
    private Faccion faccion;
}
