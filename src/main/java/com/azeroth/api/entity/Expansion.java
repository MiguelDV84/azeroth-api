package com.azeroth.api.entity;

import com.azeroth.api.enums.ExpansionNombre;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "expansiones")
public class Expansion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre", nullable = false)
    private ExpansionNombre nombre;

    @Column(name = "nivel_maximo", nullable = false)
    @Max(70)
    private int nivelMaximo;
}
