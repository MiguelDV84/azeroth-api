package com.azeroth.api.config;

import com.azeroth.api.entity.Clase;
import com.azeroth.api.entity.Faccion;
import com.azeroth.api.entity.Raza;
import com.azeroth.api.enums.Clases;
import com.azeroth.api.enums.Facciones;
import com.azeroth.api.enums.Razas;
import com.azeroth.api.repository.IClaseRepository;
import com.azeroth.api.repository.IFaccionRepository;
import com.azeroth.api.repository.IRazaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initClases(IClaseRepository claseRepository) {
        return args -> {
            // Verifica si ya existen datos
            if (claseRepository.count() == 0) {
                logger.info("Iniciando carga de datos base para clases...");

                for (Clases clase : Clases.values()) {
                    Clase entity = new Clase();
                    entity.setNombre(clase);
                    claseRepository.save(entity);
                    logger.info("Clase guardada: {}", clase.name());
                }

                logger.info("Datos base cargados exitosamente");
            } else {
                logger.info("Los datos base ya existen, omitiendo inicialización");
            }
        };
    }

    @Bean
    CommandLineRunner initFaccion(IFaccionRepository faccionRepository) {
        return args -> {
            if(faccionRepository.count() == 0) {
                logger.info("Iniciando carga de datos base para faccion...");

                for (Facciones faccion : Facciones.values()) {
                    Faccion entity = new Faccion();
                    entity.setNombre(faccion);
                    faccionRepository.save(entity);
                    logger.info("Facción guardada: {}", faccion.name());
                }
                logger.info("Datos base de facciones cargados exitosamente");
            } else {
                logger.info("Los datos base de facciones ya existen, omitiendo inicialización");
            }
        };
    }

    @Bean
    CommandLineRunner initRazas(IRazaRepository razaRepository, IFaccionRepository faccionRepository, IClaseRepository claseRepository) {
        return args -> {
            if(razaRepository.count() == 0) {
                logger.info("Iniciando carga de datos base para razas...");

                // Obtener las facciones de la BD
                Faccion alianza = faccionRepository.findByNombre(Facciones.ALIANZA)
                        .orElseThrow(() -> new RuntimeException("Facción ALIANZA no encontrada"));
                Faccion horda = faccionRepository.findByNombre(Facciones.HORDA)
                        .orElseThrow(() -> new RuntimeException("Facción HORDA no encontrada"));

                for (Razas raza : Razas.values()) {
                    Raza entity = new Raza();
                    entity.setNombre(raza);

                    // Asignar facción según la raza
                    entity.setFaccion(determinarFaccion(raza, alianza, horda));

                    // Asignar clases disponibles para esta raza
                    entity.setClasesDisponibles(obtenerClasesParaRaza(raza, claseRepository));

                    razaRepository.save(entity);
                    logger.info("Raza guardada: {} con {} clases disponibles", raza.name(), entity.getClasesDisponibles().size());
                }
                logger.info("Datos base de razas cargados exitosamente");
            } else {
                logger.info("Los datos base de razas ya existen, omitiendo inicialización");
            }
        };
    }

    private Faccion determinarFaccion(Razas raza, Faccion alianza, Faccion horda) {
        return switch (raza) {
            case HUMANO, ENANO, GNOMO, ELFO_NOCHE, DRAENEI -> alianza;
            case ORCO, NO_MUERTO, TAUREN, TROLL, ELFO_SANGRE -> horda;
        };
    }

    private List<Clase> obtenerClasesParaRaza(Razas raza, IClaseRepository claseRepository) {
        List<Clases> clasesEnum = switch (raza) {
            case HUMANO -> List.of(Clases.GUERRERO, Clases.PALADIN, Clases.MAGO, Clases.SACERDOTE,
                                   Clases.PICARO, Clases.BRUJO, Clases.CABALLERO_DE_LA_MUERTE);
            case ORCO -> List.of(Clases.GUERRERO, Clases.CAZADOR, Clases.PICARO, Clases.CHAMAN,
                                 Clases.BRUJO, Clases.CABALLERO_DE_LA_MUERTE);
            case ENANO -> List.of(Clases.GUERRERO, Clases.PALADIN, Clases.CAZADOR, Clases.PICARO,
                                  Clases.SACERDOTE, Clases.CABALLERO_DE_LA_MUERTE);
            case ELFO_NOCHE -> List.of(Clases.GUERRERO, Clases.CAZADOR, Clases.PICARO, Clases.SACERDOTE,
                                       Clases.DRUIDA, Clases.CABALLERO_DE_LA_MUERTE);
            case NO_MUERTO -> List.of(Clases.GUERRERO, Clases.PICARO, Clases.SACERDOTE, Clases.MAGO,
                                      Clases.BRUJO, Clases.CABALLERO_DE_LA_MUERTE);
            case TAUREN -> List.of(Clases.GUERRERO, Clases.CAZADOR, Clases.CHAMAN, Clases.DRUIDA,
                                   Clases.CABALLERO_DE_LA_MUERTE);
            case GNOMO -> List.of(Clases.GUERRERO, Clases.PICARO, Clases.MAGO, Clases.BRUJO,
                                  Clases.CABALLERO_DE_LA_MUERTE);
            case TROLL -> List.of(Clases.GUERRERO, Clases.CAZADOR, Clases.PICARO, Clases.SACERDOTE,
                                  Clases.CHAMAN, Clases.MAGO, Clases.CABALLERO_DE_LA_MUERTE);
            case DRAENEI -> List.of(Clases.GUERRERO, Clases.PALADIN, Clases.CAZADOR, Clases.SACERDOTE,
                                    Clases.CHAMAN, Clases.MAGO, Clases.CABALLERO_DE_LA_MUERTE);
            case ELFO_SANGRE -> List.of(Clases.GUERRERO, Clases.PALADIN, Clases.CAZADOR, Clases.PICARO,
                                        Clases.SACERDOTE, Clases.MAGO, Clases.BRUJO, Clases.CABALLERO_DE_LA_MUERTE);
        };

        // Buscar las entidades Clase correspondientes
        return clasesEnum.stream()
                .map(claseEnum -> claseRepository.findByNombre(claseEnum)
                        .orElseThrow(() -> new RuntimeException("Clase no encontrada: " + claseEnum)))
                .toList();
    }

}

