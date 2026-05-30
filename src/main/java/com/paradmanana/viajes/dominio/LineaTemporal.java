package com.paradmanana.viajes.dominio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Ancla temporal del sistema: el "ahora" desde el que se miden distancias y combustible.
 * <p>
 * Solo existen destinos del catálogo ({@link Era}). Un año como 2024 es pasado respecto
 * al presente (2026), pero no es un destino vendible: el sistema no permite viajar
 * a años arbitrarios, solo saltos a épocas históricas registradas.
 * </p>
 */
@Component
public class LineaTemporal {

    /** Valor por defecto documentado; la app usa {@code viajes.ano-presente} en runtime. */
    public static final int ANHO_ACTUAL = 2026;

    private final int anhoActual;

    public LineaTemporal(@Value("${viajes.ano-presente:" + ANHO_ACTUAL + "}") int anhoActual) {
        this.anhoActual = anhoActual;
    }

    public int getAnhoActual() {
        return anhoActual;
    }

    public AnoTemporal presente() {
        return AnoTemporal.de(anhoActual);
    }

    public boolean esAnoPresente(AnoTemporal ano) {
        return ano.valor() == anhoActual;
    }

    public boolean esPasadoRespectoAlPresente(AnoTemporal ano) {
        return ano.esAnteriorA(presente());
    }

    public boolean esFuturoRespectoAlPresente(AnoTemporal ano) {
        return ano.esPosteriorA(presente());
    }
}
