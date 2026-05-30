package com.paradmanana.viajes.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Colección mutable de billetes en la sesión del usuario.
 * <p>
 * Expone consultas por era (ida/vuelta) usadas por el validador de paradojas
 * y el calculador de recargos por solo ida.
 * </p>
 */
public class Carrito {

    private final List<Billete> billetes = new ArrayList<>();
    /** Último error de validación, para mostrarlo tras un intento fallido de añadir. */
    private String ultimoError;

    /** Copia inmutable: evita que vaciar el carrito vacíe listas ya guardadas en flash attributes. */
    public List<Billete> obtenerBilletes() {
        return List.copyOf(billetes);
    }

    public void agregar(Billete billete) {
        billetes.add(billete);
        ultimoError = null;
    }

    public void eliminar(String identificadorBillete) {
        billetes.removeIf(b -> b.identificador().equals(identificadorBillete));
        ultimoError = null;
    }

    public void vaciar() {
        billetes.clear();
        ultimoError = null;
    }

    public int obtenerCantidad() {
        return billetes.size();
    }

    /** Suma de precios totales de todos los billetes (sin recargos por solo ida). */
    public BigDecimal obtenerSubtotalBilletes() {
        return billetes.stream()
                .map(Billete::precioTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Optional<String> obtenerUltimoError() {
        return Optional.ofNullable(ultimoError);
    }

    public void registrarError(String mensaje) {
        this.ultimoError = mensaje;
    }

    public boolean tieneIda(Era era) {
        return billetes.stream()
                .anyMatch(b -> b.tipo() == TipoBillete.IDA && b.era() == era);
    }

    public boolean tieneVuelta(Era era) {
        return billetes.stream()
                .anyMatch(b -> b.tipo() == TipoBillete.VUELTA && b.era() == era);
    }

    public Optional<Billete> buscarIda(Era era) {
        return billetes.stream()
                .filter(b -> b.tipo() == TipoBillete.IDA && b.era() == era)
                .findFirst();
    }

    public Optional<Billete> buscarVuelta(Era era) {
        return billetes.stream()
                .filter(b -> b.tipo() == TipoBillete.VUELTA && b.era() == era)
                .findFirst();
    }
}
