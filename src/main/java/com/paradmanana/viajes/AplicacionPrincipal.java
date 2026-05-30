package com.paradmanana.viajes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Spring Boot.
 * Arranca el servidor embebido y escanea componentes en {@code com.paradmanana.viajes}.
 */
@SpringBootApplication
public class AplicacionPrincipal {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionPrincipal.class, args);
    }
}
