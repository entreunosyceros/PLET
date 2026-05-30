package com.paradmanana.viajes.configuracion;

import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.LineaTemporal;
import com.paradmanana.viajes.dominio.NormasPresente;
import com.paradmanana.viajes.servicio.ServicioAventuras;
import com.paradmanana.viajes.servicio.ServicioBilletesVuelta;
import com.paradmanana.viajes.servicio.ServicioCarrito;
import com.paradmanana.viajes.servicio.ServicioPosicionViajero;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Inyecta atributos comunes en todas las vistas Thymeleaf.
 * <p>
 * Incluye el contador del carrito y la posición temporal del viajero en sesión.
 * </p>
 */
@ControllerAdvice
public class AtributosGlobales {

    private final ServicioCarrito servicioCarrito;
    private final ServicioPosicionViajero servicioPosicion;
    private final ServicioBilletesVuelta servicioBilletesVuelta;
    private final ServicioAventuras servicioAventuras;
    private final LineaTemporal lineaTemporal;

    public AtributosGlobales(ServicioCarrito servicioCarrito,
                             ServicioPosicionViajero servicioPosicion,
                             ServicioBilletesVuelta servicioBilletesVuelta,
                             ServicioAventuras servicioAventuras,
                             LineaTemporal lineaTemporal) {
        this.servicioCarrito = servicioCarrito;
        this.servicioPosicion = servicioPosicion;
        this.servicioBilletesVuelta = servicioBilletesVuelta;
        this.servicioAventuras = servicioAventuras;
        this.lineaTemporal = lineaTemporal;
    }

    @ModelAttribute("cantidadCarrito")
    public int cantidadCarrito() {
        return servicioCarrito.obtenerCarrito().obtenerCantidad();
    }

    @ModelAttribute("posicionViajero")
    public String posicionViajero() {
        return servicioPosicion.obtenerEtiquetaPosicion();
    }

    @ModelAttribute("enPresente")
    public boolean enPresente() {
        return servicioPosicion.estaEnPresente();
    }

    @ModelAttribute("anoPresenteFormateado")
    public String anoPresenteFormateado() {
        return lineaTemporal.presente().formatear();
    }

    @ModelAttribute("normasPresente")
    public NormasPresente normasPresente() {
        return NormasPresente.porDefecto();
    }

    @ModelAttribute("carrito")
    public com.paradmanana.viajes.dominio.Carrito carrito() {
        return servicioCarrito.obtenerCarrito();
    }

    /** Códigos de eras con billete de vuelta pagado y pendiente de usar. */
    @ModelAttribute("erasVueltaPrepagada")
    public Set<String> erasVueltaPrepagada() {
        return servicioBilletesVuelta.obtenerTodos().keySet().stream()
                .map(Era::getCodigo)
                .collect(Collectors.toSet());
    }

    /** Código de la era donde está el viajero, o {@code null} si está en el presente. */
    @ModelAttribute("eraViajeroCodigo")
    public String eraViajeroCodigo() {
        return servicioPosicion.obtenerEraActual()
                .map(Era::getCodigo)
                .orElse(null);
    }

    @ModelAttribute("creditosViajero")
    public int creditosViajero() {
        return servicioAventuras.obtenerCreditos();
    }

    @ModelAttribute("inventarioViajero")
    public java.util.List<com.paradmanana.viajes.dominio.aventura.ObjetoTemporal> inventarioViajero() {
        return servicioAventuras.obtenerInventario();
    }

    @ModelAttribute("tituloAgente")
    public String tituloAgente() {
        return servicioAventuras.obtenerTituloAgente();
    }

    @ModelAttribute("coleccionMuseoCompleta")
    public boolean coleccionMuseoCompleta() {
        return servicioAventuras.coleccionMuseoCompleta();
    }

    @ModelAttribute("descuentoPermanentePorcentaje")
    public int descuentoPermanentePorcentaje() {
        return servicioAventuras.obtenerDescuentoPermanentePorcentaje();
    }

    @ModelAttribute("paradojaActiva")
    public boolean paradojaActiva() {
        return servicioAventuras.tieneParadojaActiva();
    }

    @ModelAttribute("mensajeParadoja")
    public String mensajeParadoja() {
        return servicioAventuras.obtenerMensajeParadojaPanel();
    }

    @ModelAttribute("recargoParadojaPorcentaje")
    public int recargoParadojaPorcentaje() {
        return servicioAventuras.obtenerRecargoParadojaPorcentaje();
    }

    @ModelAttribute("descuentoAventuraPorcentaje")
    public int descuentoAventuraPorcentaje() {
        return servicioAventuras.obtenerDescuentoCompraPorcentaje();
    }

    @ModelAttribute("aventurasDisponibles")
    public int aventurasDisponibles() {
        return servicioAventuras.listarJugablesEnPosicion().size();
    }
}
