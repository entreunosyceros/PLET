package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.AnoTemporal;
import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Carrito;
import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.TipoBillete;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Comprueba que los billetes del carrito no generen paradojas temporales.
 * <p>
 * La ida solo en el presente; la vuelta con ida en el carrito (presente)
 * o estando físicamente en la época (ida ya pagada).
 * </p>
 */
@Service
public class ValidadorParadojas {

    private final ServicioPosicionViajero servicioPosicion;

    public ValidadorParadojas(ServicioPosicionViajero servicioPosicion) {
        this.servicioPosicion = servicioPosicion;
    }

    /** Validación incremental al añadir un billete nuevo al carrito. */
    public ResultadoValidacion validarAntesDeAgregar(Carrito carrito, Billete nuevo) {
        List<String> errores = new ArrayList<>();

        if (nuevo.tipo() == TipoBillete.VUELTA) {
            validarVuelta(carrito, nuevo, errores);
        } else {
            validarIda(carrito, nuevo, errores);
        }

        validarOrdenCronologico(carrito, nuevo, errores);

        if (errores.isEmpty()) {
            return ResultadoValidacion.exito();
        }
        return ResultadoValidacion.paradoja(String.join(" ", errores));
    }

    /**
     * Validación global del carrito antes de confirmar la compra.
     * Verifica coherencia de fechas y la secuencia de idas encadenadas.
     */
    public ResultadoValidacion validarCarritoCompleto(Carrito carrito) {
        List<String> errores = new ArrayList<>();

        for (Era era : Era.values()) {
            var ida = carrito.buscarIda(era);
            var vuelta = carrito.buscarVuelta(era);

            if (ida.isPresent() && vuelta.isPresent()) {
                AnoTemporal llegadaIda = ida.get().anoDestino();
                AnoTemporal salidaVuelta = vuelta.get().anoOrigen();

                if (salidaVuelta.esAnteriorA(llegadaIda)) {
                    errores.add("🌀 PARADOJA: La vuelta desde "
                            + salidaVuelta.formatear()
                            + " es anterior a tu llegada en "
                            + llegadaIda.formatear()
                            + ". ¡No puedes irte antes de llegar!");
                }
            }
        }

        List<Billete> idas = carrito.obtenerBilletes().stream()
                .filter(b -> b.tipo() == TipoBillete.IDA)
                .sorted(Comparator.comparing(b -> b.anoEfectivo().valor()))
                .toList();

        for (int i = 1; i < idas.size(); i++) {
            AnoTemporal anterior = idas.get(i - 1).anoEfectivo();
            AnoTemporal actual = idas.get(i).anoEfectivo();
            if (actual.esAnteriorA(anterior)) {
                errores.add("🌀 Viaje encadenado imposible: visitas "
                        + actual.formatear() + " después de "
                        + anterior.formatear() + " sin vuelta intermedia.");
            }
        }

        if (errores.isEmpty()) {
            return ResultadoValidacion.exito();
        }
        return ResultadoValidacion.paradoja(String.join(" ", errores));
    }

    /** Vuelta: en la época destino, o en el presente con ida de esa era en el carrito. */
    private void validarVuelta(Carrito carrito, Billete nuevo, List<String> errores) {
        Era era = nuevo.era();

        if (!servicioPosicion.puedeComprarVuelta(era, carrito)) {
            if (servicioPosicion.obtenerEraActual().filter(e -> e == era).isPresent()) {
                errores.add("⚠️ Ya tienes un billete de vuelta pagado para "
                        + era.getNombre() + ". Úsalo con el botón «Viajar al presente».");
            } else {
                errores.add("⚠️ Necesitas un billete de IDA a "
                        + era.getNombre() + " en el carrito (desde el presente) "
                        + "o haber viajado ya a esa época.");
            }
            return;
        }

        if (carrito.tieneVuelta(era)) {
            errores.add("Ya tienes una vuelta para " + era.getNombre() + ".");
        }
    }

    /** Solo se puede comprar ida estando en el presente. */
    private void validarIda(Carrito carrito, Billete nuevo, List<String> errores) {
        if (!servicioPosicion.puedeComprarIda()) {
            errores.add("⚠️ Solo puedes comprar billetes de ida estando en el presente. "
                    + "Compra la vuelta desde la época en la que te encuentras.");
            return;
        }

        if (carrito.tieneIda(nuevo.era())) {
            errores.add("Ya tienes una ida a " + nuevo.era().getNombre() + ".");
        }
    }

    /**
     * Impide saltar a una época más antigua si aún no has vuelto
     * de un destino más reciente (sin billete de vuelta intermedio).
     */
    private void validarOrdenCronologico(Carrito carrito, Billete nuevo, List<String> errores) {
        if (nuevo.tipo() != TipoBillete.IDA) {
            return;
        }

        AnoTemporal nuevoAno = nuevo.anoEfectivo();

        for (Billete existente : carrito.obtenerBilletes()) {
            if (existente.tipo() == TipoBillete.IDA) {
                AnoTemporal existenteAno = existente.anoEfectivo();
                if (nuevoAno.esAnteriorA(existenteAno) && !carrito.tieneVuelta(existente.era())) {
                    errores.add("🌀 Quieres ir primero a "
                            + nuevoAno.formatear()
                            + " pero aún no has vuelto de "
                            + existenteAno.formatear()
                            + ". Compra la vuelta o reordena tus viajes.");
                }
            }
        }
    }

    /** Resultado de una validación: válido o mensaje de paradoja para mostrar al usuario. */
    public record ResultadoValidacion(boolean valido, String mensaje) {
        public static ResultadoValidacion exito() {
            return new ResultadoValidacion(true, null);
        }

        public static ResultadoValidacion paradoja(String mensaje) {
            return new ResultadoValidacion(false, mensaje);
        }
    }
}
