package com.paradmanana.viajes.dominio.aventura;

import com.paradmanana.viajes.dominio.Era;

import java.util.List;
import java.util.Optional;

/**
 * Escena visible pero aún no jugable (falta objeto, era incorrecta, ya completada…).
 */
public record EscenaBloqueada(
        EscenaAventura escena,
        String motivo
) {}
