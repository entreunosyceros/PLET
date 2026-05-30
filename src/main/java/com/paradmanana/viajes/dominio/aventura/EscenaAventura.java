package com.paradmanana.viajes.dominio.aventura;

import com.paradmanana.viajes.dominio.Era;

import java.util.List;
import java.util.Optional;

/**
 * Capítulo de libro-juego asociado a una era concreta.
 *
 * @param objetoRequerido id de objeto en inventario para desbloquear (vacío = sin requisito)
 */
public record EscenaAventura(
        String id,
        Era era,
        String titulo,
        String resumen,
        String parrafoIntroduccion,
        List<OpcionAventura> opciones,
        Optional<String> objetoRequerido
) {
    public EscenaAventura(
            String id,
            Era era,
            String titulo,
            String resumen,
            String parrafoIntroduccion,
            List<OpcionAventura> opciones
    ) {
        this(id, era, titulo, resumen, parrafoIntroduccion, opciones, Optional.empty());
    }

    public Optional<OpcionAventura> buscarOpcion(String opcionId) {
        return opciones.stream()
                .filter(o -> o.id().equals(opcionId))
                .findFirst();
    }

    public boolean requiereObjeto() {
        return objetoRequerido.isPresent();
    }
}
