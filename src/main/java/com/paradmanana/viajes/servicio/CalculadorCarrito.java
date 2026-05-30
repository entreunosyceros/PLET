package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Carrito;
import com.paradmanana.viajes.dominio.Era;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Calcula el total del carrito aplicando recargos por viajes de solo ida
 * (sin billete de vuelta) con su descargo legal correspondiente.
 */
@Service
public class CalculadorCarrito {

    private static final BigDecimal MULTIPLICADOR_RIESGO_ATRAPADO = new BigDecimal("6.25");

    private final BigDecimal porcentajeRecargoSoloIda;

    public CalculadorCarrito(
            @Value("${viajes.recargo-solo-ida-porcentaje}") BigDecimal porcentajeRecargoSoloIda) {
        this.porcentajeRecargoSoloIda = porcentajeRecargoSoloIda;
    }

    public ResumenCarrito calcularResumen(Carrito carrito) {
        BigDecimal subtotal = carrito.obtenerSubtotalBilletes();
        List<ResumenCarrito.RecargoSoloIda> recargos = new ArrayList<>();
        BigDecimal totalRecargos = BigDecimal.ZERO;

        // Recargo solo si hay ida a una era pero no vuelta contratada
        for (Era era : Era.values()) {
            if (carrito.tieneIda(era) && !carrito.tieneVuelta(era)) {
                Billete ida = carrito.buscarIda(era).orElseThrow();
                BigDecimal recargo = calcularRecargoSoloIda(ida);
                totalRecargos = totalRecargos.add(recargo);
                recargos.add(new ResumenCarrito.RecargoSoloIda(
                        era,
                        ida,
                        recargo,
                        construirDescargo(era)
                ));
            }
        }

        return new ResumenCarrito(
                subtotal,
                totalRecargos,
                subtotal.add(totalRecargos),
                recargos
        );
    }

    /**
     * Recargo = porcentaje sobre el billete de ida + tasa por riesgo histórico
     * de quedar atrapado en la época sin vuelta contratada.
     */
    BigDecimal calcularRecargoSoloIda(Billete ida) {
        BigDecimal porPrecio = ida.precioTotal()
                .multiply(porcentajeRecargoSoloIda);
        BigDecimal porRiesgo = MULTIPLICADOR_RIESGO_ATRAPADO
                .multiply(BigDecimal.valueOf(ida.era().getIndiceRiesgo()));
        return porPrecio.add(porRiesgo).setScale(2, RoundingMode.HALF_UP);
    }

    private String construirDescargo(Era era) {
        return "Descargo viaje de solo ida a " + era.getNombre()
                + ": sin billete de vuelta, asumes el riesgo de quedar varado en "
                + era.getAnoTemporal().formatear()
                + ". Se aplica recargo del "
                + porcentajeRecargoSoloIda.multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString()
                + "% + tasa de riesgo histórico ("
                + era.getIndiceRiesgo() + "/10). IFCD0112 no se hace responsable de paradojas derivadas.";
    }
}
