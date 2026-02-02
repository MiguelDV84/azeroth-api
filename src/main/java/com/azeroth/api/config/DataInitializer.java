package com.azeroth.api.config;

import com.azeroth.api.entity.Clase;
import com.azeroth.api.entity.Faccion;
import com.azeroth.api.entity.Logros;
import com.azeroth.api.entity.Raza;
import com.azeroth.api.enums.Clases;
import com.azeroth.api.enums.Facciones;
import com.azeroth.api.enums.Razas;
import com.azeroth.api.repository.IClaseRepository;
import com.azeroth.api.repository.IFaccionRepository;
import com.azeroth.api.repository.ILogroRepository;
import com.azeroth.api.repository.IRazaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
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

    @Bean
    CommandLineRunner initLogros(ILogroRepository logroRepository) {
        return args -> {
            if (logroRepository.count() == 0) {
                logroRepository.saveAll(List.of(
                        Logros.builder()
                                .titulo("Primer paso en Azeroth")
                                .descripcion("Alcanza el nivel 10")
                                .puntosDeLogro(BigDecimal.valueOf(500))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Aventurero experimentado")
                                .descripcion("Alcanza el nivel 20")
                                .puntosDeLogro(BigDecimal.valueOf(1000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Héroe de Azeroth")
                                .descripcion("Alcanza el nivel 40")
                                .puntosDeLogro(BigDecimal.valueOf(2500))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Leyenda viviente")
                                .descripcion("Alcanza el nivel 60")
                                .puntosDeLogro(BigDecimal.valueOf(5000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Campeón de Terrallende")
                                .descripcion("Alcanza el nivel 70")
                                .puntosDeLogro(BigDecimal.valueOf(7500))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Cazador de murlocs")
                                .descripcion("Derrota a 50 murlocs")
                                .puntosDeLogro(BigDecimal.valueOf(1000))
                                .valorObjetivo(50)
                                .build(),
                        Logros.builder()
                                .titulo("Exterminador de koboldos")
                                .descripcion("Derrota a 30 koboldos")
                                .puntosDeLogro(BigDecimal.valueOf(800))
                                .valorObjetivo(30)
                                .build(),
                        Logros.builder()
                                .titulo("Asesino de gnomos leprosos")
                                .descripcion("Derrota a 25 gnomos leprosos")
                                .puntosDeLogro(BigDecimal.valueOf(1200))
                                .valorObjetivo(25)
                                .build(),
                        Logros.builder()
                                .titulo("Domador de arañas")
                                .descripcion("Derrota a 40 arañas gigantes")
                                .puntosDeLogro(BigDecimal.valueOf(1500))
                                .valorObjetivo(40)
                                .build(),
                        Logros.builder()
                                .titulo("Conquistador de Defias")
                                .descripcion("Derrota a 35 miembros de la Hermandad Defias")
                                .puntosDeLogro(BigDecimal.valueOf(1800))
                                .valorObjetivo(35)
                                .build(),
                        Logros.builder()
                                .titulo("Recolector de lino")
                                .descripcion("Recolecta 50 unidades de lino")
                                .puntosDeLogro(BigDecimal.valueOf(1000))
                                .valorObjetivo(50)
                                .build(),
                        Logros.builder()
                                .titulo("Minero aprendiz")
                                .descripcion("Recolecta 30 unidades de cobre")
                                .puntosDeLogro(BigDecimal.valueOf(800))
                                .valorObjetivo(30)
                                .build(),
                        Logros.builder()
                                .titulo("Herborista dedicado")
                                .descripcion("Recolecta 40 hierbas medicinales")
                                .puntosDeLogro(BigDecimal.valueOf(1200))
                                .valorObjetivo(40)
                                .build(),
                        Logros.builder()
                                .titulo("Cazador de pieles")
                                .descripcion("Recolecta 60 pieles de animales")
                                .puntosDeLogro(BigDecimal.valueOf(1500))
                                .valorObjetivo(60)
                                .build(),
                        Logros.builder()
                                .titulo("Maestro del estaño")
                                .descripcion("Recolecta 25 unidades de estaño")
                                .puntosDeLogro(BigDecimal.valueOf(1800))
                                .valorObjetivo(25)
                                .build(),
                        Logros.builder()
                                .titulo("Edwin VanCleef ha caído")
                                .descripcion("Derrota a Edwin VanCleef en Las Minas de la Muerte")
                                .puntosDeLogro(BigDecimal.valueOf(3000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Monstruo del pantano")
                                .descripcion("Derrota a Mutanus el Devorador en Las Cuevas de los Lamentos")
                                .puntosDeLogro(BigDecimal.valueOf(3000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Venganza contra los Colmillorrot")
                                .descripcion("Derrota al líder de los Colmillorrot en Horado Rajacieno")
                                .puntosDeLogro(BigDecimal.valueOf(3500))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Conquistador de Gnomeregan")
                                .descripcion("Derrota a Mekaginiero Termaplugg en Gnomeregan")
                                .puntosDeLogro(BigDecimal.valueOf(4000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Purificador del Monasterio Escarlata")
                                .descripcion("Derrota a todos los jefes del Monasterio Escarlata")
                                .puntosDeLogro(BigDecimal.valueOf(4500))
                                .valorObjetivo(4)
                                .build(),
                        Logros.builder()
                                .titulo("Azote de Uldaman")
                                .descripcion("Derrota a Archaedas en Uldaman")
                                .puntosDeLogro(BigDecimal.valueOf(5000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Explorador de Zul'Farrak")
                                .descripcion("Derrota a Gahz'rilla en Zul'Farrak")
                                .puntosDeLogro(BigDecimal.valueOf(5500))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Conquistador de Maraudon")
                                .descripcion("Derrota a la Princesa Theradras en Maraudon")
                                .puntosDeLogro(BigDecimal.valueOf(6000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Héroe de La Masacre")
                                .descripcion("Derrota al Barón Osahendido en La Masacre")
                                .puntosDeLogro(BigDecimal.valueOf(7000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Limpieza de Stratholme")
                                .descripcion("Derrota al Barón Rivendare en Stratholme")
                                .puntosDeLogro(BigDecimal.valueOf(7500))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Señor de Cumbre de Roca Negra")
                                .descripcion("Derrota al General Drakkisath en Cumbre de Roca Negra")
                                .puntosDeLogro(BigDecimal.valueOf(8000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Victoria en Scholomance")
                                .descripcion("Derrota a Sombravano el Nigromante en Scholomance")
                                .puntosDeLogro(BigDecimal.valueOf(7500))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Conquistador de Dire Maul")
                                .descripcion("Derrota a todos los jefes de La Masacre Atroz")
                                .puntosDeLogro(BigDecimal.valueOf(8500))
                                .valorObjetivo(3)
                                .build(),
                        Logros.builder()
                                .titulo("Núcleo de Magma: Lucifron")
                                .descripcion("Derrota a Lucifron en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Núcleo de Magma: Magmadar")
                                .descripcion("Derrota a Magmadar en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Núcleo de Magma: Gehennas")
                                .descripcion("Derrota a Gehennas en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Núcleo de Magma: Garr")
                                .descripcion("Derrota a Garr en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Núcleo de Magma: Barón Geddon")
                                .descripcion("Derrota al Barón Geddon en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Núcleo de Magma: Shazzrah")
                                .descripcion("Derrota a Shazzrah en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Núcleo de Magma: Sulfuron")
                                .descripcion("Derrota al Presagista Sulfuron en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Núcleo de Magma: Golemagg")
                                .descripcion("Derrota a Golemagg el Incinerador en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Núcleo de Magma: Majordomo")
                                .descripcion("Derrota al Mayordomo Executus en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Señor del Fuego")
                                .descripcion("Derrota a Ragnaros en el Núcleo de Magma")
                                .puntosDeLogro(BigDecimal.valueOf(15000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Guarida de Onyxia")
                                .descripcion("Derrota a Onyxia en su guarida")
                                .puntosDeLogro(BigDecimal.valueOf(15000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("BWL: Trasgozadera")
                                .descripcion("Derrota a Trasgozadera en Alanegra")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("BWL: Vaelastrasz")
                                .descripcion("Derrota a Vaelastrasz el Corrupto en Alanegra")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("BWL: Broodlord")
                                .descripcion("Derrota a Señor de Cría Capazote en Alanegra")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("BWL: Firemaw")
                                .descripcion("Derrota a Firemaw en Alanegra")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("BWL: Chromaggus")
                                .descripcion("Derrota a Chromaggus en Alanegra")
                                .puntosDeLogro(BigDecimal.valueOf(13000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Destructor de dragones")
                                .descripcion("Derrota a Nefarian en Alanegra")
                                .puntosDeLogro(BigDecimal.valueOf(18000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("AQ20: Kurinnaxx")
                                .descripcion("Derrota a Kurinnaxx en Las Ruinas de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("AQ20: Rajaxx")
                                .descripcion("Derrota al General Rajaxx en Las Ruinas de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(10000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("AQ20: Ossirian")
                                .descripcion("Derrota a Ossirian el Sinmarcas en Las Ruinas de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("AQ40: El Profeta Skeram")
                                .descripcion("Derrota al Profeta Skeram en El Templo de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(13000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("AQ40: Bug Trio")
                                .descripcion("Derrota al Trío de Bichos en El Templo de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(13000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("AQ40: Sartura")
                                .descripcion("Derrota a Guardabatallon Sartura en El Templo de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(13000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("AQ40: Fankriss")
                                .descripcion("Derrota a Fankriss el Implacable en El Templo de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(13000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("AQ40: Huhuran")
                                .descripcion("Derrota a la Princesa Huhuran en El Templo de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(13000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("AQ40: Gemelos Emperadores")
                                .descripcion("Derrota a los Gemelos Emperadores en El Templo de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(15000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Campeón de Ahn'Qiraj")
                                .descripcion("Derrota a C'Thun en El Templo de Ahn'Qiraj")
                                .puntosDeLogro(BigDecimal.valueOf(20000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Naxxramas: Anub'Rekhan")
                                .descripcion("Derrota a Anub'Rekhan en Naxxramas")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Naxxramas: Maexxna")
                                .descripcion("Derrota a Maexxna en Naxxramas")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Naxxramas: Instructor Razuvious")
                                .descripcion("Derrota al Instructor Razuvious en Naxxramas")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Naxxramas: Noth")
                                .descripcion("Derrota a Noth el Pesteador en Naxxramas")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Naxxramas: Heigan")
                                .descripcion("Derrota a Heigan el Impuro en Naxxramas")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Naxxramas: Patchwerk")
                                .descripcion("Derrota a Remendejo en Naxxramas")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Naxxramas: Thaddius")
                                .descripcion("Derrota a Thaddius en Naxxramas")
                                .puntosDeLogro(BigDecimal.valueOf(15000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Naxxramas: Sapphiron")
                                .descripcion("Derrota a Sapphiron en Naxxramas")
                                .puntosDeLogro(BigDecimal.valueOf(16000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Azote de la Plaga")
                                .descripcion("Derrota a Kel'Thuzad en Naxxramas")
                                .puntosDeLogro(BigDecimal.valueOf(25000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Karazhan: Attumen")
                                .descripcion("Derrota a Attumen el Cazador en Karazhan")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Karazhan: Moroes")
                                .descripcion("Derrota a Moroes en Karazhan")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Karazhan: La Doncella")
                                .descripcion("Derrota a La Doncella de la Virtud en Karazhan")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Karazhan: El Gran Mal")
                                .descripcion("Derrota al Gran Mal en Karazhan")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Karazhan: Curator")
                                .descripcion("Derrota al Curator en Karazhan")
                                .puntosDeLogro(BigDecimal.valueOf(12000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Karazhan: Aran")
                                .descripcion("Derrota al Arcano de las Sombras Aran en Karazhan")
                                .puntosDeLogro(BigDecimal.valueOf(13000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Karazhan: Terestian")
                                .descripcion("Derrota a Terestian Enfermizo en Karazhan")
                                .puntosDeLogro(BigDecimal.valueOf(13000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Karazhan: Netherspite")
                                .descripcion("Derrota a Alanegra en Karazhan")
                                .puntosDeLogro(BigDecimal.valueOf(13000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Maestro de Karazhan")
                                .descripcion("Derrota al Príncipe Malchezaar en Karazhan")
                                .puntosDeLogro(BigDecimal.valueOf(15000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Caverna Santuario Serpiente: Hydross")
                                .descripcion("Derrota a Hydross el Inestable en Caverna Santuario Serpiente")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Caverna Santuario Serpiente: Lurker")
                                .descripcion("Derrota al Lurker Abajo en Caverna Santuario Serpiente")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Caverna Santuario Serpiente: Leotheras")
                                .descripcion("Derrota a Leotheras el Ciego en Caverna Santuario Serpiente")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Caverna Santuario Serpiente: Karathress")
                                .descripcion("Derrota a Morogrim Derribador de Mareas en Caverna Santuario Serpiente")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Campeón de Vashj")
                                .descripcion("Derrota a Lady Vashj en Caverna Santuario Serpiente")
                                .puntosDeLogro(BigDecimal.valueOf(18000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("El Castillo de la Tempestad: Al'ar")
                                .descripcion("Derrota a Al'ar en El Castillo de la Tempestad")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("El Castillo de la Tempestad: Solarian")
                                .descripcion("Derrota al Sumo Astromante Solarian en El Castillo de la Tempestad")
                                .puntosDeLogro(BigDecimal.valueOf(14000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Conquistador del Castillo")
                                .descripcion("Derrota a Kael'thas Caminante del Sol en El Castillo de la Tempestad")
                                .puntosDeLogro(BigDecimal.valueOf(18000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Guarida de Gruul")
                                .descripcion("Derrota a Gruul el Asesino de Dragones")
                                .puntosDeLogro(BigDecimal.valueOf(15000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Guarida de Magtheridon")
                                .descripcion("Derrota a Magtheridon en su guarida")
                                .puntosDeLogro(BigDecimal.valueOf(15000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Recolector de Alma Vil")
                                .descripcion("Recolecta 100 fragmentos de Alma Vil")
                                .puntosDeLogro(BigDecimal.valueOf(2000))
                                .valorObjetivo(100)
                                .build(),
                        Logros.builder()
                                .titulo("Cazador de demonios")
                                .descripcion("Derrota a 100 demonios en Terrallende")
                                .puntosDeLogro(BigDecimal.valueOf(2500))
                                .valorObjetivo(100)
                                .build(),
                        Logros.builder()
                                .titulo("Azote de los Etéreos")
                                .descripcion("Derrota a 75 etéreos en Terrallende")
                                .puntosDeLogro(BigDecimal.valueOf(2000))
                                .valorObjetivo(75)
                                .build(),
                        Logros.builder()
                                .titulo("Exterminador de Naga")
                                .descripcion("Derrota a 80 nagas en Costa Brumosa")
                                .puntosDeLogro(BigDecimal.valueOf(2200))
                                .valorObjetivo(80)
                                .build(),
                        Logros.builder()
                                .titulo("Domador de draeneidas")
                                .descripcion("Derrota a 60 draeneidas corruptos")
                                .puntosDeLogro(BigDecimal.valueOf(1800))
                                .valorObjetivo(60)
                                .build(),
                        Logros.builder()
                                .titulo("Explorador de Azeroth")
                                .descripcion("Explora todas las zonas de Azeroth")
                                .puntosDeLogro(BigDecimal.valueOf(5000))
                                .valorObjetivo(30)
                                .build(),
                        Logros.builder()
                                .titulo("Explorador de Terrallende")
                                .descripcion("Explora todas las zonas de Terrallende")
                                .puntosDeLogro(BigDecimal.valueOf(4000))
                                .valorObjetivo(7)
                                .build(),
                        Logros.builder()
                                .titulo("Maestro de profesiones")
                                .descripcion("Alcanza nivel 300 en una profesión")
                                .puntosDeLogro(BigDecimal.valueOf(3000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Artesano legendario")
                                .descripcion("Alcanza nivel 375 en una profesión")
                                .puntosDeLogro(BigDecimal.valueOf(5000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Jinete experimentado")
                                .descripcion("Obtén tu primera montura épica")
                                .puntosDeLogro(BigDecimal.valueOf(3500))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Señor del vuelo")
                                .descripcion("Obtén tu montura voladora épica")
                                .puntosDeLogro(BigDecimal.valueOf(5000))
                                .valorObjetivo(1)
                                .build(),
                        Logros.builder()
                                .titulo("Maestro pescador")
                                .descripcion("Pesca 500 peces")
                                .puntosDeLogro(BigDecimal.valueOf(2000))
                                .valorObjetivo(500)
                                .build(),
                        Logros.builder()
                                .titulo("Chef magistral")
                                .descripcion("Cocina 300 platos diferentes")
                                .puntosDeLogro(BigDecimal.valueOf(2500))
                                .valorObjetivo(300)
                                .build(),
                        Logros.builder()
                                .titulo("Primeros auxilios experto")
                                .descripcion("Cura a 200 jugadores con vendajes")
                                .puntosDeLogro(BigDecimal.valueOf(1500))
                                .valorObjetivo(200)
                                .build(),
                        Logros.builder()
                                .titulo("Duelista implacable")
                                .descripcion("Gana 50 duelos contra otros jugadores")
                                .puntosDeLogro(BigDecimal.valueOf(3000))
                                .valorObjetivo(50)
                                .build(),
                        Logros.builder()
                                .titulo("Héroe de los campos de batalla")
                                .descripcion("Participa en 100 campos de batalla")
                                .puntosDeLogro(BigDecimal.valueOf(4000))
                                .valorObjetivo(100)
                                .build(),
                        Logros.builder()
                                .titulo("Guardián de banderas")
                                .descripcion("Captura 25 banderas en Garganta Grito de Guerra")
                                .puntosDeLogro(BigDecimal.valueOf(3500))
                                .valorObjetivo(25)
                                .build(),
                        Logros.builder()
                                .titulo("Conquistador de cuencas")
                                .descripcion("Gana 50 batallas en Cuenca de Arathi")
                                .puntosDeLogro(BigDecimal.valueOf(3500))
                                .valorObjetivo(50)
                                .build()


                ));
                logger.info("Datos base de logros cargados exitosamente");
            }
            logger.info("Los datos base de logros ya existen, omitiendo inicialización");
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
                                   Clases.PICARO, Clases.BRUJO);
            case ORCO -> List.of(Clases.GUERRERO, Clases.CAZADOR, Clases.PICARO, Clases.CHAMAN,
                                 Clases.BRUJO);
            case ENANO -> List.of(Clases.GUERRERO, Clases.PALADIN, Clases.CAZADOR, Clases.PICARO,
                                  Clases.SACERDOTE);
            case ELFO_NOCHE -> List.of(Clases.GUERRERO, Clases.CAZADOR, Clases.PICARO, Clases.SACERDOTE,
                                       Clases.DRUIDA);
            case NO_MUERTO -> List.of(Clases.GUERRERO, Clases.PICARO, Clases.SACERDOTE, Clases.MAGO,
                                      Clases.BRUJO);
            case TAUREN -> List.of(Clases.GUERRERO, Clases.CAZADOR, Clases.CHAMAN, Clases.DRUIDA);
            case GNOMO -> List.of(Clases.GUERRERO, Clases.PICARO, Clases.MAGO, Clases.BRUJO);
            case TROLL -> List.of(Clases.GUERRERO, Clases.CAZADOR, Clases.PICARO, Clases.SACERDOTE,
                                  Clases.CHAMAN, Clases.MAGO);
            case DRAENEI -> List.of(Clases.GUERRERO, Clases.PALADIN, Clases.CAZADOR, Clases.SACERDOTE,
                                    Clases.CHAMAN, Clases.MAGO);
            case ELFO_SANGRE -> List.of(Clases.GUERRERO, Clases.PALADIN, Clases.CAZADOR, Clases.PICARO,
                                        Clases.SACERDOTE, Clases.MAGO, Clases.BRUJO);
        };

        // Buscar las entidades Clase correspondientes
        return clasesEnum.stream()
                .map(claseEnum -> claseRepository.findByNombre(claseEnum)
                        .orElseThrow(() -> new RuntimeException("Clase no encontrada: " + claseEnum)))
                .toList();
    }
}

