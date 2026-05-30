package com.paradmanana.viajes.dominio;

/**
 * Sentido del viaje respecto al presente configurado en {@link LineaTemporal}.
 */
public enum TipoBillete {
    /** Salto desde el año presente hacia la época elegida. */
    IDA("Ida", "Presente → Época"),
    /** Regreso desde la época al año presente. */
    VUELTA("Vuelta", "Época → Presente");

    private final String etiqueta;
    private final String descripcion;

    TipoBillete(String etiqueta, String descripcion) {
        this.etiqueta = etiqueta;
        this.descripcion = descripcion;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
