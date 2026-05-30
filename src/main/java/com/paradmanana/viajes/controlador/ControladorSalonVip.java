package com.paradmanana.viajes.controlador;

import com.paradmanana.viajes.dominio.MonedaTemporal;
import com.paradmanana.viajes.servicio.ServicioAventuras;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Salón VIP desbloqueable al acumular 500+ Gallifantes Temporales.
 */
@Controller
public class ControladorSalonVip {

    private final ServicioAventuras servicioAventuras;

    public ControladorSalonVip(ServicioAventuras servicioAventuras) {
        this.servicioAventuras = servicioAventuras;
    }

    @GetMapping("/salon-vip")
    public String salonVip(Model modelo, RedirectAttributes redireccion) {
        if (!servicioAventuras.tieneSalonVip()) {
            redireccion.addFlashAttribute("mensajeError",
                    "🚫 Acceso denegado. Necesitas al menos "
                            + com.paradmanana.viajes.dominio.aventura.RangoCredito.PLATINUM.getUmbralMinimo()
                            + " " + MonedaTemporal.ABREVIATURA
                            + " para el Salón VIP del Tiempo. Sigue haciendo aventuras.");
            return "redirect:/aventuras";
        }
        modelo.addAttribute("rangoCredito", servicioAventuras.obtenerRangoCredito());
        return "salon-vip";
    }
}
