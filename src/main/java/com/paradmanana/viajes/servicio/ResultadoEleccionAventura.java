package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.aventura.EscenaAventura;
import com.paradmanana.viajes.dominio.aventura.OpcionAventura;

import java.util.Optional;

/**
 * Resultado narrativo y mecánico tras elegir una opción en un libro-juego.
 */
public record ResultadoEleccionAventura(
        EscenaAventura escena,
        OpcionAventura opcion,
        int creditosTrasEleccion,
        boolean paradojaActiva,
        int descuentoCompraPorcentaje,
        Optional<com.paradmanana.viajes.dominio.aventura.TipoParadoja> tipoParadoja
) {}
