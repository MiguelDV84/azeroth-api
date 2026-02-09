package com.azeroth.api.service;

import com.azeroth.api.dto.ProgresoResponse;
import com.azeroth.api.entity.Jugador;
import com.azeroth.api.entity.Logros;
import com.azeroth.api.entity.Progreso;
import com.azeroth.api.enums.EstadoLogro;
import com.azeroth.api.mapper.ProgresoMapper;
import com.azeroth.api.repository.IJugadorRepository;
import com.azeroth.api.repository.IProgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgresoService {

    private final IProgresoRepository progresoRepository;
    private final IJugadorRepository jugadorRepository;

    private final ProgresoMapper progresoMapper;

    @Transactional
    public Optional<ProgresoResponse> actualizarProgreso(Long jugadorId, Long logroId) {
        Progreso progreso = progresoRepository.findByJugadorIdAndLogroId(jugadorId, logroId)
                .orElseThrow(() -> new RuntimeException("Progreso no encontrado para el jugador: " + jugadorId + " y logro: " + logroId + " especificados"));


        if (progreso.getEstado() == EstadoLogro.COMPLETADO) {
            return Optional.of(progresoMapper.progresoToProgresoResponse(progreso));
        }
        if(progreso.getValorActual() >= progreso.getValorObjetivo()){
            return Optional.of(progresoMapper.progresoToProgresoResponse(progreso));
        }
        progreso.setValorActual(progreso.getValorActual() + 1);

        evaluarYCompletarLogro(progreso);

        Progreso progresoActualizado = progresoRepository.save(progreso);

        return Optional.of(progresoMapper.progresoToProgresoResponse(progresoActualizado));
    }

    private void evaluarYCompletarLogro(Progreso progreso) {
        if (progreso.getValorActual() >= progreso.getValorObjetivo()) {
            progreso.setEstado(EstadoLogro.COMPLETADO);
            progreso.setFechaCompletado(LocalDate.now());

            Jugador jugador = progreso.getJugador();
            Logros logro = progreso.getLogro();

            jugador.setExperiencia(jugador.getExperiencia().add(logro.getPuntosDeLogro()));

            jugador.comprobarExperiencia();

            jugadorRepository.save(jugador);
        }
    }
}
