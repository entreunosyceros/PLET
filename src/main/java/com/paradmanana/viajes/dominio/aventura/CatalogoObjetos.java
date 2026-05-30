package com.paradmanana.viajes.dominio.aventura;

import com.paradmanana.viajes.dominio.Era;

import java.util.List;
import java.util.Optional;

/**
 * Catálogo de objetos del inventario temporal.
 * <p>
 * Los {@code id} se guardan en sesión ({@code ServicioAventuras}).
 * Para añadir un objeto: decláralo aquí y referencia su {@code id} en {@link EfectosOpcion}.
 * </p>
 */
public final class CatalogoObjetos {

    public static final ObjetoTemporal RONCHON_COSMICO = new ObjetoTemporal(
            "ronchon-cosmico",
            "Ronchón cósmico en frasco",
            Era.DINOSAURIOS,
            150,
            0,
            true
    );

    public static final ObjetoTemporal DENARIO_ROMANO = new ObjetoTemporal(
            "denario-romano",
            "Denario romano auténtico",
            Era.ROMA,
            500,
            0,
            true
    );

    public static final ObjetoTemporal MAPA_CUERO = new ObjetoTemporal(
            "mapa-cuero",
            "Mapa de cuero de la Subura",
            Era.ROMA,
            80,
            0,
            false
    );

    public static final ObjetoTemporal BOTA_PERDIDA = new ObjetoTemporal(
            "bota-subura",
            "Bota perdida en la Subura",
            Era.ROMA,
            12,
            0,
            false
    );

    public static final ObjetoTemporal ALMANAQUE_3000 = new ObjetoTemporal(
            "almanaque-3000",
            "Almanaque deportivo del año 3000",
            Era.ANO_3000,
            100,
            20,
            true
    );

    public static final ObjetoTemporal LODO_VOLCANICO = new ObjetoTemporal(
            "lodo-volcanico",
            "Lodo termal jurásico (olía a azufre y gloria)",
            Era.DINOSAURIOS,
            80,
            0,
            false
    );

    public static final ObjetoTemporal PLUMA_PTERODACTILO = new ObjetoTemporal(
            "pluma-pterodactilo",
            "Pluma de pterodáctilo con ticket de acceso",
            Era.DINOSAURIOS,
            130,
            0,
            false
    );

    public static final ObjetoTemporal ANFORA_ROTA = new ObjetoTemporal(
            "anfora-rota",
            "Ánfora rota con inscripción «yo voté no»",
            Era.ROMA,
            55,
            0,
            false
    );

    public static final ObjetoTemporal PERGAMINO_FALSO = new ObjetoTemporal(
            "pergamino-falso",
            "Pergamino de profecía claramente falsificado",
            Era.ROMA,
            40,
            10,
            false
    );

    public static final ObjetoTemporal BEBIDA_NEURAL = new ObjetoTemporal(
            "bebida-neural",
            "Bebida neural «Sabor a nostalgia»",
            Era.ANO_3000,
            90,
            15,
            false
    );

    public static final ObjetoTemporal STICKER_HOVER = new ObjetoTemporal(
            "sticker-hover",
            "Pegatina holográfica «Mi otro hoverboard es más rápido»",
            Era.ANO_3000,
            35,
            0,
            false
    );

    private static final List<ObjetoTemporal> TODOS = List.of(
            RONCHON_COSMICO,
            DENARIO_ROMANO,
            MAPA_CUERO,
            BOTA_PERDIDA,
            ALMANAQUE_3000,
            LODO_VOLCANICO,
            PLUMA_PTERODACTILO,
            ANFORA_ROTA,
            PERGAMINO_FALSO,
            BEBIDA_NEURAL,
            STICKER_HOVER
    );

    /** Una pieza de museo por era; completarlas desbloquea Agente Veterano (−5 % permanente). */
    private static final List<ObjetoTemporal> PIEZAS_MUSEO = List.of(
            RONCHON_COSMICO,
            DENARIO_ROMANO,
            ALMANAQUE_3000
    );

    private CatalogoObjetos() {
    }

    public static List<ObjetoTemporal> todos() {
        return TODOS;
    }

    public static List<ObjetoTemporal> piezasDelMuseo() {
        return PIEZAS_MUSEO;
    }

    public static Optional<ObjetoTemporal> buscarPorId(String id) {
        return TODOS.stream()
                .filter(o -> o.id().equals(id))
                .findFirst();
    }

    public static boolean coleccionMuseoCompleta(java.util.Set<String> inventario) {
        return PIEZAS_MUSEO.stream().allMatch(p -> inventario.contains(p.id()));
    }
}
