package com.paradmanana.viajes.vista;

import com.paradmanana.viajes.dominio.AnoTemporal;
import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.LineaTemporal;

import java.util.List;

/**
 * DTO de vista con los datos que necesita la animación JavaScript
 * en la página de confirmación ({@code salto-temporal.js}).
 */
public record DestinoAnimacion(
        int valor,
        String etiqueta,
        String nombre,
        String emoji,
        String color,
        String lema,
        String claseCss,
        String advertenciaPrincipal,
        List<String> advertencias
) {
    public DestinoAnimacion {
        advertencias = advertencias == null ? List.of() : List.copyOf(advertencias);
    }

    /** Destino de ida hacia una era del catálogo. */
    public static DestinoAnimacion desdeEra(Era era, AnoTemporal destino) {
        String advertencia = era.getAdvertencias().isEmpty()
                ? null
                : era.getAdvertencias().getFirst();
        return new DestinoAnimacion(
                destino.valor(),
                destino.formatear(),
                era.getNombre(),
                era.getEmoji(),
                era.getColor(),
                era.getLema(),
                era.getClaseCss(),
                advertencia,
                era.getAdvertencias()
        );
    }

    /** Regreso al presente tras usar vuelta prepagada o comprar solo vuelta. */
    public static DestinoAnimacion regresoPresente(Era eraOrigen, LineaTemporal lineaTemporal) {
        AnoTemporal presente = lineaTemporal.presente();
        String advertencia = eraOrigen.getAdvertencias().isEmpty()
                ? "Has salido de " + eraOrigen.getNombre() + ". Bienvenido al presente."
                : "Al abandonar " + eraOrigen.getNombre() + ": "
                + eraOrigen.getAdvertencias().getFirst();
        return new DestinoAnimacion(
                presente.valor(),
                presente.formatear(),
                "Presente",
                "⏳",
                "#00d4ff",
                "Línea temporal restablecida",
                "presente",
                advertencia,
                eraOrigen.getAdvertencias()
        );
    }
}
