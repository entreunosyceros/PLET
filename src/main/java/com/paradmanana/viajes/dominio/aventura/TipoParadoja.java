package com.paradmanana.viajes.dominio.aventura;

/**
 * Consecuencias mecánicas de una paradoja temporal activada en aventuras.
 * Persisten en sesión hasta el viajero pague el recargo de estabilización en el carrito.
 */
public enum TipoParadoja {

    EFECTO_MARIPOSA(
            "Línea temporal inestable",
            """
            Has alterado el pasado de forma irreversible. El seguro Efecto Mariposa \
            oscila con el triple de intensidad y la agencia te cobrará un recargo de \
            estabilización en tu próximo billete.
            """.trim(),
            """
            Recargo por inestabilidad de línea temporal (efecto mariposa descontrolado). \
            Se liquida al confirmar este pedido.
            """.trim(),
            20,
            0.03
    ),

    PARADOJA_GENETICA(
            "Existencia dudosa",
            """
            Tu negativa ha deshilachado tu continuidad en la línea temporal. La agencia \
            te exige un recargo de estabilización genética antes del próximo salto.
            """.trim(),
            """
            Recargo por paradoja genealógica. Tus documentos temporales requieren \
            revalidación antes del siguiente viaje.
            """.trim(),
            25,
            0.03
    ),

    PARADOJA_HISTORICA(
            "Contaminador cronológico",
            """
            Has introducido anacronismos en una época catalogada. El Senado Temporal \
            ha abierto expediente y tu seguro Mariposa ya no fiaba en ti desde antes.
            """.trim(),
            """
            Recargo por alteración del registro histórico. Incluye tasas administrativas \
            del Pretorio de la Línea Temporal.
            """.trim(),
            22,
            0.03
    );

    private final String tituloAgente;
    private final String mensajePanel;
    private final String mensajeCarrito;
    private final int recargoPorcentaje;
    private final double amplitudMariposa;

    TipoParadoja(String tituloAgente, String mensajePanel, String mensajeCarrito,
                 int recargoPorcentaje, double amplitudMariposa) {
        this.tituloAgente = tituloAgente;
        this.mensajePanel = mensajePanel;
        this.mensajeCarrito = mensajeCarrito;
        this.recargoPorcentaje = recargoPorcentaje;
        this.amplitudMariposa = amplitudMariposa;
    }

    public String getTituloAgente() {
        return tituloAgente;
    }

    public String getMensajePanel() {
        return mensajePanel;
    }

    public String getMensajeCarrito() {
        return mensajeCarrito;
    }

    public int getRecargoPorcentaje() {
        return recargoPorcentaje;
    }

    public double getAmplitudMariposa() {
        return amplitudMariposa;
    }
}
