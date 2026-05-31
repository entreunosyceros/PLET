package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.aventura.CatalogoAventuras;
import com.paradmanana.viajes.dominio.aventura.CatalogoObjetos;
import com.paradmanana.viajes.dominio.aventura.EfectosOpcion;
import com.paradmanana.viajes.dominio.aventura.EscenaAventura;
import com.paradmanana.viajes.dominio.aventura.EscenaBloqueada;
import com.paradmanana.viajes.dominio.aventura.ObjetoTemporal;
import com.paradmanana.viajes.dominio.aventura.OpcionAventura;
import com.paradmanana.viajes.dominio.aventura.RangoCredito;
import com.paradmanana.viajes.dominio.aventura.TipoParadoja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Economía circular temporal: Gallifantes Temporales, inventario, museo, reventa y consumibles.
 */
@Service
@SessionScope
public class ServicioAventuras {

    private static final int DESCUENTO_AGENTE_VETERANO = 5;
    private static final String TITULO_BASE = "Viajero temporal";
    private static final String TITULO_VETERANO = "Agente Veterano del Tiempo";

    private final ServicioPosicionViajero servicioPosicion;
    private int creditos;
    private final Set<String> inventario = new LinkedHashSet<>();
    private final Set<String> escenasCompletadas = new LinkedHashSet<>();
    private boolean paradojaActiva;
    private TipoParadoja tipoParadojaActiva;
    private int descuentoCompraPorcentaje;

    @Autowired
    public ServicioAventuras(ServicioPosicionViajero servicioPosicion) {
        this.servicioPosicion = servicioPosicion;
    }

    ServicioAventuras(ServicioPosicionViajero servicioPosicion, int creditosIniciales) {
        this.servicioPosicion = servicioPosicion;
        this.creditos = creditosIniciales;
    }

    public int obtenerCreditos() {
        return creditos;
    }

    public List<String> obtenerInventarioIds() {
        return List.copyOf(inventario);
    }

    public List<ObjetoTemporal> obtenerInventario() {
        return inventario.stream()
                .map(CatalogoObjetos::buscarPorId)
                .flatMap(Optional::stream)
                .toList();
    }

    public boolean tieneObjeto(String objetoId) {
        return inventario.contains(objetoId);
    }

    public boolean tieneParadojaActiva() {
        return paradojaActiva;
    }

    public Optional<TipoParadoja> obtenerTipoParadojaActiva() {
        return Optional.ofNullable(tipoParadojaActiva);
    }

    public double obtenerAmplitudMariposaParadoja() {
        return tipoParadojaActiva != null
                ? tipoParadojaActiva.getAmplitudMariposa()
                : 0.01;
    }

    public int obtenerRecargoParadojaPorcentaje() {
        return tipoParadojaActiva != null ? tipoParadojaActiva.getRecargoPorcentaje() : 0;
    }

    public String obtenerMensajeParadojaPanel() {
        return tipoParadojaActiva != null ? tipoParadojaActiva.getMensajePanel() : null;
    }

    public void resolverParadoja() {
        paradojaActiva = false;
        tipoParadojaActiva = null;
    }

    public int obtenerDescuentoCompraPorcentaje() {
        return descuentoCompraPorcentaje;
    }

    public int obtenerDescuentoPermanentePorcentaje() {
        return coleccionMuseoCompleta() ? DESCUENTO_AGENTE_VETERANO : 0;
    }

    public String obtenerTituloAgente() {
        if (paradojaActiva && tipoParadojaActiva != null) {
            if (coleccionMuseoCompleta()) {
                return TITULO_VETERANO + " (bajo sospecha)";
            }
            return tipoParadojaActiva.getTituloAgente();
        }
        if (coleccionMuseoCompleta()) {
            return TITULO_VETERANO;
        }
        RangoCredito rango = obtenerRangoCredito();
        if (rango != RangoCredito.BRONCE) {
            return rango.getTitulo();
        }
        return TITULO_BASE;
    }

    public RangoCredito obtenerRangoCredito() {
        return RangoCredito.porCreditos(creditos);
    }

    public int obtenerProgresoRangoCredito() {
        return RangoCredito.progresoHaciaSiguiente(creditos);
    }

    /** Sin GT, inventario ni aventuras jugadas: aún no ha empezado la economía temporal. */
    public boolean esViajeroSinExperiencia() {
        return creditos == 0
                && inventario.isEmpty()
                && escenasCompletadas.isEmpty()
                && descuentoCompraPorcentaje == 0
                && !paradojaActiva;
    }

    public boolean tieneSalonVip() {
        return obtenerRangoCredito().tieneSalonVip();
    }

    public boolean tieneNavDorado() {
        return obtenerRangoCredito().tieneNavDorado();
    }

    public boolean coleccionMuseoCompleta() {
        return CatalogoObjetos.coleccionMuseoCompleta(inventario);
    }

    public boolean escenaCompletada(String escenaId) {
        return escenasCompletadas.contains(escenaId);
    }

    public List<EscenaAventura> listarJugablesEnPosicion() {
        return servicioPosicion.obtenerEraActual()
                .map(this::listarJugablesEnEra)
                .orElse(List.of());
    }

    public List<EscenaBloqueada> listarBloqueadasEnPosicion() {
        return servicioPosicion.obtenerEraActual()
                .map(this::listarBloqueadasEnEra)
                .orElse(List.of());
    }

    public List<EscenaAventura> listarJugablesEnEra(Era era) {
        List<EscenaAventura> jugables = new ArrayList<>();
        for (EscenaAventura escena : CatalogoAventuras.porEra(era)) {
            if (puedeJugar(escena)) {
                jugables.add(escena);
            }
        }
        return jugables;
    }

    public List<EscenaBloqueada> listarBloqueadasEnEra(Era era) {
        List<EscenaBloqueada> bloqueadas = new ArrayList<>();
        for (EscenaAventura escena : CatalogoAventuras.porEra(era)) {
            if (servicioPosicion.estaEnEra(era) && !escenasCompletadas.contains(escena.id()) && !puedeJugar(escena)) {
                bloqueadas.add(new EscenaBloqueada(escena, motivoBloqueo(escena)));
            }
        }
        return bloqueadas;
    }

    public List<EscenaAventura> listarCompletadas() {
        return CatalogoAventuras.todas().stream()
                .filter(e -> escenasCompletadas.contains(e.id()))
                .toList();
    }

    public List<ObjetoTemporal> listarConsumiblesEnInventario() {
        return obtenerInventario().stream()
                .filter(ObjetoTemporal::esConsumible)
                .toList();
    }

    public List<ObjetoTemporal> listarVendiblesEnInventario() {
        return obtenerInventario().stream()
                .filter(ObjetoTemporal::esVendible)
                .toList();
    }

    public boolean puedeJugar(EscenaAventura escena) {
        if (escenasCompletadas.contains(escena.id())) {
            return false;
        }
        if (!servicioPosicion.estaEnEra(escena.era())) {
            return false;
        }
        return cumpleRequisitoObjeto(escena);
    }

    private boolean cumpleRequisitoObjeto(EscenaAventura escena) {
        return escena.objetoRequerido()
                .map(inventario::contains)
                .orElse(true);
    }

    private String motivoBloqueo(EscenaAventura escena) {
        if (escena.objetoRequerido().isPresent()) {
            String id = escena.objetoRequerido().get();
            String nombre = CatalogoObjetos.buscarPorId(id)
                    .map(ObjetoTemporal::nombre)
                    .orElse(id);
            return "Necesitas el objeto «" + nombre + "» en tu inventario.";
        }
        return "No disponible.";
    }

    public Optional<ResultadoEleccionAventura> elegir(String escenaId, String opcionId) {
        Optional<EscenaAventura> escenaOpt = CatalogoAventuras.buscarPorId(escenaId);
        if (escenaOpt.isEmpty() || !puedeJugar(escenaOpt.get())) {
            return Optional.empty();
        }
        EscenaAventura escena = escenaOpt.get();
        Optional<OpcionAventura> opcionOpt = escena.buscarOpcion(opcionId);
        if (opcionOpt.isEmpty()) {
            return Optional.empty();
        }

        aplicarEfectos(opcionOpt.get().efectos());
        escenasCompletadas.add(escena.id());

        return Optional.of(new ResultadoEleccionAventura(
                escena,
                opcionOpt.get(),
                creditos,
                paradojaActiva,
                descuentoCompraPorcentaje,
                Optional.ofNullable(tipoParadojaActiva)
        ));
    }

    /** Vende un objeto en el presente (casa de empeños temporal). */
    public Optional<Integer> venderObjeto(String objetoId) {
        if (!servicioPosicion.estaEnPresente()) {
            return Optional.empty();
        }
        Optional<ObjetoTemporal> objeto = CatalogoObjetos.buscarPorId(objetoId);
        if (objeto.isEmpty() || !inventario.contains(objetoId) || !objeto.get().esVendible()) {
            return Optional.empty();
        }
        inventario.remove(objetoId);
        int precio = objeto.get().precioReventa();
        creditos += precio;
        return Optional.of(precio);
    }

    /** Consume un objeto consumible y activa su descuento en el carrito. */
    public Optional<Integer> aplicarConsumible(String objetoId) {
        Optional<ObjetoTemporal> objeto = CatalogoObjetos.buscarPorId(objetoId);
        if (objeto.isEmpty() || !inventario.contains(objetoId) || !objeto.get().esConsumible()) {
            return Optional.empty();
        }
        inventario.remove(objetoId);
        int descuento = objeto.get().descuentoConsumible();
        descuentoCompraPorcentaje = Math.max(descuentoCompraPorcentaje, descuento);
        return Optional.of(descuento);
    }

    private void aplicarEfectos(EfectosOpcion efectos) {
        creditos += efectos.cambioCreditos();
        if (efectos.activarParadoja()) {
            paradojaActiva = true;
            tipoParadojaActiva = efectos.tipoParadoja().orElse(TipoParadoja.EFECTO_MARIPOSA);
        }
        if (efectos.descuentoProximaCompraPorcentaje() > 0) {
            descuentoCompraPorcentaje = Math.max(
                    descuentoCompraPorcentaje,
                    efectos.descuentoProximaCompraPorcentaje()
            );
        }
        efectos.objetoInventario().ifPresent(inventario::add);
        efectos.destinoForzado().ifPresent(servicioPosicion::teletransportarA);
        if (efectos.regresarAlPresente()) {
            servicioPosicion.regresarAlPresente();
        }
    }

    public ResumenCarrito aplicarBeneficiosAlResumen(ResumenCarrito base) {
        ResumenCarrito conParadoja = aplicarRecargoParadoja(base);
        BigDecimal total = conParadoja.totalAntesBeneficios();
        BigDecimal descuentoPermanente = BigDecimal.ZERO;
        BigDecimal descuentoPorcentaje = BigDecimal.ZERO;

        int descPerm = obtenerDescuentoPermanentePorcentaje();
        if (descPerm > 0) {
            descuentoPermanente = total
                    .multiply(BigDecimal.valueOf(descPerm))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            total = total.subtract(descuentoPermanente);
        }

        if (descuentoCompraPorcentaje > 0) {
            descuentoPorcentaje = total
                    .multiply(BigDecimal.valueOf(descuentoCompraPorcentaje))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            total = total.subtract(descuentoPorcentaje);
        }

        BigDecimal descuentoCreditos = BigDecimal.ZERO;
        if (creditos > 0 && total.compareTo(BigDecimal.ZERO) > 0) {
            descuentoCreditos = total.min(BigDecimal.valueOf(creditos));
            total = total.subtract(descuentoCreditos);
        }

        return conParadoja.conBeneficiosAventura(
                descuentoPermanente,
                descuentoPorcentaje,
                descuentoCreditos,
                total.max(BigDecimal.ZERO)
        );
    }

    private ResumenCarrito aplicarRecargoParadoja(ResumenCarrito base) {
        if (!paradojaActiva || tipoParadojaActiva == null) {
            return base;
        }
        BigDecimal baseRecargo = base.subtotalBilletes().add(base.totalRecargosSoloIda());
        if (baseRecargo.compareTo(BigDecimal.ZERO) <= 0) {
            return base;
        }
        BigDecimal recargo = baseRecargo
                .multiply(BigDecimal.valueOf(tipoParadojaActiva.getRecargoPorcentaje()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return base.conRecargoParadoja(recargo, tipoParadojaActiva.getMensajeCarrito());
    }

    public void consumirCreditosAplicados(BigDecimal importeUsado) {
        if (importeUsado.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        creditos = Math.max(0, creditos - importeUsado.intValue());
    }

    public void consumirDescuentoCompra() {
        descuentoCompraPorcentaje = 0;
    }
}
