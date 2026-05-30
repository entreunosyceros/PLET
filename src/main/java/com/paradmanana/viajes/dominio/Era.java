package com.paradmanana.viajes.dominio;

import java.util.List;

/**
 * Catálogo de destinos temporales vendibles.
 * <p>
 * Cada era define su año representativo, índice de riesgo (afecta al precio),
 * metadatos visuales y contenido de humor (advertencias, curiosidades, restricciones).
 * </p>
 */
public enum Era {

    DINOSAURIOS(
            "Era de los Dinosaurios",
            "Cretácico Superior — convive con T-Rex (bajo tu responsabilidad)",
            "Donde el seguro de vida es opcional",
            -66_000_000,
            9.8,
            "🦖",
            "#2d5016",
            List.of(
                    "No alimentar a los T-Rex.",
                    "Si oye un crujido en la maleza, no es su móvil vibrando."
            ),
            List.of(
                    "El oxígeno era ~30 % mayor: correrá más, sudará más, lamentará más.",
                    "Ningún dinosaurio ha firmado aún la carta de bienvenida del hotel."
            ),
            List.of(
                    "Prohibido recoger huevos como souvenir (aquí aún no son fósiles).",
                    "Máximo 1 selfie con depredador apex por grupo."
            )
    ),
    ROMA(
            "Antigua Roma",
            "Imperio en su apogeo — foro, gladiadores y togas",
            "SPQR — Sin Paradojas, Quick Return",
            -117,
            4.2,
            "🏛️",
            "#8b4513",
            List.of(
                    "No revelar tecnologías futuras.",
                    "No corregir la pronunciación del latín a los locales."
            ),
            List.of(
                    "El pan de molde llegará… dentro de un par de milenios.",
                    "Los gladiadores no aceptan propinas en PayPal."
            ),
            List.of(
                    "Toga obligatoria en el Foro (préstamo en taquilla: 12 denarios).",
                    "Prohibido decir «Roma no se construyó en un día» — ya lo saben."
            )
    ),
    ANO_3000(
            "Año 3000",
            "Civilización post-humana con hoverboards regulados",
            "El futuro con papeles en regla",
            3000,
            6.5,
            "🚀",
            "#0066cc",
            List.of(
                    "Evite mencionar criptomonedas del siglo XXI.",
                    "No pregunte si el hoverboard «ya es legal en su época»."
            ),
            List.of(
                    "El café sigue siendo caro. Al menos ahora explican por qué.",
                    "Los robots tienen derecho a pausa; usted, no tanto."
            ),
            List.of(
                    "Ropa «vintage 2020» solo en zonas turísticas habilitadas.",
                    "Importación de memes antiguos sujeta a aranceles temporales."
            )
    );

    private final String nombre;
    private final String descripcion;
    private final String lema;
    private final int anoRepresentativo;
    private final double indiceRiesgo;
    private final String emoji;
    private final String color;
    private final List<String> advertencias;
    private final List<String> curiosidades;
    private final List<String> restricciones;

    Era(String nombre, String descripcion, String lema, int anoRepresentativo,
        double indiceRiesgo, String emoji, String color,
        List<String> advertencias, List<String> curiosidades, List<String> restricciones) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.lema = lema;
        this.anoRepresentativo = anoRepresentativo;
        this.indiceRiesgo = indiceRiesgo;
        this.emoji = emoji;
        this.color = color;
        this.advertencias = List.copyOf(advertencias);
        this.curiosidades = List.copyOf(curiosidades);
        this.restricciones = List.copyOf(restricciones);
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /** Frase corta que resume el tono de la era en catálogo y ficha. */
    public String getLema() {
        return lema;
    }

    public int getAnoRepresentativo() {
        return anoRepresentativo;
    }

    public AnoTemporal getAnoTemporal() {
        return AnoTemporal.de(anoRepresentativo);
    }

    public double getIndiceRiesgo() {
        return indiceRiesgo;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getColor() {
        return color;
    }

    public List<String> getAdvertencias() {
        return advertencias;
    }

    public List<String> getCuriosidades() {
        return curiosidades;
    }

    public List<String> getRestricciones() {
        return restricciones;
    }

    /** Identificador de URL: DINOSAURIOS, ROMA, ANO_3000. */
    public String getCodigo() {
        return name();
    }

    /** Slug CSS: dinosaurios, roma, ano-3000. */
    public String getClaseCss() {
        return switch (this) {
            case DINOSAURIOS -> "dinosaurios";
            case ROMA -> "roma";
            case ANO_3000 -> "ano-3000";
        };
    }
}
