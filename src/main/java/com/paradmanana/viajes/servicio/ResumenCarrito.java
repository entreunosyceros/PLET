package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Era;

import java.math.BigDecimal;
import java.util.List;

/**
 * Resumen económico del carrito, incluyendo recargos por viajes de solo ida
 * y beneficios de la economía circular temporal.
 */
public record ResumenCarrito(
        BigDecimal subtotalBilletes,
        BigDecimal totalRecargosSoloIda,
        BigDecimal totalFinal,
        List<RecargoSoloIda> recargosSoloIda,
        BigDecimal descuentoPermanenteAventura,
        BigDecimal descuentoPorcentajeAventura,
        BigDecimal descuentoCreditosAventura
) {
    public ResumenCarrito(
            BigDecimal subtotalBilletes,
            BigDecimal totalRecargosSoloIda,
            BigDecimal totalFinal,
            List<RecargoSoloIda> recargosSoloIda
    ) {
        this(subtotalBilletes, totalRecargosSoloIda, totalFinal, recargosSoloIda,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public BigDecimal totalAntesBeneficios() {
        return subtotalBilletes.add(totalRecargosSoloIda);
    }

    public boolean tieneRecargosSoloIda() {
        return totalRecargosSoloIda.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean tieneBeneficiosAventura() {
        return descuentoPermanenteAventura.compareTo(BigDecimal.ZERO) > 0
                || descuentoPorcentajeAventura.compareTo(BigDecimal.ZERO) > 0
                || descuentoCreditosAventura.compareTo(BigDecimal.ZERO) > 0;
    }

    public ResumenCarrito conBeneficiosAventura(
            BigDecimal descuentoPermanente,
            BigDecimal descuentoPorcentaje,
            BigDecimal descuentoCreditos,
            BigDecimal totalConBeneficios
    ) {
        return new ResumenCarrito(
                subtotalBilletes,
                totalRecargosSoloIda,
                totalConBeneficios,
                recargosSoloIda,
                descuentoPermanente,
                descuentoPorcentaje,
                descuentoCreditos
        );
    }

    public record RecargoSoloIda(
            Era era,
            Billete billeteIda,
            BigDecimal importe,
            String descargo
    ) {}
}
