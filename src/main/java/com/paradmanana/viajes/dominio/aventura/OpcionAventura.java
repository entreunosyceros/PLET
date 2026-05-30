package com.paradmanana.viajes.dominio.aventura;

/**
 * Una decisión disponible en una escena de aventura.
 *
 * @param id             identificador único dentro de la escena (p. ej. {@code dejar-picar})
 * @param textoBoton     etiqueta del botón en el formulario POST
 * @param resultadoTexto narrativa mostrada tras elegir
 * @param efectos        cambios de estado en sesión
 */
public record OpcionAventura(
        String id,
        String textoBoton,
        String resultadoTexto,
        EfectosOpcion efectos
) {}
