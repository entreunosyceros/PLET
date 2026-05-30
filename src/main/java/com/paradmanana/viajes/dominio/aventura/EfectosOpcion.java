package com.paradmanana.viajes.dominio.aventura;

import com.paradmanana.viajes.dominio.Era;

import java.util.Optional;

/**
 * Consecuencias mecánicas de una elección en un libro-juego.
 * Definir aquí los efectos al crear nuevas opciones en {@link CatalogoAventuras}.
 */
public record EfectosOpcion(
        int cambioCreditos,
        boolean activarParadoja,
        Optional<Era> destinoForzado,
        Optional<String> objetoInventario,
        int descuentoProximaCompraPorcentaje,
        boolean regresarAlPresente
) {
    public static EfectosOpcion ninguno() {
        return new EfectosOpcion(0, false, Optional.empty(), Optional.empty(), 0, false);
    }

    public static EfectosOpcion creditos(int cantidad) {
        return new EfectosOpcion(cantidad, false, Optional.empty(), Optional.empty(), 0, false);
    }
}
