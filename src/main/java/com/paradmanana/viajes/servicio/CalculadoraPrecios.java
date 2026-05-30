package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.AnoTemporal;
import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.LineaTemporal;
import com.paradmanana.viajes.dominio.TipoBillete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calcula el precio de cada billete según distancia temporal, riesgo de la era
 * y seguro Efecto Mariposa (variación aleatoria por petición).
 */
@Service
public class CalculadoraPrecios {

    /** Tarifa fija por billete, independiente del destino. */
    private static final BigDecimal PRECIO_BASE = new BigDecimal("49.99");
    /** Combustible temporal: tramo largo (≥ 1 millón de años). */
    private static final BigDecimal PRECIO_POR_MILLON_ANOS = new BigDecimal("0.15");
    /** Combustible temporal: tramo corto o resto tras millones completos. */
    private static final BigDecimal PRECIO_POR_ANO_CERCANO = new BigDecimal("0.008");
    /** Factor multiplicador del índice de riesgo histórico de la era. */
    private static final BigDecimal MULTIPLICADOR_RIESGO = new BigDecimal("12.50");

    private final LineaTemporal lineaTemporal;
    private final SeguroEfectoMariposa seguroMariposa;
    private final ServicioPosicionViajero servicioPosicion;

    @Autowired
    public CalculadoraPrecios(LineaTemporal lineaTemporal, SeguroEfectoMariposa seguroMariposa,
                              ServicioPosicionViajero servicioPosicion) {
        this.lineaTemporal = lineaTemporal;
        this.seguroMariposa = seguroMariposa;
        this.servicioPosicion = servicioPosicion;
    }

    /** Constructor para tests unitarios sin variación del seguro mariposa. */
    CalculadoraPrecios(LineaTemporal lineaTemporal) {
        this(lineaTemporal, SeguroEfectoMariposa.sinVariacion(), new ServicioPosicionViajero(lineaTemporal));
    }

    public AnoTemporal obtenerAnoPresente() {
        return lineaTemporal.presente();
    }

    public LineaTemporal getLineaTemporal() {
        return lineaTemporal;
    }

    /**
     * Crea un billete con origen en la posición actual del viajero:
     * ida = posición → era; vuelta = era → presente.
     */
    public Billete crearBillete(TipoBillete tipo, Era era) {
        AnoTemporal presente = obtenerAnoPresente();
        AnoTemporal destinoEra = era.getAnoTemporal();
        AnoTemporal posicionViajero = servicioPosicion.obtenerPosicion();

        AnoTemporal origen;
        AnoTemporal destino;

        if (tipo == TipoBillete.IDA) {
            origen = posicionViajero;
            destino = destinoEra;
        } else {
            origen = destinoEra;
            destino = presente;
        }

        int distancia = origen.distanciaAbsoluta(destino);
        BigDecimal combustible = calcularCombustible(distancia);
        BigDecimal riesgo = calcularRecargoRiesgo(era);
        BigDecimal subtotal = PRECIO_BASE.add(combustible).add(riesgo);
        BigDecimal seguro = seguroMariposa.calcularImporte(subtotal);
        BigDecimal total = subtotal.add(seguro);

        return new Billete(tipo, era, origen, destino, PRECIO_BASE, combustible, riesgo, seguro, total);
    }

    /**
     * Tarifa escalonada: millones de años a precio reducido por bloque,
     * años restantes al precio unitario cercano.
     */
    private BigDecimal calcularCombustible(int distanciaAnos) {
        if (distanciaAnos >= 1_000_000) {
            int millones = distanciaAnos / 1_000_000;
            int resto = distanciaAnos % 1_000_000;
            BigDecimal porMillones = PRECIO_POR_MILLON_ANOS
                    .multiply(BigDecimal.valueOf(millones));
            BigDecimal porResto = PRECIO_POR_ANO_CERCANO
                    .multiply(BigDecimal.valueOf(resto));
            return porMillones.add(porResto).setScale(2, RoundingMode.HALF_UP);
        }
        return PRECIO_POR_ANO_CERCANO
                .multiply(BigDecimal.valueOf(distanciaAnos))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /** Recargo proporcional al índice de riesgo de la era (0–10). */
    private BigDecimal calcularRecargoRiesgo(Era era) {
        return MULTIPLICADOR_RIESGO
                .multiply(BigDecimal.valueOf(era.getIndiceRiesgo()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /** Desglose detallado para mostrar en catálogo y ficha de era. */
    public DesglosePrecio obtenerDesglose(Era era) {
        return obtenerDesglose(era, TipoBillete.IDA);
    }

    public DesglosePrecio obtenerDesglose(Era era, TipoBillete tipo) {
        Billete billete = crearBillete(tipo, era);
        return new DesglosePrecio(
                era,
                tipo,
                billete.precioBase(),
                billete.costeCombustible(),
                billete.recargoRiesgo(),
                billete.seguroMariposa(),
                seguroMariposa.getPorcentajeDisplay(),
                billete.precioTotal(),
                billete.distanciaTemporal(),
                era.getIndiceRiesgo()
        );
    }

    /** Vista de precios desglosados para las plantillas Thymeleaf. */
    public record DesglosePrecio(
            Era era,
            TipoBillete tipo,
            BigDecimal precioBase,
            BigDecimal combustible,
            BigDecimal riesgo,
            BigDecimal seguroMariposa,
            BigDecimal porcentajeSeguroMariposa,
            BigDecimal total,
            int distanciaAnos,
            double indiceRiesgo
    ) {
        /** Compatibilidad con plantillas que usan {@code totalIda}. */
        public BigDecimal totalIda() {
            return total;
        }
    }
}
