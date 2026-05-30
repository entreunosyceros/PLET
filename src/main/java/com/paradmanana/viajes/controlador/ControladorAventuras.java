package com.paradmanana.viajes.controlador;

import com.paradmanana.viajes.dominio.aventura.CatalogoAventuras;
import com.paradmanana.viajes.servicio.ServicioAventuras;
import com.paradmanana.viajes.servicio.ServicioPosicionViajero;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Libro-juego «Elige tu propia aventura» por sesión HTTP.
 */
@Controller
@RequestMapping("/aventuras")
public class ControladorAventuras {

    private final ServicioAventuras servicioAventuras;
    private final ServicioPosicionViajero servicioPosicion;

    public ControladorAventuras(ServicioAventuras servicioAventuras,
                                ServicioPosicionViajero servicioPosicion) {
        this.servicioAventuras = servicioAventuras;
        this.servicioPosicion = servicioPosicion;
    }

    @GetMapping
    public String indice(Model modelo) {
        modelo.addAttribute("escenasJugables", servicioAventuras.listarJugablesEnPosicion());
        modelo.addAttribute("escenasBloqueadas", servicioAventuras.listarBloqueadasEnPosicion());
        modelo.addAttribute("escenasCompletadas", servicioAventuras.listarCompletadas());
        modelo.addAttribute("enPresente", servicioPosicion.estaEnPresente());
        return "aventuras";
    }

    @GetMapping("/resultado")
    public String resultado(Model modelo) {
        if (!modelo.containsAttribute("resultadoAventura")) {
            return "redirect:/aventuras";
        }
        return "aventura-resultado";
    }

    @GetMapping("/{id}")
    public String escena(@PathVariable String id, Model modelo, RedirectAttributes redireccion) {
        var escenaOpt = CatalogoAventuras.buscarPorId(id);
        if (escenaOpt.isEmpty() || !servicioAventuras.puedeJugar(escenaOpt.get())) {
            redireccion.addFlashAttribute("mensajeError",
                    "Esa aventura no está disponible en tu posición temporal.");
            return "redirect:/aventuras";
        }
        modelo.addAttribute("escena", escenaOpt.get());
        return "aventura";
    }

    @PostMapping("/{id}")
    public String elegir(@PathVariable String id,
                         @RequestParam String opcionId,
                         RedirectAttributes redireccion) {
        var resultado = servicioAventuras.elegir(id, opcionId);
        if (resultado.isEmpty()) {
            redireccion.addFlashAttribute("mensajeError", "No se pudo registrar tu elección.");
            return "redirect:/aventuras";
        }
        redireccion.addFlashAttribute("resultadoAventura", resultado.get());
        return "redirect:/aventuras/resultado";
    }
}
