package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Carrito;
import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.TipoBillete;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Fachada del carrito de compra por sesión HTTP.
 * <p>
 * Cada usuario tiene su propio carrito ({@code @SessionScope}). Antes de añadir
 * un billete, delega en {@link ValidadorParadojas} para evitar inconsistencias
 * temporales (vuelta sin ida, orden cronológico imposible, etc.).
 * </p>
 */
@Service
@SessionScope
public class ServicioCarrito {

    /** Carrito en memoria asociado a la sesión del navegador. */
    private final Carrito carrito = new Carrito();
    private final CalculadoraPrecios calculadora;
    private final ValidadorParadojas validador;
    private final CalculadorCarrito calculadorCarrito;
    private final ServicioAventuras servicioAventuras;

    public ServicioCarrito(CalculadoraPrecios calculadora, ValidadorParadojas validador,
                           CalculadorCarrito calculadorCarrito, ServicioAventuras servicioAventuras) {
        this.calculadora = calculadora;
        this.validador = validador;
        this.calculadorCarrito = calculadorCarrito;
        this.servicioAventuras = servicioAventuras;
    }

    public Carrito obtenerCarrito() {
        return carrito;
    }

    /**
     * Crea el billete, valida paradojas y lo añade al carrito si es coherente.
     *
     * @return {@code true} si se añadió; {@code false} si el validador rechazó la operación
     */
    public boolean agregarBillete(TipoBillete tipo, Era era) {
        Billete billete = calculadora.crearBillete(tipo, era);
        var resultado = validador.validarAntesDeAgregar(carrito, billete);

        if (!resultado.valido()) {
            carrito.registrarError(resultado.mensaje());
            return false;
        }

        carrito.agregar(billete);
        return true;
    }

    public void eliminarBillete(String identificador) {
        carrito.eliminar(identificador);
    }

    public void vaciar() {
        carrito.vaciar();
    }

    /** Validación completa antes de permitir el pago. */
    public ValidadorParadojas.ResultadoValidacion validarCompra() {
        return validador.validarCarritoCompleto(carrito);
    }

    /** Subtotal, recargos por solo ida, beneficios de aventura y total final. */
    public ResumenCarrito obtenerResumen() {
        ResumenCarrito base = calculadorCarrito.calcularResumen(carrito);
        return servicioAventuras.aplicarBeneficiosAlResumen(base);
    }

    /** Descuenta créditos, consume el descuento % y liquida paradojas tras un pago confirmado. */
    public void aplicarBeneficiosTrasCompra(ResumenCarrito resumen) {
        servicioAventuras.consumirCreditosAplicados(resumen.descuentoCreditosAventura());
        if (resumen.descuentoPorcentajeAventura().compareTo(java.math.BigDecimal.ZERO) > 0) {
            servicioAventuras.consumirDescuentoCompra();
        }
        if (resumen.tieneRecargoParadoja()) {
            servicioAventuras.resolverParadoja();
        }
    }

    public CalculadoraPrecios obtenerCalculadora() {
        return calculadora;
    }

    public java.util.List<com.paradmanana.viajes.dominio.aventura.ObjetoTemporal> obtenerConsumiblesDisponibles() {
        return servicioAventuras.listarConsumiblesEnInventario();
    }

    public java.util.Optional<Integer> aplicarConsumible(String objetoId) {
        return servicioAventuras.aplicarConsumible(objetoId);
    }
}
