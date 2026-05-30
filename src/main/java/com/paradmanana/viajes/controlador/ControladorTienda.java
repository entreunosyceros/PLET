package com.paradmanana.viajes.controlador;

import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.LineaTemporal;
import com.paradmanana.viajes.dominio.NormasPresente;
import com.paradmanana.viajes.dominio.TipoBillete;
import com.paradmanana.viajes.servicio.CalculadoraPrecios;
import com.paradmanana.viajes.servicio.ServicioCarrito;
import com.paradmanana.viajes.servicio.ServicioPosicionViajero;
import com.paradmanana.viajes.vista.DestinoAnimacion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador MVC de la tienda de viajes temporales.
 */
@Controller
public class ControladorTienda {

    private final ServicioCarrito servicioCarrito;
    private final CalculadoraPrecios calculadora;
    private final LineaTemporal lineaTemporal;
    private final ServicioPosicionViajero servicioPosicion;

    public ControladorTienda(ServicioCarrito servicioCarrito, CalculadoraPrecios calculadora,
                             LineaTemporal lineaTemporal, ServicioPosicionViajero servicioPosicion) {
        this.servicioCarrito = servicioCarrito;
        this.calculadora = calculadora;
        this.lineaTemporal = lineaTemporal;
        this.servicioPosicion = servicioPosicion;
    }

    @GetMapping("/")
    public String inicio(Model modelo) {
        modelo.addAttribute("eras", Era.values());
        modelo.addAttribute("desgloses", java.util.Arrays.stream(Era.values())
                .map(calculadora::obtenerDesglose)
                .toList());
        modelo.addAttribute("desglosesVuelta", java.util.Arrays.stream(Era.values())
                .collect(java.util.stream.Collectors.toMap(
                        Era::getCodigo,
                        era -> calculadora.obtenerDesglose(era, TipoBillete.VUELTA))));
        modelo.addAttribute("carrito", servicioCarrito.obtenerCarrito());
        return "inicio";
    }

    @GetMapping("/era/{codigo}")
    public String detalleEra(@PathVariable String codigo, Model modelo) {
        Era era = Era.valueOf(codigo.toUpperCase());
        modelo.addAttribute("era", era);
        modelo.addAttribute("desglose", calculadora.obtenerDesglose(era));
        modelo.addAttribute("billeteIda", calculadora.crearBillete(TipoBillete.IDA, era));
        modelo.addAttribute("billeteVuelta", calculadora.crearBillete(TipoBillete.VUELTA, era));
        modelo.addAttribute("carrito", servicioCarrito.obtenerCarrito());
        return "era";
    }

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(
            @RequestParam TipoBillete tipo,
            @RequestParam Era era,
            @RequestParam(required = false) String origen,
            RedirectAttributes redireccion) {

        boolean agregado = servicioCarrito.agregarBillete(tipo, era);
        if (agregado) {
            redireccion.addFlashAttribute("mensajeExito",
                    "✓ Billete de " + tipo.getEtiqueta().toLowerCase()
                            + " a " + era.getNombre() + " añadido al carrito. "
                            + "Puedes seguir comprando o ir al carrito cuando quieras.");
        } else {
            servicioCarrito.obtenerCarrito().obtenerUltimoError().ifPresent(error ->
                    redireccion.addFlashAttribute("mensajeError", error));
        }
        return "redirect:" + resolverDestinoTrasAnadir(origen);
    }

    private String resolverDestinoTrasAnadir(String origen) {
        if (origen == null || origen.isBlank()) {
            return "/";
        }
        try {
            URI uri = URI.create(origen.startsWith("/") ? origen : "/" + origen);
            String ruta = uri.getPath();
            if (ruta != null && !ruta.isBlank()
                    && !ruta.startsWith("/compra") && !ruta.startsWith("/confirmacion")) {
                return ruta;
            }
        } catch (IllegalArgumentException ignored) {
            // origen inválido → catálogo
        }
        return "/";
    }

    @GetMapping("/carrito")
    public String verCarrito(Model modelo) {
        modelo.addAttribute("carrito", servicioCarrito.obtenerCarrito());
        modelo.addAttribute("resumen", servicioCarrito.obtenerResumen());
        modelo.addAttribute("validacion", servicioCarrito.validarCompra());
        modelo.addAttribute("consumibles", servicioCarrito.obtenerConsumiblesDisponibles());
        return "carrito";
    }

    @PostMapping("/carrito/usar-objeto")
    public String usarObjetoConsumible(@RequestParam String objetoId, RedirectAttributes redireccion) {
        var descuento = servicioCarrito.aplicarConsumible(objetoId);
        if (descuento.isEmpty()) {
            redireccion.addFlashAttribute("mensajeError", "No se pudo aplicar ese objeto.");
        } else {
            redireccion.addFlashAttribute("mensajeExito",
                    "🏷️ Objeto consumido. Descuento del " + descuento.get() + " % aplicado a este pedido.");
        }
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/eliminar/{identificador}")
    public String eliminarBillete(@PathVariable String identificador, RedirectAttributes redireccion) {
        servicioCarrito.eliminarBillete(identificador);
        redireccion.addFlashAttribute("mensajeExito", "Billete eliminado.");
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/vaciar")
    public String vaciarCarrito(RedirectAttributes redireccion) {
        servicioCarrito.vaciar();
        redireccion.addFlashAttribute("mensajeExito", "Carrito vaciado. Línea temporal restablecida.");
        return "redirect:/carrito";
    }

    @PostMapping("/compra/confirmar")
    public String confirmarCompra(RedirectAttributes redireccion) {
        var validacion = servicioCarrito.validarCompra();
        if (!validacion.valido()) {
            redireccion.addFlashAttribute("mensajeError", validacion.mensaje());
            return "redirect:/carrito";
        }
        if (servicioCarrito.obtenerCarrito().obtenerCantidad() == 0) {
            redireccion.addFlashAttribute("mensajeError", "El carrito está vacío.");
            return "redirect:/carrito";
        }
        var resumen = servicioCarrito.obtenerResumen();
        var billetes = new ArrayList<>(servicioCarrito.obtenerCarrito().obtenerBilletes());
        redireccion.addFlashAttribute("anoInicioSalto", servicioPosicion.obtenerPosicion().valor());
        redireccion.addFlashAttribute("total", resumen.totalFinal());
        redireccion.addFlashAttribute("subtotal", resumen.subtotalBilletes());
        redireccion.addFlashAttribute("recargosSoloIda", resumen.recargosSoloIda());
        redireccion.addFlashAttribute("totalRecargos", resumen.totalRecargosSoloIda());
        redireccion.addFlashAttribute("descuentoAventura", resumen.descuentoPorcentajeAventura());
        redireccion.addFlashAttribute("descuentoCreditos", resumen.descuentoCreditosAventura());
        redireccion.addFlashAttribute("billetes", billetes);
        boolean idaYVuelta = billetes.stream().anyMatch(b -> b.tipo() == TipoBillete.IDA)
                && billetes.stream().anyMatch(b -> b.tipo() == TipoBillete.VUELTA);
        redireccion.addFlashAttribute("mostrarAvisoVueltaPrepagada", idaYVuelta);
        boolean soloVueltas = !billetes.isEmpty()
                && billetes.stream().allMatch(b -> b.tipo() == TipoBillete.VUELTA);
        if (soloVueltas) {
            redireccion.addFlashAttribute("soloVuelta", true);
            redireccion.addFlashAttribute("esConfirmacionViaje", true);
        }
        servicioCarrito.aplicarBeneficiosTrasCompra(resumen);
        servicioPosicion.aplicarBilletes(billetes);
        servicioCarrito.vaciar();
        return "redirect:/confirmacion";
    }

    @PostMapping("/viaje/usar-vuelta")
    public String usarVueltaPrepagada(@RequestParam Era era, RedirectAttributes redireccion) {
        if (!servicioPosicion.puedeUsarVueltaPrepagada(era)) {
            redireccion.addFlashAttribute("mensajeError",
                    "No tienes un billete de vuelta prepagado para usar desde "
                            + era.getNombre() + ".");
            return "redirect:/";
        }
        int anoOrigen = servicioPosicion.obtenerPosicion().valor();
        servicioPosicion.usarVueltaPrepagada(era);
        var presente = lineaTemporal.presente();
        redireccion.addFlashAttribute("esConfirmacionViaje", true);
        redireccion.addFlashAttribute("soloVuelta", true);
        redireccion.addFlashAttribute("eraSalida", era);
        redireccion.addFlashAttribute("anoInicioSalto", anoOrigen);
        redireccion.addFlashAttribute("destinosViaje", List.of(
                DestinoAnimacion.regresoPresente(era, lineaTemporal)
        ));
        redireccion.addFlashAttribute("mensajeExito",
                "⏳ Has regresado al presente (" + presente.formatear() + ").");
        return "redirect:/confirmacion";
    }

    @GetMapping("/confirmacion")
    public String confirmacion(Model modelo) {
        boolean esViaje = modelo.containsAttribute("total")
                || modelo.containsAttribute("esConfirmacionViaje");
        if (!esViaje) {
            return "redirect:/";
        }
        if (!modelo.containsAttribute("destinosViaje")) {
            modelo.addAttribute("destinosViaje", extraerDestinosAnimacion(modelo));
        }
        if (!modelo.containsAttribute("anoInicioSalto")) {
            modelo.addAttribute("anoInicioSalto", lineaTemporal.getAnhoActual());
        }
        modelo.addAttribute("soloVuelta", Boolean.TRUE.equals(modelo.getAttribute("soloVuelta")));
        modelo.addAttribute("mostrarAvisoVueltaPrepagada",
                Boolean.TRUE.equals(modelo.getAttribute("mostrarAvisoVueltaPrepagada")));
        boolean esRegresoPresente = esRegresoAlPresente(modelo);
        modelo.addAttribute("esRegresoPresente", esRegresoPresente);
        if (esRegresoPresente) {
            modelo.addAttribute("normasPresente", NormasPresente.porDefecto());
            modelo.addAttribute("erasAvisos", List.of());
        } else if (!modelo.containsAttribute("erasAvisos")) {
            modelo.addAttribute("erasAvisos", extraerErasAvisos(modelo));
        }
        return "confirmacion";
    }

    private boolean esRegresoAlPresente(Model modelo) {
        if (Boolean.TRUE.equals(modelo.getAttribute("soloVuelta"))) {
            return true;
        }
        Object billetesAttr = modelo.getAttribute("billetes");
        if (billetesAttr instanceof List<?> lista && !lista.isEmpty()) {
            boolean tieneIda = lista.stream()
                    .anyMatch(b -> b instanceof Billete bil && bil.tipo() == TipoBillete.IDA);
            return !tieneIda;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private List<Era> extraerErasAvisos(Model modelo) {
        Object billetesAttr = modelo.getAttribute("billetes");
        if (!(billetesAttr instanceof List<?> lista)) {
            return List.of();
        }
        return lista.stream()
                .filter(b -> b instanceof Billete bil && bil.tipo() == TipoBillete.IDA)
                .map(b -> ((Billete) b).era())
                .distinct()
                .toList();
    }

    @SuppressWarnings("unchecked")
    private List<DestinoAnimacion> extraerDestinosAnimacion(Model modelo) {
        Object attr = modelo.getAttribute("billetes");
        if (!(attr instanceof List<?> lista) || lista.isEmpty()) {
            return List.of();
        }
        List<DestinoAnimacion> destinos = new ArrayList<>();
        var erasConIdaEnCompra = lista.stream()
                .filter(b -> b instanceof Billete bil && bil.tipo() == TipoBillete.IDA)
                .map(b -> ((Billete) b).era())
                .collect(java.util.stream.Collectors.toSet());

        for (Object item : lista) {
            if (item instanceof Billete b && b.tipo() == TipoBillete.IDA) {
                destinos.add(DestinoAnimacion.desdeEra(b.era(), b.anoDestino()));
            } else if (item instanceof Billete b && b.tipo() == TipoBillete.VUELTA
                    && !erasConIdaEnCompra.contains(b.era())) {
                destinos.add(DestinoAnimacion.regresoPresente(b.era(), lineaTemporal));
            }
        }
        if (destinos.isEmpty() && lista.getFirst() instanceof Billete b) {
            destinos.add(DestinoAnimacion.desdeEra(b.era(), b.anoEfectivo()));
        }
        return destinos;
    }

    @GetMapping("/logica-temporal")
    public String logicaTemporal(Model modelo) {
        modelo.addAttribute("anoPresente", calculadora.obtenerAnoPresente());
        modelo.addAttribute("lineaTemporal", lineaTemporal);
        return "logica";
    }
}
