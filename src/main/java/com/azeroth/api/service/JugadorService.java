package com.azeroth.api.service;

import com.azeroth.api.dto.*;
import com.azeroth.api.entity.*;
import com.azeroth.api.enums.ErrorCode;
import com.azeroth.api.enums.EstadoLogro;
import com.azeroth.api.enums.Role;
import com.azeroth.api.exception.BussinesException;
import com.azeroth.api.mapper.JugadorLogroMapper;
import com.azeroth.api.mapper.JugadorMapper;
import com.azeroth.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JugadorService {

    private final JugadorMapper jugadorMapper;
    private final JugadorLogroMapper jugadorLogroMapper;

    private final IJugadorRepository jugadorRepository;
    private final IFaccionRepository faccionRepository;
    private final IClaseRepository claseRepository;
    private final IRazaRepository razaRepository;
    private final IHermandadRepository hermandadRepository;
    private final ILogroRepository logroRepository;
    private final IProgresoRepository progresoRepository;
    private final IUsuarioRepository usuarioRepository;

        private Usuario getUsuarioAutenticado() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                return (Usuario) authentication.getPrincipal();
        }

        private boolean isAdmin(Usuario usuario) {
                return usuario != null && usuario.getRole() == Role.ADMIN;
        }

        private Jugador obtenerJugadorConPermisos(Long id, Usuario usuarioAutenticado) {
                if (isAdmin(usuarioAutenticado)) {
                        return jugadorRepository.findById(id)
                                        .orElseThrow(() -> new BussinesException(
                                                        "Jugador no encontrado",
                                                        ErrorCode.JUGADOR_NO_ENCONTRADO
                                        ));
                }
                return jugadorRepository.findByIdAndUsuario(id, usuarioAutenticado)
                                .orElseThrow(() -> new BussinesException(
                                                "Jugador no encontrado o no tienes permiso para modificarlo",
                                                ErrorCode.JUGADOR_NO_ENCONTRADO
                                ));
        }

    @Transactional
    public Optional<JugadorResponse> guardar(JugadorRequest request) {
        // Obtener el usuario autenticado
                Usuario usuarioAutenticado = getUsuarioAutenticado();

        Jugador jugador = jugadorMapper.jugadorRequestToJugador(request);
        Clase clase = claseRepository.findById(request.claseId())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + request.claseId()));
        Raza raza = razaRepository.findById(request.razaId())
                .orElseThrow(() -> new RuntimeException("Raza no encontrada con id: " + request.razaId()));
        Faccion faccion = faccionRepository.findById(request.faccionId())
                .orElseThrow(() -> new RuntimeException("Facción no encontrada con id: " + request.faccionId()));

        if(jugadorRepository.findByNombre(request.nombre()).isPresent()) {
            throw new BussinesException(
                    String.format("El nombre %s ya está en uso por otro jugador", request.nombre()),
                    ErrorCode.NOMBRE_JUGADOR_YA_EN_USO
            );
        }

        boolean claseValida = raza.getClasesDisponibles().stream()
                .anyMatch(c -> c.getId().equals(clase.getId()));

        if (!claseValida) {
            throw new BussinesException(
                    String.format("La clase %s no está disponible para la raza %s",
                            clase.getNombre(), raza.getNombre()),
                    ErrorCode.CLASE_NO_DISPONIBLE_PARA_RAZA
            );
        }

        if (!raza.getFaccion().getId().equals(faccion.getId())) {
            throw new BussinesException(
                    String.format("La raza %s no pertenece a la facción %s",
                            raza.getNombre(), faccion.getNombre()),
                    ErrorCode.RAZA_NO_DISPONIBLE_PARA_FACCION
            );
        }

        jugador.setUsuario(usuarioAutenticado);
        jugador.setFaccion(faccion);
        jugador.setClase(clase);
        jugador.setRaza(raza);

        Jugador jugadorGuardado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorGuardado));
    }

    @Transactional(readOnly = true)
    public Page<JugadorResponse> findAll(Pageable pageable) {
        // Obtener el usuario autenticado
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        if (isAdmin(usuarioAutenticado)) {
            return jugadorRepository.findAll(pageable)
                    .map(jugadorMapper::jugadorToJugadorResponse);
        }

        return jugadorRepository.findByUsuario(usuarioAutenticado, pageable)
                .map(jugadorMapper::jugadorToJugadorResponse);
    }

    @Transactional(readOnly = true)
    public Optional<JugadorResponse> findById(Long id) {
        // Obtener el usuario autenticado
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        if (isAdmin(usuarioAutenticado)) {
            return jugadorRepository.findById(id)
                    .map(jugadorMapper::jugadorToJugadorResponse);
        }

        return jugadorRepository.findByIdAndUsuario(id, usuarioAutenticado)
                .map(jugadorMapper::jugadorToJugadorResponse);
    }


    @Transactional
    public void eliminar(Long id) {
        // Obtener el usuario autenticado
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        Jugador jugador = obtenerJugadorConPermisos(id, usuarioAutenticado);

        jugadorRepository.delete(jugador);
    }

    @Transactional
    public Optional<JugadorResponse> editar(Long idJugador, JugadorEditarRequest request) {
        // Obtener el usuario autenticado
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        Jugador jugador = obtenerJugadorConPermisos(idJugador, usuarioAutenticado);

        jugador.setNombre(request.nombre());
        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorActualizado));
    }

    @Transactional
    public Optional<JugadorResponse> asignarHermandad(Long idJugador, JugadorHermandadRequest request) {
        // Obtener el usuario autenticado
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        Jugador jugador = obtenerJugadorConPermisos(idJugador, usuarioAutenticado);

        Hermandad hermandad = hermandadRepository.findById(request.hermandadId())
                .orElseThrow(() -> new RuntimeException("Hermandad no encontrada con id: " + request.hermandadId()));

        if(!jugador.getFaccion().getId().equals(hermandad.getFaccion().getId())) {
            throw new BussinesException(
                    String.format("El jugador %s no puede unirse a la hermandad %s debido a que pertenecen a facciones diferentes.",
                            jugador.getNombre(), hermandad.getNombre()),
                    ErrorCode.HERMANDAD_FACION_NO_COINCIDE
            );
        }

        jugador.setHermandad(hermandad);
        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorActualizado));
    }

    @Transactional
    public Optional<JugadorResponse> eliminarHermandad(Long idJugador) {
        // Obtener el usuario autenticado
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        Jugador jugador = obtenerJugadorConPermisos(idJugador, usuarioAutenticado);

        jugador.setHermandad(null);
        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorActualizado));
    }

    @Transactional
    public Optional<JugadorResponse> ganarExperiencia(Long jugadorId, BigDecimal cantidadGanada) {
        // Obtener el usuario autenticado
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        Jugador jugador = obtenerJugadorConPermisos(jugadorId, usuarioAutenticado);

        jugador.setExperiencia(jugador.getExperiencia().add(cantidadGanada.setScale(0, RoundingMode.DOWN)));

        jugador.comprobarExperiencia();

        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return Optional.of(jugadorMapper.jugadorToJugadorResponse(jugadorActualizado));
    }

    @Transactional
    public Optional<JugadorLogrosResponse> inicializarProgresoParaJugador(Long jugadorId) {
        // Obtener el usuario autenticado
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        Jugador jugador = obtenerJugadorConPermisos(jugadorId, usuarioAutenticado);

        List<Logros> todosLosLogros = logroRepository.findAll();

        List<Progreso> progresos = todosLosLogros.stream()
                .map(logro -> Progreso.builder()
                        .jugador(jugador)
                        .logro(logro)
                        .valorActual(0)
                        .valorObjetivo(logro.getValorObjetivo())
                        .estado(EstadoLogro.EN_PROGRESO)
                        .build())
                .toList();

        progresoRepository.saveAll(progresos);

        return Optional.of(jugadorLogroMapper.jugadorToJugadorLogrosResponse(jugador));
    }

    @Transactional(readOnly = true)
    public Optional<JugadorLogrosResponse> obtenerLogrosJugador(Long jugadorId) {
        // Obtener el usuario autenticado
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        Jugador jugador = obtenerJugadorConPermisos(jugadorId, usuarioAutenticado);

        return Optional.of(jugadorLogroMapper.jugadorToJugadorLogrosResponse(jugador));
    }
}
