package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.LineaTemporal;
import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Carrito;
import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.TipoBillete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorParadojasTest {

    private ValidadorParadojas validador;
    private CalculadoraPrecios calculadora;
    private ServicioPosicionViajero posicion;
    private Carrito carrito;

    @BeforeEach
    void preparar() {
        posicion = new ServicioPosicionViajero(new LineaTemporal(2026));
        calculadora = new CalculadoraPrecios(new LineaTemporal(2026));
        validador = new ValidadorParadojas(posicion);
        carrito = new Carrito();
    }

    @Test
    void rechazaVueltaSinIdaEnCarritoNiEstarEnEra() {
        Billete vuelta = calculadora.crearBillete(TipoBillete.VUELTA, Era.ROMA);
        var resultado = validador.validarAntesDeAgregar(carrito, vuelta);

        assertFalse(resultado.valido());
        assertTrue(resultado.mensaje().contains("IDA"));
    }

    @Test
    void aceptaVueltaConIdaEnCarritoDesdePresente() {
        carrito.agregar(calculadora.crearBillete(TipoBillete.IDA, Era.ROMA));

        Billete vuelta = calculadora.crearBillete(TipoBillete.VUELTA, Era.ROMA);
        var resultado = validador.validarAntesDeAgregar(carrito, vuelta);

        assertTrue(resultado.valido());
    }

    @Test
    void aceptaVueltaDesdeEra() {
        posicion.aplicarBilletes(List.of(calculadora.crearBillete(TipoBillete.IDA, Era.ROMA)));

        Billete vuelta = calculadora.crearBillete(TipoBillete.VUELTA, Era.ROMA);
        var resultado = validador.validarAntesDeAgregar(carrito, vuelta);

        assertTrue(resultado.valido());
    }

    @Test
    void rechazaIdaSiNoEstaEnPresente() {
        posicion.aplicarBilletes(List.of(calculadora.crearBillete(TipoBillete.IDA, Era.ROMA)));

        Billete ida = calculadora.crearBillete(TipoBillete.IDA, Era.ANO_3000);
        var resultado = validador.validarAntesDeAgregar(carrito, ida);

        assertFalse(resultado.valido());
        assertTrue(resultado.mensaje().contains("presente"));
    }

    @Test
    void rechazaDosIdasAlMismoDestino() {
        Billete ida1 = calculadora.crearBillete(TipoBillete.IDA, Era.ANO_3000);
        carrito.agregar(ida1);

        Billete ida2 = calculadora.crearBillete(TipoBillete.IDA, Era.ANO_3000);
        var resultado = validador.validarAntesDeAgregar(carrito, ida2);

        assertFalse(resultado.valido());
    }

    @Test
    void aceptaIdaYVueltaEnCarritoCoherentes() {
        Billete ida = calculadora.crearBillete(TipoBillete.IDA, Era.ROMA);
        carrito.agregar(ida);

        posicion.aplicarBilletes(List.of(ida));
        Billete vuelta = calculadora.crearBillete(TipoBillete.VUELTA, Era.ROMA);
        carrito.agregar(vuelta);

        assertTrue(validador.validarCarritoCompleto(carrito).valido());
    }

    @Test
    void dinosauriosEsElViajeMasCaro() {
        var desgloseDino = calculadora.obtenerDesglose(Era.DINOSAURIOS);
        var desgloseRoma = calculadora.obtenerDesglose(Era.ROMA);

        assertTrue(desgloseDino.total().compareTo(desgloseRoma.total()) > 0);
        assertTrue(desgloseDino.combustible().compareTo(desgloseRoma.combustible()) > 0);
    }
}
