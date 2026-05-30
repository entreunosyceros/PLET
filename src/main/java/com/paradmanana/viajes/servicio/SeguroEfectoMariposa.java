package com.paradmanana.viajes.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Seguro contra el Efecto Mariposa: variación aleatoria por petición HTTP.
 * Con paradoja activa la amplitud se triplica (±3 % en lugar de ±1 %).
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SeguroEfectoMariposa {

    private final BigDecimal porcentaje;

    @Autowired
    public SeguroEfectoMariposa(ServicioAventuras servicioAventuras) {
        double amplitud = servicioAventuras.tieneParadojaActiva()
                ? servicioAventuras.obtenerAmplitudMariposaParadoja()
                : 0.01;
        double variacion = ThreadLocalRandom.current().nextDouble(-amplitud, amplitud);
        this.porcentaje = BigDecimal.valueOf(variacion);
    }

    /** Solo para tests unitarios ({@link #sinVariacion()}). */
    private SeguroEfectoMariposa(BigDecimal porcentajeFijo) {
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

    /** Importe del seguro = subtotal × porcentaje aleatorio. */
    public BigDecimal calcularImporte(BigDecimal subtotal) {
        return subtotal.multiply(porcentaje).setScale(2, RoundingMode.HALF_UP);
    }
}
