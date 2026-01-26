package com.azeroth.api.entity;

import com.azeroth.api.enums.Clases;
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
@Table(name = "clases")
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre", nullable = false, unique = true)
    private Clases nombre;

    @OneToMany(mappedBy = "clase", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Jugador> jugador;

    @ManyToMany(mappedBy = "clasesDisponibles")
    @Builder.Default
    private List<Raza> razas = new ArrayList<>();
}
