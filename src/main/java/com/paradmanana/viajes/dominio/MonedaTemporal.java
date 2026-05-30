package com.paradmanana.viajes.dominio;

/**
 * Moneda virtual de la economía circular temporal.
 */
public final class MonedaTemporal {

    public static final String NOMBRE = "Gallifantes Temporales";
    public static final String SINGULAR = "Gallifante Temporal";
    public static final String ABREVIATURA = "GT";

    private MonedaTemporal() {
    }

    public static String formatear(int cantidad) {
        return cantidad + " " + ABREVIATURA;
    }

    public static String formatearConNombre(int cantidad) {
        if (cantidad == 1) {
            return "1 " + SINGULAR;
        }
        return cantidad + " " + NOMBRE;
    }
}
