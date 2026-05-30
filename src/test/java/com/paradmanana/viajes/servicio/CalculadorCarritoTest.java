package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.LineaTemporal;
import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Carrito;
import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.TipoBillete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculadorCarritoTest {

    private CalculadoraPrecios calculadora;
    private CalculadorCarrito calculadorCarrito;
    private Carrito carrito;

    @BeforeEach
    void preparar() {
        calculadora = new CalculadoraPrecios(new LineaTemporal(2026));
        calculadorCarrito = new CalculadorCarrito(new BigDecimal("0.30"));
        carrito = new Carrito();
    }

    @Test
    void sinRecargoCuandoHayIdaYVuelta() {
        carrito.agregar(calculadora.crearBillete(TipoBillete.IDA, Era.ROMA));
        carrito.agregar(calculadora.crearBillete(TipoBillete.VUELTA, Era.ROMA));

        var resumen = calculadorCarrito.calcularResumen(carrito);

        assertFalse(resumen.tieneRecargosSoloIda());
        assertEquals(0, resumen.totalRecargosSoloIda().compareTo(BigDecimal.ZERO));
        assertEquals(resumen.subtotalBilletes(), resumen.totalFinal());
    }

    @Test
    void aplicaRecargoYSoloIdaConDescargo() {
        Billete ida = calculadora.crearBillete(TipoBillete.IDA, Era.ROMA);
        carrito.agregar(ida);

        var resumen = calculadorCarrito.calcularResumen(carrito);

        assertTrue(resumen.tieneRecargosSoloIda());
        assertEquals(1, resumen.recargosSoloIda().size());
        assertTrue(resumen.totalRecargosSoloIda().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(resumen.totalFinal().compareTo(resumen.subtotalBilletes()) > 0);
        assertTrue(resumen.recargosSoloIda().getFirst().descargo().contains("Descargo"));
        assertTrue(resumen.recargosSoloIda().getFirst().descargo().contains("solo ida"));
    }

    @Test
    void recargoMayorEnEpocaMasRiesgosa() {
        carrito.agregar(calculadora.crearBillete(TipoBillete.IDA, Era.ROMA));
        var resumenRoma = calculadorCarrito.calcularResumen(carrito);

        carrito.vaciar();
        carrito.agregar(calculadora.crearBillete(TipoBillete.IDA, Era.DINOSAURIOS));
        var resumenDino = calculadorCarrito.calcularResumen(carrito);

        assertTrue(resumenDino.totalRecargosSoloIda()
                .compareTo(resumenRoma.totalRecargosSoloIda()) > 0);
    }
}
