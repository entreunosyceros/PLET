package com.paradmanana.viajes.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnoTemporalTest {

    @Test
    void anosNegativosSonAnterioresAAnosPositivos() {
        AnoTemporal roma = AnoTemporal.de(-117);
        AnoTemporal presente = AnoTemporal.de(2026);
        AnoTemporal futuro = AnoTemporal.de(3000);

        assertTrue(roma.esAnteriorA(presente));
        assertTrue(presente.esAnteriorA(futuro));
        assertTrue(AnoTemporal.de(-66_000_000).esAnteriorA(roma));
    }

    @Test
    void anoCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> AnoTemporal.de(0));
    }

    @Test
    void toStringFormateaAcDc() {
        assertEquals("117 a.C.", AnoTemporal.de(-117).toString());
        assertEquals("3000 d.C.", AnoTemporal.de(3000).toString());
    }

    @Test
    void formateoConSeparadoresParaEpocasPrehistoricas() {
        assertEquals("117 a.C.", AnoTemporal.de(-117).formatear());
        assertTrue(AnoTemporal.de(-66_000_000).formatear().contains("66.000.000"));
    }

    @Test
    void distanciaAbsolutaEntreEpocas() {
        AnoTemporal presente = AnoTemporal.de(2026);
        AnoTemporal roma = AnoTemporal.de(-117);
        assertEquals(2143, presente.distanciaAbsoluta(roma));
    }
}
