package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.Era;
import com.paradmanana.viajes.dominio.LineaTemporal;
import com.paradmanana.viajes.dominio.aventura.CatalogoObjetos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicioAventurasTest {

    private ServicioPosicionViajero posicion;
    private ServicioAventuras aventuras;

    @BeforeEach
    void setUp() {
        posicion = new ServicioPosicionViajero(new LineaTemporal(2026));
        aventuras = new ServicioAventuras(posicion, 0);
    }

    @Test
    void escenaDisponibleSoloEnLaEraCorrecta() {
        assertTrue(aventuras.listarJugablesEnPosicion().isEmpty());

        posicion.teletransportarA(Era.DINOSAURIOS);
        assertEquals(1, aventuras.listarJugablesEnPosicion().size());
        assertEquals("dino-mosquito", aventuras.listarJugablesEnPosicion().getFirst().id());
    }

    @Test
    void elegirOpcionAplicaCreditosObjetoYMarcaCompletada() {
        posicion.teletransportarA(Era.DINOSAURIOS);

        var resultado = aventuras.elegir("dino-mosquito", "dejar-picar");

        assertTrue(resultado.isPresent());
        assertEquals(50, resultado.get().creditosTrasEleccion());
        assertTrue(aventuras.tieneObjeto(CatalogoObjetos.RONCHON_COSMICO.id()));
        assertTrue(aventuras.escenaCompletada("dino-mosquito"));
    }

    @Test
    void tesoroCesarBloqueadoSinMapa() {
        posicion.teletransportarA(Era.ROMA);

        assertFalse(aventuras.listarJugablesEnPosicion().stream()
                .anyMatch(e -> e.id().equals("roma-tesoro-cesar")));
        assertEquals(1, aventuras.listarBloqueadasEnPosicion().stream()
                .filter(b -> b.escena().id().equals("roma-tesoro-cesar"))
                .count());
    }

    @Test
    void tesoroCesarDesbloqueadoConMapa() {
        posicion.teletransportarA(Era.ROMA);
        aventuras.elegir("roma-mapas", "comprar-mapa");

        assertTrue(aventuras.puedeJugar(
                com.paradmanana.viajes.dominio.aventura.CatalogoAventuras.buscarPorId("roma-tesoro-cesar").orElseThrow()
        ));
    }

    @Test
    void venderDenarioEnPresente() {
        posicion.teletransportarA(Era.ROMA);
        aventuras.elegir("roma-taberna", "aceptar-trato");
        posicion.regresarAlPresente();

        var precio = aventuras.venderObjeto(CatalogoObjetos.DENARIO_ROMANO.id());

        assertTrue(precio.isPresent());
        assertEquals(500, precio.get());
        assertEquals(200 + 500, aventuras.obtenerCreditos());
        assertFalse(aventuras.tieneObjeto(CatalogoObjetos.DENARIO_ROMANO.id()));
    }

    @Test
    void consumibleAlmanaqueAplicaDescuento() {
        posicion.teletransportarA(Era.ANO_3000);
        aventuras.elegir("3000-tataranieto", "prestar-creditos");

        var descuento = aventuras.aplicarConsumible(CatalogoObjetos.ALMANAQUE_3000.id());

        assertTrue(descuento.isPresent());
        assertEquals(20, descuento.get());
        assertEquals(20, aventuras.obtenerDescuentoCompraPorcentaje());
        assertFalse(aventuras.tieneObjeto(CatalogoObjetos.ALMANAQUE_3000.id()));
    }

    @Test
    void coleccionMuseoCompletaDaTituloVeterano() {
        posicion.teletransportarA(Era.DINOSAURIOS);
        aventuras.elegir("dino-mosquito", "dejar-picar");
        posicion.teletransportarA(Era.ROMA);
        aventuras.elegir("roma-taberna", "aceptar-trato");
        posicion.teletransportarA(Era.ANO_3000);
        aventuras.elegir("3000-tataranieto", "prestar-creditos");

        assertTrue(aventuras.coleccionMuseoCompleta());
        assertEquals(5, aventuras.obtenerDescuentoPermanentePorcentaje());
        assertTrue(aventuras.obtenerTituloAgente().contains("Veterano"));
    }
}
