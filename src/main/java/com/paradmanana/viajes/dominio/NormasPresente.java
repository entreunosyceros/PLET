package com.paradmanana.viajes.dominio;

import java.util.List;

/**
 * Normas y tono del presente (línea temporal de referencia).
 * Se muestran al regresar desde una época, no al viajar hacia ella.
 */
public record NormasPresente(
        String lema,
        List<String> advertencias,
        List<String> curiosidades,
        List<String> restricciones
) {
    public static final String CLASE_CSS = "presente";

    public NormasPresente {
        advertencias = List.copyOf(advertencias);
        curiosidades = List.copyOf(curiosidades);
        restricciones = List.copyOf(restricciones);
    }

    public static NormasPresente porDefecto() {
        return new NormasPresente(
                "El ahora es el único sitio donde el café está caliente",
                List.of(
                        "No intente convencer a nadie de que acaba de ver un T-Rex.",
                        "Revise que no traiga huevos, togas ni hoverboards en el equipaje."
                ),
                List.of(
                        "Aquí el oxígeno es el de siempre: suficiente para quejarse del tráfico.",
                        "Los dinosaurios siguen siendo fósiles. Por favor, no los despierte."
                ),
                List.of(
                        "Prohibido alterar la línea temporal desde el sofá.",
                        "Máximo 1 braguetazo temporal por día laborable."
                )
        );
    }
}
