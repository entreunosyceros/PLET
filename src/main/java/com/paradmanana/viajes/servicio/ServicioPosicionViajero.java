package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.AnoTemporal;
import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Carrito;
import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.LineaTemporal;
import com.paradmanana.viajes.dominio.TipoBillete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Posición temporal del viajero en la sesión HTTP.
 * <p>
 * Tras confirmar una compra, solo las <strong>idas</strong> mueven al viajero.
 * Las vueltas compradas junto con la ida se guardan en {@link ServicioBilletesVuelta}
 * hasta que el usuario las utilice.
 * </p>
 */
@Service
@SessionScope
public class ServicioPosicionViajero {

    private final LineaTemporal lineaTemporal;
    private final ServicioBilletesVuelta servicioBilletesVuelta;
    private AnoTemporal posicionActual;
    private Era eraActual;

    @Autowired
    public ServicioPosicionViajero(LineaTemporal lineaTemporal,
                                   ServicioBilletesVuelta servicioBilletesVuelta) {
        this.lineaTemporal = lineaTemporal;
        this.servicioBilletesVuelta = servicioBilletesVuelta;
        reiniciar();
    }

    /** Constructor para tests sin almacén de vueltas. */
    ServicioPosicionViajero(LineaTemporal lineaTemporal) {
        this(lineaTemporal, new ServicioBilletesVuelta());
    }

    public void reiniciar() {
        posicionActual = lineaTemporal.presente();
        eraActual = null;
    }

    public AnoTemporal obtenerPosicion() {
        return posicionActual;
    }

    public Optional<Era> obtenerEraActual() {
        return Optional.ofNullable(eraActual);
    }

    public boolean estaEnPresente() {
        return lineaTemporal.esAnoPresente(posicionActual);
    }

    public boolean estaEnEra(Era era) {
        return eraActual == era;
    }

    public boolean puedeComprarIda() {
        return estaEnPresente();
    }

    /**
     * Comprar vuelta en el carrito: con ida en carrito (presente) o estando en la era,
     * siempre que no haya ya un billete de vuelta prepagado sin usar.
     */
    public boolean puedeComprarVuelta(Era era, Carrito carrito) {
        if (servicioBilletesVuelta.tiene(era)) {
            return false;
        }
        if (estaEnEra(era)) {
            return true;
        }
        return estaEnPresente() && carrito.tieneIda(era);
    }

    /** Usar un billete de vuelta ya pagado estando en esa época. */
    public boolean puedeUsarVueltaPrepagada(Era era) {
        return estaEnEra(era) && servicioBilletesVuelta.tiene(era);
    }

    public String obtenerPosicionFormateada() {
        return posicionActual.formatear();
    }

    public String obtenerEtiquetaPosicion() {
        if (estaEnPresente()) {
            return posicionActual.formatear() + " (presente)";
        }
        if (eraActual != null) {
            return eraActual.getEmoji() + " " + posicionActual.formatear();
        }
        return posicionActual.formatear();
    }

    /**
     * Ejecuta idas al confirmar la compra. Las vueltas compradas junto a una ida
     * en el mismo pedido se guardan; las vueltas solas (desde la era) se ejecutan ya.
     */
    public void aplicarBilletes(List<Billete> billetes) {
        Set<Era> erasConIdaEnCompra = billetes.stream()
                .filter(b -> b.tipo() == TipoBillete.IDA)
                .map(Billete::era)
                .collect(Collectors.toSet());

        for (Billete billete : billetes) {
            if (billete.tipo() == TipoBillete.IDA) {
                posicionActual = billete.anoDestino();
                eraActual = billete.era();
            } else if (erasConIdaEnCompra.contains(billete.era())) {
                servicioBilletesVuelta.guardar(billete);
            } else {
                regresarAlPresente();
            }
        }
    }

    /** Ejecuta un billete de vuelta prepagado y regresa al presente. */
    public void usarVueltaPrepagada(Era era) {
        if (!puedeUsarVueltaPrepagada(era)) {
            throw new IllegalStateException("No hay billete de vuelta prepagado para " + era);
        }
        servicioBilletesVuelta.consumir(era);
        regresarAlPresente();
    }

    /** Salto accidental de época provocado por una aventura. */
    public void teletransportarA(Era era) {
        posicionActual = era.getAnoTemporal();
        eraActual = era;
    }

    public void regresarAlPresente() {
        posicionActual = lineaTemporal.presente();
        eraActual = null;
    }
}
