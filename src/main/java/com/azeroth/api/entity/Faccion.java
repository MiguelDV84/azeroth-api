package com.azeroth.api.entity;

import com.azeroth.api.enums.Facciones;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "facciones")
public class Faccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre", nullable = false, unique = true)
    private Facciones nombre;

    @OneToMany(mappedBy = "faccion",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Jugador> jugador;

    @OneToMany(mappedBy = "faccion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Raza> raza;

    @OneToMany(mappedBy = "faccion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Hermandad> hermandad;
}
