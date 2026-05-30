package com.paradmanana.viajes.dominio.aventura;

import java.util.Optional;

/**
 * Hitos de riqueza temporal. Al acumular Gallifantes Temporales el viajero desbloquea títulos y ventajas cosméticas.
 */
public enum RangoCredito {

    BRONCE(0, "Viajero temporal", null,
            "Empiezas con buena fe y cartera vacía."),
    PLATA(100, "Carterista temporal certificado", "bronce-chispas",
            "Has demostrado solvencia interdimensional mínima. El contador de Gallifantes Temporales brilla con orgullo."),
    ORO(250, "Magnate del cronodólar", "nav-dorado",
            "La barra de navegación adopta un tinte dorado. No es oro real, pero impresiona en las tabernas."),
    PLATINUM(500, "Miembro Platinum del Salón VIP", "salon-vip",
            "Desbloqueas el Salón VIP del Tiempo en el menú. Champán holográfico incluido (sin burbujas)."),
    LEYENDA(1000, "Leyenda con liquidez interdimensional", "salon-vip-leyenda",
            "Eres la envidia del multiverso. El salón VIP te trata como si fueras dueño del 51 % de la línea temporal.");

    private final int umbralMinimo;
    private final String titulo;
    private final String idPerk;
    private final String descripcionPerk;

    RangoCredito(int umbralMinimo, String titulo, String idPerk, String descripcionPerk) {
        this.umbralMinimo = umbralMinimo;
        this.titulo = titulo;
        this.idPerk = idPerk;
        this.descripcionPerk = descripcionPerk;
    }

    public int getUmbralMinimo() {
        return umbralMinimo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIdPerk() {
        return idPerk;
    }

    public String getDescripcionPerk() {
        return descripcionPerk;
    }

    public static RangoCredito porCreditos(int creditos) {
        RangoCredito actual = BRONCE;
        for (RangoCredito rango : values()) {
            if (creditos >= rango.umbralMinimo) {
                actual = rango;
            }
        }
        return actual;
    }

    public Optional<RangoCredito> siguiente() {
        RangoCredito[] todos = values();
        for (int i = 0; i < todos.length - 1; i++) {
            if (todos[i] == this) {
                return Optional.of(todos[i + 1]);
            }
        }
        return Optional.empty();
    }

    /** Porcentaje 0–100 hacia el siguiente hito (100 si ya estás en leyenda). */
    public static int progresoHaciaSiguiente(int creditos) {
        RangoCredito actual = porCreditos(creditos);
        Optional<RangoCredito> next = actual.siguiente();
        if (next.isEmpty()) {
            return 100;
        }
        int piso = actual.umbralMinimo;
        int techo = next.get().umbralMinimo;
        if (techo <= piso) {
            return 100;
        }
        int progreso = (int) Math.round(100.0 * (creditos - piso) / (techo - piso));
        return Math.max(0, Math.min(100, progreso));
    }

    public boolean tieneSalonVip() {
        return umbralMinimo >= PLATINUM.umbralMinimo;
    }

    public boolean tieneNavDorado() {
        return umbralMinimo >= ORO.umbralMinimo;
    }
}
