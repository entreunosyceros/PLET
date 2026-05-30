package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.LineaTemporal;
import com.paradmanana.viajes.dominio.TipoBillete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServicioPosicionViajeroTest {

    private ServicioBilletesVuelta billetesVuelta;
    private ServicioPosicionViajero servicio;
    private CalculadoraPrecios calculadora;

    @BeforeEach
    void preparar() {
        billetesVuelta = new ServicioBilletesVuelta();
        servicio = new ServicioPosicionViajero(new LineaTemporal(2026), billetesVuelta);
        calculadora = new CalculadoraPrecios(new LineaTemporal(2026));
    }

    @Test
    void iniciaEnPresente() {
        assertTrue(servicio.estaEnPresente());
        assertEquals("2026 d.C. (presente)", servicio.obtenerEtiquetaPosicion());
    }

    @Test
    void idaMueveAlViajeroAEra() {
        var billete = calculadora.crearBillete(TipoBillete.IDA, Era.ROMA);
        servicio.aplicarBilletes(List.of(billete));

        assertFalse(servicio.estaEnPresente());
        assertEquals(-117, servicio.obtenerPosicion().valor());
    }

    @Test
    void idaYVueltaJuntasGuardaVueltaSinEjecutarla() {
        var ida = calculadora.crearBillete(TipoBillete.IDA, Era.ROMA);
        var vuelta = calculadora.crearBillete(TipoBillete.VUELTA, Era.ROMA);
        servicio.aplicarBilletes(List.of(ida, vuelta));

        assertFalse(servicio.estaEnPresente());
        assertTrue(billetesVuelta.tiene(Era.ROMA));
        assertTrue(servicio.puedeUsarVueltaPrepagada(Era.ROMA));
    }

    @Test
    void usarVueltaPrepagadaRegresaAlPresente() {
        var ida = calculadora.crearBillete(TipoBillete.IDA, Era.ROMA);
        var vuelta = calculadora.crearBillete(TipoBillete.VUELTA, Era.ROMA);
        servicio.aplicarBilletes(List.of(ida, vuelta));

        servicio.usarVueltaPrepagada(Era.ROMA);

        assertTrue(servicio.estaEnPresente());
        assertFalse(billetesVuelta.tiene(Era.ROMA));
    }

    @Test
    void vueltaSolaDesdeEraSeEjecutaAlInstante() {
        servicio.aplicarBilletes(List.of(calculadora.crearBillete(TipoBillete.IDA, Era.ROMA)));
        var vuelta = calculadora.crearBillete(TipoBillete.VUELTA, Era.ROMA);
        servicio.aplicarBilletes(List.of(vuelta));

        assertTrue(servicio.estaEnPresente());
        assertFalse(billetesVuelta.tiene(Era.ROMA));
    }
}
