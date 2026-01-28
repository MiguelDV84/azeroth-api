package com.azeroth.api.service;

import com.azeroth.api.dto.JugadorEditarRequest;
import com.azeroth.api.dto.JugadorHermandadRequest;
import com.azeroth.api.dto.JugadorRequest;
import com.azeroth.api.dto.JugadorResponse;
import com.azeroth.api.entity.*;
import com.azeroth.api.mapper.JugadorMapper;
import com.azeroth.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JugadorService {

    private final JugadorMapper jugadorMapper;
    private final IJugadorRepository jugadorRepository;
    private final IFaccionRepository faccionRepository;
    private final IClaseRepository claseRepository;
    private final IRazaRepository razaRepository;
    private final IHermandadRepository hermandadRepository;

    public Optional<JugadorResponse> guardar(JugadorRequest request) {
        Jugador jugador = jugadorMapper.jugadorRequestToJugador(request);
        Clase clase = claseRepository.findById(request.claseId())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + request.claseId()));
        Raza raza = razaRepository.findById(request.razaId())
                .orElseThrow(() -> new RuntimeException("Raza no encontrada con id: " + request.razaId()));
        Faccion faccion = faccionRepository.findById(request.faccionId())
                .orElseThrow(() -> new RuntimeException("Facción no encontrada con id: " + request.faccionId()));

        boolean claseValida = raza.getClasesDisponibles().stream()
                .anyMatch(c -> c.getId().equals(clase.getId()));

        if (!claseValida) {
            throw new RuntimeException(
                    String.format("La clase %s no está disponible para la raza %s",
                            clase.getNombre(), raza.getNombre())
            );
        }

        if (!raza.getFaccion().getId().equals(faccion.getId())) {
            throw new RuntimeException(
                    String.format("La raza %s no pertenece a la facción %s",
                            raza.getNombre(), faccion.getNombre())
            );
        }

        jugador.setFaccion(faccion);
        jugador.setClase(clase);
        jugador.setRaza(raza);

        Jugador jugadorGuardado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorGuardado));
    }

    public Page<JugadorResponse> findAll(Pageable pageable) {
        return jugadorRepository.findAll(pageable)
                .map(jugadorMapper::jugadorToJugadorResponse);
    }

    public Optional<JugadorResponse> findById(Long id) {
        return jugadorRepository.findById(id)
                .map(jugadorMapper::jugadorToJugadorResponse);
    }

    public void eliminar(Long id) {
        jugadorRepository.deleteById(id);
    }

    public Optional<JugadorResponse> editar(Long idJugador, JugadorEditarRequest request) {
        Jugador jugador = jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + idJugador));
        jugador.setNombre(request.nombre());
        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorActualizado));
    }

    public Optional<JugadorResponse> asignarHermandad(Long idJugador, JugadorHermandadRequest request) {
        Jugador jugador = jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + idJugador));
        Hermandad hermandad = hermandadRepository.findById(request.hermandadId())
                .orElseThrow(() -> new RuntimeException("Hermandad no encontrada con id: " + request.hermandadId()));

        if(!jugador.getFaccion().getId().equals(hermandad.getFaccion().getId())) {
            throw new RuntimeException(
                    String.format("El jugador %s no puede unirse a la hermandad %s debido a que pertenecen a facciones diferentes.",
                            jugador.getNombre(), hermandad.getNombre())
            );
        }

        jugador.setHermandad(hermandad);
        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorActualizado));
    }

    public Optional<JugadorResponse> eliminarHermandad(Long idJugador) {
        Jugador jugador = jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + idJugador));
        jugador.setHermandad(null);
        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorActualizado));
    }

    public Optional<JugadorResponse> ganarExperiencia(Long jugadorId, BigDecimal cantidadGanada) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + jugadorId));

        jugador.setExperiencia(jugador.getExperiencia().add(cantidadGanada.setScale(0, RoundingMode.DOWN)));

        while (jugador.getExperiencia().compareTo(jugador.calcularXpRequerida(jugador.getNivel())) >= 0) {
            jugador.subirNivel(jugador);
        }

        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorActualizado));
    }
}
