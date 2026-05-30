package com.paradmanana.viajes.servicio;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Seguro contra el Efecto Mariposa: variación aleatoria de ±1 % por petición HTTP.
 * Cada recarga de página obtiene un porcentaje distinto ({@code @RequestScope}).
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SeguroEfectoMariposa {

    private final BigDecimal porcentaje;

    public SeguroEfectoMariposa() {
        double variacion = ThreadLocalRandom.current().nextDouble(-0.01, 0.01);
        this.porcentaje = BigDecimal.valueOf(variacion);
    }

    /** Para tests: porcentaje fijo (p. ej. {@code BigDecimal.ZERO}). */
    SeguroEfectoMariposa(BigDecimal porcentajeFijo) {
        this.porcentaje = porcentajeFijo;
    }

    public static SeguroEfectoMariposa sinVariacion() {
        return new SeguroEfectoMariposa(BigDecimal.ZERO);
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public BigDecimal getPorcentajeDisplay() {
        return porcentaje.multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /** Importe del seguro = subtotal × porcentaje aleatorio (±1 %). */
    public BigDecimal calcularImporte(BigDecimal subtotal) {
        return subtotal.multiply(porcentaje).setScale(2, RoundingMode.HALF_UP);
    }
}
