package com.paradmanana.viajes.dominio;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Billete de viaje temporal con desglose de precio y ruta origen → destino.
 * <p>
 * Cada billete recibe un identificador corto único para poder eliminarlo
 * del carrito sin ambigüedad.
 * </p>
 */
public record Billete(
        String identificador,
        TipoBillete tipo,
        Era era,
        AnoTemporal anoOrigen,
        AnoTemporal anoDestino,
        BigDecimal precioBase,
        BigDecimal costeCombustible,
        BigDecimal recargoRiesgo,
        BigDecimal seguroMariposa,
        BigDecimal precioTotal
) {

    /** Constructor de conveniencia: genera el identificador automáticamente. */
    public Billete(TipoBillete tipo, Era era, AnoTemporal anoOrigen, AnoTemporal anoDestino,
                   BigDecimal precioBase, BigDecimal costeCombustible,
                   BigDecimal recargoRiesgo, BigDecimal seguroMariposa, BigDecimal precioTotal) {
        this(
                UUID.randomUUID().toString().substring(0, 8),
                tipo,
                era,
                anoOrigen,
                anoDestino,
                precioBase,
                costeCombustible,
                recargoRiesgo,
                seguroMariposa,
                precioTotal
        );
    }

    /**
     * Año en el que el viajero «aterriza» con este billete:
     * destino en ida, origen en vuelta.
     */
    public AnoTemporal anoEfectivo() {
        return tipo == TipoBillete.IDA ? anoDestino : anoOrigen;
    }

    /** Distancia en años entre origen y destino (valor absoluto). */
    public int distanciaTemporal() {
        return anoOrigen.distanciaAbsoluta(anoDestino);
    }
}
