package com.paradmanana.viajes.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineaTemporalTest {

    @Test
    void anho2024EsPasadoRespectoAlPresente() {
        LineaTemporal linea = new LineaTemporal(2026);
        assertTrue(linea.esPasadoRespectoAlPresente(AnoTemporal.de(2024)));
        assertFalse(linea.esPasadoRespectoAlPresente(AnoTemporal.de(3000)));
    }

    @Test
    void presenteCoincideConAnhoActual() {
        LineaTemporal linea = new LineaTemporal(2026);
        assertTrue(linea.esAnoPresente(linea.presente()));
        assertEquals(LineaTemporal.ANHO_ACTUAL, linea.getAnhoActual());
    }
}
