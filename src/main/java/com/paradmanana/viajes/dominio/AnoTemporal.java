package com.paradmanana.viajes.dominio;

/**
 * Tipo fuerte para años en la línea temporal.
 * Encapsula la excepción del año 0 y el formato a.C./d.C.
 */
public record AnoTemporal(int valor) implements Comparable<AnoTemporal> {

    public AnoTemporal {
        if (valor == 0) {
            throw new IllegalArgumentException(
                    "El año 0 no existe en la continuidad espacio-temporal.");
        }
    }

    public static AnoTemporal de(int valor) {
        return new AnoTemporal(valor);
    }

    public int distanciaAbsoluta(AnoTemporal otro) {
        // Los años a.C. son negativos, pero la distancia siempre es positiva
        return Math.abs(this.valor - otro.valor);
    }

    /** Compara en la línea temporal: -66M a.C. es anterior a -117 a.C. */
    public boolean esAnteriorA(AnoTemporal otro) {
        return this.valor < otro.valor;
    }

    public boolean esPosteriorA(AnoTemporal otro) {
        return this.valor > otro.valor;
    }

    /** Formato legible con separadores de miles para épocas prehistóricas. */
    public String formatear() {
        if (valor < 0) {
            return formatearNumero(Math.abs(valor)) + " a.C.";
        }
        return formatearNumero(valor) + " d.C.";
    }

    private static String formatearNumero(int numero) {
        if (numero >= 1_000_000) {
            return String.format("%,d", numero).replace(',', '.');
        }
        return String.valueOf(numero);
    }

    @Override
    public String toString() {
        return valor < 0 ? Math.abs(valor) + " a.C." : valor + " d.C.";
    }

    @Override
    public int compareTo(AnoTemporal otro) {
        return Integer.compare(this.valor, otro.valor);
    }
}
