package com.paradmanana.viajes.dominio.aventura;

import com.paradmanana.viajes.dominio.Era;

/**
 * Objeto coleccionable o consumible obtenido en aventuras.
 *
 * @param id                  identificador en inventario (sesión)
 * @param nombre              nombre visible
 * @param era                 era de origen
 * @param precioReventa       GT al vender en el presente (0 = no vendible)
 * @param descuentoConsumible % de descuento al consumir en el carrito (0 = no consumible)
 * @param coleccionable       cuenta para el Museo del Tiempo
 */
public record ObjetoTemporal(
        String id,
        String nombre,
        Era era,
        int precioReventa,
        int descuentoConsumible,
        boolean coleccionable
) {
    public boolean esConsumible() {
        return descuentoConsumible > 0;
    }

    public boolean esVendible() {
        return precioReventa > 0;
    }
}
